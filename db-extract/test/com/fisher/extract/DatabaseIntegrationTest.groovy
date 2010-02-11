package com.fisher.extract;

import static org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import groovy.text.SimpleTemplateEngine
import groovyjarjarantlr.Version;
import com.fisher.extract.config.*
import com.fisher.extract.defs.*
import java.text.SimpleDateFormat;
import java.util.Date;

class DatabaseIntegrationTest {
	def mapping = 
		new TableTransformDef(to: "role",logModificationData:true, fieldTransformDefs: [
			new FieldTransformDef(from: "id", to: "id", useSeq: true),
			new FieldTransformDef(from: "name", fn: {it}, to: "name"),
			new FieldTransformDef(from: "description", fn: {it}, to: "description")
			],
		sourceSql: new SimpleTemplateEngine().createTemplate('''
				SELECT id,name,description, 'test' as test 
				FROM role WHERE id = ${role_id}
		'''),
		tableSeq: "SEQ_ROLE_ID")
	
	@Test
	public void testNothing() {
		assert true
	}
	
	@Ignore
	@Test
	public void testGetNextKey() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def newKey = db.getNextKey(mapping)
		assert newKey != null
		assert newKey == 111
	}
	
	@Ignore
	@Test
	public void testReadSource() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def data = db.readSource(mapping, [role_id: 6])
		assert data != null
		assert data[0].id == 6
		assert data[0].name == "Project Manager"
	}
	
	@Ignore
	@Test
	public void testWriteTargetData() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def modifiedDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getTime())
		def transformedRec = [id: "{autogen}", name: "just a role", description: "a description", modifiedDate: modifiedDate, modifiedBy: "ETL"]
		def key = db.writeTargetData(mapping, transformedRec)
		assert key != null
//		assert key == 112
	}
	
	/**
	 * a record should be inserted when it does not already exist in the database
	 */
	@Ignore
	@Test
	public void testWriteTargetRefData() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		
		def mergeDef = new ConfigElementFactory().getStdMergeDef()
		def transformedRec = [trainweek: 777, name: null, start_date: null, end_date: null]
		db.writeTargetRefData mergeDef, transformedRec
		assert true, 'no exceptions thrown'
	}
}