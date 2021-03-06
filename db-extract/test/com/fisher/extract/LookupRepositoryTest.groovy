package com.fisher.extract;

import groovy.util.GroovyTestCase;
import static org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*

class LookupRepositoryTest extends GroovyTestCase {
	def remote
	def local
	def cnfg
	def mapping
	
	@Before
	public void setUp() {
		remote = [[id: 1, name: "one"],
		          [id: 2, name: "two"],
		          [id: 3, name: "three"]]
		local = [[id: 2, name: "three"],
		         [id: 3, name: "four"]]
		cnfg = [table: "table", sqlSource: null, sqlTarget: null, areSame: {r, l -> r.name == l.name}]
		
		mapping = 
		    new TableTransformDef(to: "workflow", fieldTransformDefs: [
				new FieldTransformDef(from: "id", fn: {it}, to: "id", useGenerator: true),
				new FieldTransformDef(from: "name", fn: {it}, to: "name"),
				new FieldTransformDef(from: "type", fn: {it}, to: "type")],
			sourceSql: new SimpleTemplateEngine().createTemplate("SELECT id,name,type FROM workflow"),
			idGenerator: "")
	}
	
	/**
	 * should join the maps on id 
	 */
	@Test
	public void testInnerJoin() {
		def repo = new LookupRepository()
		def actual = repo.innerJoin(remote, local, cnfg)
		assert actual.size() == 1
		assert actual.get(3) == 2
	}
	
	/**
	 * should return a mapped local pk given a table name and source pk
	 */
	@Test
	public void testQueryRepo() {
		def repo = new LookupRepository()
		repo.lookupTables.put "aTable", [1: 2, 2: 3, 3: 4]
		def actual = repo.queryRepo("aTable", 3)
		assert 4 == actual
	}
	
	/**
	 * should initialize a repository with two pk to pk mappings
	 */
	@Test
	public void testInitRepository() {
		local << [id: 23, name: "one"]
		def config = [lookups: [
	         [table: "project_size", 
		      sqlSource: "SELECT id, name FROM project_size",
		      sqlTarget: "SELECT id, name FROM project_size",
		      areSame: {r, l -> r.name == l.name}]
			]
		]
		def db = [queryLookupTables: {cfg, env ->
			[remote, local]
		}]
		
		def repo = new LookupRepository()
		repo.config = config
		repo.db = db
		repo.initRepository([:])
		
		assert 2 == repo.queryRepo("project_size", 3)
		assert 23 == repo.queryRepo("project_size", 1)
	}
	
	/**
	 * if not suitable match exists in a lookup table then return a null value
	 * for that lookup
	 */
	@Test
	public void testNoLookupResultReturnsNull() {
		def repo = new LookupRepository()
		repo.lookupTables.put "table_name", [1:2, 2:3]
		def actual = repo.queryRepo("table_name", 3)
		assert null == actual
	}
	
	/**
	 * no changes should occur in the repo when a new pk is not generated
	 */
	@Test
	public void testUpdateRepoForNoNewPk() {
		def repo = new LookupRepository()
		repo.lookupTables.put "table_name", [1:2, 2:3]
		
		repo.updateRepo mapping, [1, "aName", "aType"], null
		assert 1 == repo.lookupTables.size()
		assert null != repo.lookupTables.get("table_name")
		assert null == repo.lookupTables.get("workflow")
	}
	
	/**
	 * if the lookup table does not already exist in the repo create it
	 * and add one key map entry
	 */
	@Test
	public void testCreationOfNewLookupMap() {
		def repo = new LookupRepository()
		repo.lookupTables.put "table_name", [1:2, 2:3]
		
		repo.updateRepo mapping, [1, "aName", "aType"], 5
		assert 2 == repo.lookupTables.size()
		def workflowLookupTable = repo.lookupTables.get("workflow")
		assert workflowLookupTable != null
		assert 5 == repo.queryRepo("workflow", 1)
	}
	
	/**
	 * if the lookup table exists then an entry is added added to the map
	 */
	@Test
	public void testStandardUpdateCase() {
		def repo = new LookupRepository()
		repo.lookupTables.put "table_name", [1:2, 2:3]
		mapping.to = "table_name"
		
		repo.updateRepo mapping, [3, "aName", "aType"], 4
		assert 1 == repo.lookupTables.size()
		def workflowLookupTable = repo.lookupTables.get("table_name")
		assert workflowLookupTable != null
		assert 4 == repo.queryRepo("table_name", 3)
	}
}
