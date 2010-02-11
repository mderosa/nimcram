package com.fisher.extract.defs;

/**
 * Defines a field mapping from a source -> target.
 * <p>
 * In the transformation one of the following is required (a) a function specifying how
 * to transform the source value (b) a sequence name to specify that the target field should
 * be generated from a sequence (c) the name of a table lookup map in the repository
 * <p>
 * Metadata generation support is provided by the properties isMeta and includeInInsert.  When true, the 
 * isMeta property indicates that the fn property is a function with an expanded set of arguments.  For
 * an example see TransformerTest.testTransformFunctionalFieldForMetadata().  The includeInInsert parameter
 * can be used to bring in extra fields info that is specific to metadata content but which will are not needed
 * outside of the metadata creation process
 */
class FieldTransformDef {
	def from
	def to
	def useSeq = false
	def isMeta = false
	def fn
	def lookup
	def includeInInsert = true
	
	def assertInvariant() {
		assert from, "Configuration Error: FieldTransformDef.from is a required parameter"
		assert to, "Configuration Error: FieldTransformDef.to is a required parameter"
		
		def count = 0
		if (useSeq) {count++}
		if (fn) {count++}
		if (lookup) {count++}
		if (count == 0) {
			assert false, "Configuration Error: one of useSeq, fn, or lookup must be defined"
		}
		if (count > 1) {
			assert false, "Configuration Error: only one of useSeq, fn, or lookup should be defined"
		}
		
		if (lookup) {
			assert lookup instanceof String, "FieldTransformDef.lookup must be a string"
		}
		if (isMeta) {
			assert fn, "Fields that contain metadata, FieldTransformDef.isMeta, must have an associated transform " +
			"function specified to create that metadata"
		}
		if (fn && fn.getMaximumNumberOfParameters() == 2) {
			assert isMeta, "A closure that takes more than one parameter can only be handled if isMeta = true"
		}

	}
}
