package com.fisher.extract.config;

import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*

class Config3To3 extends Config {
	
	def mappings = [
	    new TableTransformDef(to: "workflow", fieldTransformDefs: [
	        new FieldTransformDef(from: "id", to: "id", useGenerator: true),
	        new FieldTransformDef(from: "name", to: "name", fn: {it}),
			new FieldTransformDef(from: "type", to: "type", fn: {it}),
			new FieldTransformDef(from: "metadata", to: "metadata", fn: {it}),
			//new FieldTransformDef(from: "parent", fn: {it}, to: "parent"),
			new FieldTransformDef(from: "enabled", to: "enabled", fn: {it}),
			new FieldTransformDef(from: "project_type_id", to: "project_type_id", lookup: "project_type"),
			new FieldTransformDef(from: "project_size_id", to: "project_size_id", lookup: "project_size"),
			new FieldTransformDef(from: "dedicated_team_id", to: "dedicated_team_id", lookup: "dedicated_team")
		],
	    sourceSql: new SimpleTemplateEngine().createTemplate(""),
	    idGenerator: "SEQ_WORKFLOW_ID",
		idGeneratorType: IdGeneratorType.SEQUENCE)
	
	]
	
	def dbSource = [url: "localhost", sid: "orcl", loginName: "tracker30", password: "tracker30"]
	def dbTarget = [url: "localhost", sid: "orcl", loginName: "tracker30", password: "tracker30"]
	
	def lookups = [
	         [table: "project_size", 
		      sqlSource: "SELECT id, name FROM project_size",
		      sqlTarget: "SELECT id, name FROM project_size",
		      areSame: {r, l -> r.name == l.name}],
		    [table: "project_type",
	    	 sqlSource: "SELECT id, name FROM project_type",
	    	 sqlTarget: "SELECT id, name FROM project_type",
	    	 areSame: {r, l -> r.name == l.name}],
	    	[table: "dedicated_team",
	    	 sqlSource: "SELECT id, name FROM dedicated_team",
	    	 sqlTarget: "SELECT id, name FROM dedicated_team",
	    	 areSame: {r, l -> r.name == l.name}]
	    ]
	
}
