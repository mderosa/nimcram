package com.fisher.extract;

class Transformer {
	def repo
	
	/**
	 * use this constructor when there is a repository available 
	 * <p>
	 * The assert on the repository size has been removed as it gets in the way of incremental
	 * development of the config files
	 */
	public Transformer(repository) {
		//assert repository.lookupTables.size() > 0, "expected the repository to have lookup table mappings"
		repo = repository
	}
	
	/*
	 * use this constructor when there is not repository available and we
	 * will just be transforming ref data
	 */
	public Transformer() {}
	
	/**
	 * transformSourceData :: TableTransformDef -> Rec -> Rec
	 * <p>
	 * This is the primary transform method for records that will be treated as new data
	 * for the local database.  It transforms a record as
	 * [pk, data1, data2,...dataN] -> [{autogen}, data1',data2',...dataN']
	 * Here the autogen is a place holder for a new sequence generated pk that will be
	 * generated in the Database class
	 */
	def transformSourceData(transformDef, recSource, env) {
		def temp = [:]
		recSource.eachWithIndex {elmt, i ->
			def fld = transformDef.fieldTransformDefs[i]
			if (!fld.includeInInsert) {return}
			
			if (fld.useGenerator) {
				temp.put fld.to, "{autogen}"
			} else if (fld.fn) {
				temp.putAll transformFunctionalField(fld, recSource, elmt)
			} else if (fld.lookup) {
				temp.put fld.to, repo.queryRepo(fld.lookup, elmt.value)
			} else {
				assert false, "a field configuration should specify a sequence, function, or lookup"
			}
		}
		appendModificationFieldValues(transformDef, temp, env)
		return temp
	}
	
	/**
	 * transformRefData :: MergeDef -> Rec -> Rec
	 * <p>
	 * This is the transformation for ref data that needs to be merged into the target database.
	 * The general form of the transform is
	 * [pk, data1, data2,...dataN] -> [data1', data2',...dataN']
	 * Here useGenerator fields are eliminated from the transformed data set as they will be generated
	 * in Database with a seq.nextval in the insert clause.
	 */
	def transformRefData(mergeDef, recSource, env) {
		def temp = [:]
		recSource.eachWithIndex {elmt, i ->
			def fld = mergeDef.fieldTransformDefs[i]
			if (!fld.includeInInsert) {return}
			
			if (fld.useGenerator) {
				//pass
			} else if (fld.fn) {
				temp.putAll transformFunctionalField(fld, recSource, elmt)
			} else if (fld.lookup) {
				assert false, "a lookup field can not be resolved during the data merge stage"
			} else {
				assert false, "a field configuration should specify a sequence, function, or lookup"
			}
		}
		appendModificationFieldValues(mergeDef, temp, env)
		return temp
	}
	
	/**
	 * append the env modifiedDate and modifiedBy to the params
	 */
	def appendModificationFieldValues(transformDef, temp, env) {
		if(transformDef.logModificationData) {
			temp.modified_date = env.modifiedDate 
			temp.modified_by = env.modifiedBy
		}
	}
	
	/**
	 * transformFunctionalField ::  FieldTransformDef -> Rec -> Map.Element -> Map
	 */
	def transformFunctionalField(fldDef, recSource, elmt) {
		def temp = [:]
		if (fldDef.isMeta) {
			temp.put fldDef.to, fldDef.fn(recSource, elmt)
		} else {
			temp.put fldDef.to, fldDef.fn(elmt.value)
		}
		return temp
	}
	
}
