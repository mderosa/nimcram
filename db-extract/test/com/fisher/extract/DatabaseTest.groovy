package com.fisher.extract;

import groovy.util.GroovyTestCase
import static org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.FieldTransformDef

class DatabaseTest extends GroovyTestCase {
	def basicDef
	
	@Before
	void setUp() {
		basicDef = new ConfigElementFactory().getStdTableTransformDef()
	}
	
	/**
	 * should produce an insert statement with a sequence at the pk position
	 */
	@Test
	public void testTransformDefToInsert() {
		def expected = "INSERT INTO person (id,login_name,modified_date,modified_by) VALUES (?,?,?,?)"
		
		def db = new Database()
		def actual = db.transformDefToInsert(basicDef)
		assert expected == actual
	}
	
	/**
	 * should transform a table transform definition into a select clause 
	 */
	@Test
	public void testTransformDefToSelect() {
		def expected = "SELECT person_id,name FROM person"
		
		def db = new Database()
		def actual = db.transformDefToSelect(basicDef, [:])
		assert expected == actual
	}
	
	/**
	 * should create an sql clause with join and where info when that info
	 * is present in the def
	 */
	@Test
	public void testTransformDefToSelect2() {
		basicDef.sourceSql = new SimpleTemplateEngine().createTemplate("SELECT person_id,name FROM person WHERE 1 = 2")
		def expected = "SELECT person_id,name FROM person WHERE 1 = 2"
		
		def db = new Database()
		def actual = db.transformDefToSelect(basicDef, [:])
		assert expected == actual
	}
	
	/**
	 * should produce a valid insert clause
	 */
	@Test
	public void testMergeDefToInsert() {
		def mergeDef = new ConfigElementFactory().getStdMergeDef()
		def db = new Database()
		def actual = db.mergeDefToInsert(mergeDef)
		def expected = "INSERT INTO release (id,trainweek,name,start_date,end_date) " +
			"VALUES (seq_release_id.nextval,?,?,?,?)"
		assert expected == actual
	}
	
	/**
	 * should produce a valid insert clause
	 */
	@Test
	public void testMergeDefToInsertWithModification() {
		def mergeDef = new ConfigElementFactory().getStdMergeDef()
		mergeDef.logModificationData = true
		def db = new Database()
		def actual = db.mergeDefToInsert(mergeDef)
		def expected = "INSERT INTO release (id,trainweek,name,start_date,end_date,modified_date,modified_by) " +
				"VALUES (seq_release_id.nextval,?,?,?,?,?,?)"
		assert expected == actual
	}
	
	/**
	 * should be able to generate a select clause from out standard merge def
	 */
	@Test
	public void testMergeDefSelectGeneration() {
		def mergeDef = new ConfigElementFactory().getStdMergeDef()
		def db = new Database()
		def actual = db.transformDefToSelect(mergeDef, [:])
		def expected = "SELECT trainid, train, null as name, releasestartdate, releaseenddate FROM cttrain"
		assert expected == actual
	}
	
	/**
	 * field definitions that are marked as includeInInsert = false should not be included in
	 * any insert statement to the database
	 */
	@Test
	public void testTableDefToInsertIncludeInInsertFalse() {
		def tableDef = new ConfigElementFactory().getStdTableTransformDef()
		tableDef.fieldTransformDefs << 
			new FieldTransformDef(from: "status", fn: {it}, to: "status", includeInInsert: false)
		
		def db = new Database()
		def actual = db.transformDefToInsert(tableDef)
		def expected = "INSERT INTO person (id,login_name,modified_date,modified_by) VALUES (?,?,?,?)"
		assert expected == actual
	}
	
	/**
	 * field definitions that are marked as includeInInsert = false should not be included during
	 * merge inserts
	 */
	@Test
	public void testMergeDefToInsertIncludeInInsertFalse() {
		def mergeDef = new ConfigElementFactory().getStdMergeDef()
		mergeDef.fieldTransformDefs <<
			new FieldTransformDef(from: "status", fn: {it}, to: "status", includeInInsert: false)
		
		def db = new Database()
		def actual = db.mergeDefToInsert(mergeDef)
		def expected = "INSERT INTO release (id,trainweek,name,start_date,end_date) VALUES " +
			"(seq_release_id.nextval,?,?,?,?)"
		assert expected == actual
	}
}
