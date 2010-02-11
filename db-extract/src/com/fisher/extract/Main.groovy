package com.fisher.extract;

import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

class Main {
	def static logger = Logger.getLogger(Main.class.getName())
	
	static main(args) {
		def params = new Menu().run()

		def env, repo
		try {
			env = [projectId: params.projectId, modifiedDate: new java.sql.Date(new Date().getTime()), modifiedBy: "ETL"]
			def db = new Database(params.configuration.dbSource, params.configuration.dbTarget)
			
			mergeRefData(params, db, env)
			repo = new LookupRepository(db, params.configuration, env)
			loadSourceData(params, db, env, repo)
			
		} catch (Exception e) {
			println e.getMessage()
			logApplicationState e, env, repo
		}
		 
		println "finished"
	}

	static def logApplicationState(e, env, repo) {
		logger.error e.getMessage()
		logger.error e.stackTrace.toString()
		if (env) {
			logger.error "begin environment dump:"
			logger.error env.toString()
		}
		if (repo) {
			logger.error "begin lookup table dump:"
			repo.lookupTables.each {k, v ->
				logger.error(k + ":")
				logger.error v
			}
		}
	}
	
	static def mergeRefData(params, db, env) {
		def trans = new Transformer()
		for (merge in params.configuration.merges) {
			println 'begin: merge to ' + merge.to
			def recsSrc = db.readSource(merge, env)
			for (recSrc in recsSrc) {
				def recTgt = trans.transformRefData(merge, recSrc, env)
				db.writeTargetRefData merge, recTgt
			}
			println 'finish: merge to ' + merge.to
		}
	}
	
	static def loadSourceData(params, db, env, repo) {
		def trans = new Transformer(repo);
		for (mapping in params.configuration.mappings) {
			println 'begin: transfer to ' + mapping.to
			def recsSrc = db.readSource(mapping, env)
			for (recSrc in recsSrc) {
				def recTgt = trans.transformSourceData(mapping, recSrc, env)
				def key = db.writeTargetData(mapping, recTgt)
				repo.updateRepo mapping, recSrc, key
			}
			println "finish: transfer to " + mapping.to
		}
	}
	
}
