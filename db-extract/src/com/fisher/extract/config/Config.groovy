package com.fisher.extract.config;

import com.fisher.extract.defs.TableTransformDef

class Config {
	
	def assertInvariant() {
		mappings.each { tblDef ->
			tblDef.assertInvariant() 
			if (tblDef instanceof TableTransformDef) {
				tblDef.fieldTransformDefs.each { fldDef ->
					fldDef.assertInvariant()
				}
			}
		}
		
		merges.each {
			it.assertInvariant()
		}
	}
}
