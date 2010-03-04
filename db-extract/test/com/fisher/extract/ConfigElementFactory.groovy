package com.fisher.extract;

import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*

class ConfigElementFactory {
	
	def getStdMergeDef() {
		def mergeDef = 
			new MergeDef(to: "release", fieldTransformDefs: [
					new FieldTransformDef(from: "trainid", to: "id", useGenerator: true),
					new FieldTransformDef(from: "train", to: "trainweek", fn: {it}),
					new FieldTransformDef(from: "name", to: "name", fn: {it}),
					new FieldTransformDef(from: "releasestartdate", to: "start_date", fn: {it}),
					new FieldTransformDef(from: "releaseenddate", to: "end_date", fn: {it})
				],
				sourceSql: new SimpleTemplateEngine().createTemplate("SELECT trainid, train, null as name, releasestartdate, releaseenddate FROM cttrain"),
				conditionalSql: new SimpleTemplateEngine().createTemplate('SELECT 1 FROM release WHERE trainweek = ${trainweek}'),
				idGenerator: "seq_release_id",
				idGeneratorType: IdGeneratorType.SEQUENCE
			);
		return mergeDef
	}
	
	def getStdTableTransformDef() {
		def tblTransDef = 
			new TableTransformDef(to: "person", logModificationData: true, fieldTransformDefs: [
				new FieldTransformDef(from: "person_id", fn: null, to: "id", useGenerator: true),
				new FieldTransformDef(from: "name", fn: {it}, to: "login_name")],
			sourceSql: new SimpleTemplateEngine().createTemplate("SELECT person_id,name FROM person"),
			idGenerator: "seq",
			idGeneratorType: IdGeneratorType.SEQUENCE)
		return tblTransDef
	}
	
	def getRoleToRoleTableTransformDef() {
		return new TableTransformDef(to: "role", logModificationData:true, fieldTransformDefs: [
				new FieldTransformDef(from: "id", to: "id", useGenerator: true),
				new FieldTransformDef(from: "name", fn: {it}, to: "name"),
				new FieldTransformDef(from: "description", fn: {it}, to: "description")
			],
			sourceSql: new SimpleTemplateEngine().createTemplate('''
		        SELECT id,name,description, 'test' as test 
		        FROM role WHERE id = ${role_id}
		    '''),
			idGenerator: "SEQ_ROLE_ID",
			idGeneratorType: IdGeneratorType.SEQUENCE)
	}
}
