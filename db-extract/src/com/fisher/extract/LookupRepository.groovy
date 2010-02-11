package com.fisher.extract;

class LookupRepository {
	def db
	def config
	def lookupTables = [:]
	
	public LookupRepository(database, configuration, env) {
		db = database
		config = configuration
		initRepository(env)
	}
	
	public LookupRepository() {}
	
	def initRepository(env) {
		println 'begin: reposistory initailization'
		for (cnfg in config.lookups) {
			println "\t" + cnfg.table
			def tuple = db.queryLookupTables(cnfg, env)
			def lookupTbl = innerJoin(tuple[0], tuple[1], cnfg)
			lookupTables.put cnfg.table, lookupTbl
		}
		println 'finish: repository initialization'
	}
	
	def innerJoin(remoteRecs, localRecs, cnfg) {
		def temp = [:]
		remoteRecs.each {r ->
			for (l in localRecs) {
				if (cnfg.areSame(r, l)) {
					temp.put r.id, l.id
					continue
				}
			}
		}
		return temp
	}
	
	/**
	 * queryRepo :: String -> Integer -> Maybe Integer
	 */
	def queryRepo(tableName, pkSource) {
		lookupTables.get(tableName).get pkSource
	}
	
	def updateRepo(mapping, recSrc, pkTarget) {
		if (pkTarget == null)  {
			return 
		}
		assert mapping.fieldTransformDefs != null, "unexpected missing field definitions"
		for (i in (0..<mapping.fieldTransformDefs.size())) {
			def fld = mapping.fieldTransformDefs[i]
			if (fld.useSeq) {
				def pkSource = recSrc[i]
				if (!existsLookupTableEntry(mapping.to)) {
					createNewLookupTableEntry(mapping.to)
				}
				appendRepo mapping.to, pkSource, pkTarget 
				return
			}
		}
	}
	
	/**
	 * existsLookupTableEntry :: Sting -> Boolean 
	 */
	def existsLookupTableEntry(tableName) {
		return (this.lookupTables.get(tableName) != null)
	}
	
	/**
	 * createNewLookupTableEntry :: String -> () 
	 */
	def createNewLookupTableEntry(tableName) {
		this.lookupTables.put tableName, [:]
	}
	
	def appendRepo(tableName, pkSource, pkTarget) {
		lookupTables.get(tableName).put pkSource, pkTarget
	}

}
