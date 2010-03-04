package com.fisher.extract.defs;

/**
 * A configuration class the describes how reference data is to be merged into existing reference data
 * <p>
 * The properties have the same meaning as for a TableTransformDef.  The only addition is conditionalSql which
 * is the sql which determines if a record from the source will be added to the target database.  If conditionalSql
 * returns any records the source record is not added to the target
 */
class MergeDef {
	def to
	def fieldTransformDefs
	def sourceSql
	def conditionalSql
	def idGenerator
	def idGeneratorType
	def logModificationData = false
	
	def assertInvariant() {
		assert to, "Configuration Error: MergeDef.to is a required parameter"
		assert sourceSql, "Configuration Error: MergeDef.sourceSql is a required parameter"
		fieldTransformDefs.each { 
			if (it.lookup) {
				assert false, "FieldTransformDef.lookup is not a valid parameter when contained in a merge definition"
				it.assertInvariant()
			}
		}
		
		if (idGenerator) {
			assert idGeneratorType, "Configuration Error: MergeDef.idGeneratorType must be specified when idGenerator is specified"
			if (idGeneratorType == IdGeneratorType.SEQUENCE) {
				assert idGenerator.contains(to), "Warning: in most cases the sequence for a table in the tracker db is named after the table itself"
			}
		}
	}
}
