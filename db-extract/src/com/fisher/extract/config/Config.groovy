package com.fisher.extract.config;

class Config {
	
	def assertInvariant() {
		mappings.each { tblDef ->
			tblDef.assertInvariant() 
			tblDef.fieldTransformDefs.each { fldDef ->
				fldDef.assertInvariant()
			}
		}
		
		merges.each {
			it.assertInvariant()
		}
	}
}
