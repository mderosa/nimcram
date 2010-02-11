package com.fisher.extract;

import groovy.sql.Sql
import org.apache.log4j.Logger

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
	
	def getNextKey(transformDef) {
		if (transformDef.tableSeq == null) {
			return null
		}
		def sql = "SELECT " + transformDef.tableSeq + ".nextval FROM dual"
		def newKey
		dbTarget.eachRow(sql) { row ->
			newKey = row[0]
		}
		return newKey
	}
	
	/**
	 * writeTargetData :: transformDef -> transformedDataRec -> Integer
	 * <p>
	 * After writing a new record to the database we return the primary key of '
	 * the newly inserted record
	 */
	def writeTargetData(transformDef, recTarget) {
		def insertSql = this.transformDefToInsert(transformDef)
		def nextKey = getNextKey(transformDef)
		
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
				if (it.useSeq) {
					vals << mergeDef.tableSeq + ".nextval"
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
	
}