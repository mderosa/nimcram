package com.fisher.extract.config;

import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*
import net.sf.json.JSONObject

/**
 * Defines the import actions and their sequences
 * <p>
 * An import consists of three stages merges, lookups, and mappings.  
 * Merges insert records from the source database 
 * into the target database, modifying the target database only if the source records do not already exist. 
 * Merges are used for ref data; the general use case being that both databases share a common set of ref data 
 * however the source database may have a few records that do not already exist in the target
 * Lookups create a one to one mapping between primary keys in both the source and the target.  Lookups are used to
 * maintain data integrity of foreign keys values as rows are inserted into the target database
 * Mappings brings records from the source database into the target database.  The mapping are for records that are
 * entirely new to the database and are usually the records that are of interest for the import. 
 */
class Config2To3 extends Config {
	
	def mappings = [
	    new TableTransformDef(to: "workflow", logModificationData: true, fieldTransformDefs: [
	        new FieldTransformDef(from: "parentid", to: "id", useSeq: true),
			new FieldTransformDef(from: "parent_title", to: "name", fn: {it}),
			new FieldTransformDef(from: 'type', to: 'type', fn: {it}),
			new FieldTransformDef(from: 'metadata', to: 'metadata', fn: {rec, elmt ->
				def jsnInner = [type: 'Project', parent: rec.strategy_id, name: rec.parent_title,
				                targetName: 'eBay Marketplaces', description: rec.description] as JSONObject
				def obj = new JSONObject()
				obj.element rec.parent_title, jsnInner
				return obj.toString()
			}, isMeta: true),
			new FieldTransformDef(from: 'strategy_id', to: 'parent', lookup: 'strategy'),
			new FieldTransformDef(from: 'enabled', to: 'enabled', fn: {it}),
			new FieldTransformDef(from: 'project_type_id', to: 'project_type_id', lookup: 'project_type'),
			new FieldTransformDef(from: 'project_size_name', to: 'project_size_id', lookup: 'project_size'),
			new FieldTransformDef(from: 'dedicated_team_name', to: 'dedicated_team_id', lookup: 'dedicated_team'),
	        new FieldTransformDef(from: 'description', to: 'description', fn: {it}, includeInInsert: false)
	        ],
	    sourceSql: new SimpleTemplateEngine().createTemplate('''
SELECT
cp.parentid, 
cp.parent_title,
'Project' as type,
null as metadata,
cp.strategy_id,
1 as enabled,
pt.project_type_id,
'Medium' as project_size_name,
'Platform And Systems' as dedicated_team_name,
cp.description
FROM ctfeature_parent cp
INNER JOIN project_typess pt ON pt.project_type_id = cp.project_type_id
where parentid = ${projectId}
	    		'''),
	    tableSeq: "seq_workflow_id"),
	    
	    new TableTransformDef(to: "project_task", logModificationData: true, fieldTransformDefs: [
	        new FieldTransformDef(from: "taskid", to: "id", useSeq: true),
			new FieldTransformDef(from: "task_name", to: "name", fn: {it}),
			new FieldTransformDef(from: 'user_id', to: 'person_id', lookup: "person"),
			new FieldTransformDef(from: 'parentid', to: 'project_id', lookup: 'workflow'),
	        ],
	    sourceSql: new SimpleTemplateEngine().createTemplate('''
SELECT DISTINCT
t.task_id, 
t.task_name, 
ta.user_id, 
cp.parentid
FROM ctfeature_parent cp
INNER JOIN taskss t on t.parentid = cp.parentid
INNER JOIN task_assignments ta on ta.task_id = t.task_id
WHERE cp.parentid = ${projectId}
   	    		'''),
   	    tableSeq: "seq_project_task_id")
	]
	
	def dbSource = [url: "cfdb04qa.vip.its.ebay.com", sid: "cfdb04", loginName: "bugsuser", password: "bugs_test"]
	def dbTarget = [url: "localhost", sid: "orcl", loginName: "tracker30", password: "tracker30"]
	
	def lookups = [
			[table: "project_size", 
			sqlSource: new SimpleTemplateEngine().createTemplate("SELECT 'Medium' as id, 'Medium (150-500 Days)' as name FROM dual"),
			sqlTarget: "SELECT id, name FROM project_size",
			areSame: {r, l -> r.name == l.name}],
			[table: "project_type",
			sqlSource: new SimpleTemplateEngine().createTemplate("select project_type_id as id, project_type_name from project_typess"),
			sqlTarget: "SELECT id, name FROM project_type",
			areSame: {r, l -> r.project_type_name == l.name}],
			[table: "dedicated_team",
			sqlSource: new SimpleTemplateEngine().createTemplate("SELECT 'Platform And Systems' as id, 'Platform And Systems' as name FROM dual"),
			sqlTarget: "SELECT id, name FROM dedicated_team",
			areSame: {r, l -> r.name == l.name}],
			[table: 'strategy',
			sqlSource: new SimpleTemplateEngine().createTemplate('SELECT strategy_id as id, strategy FROM rs_strategies'),
			sqlTarget: "SELECT id, name FROM workflow WHERE type in ('Program', 'Strategy')",
			areSame: {r, l -> r.strategy == l.name}],
			[table: "person",
			sqlSource: new SimpleTemplateEngine().createTemplate('''
					SELECT DISTINCT u.user_id as id, u.login_name 
					FROM users u
					INNER JOIN task_assignments ta ON ta.user_id = u.user_id
					INNER JOIN taskss t ON t.task_id = ta.task_id
					INNER JOIN ctfeature_parent cp ON cp.parentid = t.parentid
					WHERE u.user_active = 'Y'
					and cp.parentid = ${projectId}
			'''),
			sqlTarget: "SELECT person_id as id, login_name FROM employee",
			areSame: {r, l -> r.login_name == l.login_name}]
		]
	
	def merges = [
	    new MergeDef(to: "release", fieldTransformDefs: [
	        new FieldTransformDef(from: "train_id", to: "id", useSeq: true),
	        new FieldTransformDef(from: "weeknumber", to: "trainweek", fn: {it}),
	        new FieldTransformDef(from: "name", to: "name", fn: {it}),
	        new FieldTransformDef(from: "releasestartdate", to: "start_date", fn: {it}),
	        new FieldTransformDef(from: "releaseenddate", to: "end_date", fn: {it})
	    ],
	    sourceSql: new SimpleTemplateEngine().createTemplate('''SELECT DISTINCT 
	    	t.train_id, 
	    	t.weeknumber, 
	    	null as name, 
	    	min(c.releasestartdate) as releasestartdate, 
	    	max(c.releaseenddate) as releaseenddate
	    	FROM trains t
	    	INNER JOIN cttrains c on c.train_id = t.train_id
	    	GROUP BY t.train_id, t.weeknumber
	    	HAVING min(c.releasestartdate) > sysdate - 90'''),
	    conditionalSql: new SimpleTemplateEngine().createTemplate('SELECT 1 FROM release WHERE trainweek = ${trainweek}'),
	    tableSeq: "seq_release_id")
	]
	
}
