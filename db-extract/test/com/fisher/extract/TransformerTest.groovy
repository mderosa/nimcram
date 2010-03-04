package com.fisher.extract;

import groovy.util.GroovyTestCase
import static org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*
import net.sf.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class TransformerTest extends GroovyTestCase {
	def basicDef
	def repo
	def environment
	
	@Before
	public void setUp() {
		basicDef = new TableTransformDef(to: "person", fieldTransformDefs: [
				new FieldTransformDef(from: "person_id", fn: {it}, to: "id"),
				new FieldTransformDef(from: "name", fn: {"yo"}, to: "login_name")],
			sourceSql: new SimpleTemplateEngine().createTemplate(""),
			idGenerator: "seq")
		
		repo = new LookupRepository()
		repo.lookupTables.put "project_type", [1:2, 2:3, 3:4]
		
		environment = [projectId: 123, modifiedDate: new java.sql.Date(new Date().getTime()), modifiedBy: 'ETL']
	}
	
	/**
	 * a transformed record should be complete when there are no primary keys
	 */
	@Test
	public void testCompleteTransformSourceData() {
		def data = [person_id: 1, name: "marc"]
		def trn = new Transformer()
		def actual = trn.transformSourceData(basicDef, data, environment)
		assert [id: 1, login_name: "yo"] == actual
	}
	
	
	/**
	 * a transformed record should be complete when there are no primary keys
	 */
	@Test
	public void testTransformSourceDataWithModification() {
		basicDef.logModificationData = true
		def data = [person_id: 1, name: "marc"]
		
		def trn = new Transformer()
		def actual = trn.transformSourceData(basicDef, data, environment)
		assert [id: 1, login_name: "yo", modified_date: environment.modifiedDate, modified_by: "ETL"] == actual
	}
	/**
	 * a transformed record should contain a substitute field to be completed
	 * later when there are primary keys
	 */
	@Test
	public void testIncompleteTransformSourceData() {
		basicDef.fieldTransformDefs[0].useGenerator = true
		def data = [person_id: 1, name: "marc"]
		
		def trn = new Transformer()
		def actual = trn.transformSourceData(basicDef, data, environment)
		assert [id: "{autogen}", login_name: "yo"] == actual
	}
	
	/**
	 * for records that have a lookup field we should get run a pk mapping lookup
	 * in from the repository 
	 */
	@Test
	public void testTransformationsFromLookupTables() {
		basicDef.fieldTransformDefs << new FieldTransformDef(from: "project_type_id", lookup: "project_type", to: "project_type_id")
		
		def trans = new Transformer(this.repo)
		def actual = trans.transformSourceData(basicDef, [person_id: 1, name: "marc", project_type_id: 2], [:])
		assert 1 == actual.id
		assert "yo" == actual.login_name
		assert 3 == actual.project_type_id
	}
	
	/**
	 * For the standard transform situation, where the primary keys of any inserted records
	 * will be generated by a sequence in the target database, we should get a record
	 * of n-1 length back for each input of n length
	 */
	@Test
	public void testTransformRefData() {
		def mergeDef = new ConfigElementFactory().getStdMergeDef()
		mergeDef.logModificationData = true
		def now = new Date()
		def data = [trainid: 1, train: 234, name: null, releasestartdate: now, releaseenddata: now]
		
		def trans = new Transformer(this.repo)
		def actual = trans.transformRefData(mergeDef, data, environment)
		assert 234 == actual.trainweek
		assert now == actual.start_date
		assert environment.modifiedDate == actual.modified_date
		assert "ETL".equals(actual.modified_by)
		assert 6 == actual.size()
	}
	
	/**
	 * When a field is defined as isMeta we want to process the given field according to metadata 
	 * processing defined in fn
	 */
	@Test
	public void testTransformFunctionalFieldForMetadata() {
		def fldDef = new FieldTransformDef(from: "source", to: "target", isMeta: true, fn: {rec, elmt -> 
			def obj = new JSONObject()
			obj = [1: rec.one, 2: rec.two] as JSONObject
			obj.toString()
		})
		def rec = [one: 'first', two: 'second']
		
		def trans = new Transformer()
		def actual = trans.transformFunctionalField(fldDef, rec, null)
		assert [target: '{"1":"first","2":"second"}'] == actual
	}
	
	/**
	 * During the transformation process we use all of the fields in recSource to produce recTarget
	 * however we should not include fields marked as includeInInsert = false directly in the target
	 * recordset
	 */
	@Test
	public void testInclusionOfFieldsMarkedAsIncludeInInsert() {
		basicDef.fieldTransformDefs <<  new FieldTransformDef(from: 'description', to: 'description', fn: {it}, includeInInsert: false)
		def recSrc = [person_id: 1, name: "marc", description: 'a description']
		
		def trans = new Transformer()
		def actual = trans.transformSourceData(basicDef, recSrc, environment)
		assert [id: 1, login_name: "yo"] == actual
	}
	
	/**
	 * Particularly for stored proc updates we want to be able to run a stored proc where we are not feeding
	 * the new pk to the proc -- we will get it from the table of interest after the call to the stored proc.
	 * For this use case we want the table transform def to specify what the primary key is but we dont
	 * want the primary key to be added to the transformed dataset by the Transformer
	 */
	@Test
	public void testPrimaryKeysMardkedAsDontIncludeAreNotIncluded() {
		def ttDef = new ConfigElementFactory().getRoleToRoleTableTransformDef();
		ttDef.fieldTransformDefs[0].includeInInsert = false
		ttDef.idGenerator = 'default'
		ttDef.idGeneratorType = IdGeneratorType.INCREMENT
		ttDef.logModificationData = false
		def recSrc = [id: null, name: 'name', description: 'description']
		
		def trans = new Transformer()
		def actual = trans.transformSourceData(ttDef, recSrc, environment)
		assert [name: 'name', description: 'description'] == actual
	}

}
