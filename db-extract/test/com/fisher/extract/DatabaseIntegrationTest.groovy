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
	
	@Test
	public void testNothing() {
		assert true
	}
	
	/**
	 * When we are doing a increment type key generation then we should get the next
	 * maximum key value from the table of interest
	 */
	@Ignore
	@Test
	public void testGetNextKeyIncrement() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def ttDef = new ConfigElementFactory().getRoleToRoleTableTransformDef()
		ttDef.idGeneratorType = IdGeneratorType.INCREMENT
		def newKey = db.getNextKey(ttDef)
		assert newKey != null
		assert newKey == 67
	}
	
	@Ignore
	@Test
	public void testGetNextKey() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def ttDef = new ConfigElementFactory().getRoleToRoleTableTransformDef()
		def newKey = db.getNextKey(ttDef)
		assert newKey != null
		assert newKey == 111
	}
	
	@Ignore
	@Test
	public void testReadSource() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def ttDef = new ConfigElementFactory().getRoleToRoleTableTransformDef()
		def data = db.readSource(ttDef, [role_id: 6])
		assert data != null
		assert data[0].id == 6
		assert data[0].name == "Project Manager"
	}
	
	@Ignore
	@Test
	public void testWriteTargetData() {
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		def ttDef = new ConfigElementFactory().getRoleToRoleTableTransformDef()
		def modifiedDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getTime())
		def transformedRec = [id: "{autogen}", name: "just a role", description: "a description", modifiedDate: modifiedDate, modifiedBy: "ETL"]
		def key = db.writeTargetData(ttDef, transformedRec)
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
	
	@Ignore
	@Test
	public void testQueryExpandedLookup() {
		def expanderDef = new DynamicLookupExpanderDef(
		sourceKeyName : 'workflow',
		createKeyName : 'product',
		lookupSql : '''
				SELECT p.id
				FROM workflow w 
				INNER JOIN product_workflow pw ON pw.workitem_id = w.id
				INNER JOIN product p ON p.id = pw.product_id
				WHERE w.id = ?'''
		)
		def sourceMap = [535:535, 536:536]
		def config = new Config3To3()
		def db = new Database(config.dbSource, config.dbTarget)
		
		def newMap = db.queryExpandedLookup(sourceMap, expanderDef)
		newMap.each {k, v ->
			assert k == 535 || k == 536, "we should retain the original keys"
			assert v == 6, "we should have new values"
		}
	}
}
