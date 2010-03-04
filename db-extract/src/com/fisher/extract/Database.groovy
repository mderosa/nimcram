package com.fisher.extract;

import groovy.sql.Sql
import org.apache.log4j.Logger
import com.fisher.extract.defs.IdGeneratorType
import com.fisher.extract.defs.WriteMethod
import com.fisher.extract.defs.OutputParamsLocation

class Database {
	def static logger = Logger.getLogger(Database.class.getName())
	def dbSource
	def dbTarget
	
	public Database(src, trgt) {
		dbSource = Sql.newInstance("jdbc:oracle:thin:@${src.url}:1521:${src.sid}", 
			"${src.loginName}",	"${src.password}", "oracle.jdbc.OracleDriver")
		dbTarget = Sql.newInstance("jdbc:oracle:thin:@${trgt.url}:1521:${trgt.sid}", 
			"${trgt.loginName}",	"${trgt.password}", "oracle.jdbc.OracleDriver")
	}
	
	public Database() {}
	
	/** 
	 * readSource :: transformDef -> Int -> [dataRec]
	 */
	def readSource(transformDef, env) {
		def selectSql = transformDefToSelect(transformDef, env)
		logger.debug selectSql
		dbSource.rows(selectSql)
	}
	
	def getNextKey(tblTransformDef) {
		if (tblTransformDef.idGenerator == null) {
			return null
		}
		
		def sql
		switch (tblTransformDef.idGeneratorType) {
			case IdGeneratorType.SEQUENCE:
				sql = "SELECT " + tblTransformDef.idGenerator + ".nextval FROM dual"
				break;
			case IdGeneratorType.INCREMENT:
				sql = "SELECT max(" + tblTransformDef.targetPrimaryKey() + ") + 1 FROM " + tblTransformDef.to
				break;
			default:
				assert "A valid value of idGeneratorType was not found for table " + tblTransformDef.to
		}
		def newKey
		dbTarget.eachRow(sql) { row ->
			newKey = row[0]
		}
		return newKey
	}
	
	/**
	 * writeTargetData :: TableTransformDef -> transformedDataRec -> Integer
	 */
	def writeTargetData(tblTransDef, recTarget) {
		if (tblTransDef.writer.method == WriteMethod.SQL) {
			_writeTargetDataViaSql(tblTransDef, recTarget)
		} else if (tblTransDef.writer.method == WriteMethod.PROC) {
			_writeTargetDataViaStoredProc(tblTransDef, recTarget)
		} else {
			assert false, "invalid WriteMethod encountered"
		}
	}
	
	/**
	 * writeTargetData :: TableTransformDef -> transformedDataRec -> Integer
	 * <p>
	 * After writing a new record to the database we return the primary key of
	 * the newly inserted record
	 */
	def _writeTargetDataViaSql(tblTransDef, recTarget) {
		def insertSql = this.transformDefToInsert(tblTransDef)
		def nextKey = getNextKey(tblTransDef)
		
		def params = recTarget.collect {k, v -> v}
		if (nextKey) {
			params = params.collect {
				if (it == "{autogen}") {
					nextKey
				} else {
					it
				}
			}
		}
		logger.debug insertSql; logger.debug params.toString()
		dbTarget.executeInsert(insertSql, params)
		
		return nextKey
	}
	
	/**
	 * writeTargetData :: TableTransformDef -> transformedDataRec -> Integer
	 * <p>
	 * After writing a new record to the database we return the primary key of
	 * the newly inserted record
	 */
	def _writeTargetDataViaStoredProc(tblTransDef, recTarget) {
		def nextKey
		if (tblTransDef.idGeneratorType == IdGeneratorType.SEQUENCE) {
			nextKey = getNextKey(tblTransDef)
		}
		def params = recTarget.collect {k, v -> v}
		if (nextKey) {
			params = params.collect {
				if (it == "{autogen}") {
					nextKey
				} else {
					it
				}
			}
		}
		def allParams
		if (tblTransDef.writer.outputParams && tblTransDef.writer.outputParamsLocation == OutputParamsLocation.PRE) {
			allParams = tblTransDef.writer.outputParams + params
		} else {
			allParams = params + tblTransDef.writer.outputParams
		}
		logger.debug tblTransDef.writer.statement; logger.debug allParams.toString()
		dbTarget.call(tblTransDef.writer.statement, allParams)

		if (tblTransDef.idGeneratorType == IdGeneratorType.INCREMENT) {
			nextKey = getNextKey(tblTransDef) - 1
		}
		return nextKey
	}
	
	/**
	 * writeTargetRefData :: MergeDef -> Rec -> ()
	 * Writes reference data to the target table.  It does a select to check for no
	 * records before it runs the insert clause.  This is done, in preference using
	 * a single insert statement, so that jdbc parameterize all each of the 
	 * sql statments 
	 * <p>
	 * Surprisingly recTarget is modified by the call to make() so we want to create
	 * our param list first thing
	 */
	def writeTargetRefData(mergeDef, recTarget) {
		def params = recTarget.collect {k, v ->	v}
		def qry = mergeDef.conditionalSql.make(recTarget).toString()
		def srcRows = dbTarget.rows(qry)
		if (srcRows.size() == 0) {
			def insertSql = this.mergeDefToInsert(mergeDef)
			dbTarget.executeInsert(insertSql, params)
		}
	}
	
	/**
	 * queryLookupTables :: Configuration -> [sourceDbRecs, targetDbRecs]
	 */
	def queryLookupTables(config, env) {
		def sqlSource = config.sqlSource.make(env).toString()
		def srcRows = dbSource.rows(sqlSource)
		if (srcRows.size() == 0) { 
			logger.warn '''queryLookupTables: usually we should not have zero rows come back from a lookup table
				query.  This is possible but it may also indicate that the lookup table query is incorrect.'''
		}
		def tgtRows = dbTarget.rows(config.sqlTarget)
		assert tgtRows.size() > 0, "queryLookupTables: we should never have zero rows in a lookup table"
		[srcRows, tgtRows]
	}
	
	/**
	 * transformDefToSelect :: transformDef -> String 
	 */
	def transformDefToSelect(transformDef, env) {
		def sql = transformDef.sourceSql.make(env).toString()
		sql.toString()
	}
	
	/**
	 * transformDefToInsert :: transformDef -> String
	 */
	def transformDefToInsert(transformDef) {
		def table = transformDef.to
		def flds = []; def vals = []
		transformDef.fieldTransformDefs.each {
			if (it.includeInInsert) {
				flds << it.to
				vals << "?"
			}
		}
		appendModificationFields(transformDef, flds, vals)
		flds = flds.join(",")
		vals = vals.join(",")
		def sql = "INSERT INTO ${table} (${flds}) VALUES (${vals})"
		sql.toString()
	}
	
	/**
	 * mergeDefToInsert :: mergeDef -> String 
	 */
	def mergeDefToInsert(mergeDef) {
		def table = mergeDef.to
		def flds = []; def vals = []
		mergeDef.fieldTransformDefs.each {
			if (it.includeInInsert) {
				flds << it.to
				if (it.useGenerator) {
					vals << mergeDef.idGenerator + ".nextval"
				} else {
					vals << "?"
				}
			}
		}
		appendModificationFields(mergeDef, flds, vals)
		flds = flds.join(",")
		vals = vals.join(",")
		def sql = "INSERT INTO ${table} (${flds}) VALUES (${vals})"
	}

	/**
	 * append modified_date and modified_by to the Insert sql
	 */
	def appendModificationFields (transformDef, flds, vals) {
		if(transformDef.logModificationData) {
			flds << "modified_date" << "modified_by"
			vals << "?" << "?"
		}
	}
	
	/**
	 * queryExpandedLookup :: Map -> DynamicLookupExpander -> Map
	 * <p>
	 * This function performs a lookup in the target database expanding an existing map so that
	 * the source keys in the original map point are retained but point to new derived keys.
	 * <p>
	 * For example if we start with the map [sourceProjId1 : targetProjId1, sourceProjId2 : targetProjId2] 
	 * but we also need a mapping from projectIds in the source database to planIds in the 
	 * target database, [sourceProjId1 : targetPlanId1, sourceProjId2 : targetPlanId2].  Then this
	 * function will do that transformation
	 */
	def queryExpandedLookup(existingMap, dynamicExpander) {
		def newMap = [:]
		existingMap.each {srcDbKey, tgtDbValOld ->
			def tgtDbValNew = dbTarget.firstRow(dynamicExpander.lookupSql, [tgtDbValOld])
			assert tgtDbValNew.size() == 1, "expecting only one result as for the moment we only handle unique mappings"
			tgtDbValNew.each {k, v ->
				newMap[srcDbKey] = v
			}
		}
		return newMap
	}
	
}
