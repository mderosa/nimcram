package com.fisher.extract.defs;

class DynamicLookupExpanderDef {
	def sourceKeyName
	def createKeyName
	def lookupSql
	
	def assertInvariant() {
		assert sourceKeyName, "DynamicLookupExpanderDef.sourceKeyName is a required field"
		assert createKeyName, "DynamicLookupExpanderDef.createKeyName is a required field"
		assert lookupSql, "DynamicLookupExpanderDef.lookupSql is a required field"
	}
}
