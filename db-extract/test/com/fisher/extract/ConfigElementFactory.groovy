package com.fisher.extract;

import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*

class ConfigElementFactory {
	
	def getStdMergeDef() {
		def mergeDef = 
			new MergeDef(to: "release", fieldTransformDefs: [
					new FieldTransformDef(from: "trainid", to: "id", useSeq: true),
					new FieldTransformDef(from: "train", to: "trainweek", fn: {it}),
					new FieldTransformDef(from: "name", to: "name", fn: {it}),
					new FieldTransformDef(from: "releasestartdate", to: "start_date", fn: {it}),
					new FieldTransformDef(from: "releaseenddate", to: "end_date", fn: {it})
				],
				sourceSql: new SimpleTemplateEngine().createTemplate("SELECT trainid, train, null as name, releasestartdate, releaseenddate FROM cttrain"),
				conditionalSql: new SimpleTemplateEngine().createTemplate('SELECT 1 FROM release WHERE trainweek = ${trainweek}'),
				tableSeq: "seq_release_id"
			);
		return mergeDef
	}
	
	def getStdTableTransformDef() {
		def tblTransDef = 
			new TableTransformDef(to: "person", logModificationData: true, fieldTransformDefs: [
				new FieldTransformDef(from: "person_id", fn: null, to: "id", useSeq: true),
				new FieldTransformDef(from: "name", fn: {it}, to: "login_name")],
			sourceSql: new SimpleTemplateEngine().createTemplate("SELECT person_id,name FROM person"),
			tableSeq: "seq")
		return tblTransDef
	}
}
