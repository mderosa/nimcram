package com.fisher.extract.config;

import groovy.text.SimpleTemplateEngine
import com.fisher.extract.defs.*
import net.sf.json.JSONObject
import groovy.sql.Sql

class Config2ToPnet extends Config {
	
	def mappings = [
	//bring the project back
	new TableTransformDef(to: "pn_project_space", fieldTransformDefs: [
	    new FieldTransformDef(from: 'project_id', to: 'project_id', useGenerator: true, includeInInsert: false),                                                              
		new FieldTransformDef(from: 'p_proj_creator', to: 'p_proj_creator', fn: {it}),
		new FieldTransformDef(from: "p_subproject_of", to: "p_subproject_of", fn: {it}),
		new FieldTransformDef(from: 'p_business_space_id', to: 'p_business_space_id', fn: {it}),
		new FieldTransformDef(from: 'p_project_visibility', to: 'p_project_visibility', fn: {it}),
		new FieldTransformDef(from: 'p_proj_name', to: 'p_proj_name', fn: {it}),
		
		new FieldTransformDef(from: 'p_proj_desc', to: 'p_proj_desc', fn: {it}),
		new FieldTransformDef(from: 'p_proj_status', to: 'p_proj_status', fn: {it}),
		new FieldTransformDef(from: 'p_proj_color_code', to: 'p_proj_color_code', fn: {it}),
		new FieldTransformDef(from: 'p_calculation_method', to: 'p_calculation_method', fn: {it}),
		new FieldTransformDef(from: 'p_percent_complete', to: 'p_percent_complete', fn: {it}),
		
		new FieldTransformDef(from: 'p_start_date', to: 'p_start_date', fn: {it}),
		new FieldTransformDef(from: 'p_end_date', to: 'p_end_date', fn: {it}),
		new FieldTransformDef(from: 'p_serial', to: 'p_serial', fn: {it}),
		new FieldTransformDef(from: 'p_project_logo_id', to: 'p_project_logo_id', fn: {it}),
		new FieldTransformDef(from: 'p_default_currency_code', to: 'p_default_currency_code', fn: {it}),
		
		new FieldTransformDef(from: 'p_sponsor', to: 'p_sponsor', fn: {it}),
		new FieldTransformDef(from: 'p_improvement_code_id', to: 'p_improvement_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_current_status_description', to: 'p_current_status_description', fn: {it}),
		new FieldTransformDef(from: 'p_financial_stat_color_code_id', to: 'p_financial_stat_color_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_financial_stat_imp_code_id', to: 'p_financial_stat_imp_code_id', fn: {it}),
		
		new FieldTransformDef(from: 'p_budgeted_total_cost_value', to: 'p_budgeted_total_cost_value', fn: {it}),
		new FieldTransformDef(from: 'p_budgeted_total_cost_cc', to: 'p_budgeted_total_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_current_est_total_cost_value', to: 'p_current_est_total_cost_value', fn: {it}),
		new FieldTransformDef(from: 'p_current_est_total_cost_cc', to: 'p_current_est_total_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_actual_to_date_cost_value', to: 'p_actual_to_date_cost_value', fn: {it}),
		
		new FieldTransformDef(from: 'p_actual_to_date_cost_cc', to: 'p_actual_to_date_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_estimated_roi_cost_value', to: 'p_estimated_roi_cost_value', fn: {it}),
		new FieldTransformDef(from: 'p_estimated_roi_cost_cc', to: 'p_estimated_roi_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_cost_center', to: 'p_cost_center', fn: {it}),
		new FieldTransformDef(from: 'p_schedule_stat_color_code_id', to: 'p_schedule_stat_color_code_id', fn: {it}),
		
		new FieldTransformDef(from: 'p_schedule_stat_imp_code_id', to: 'p_schedule_stat_imp_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_resource_stat_color_code_id', to: 'p_resource_stat_color_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_resource_stat_imp_code_id', to: 'p_resource_stat_imp_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_priority_code_id', to: 'p_priority_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_risk_rating_code_id', to: 'p_risk_rating_code_id', fn: {it}),
		
		new FieldTransformDef(from: 'p_visibility_id', to: 'p_visibility_id', fn: {it}),
		new FieldTransformDef(from: 'p_autocalc_schedule', to: 'p_autocalc_schedule', fn: {it}),
		new FieldTransformDef(from: 'p_plan_name', to: 'p_plan_name', fn: {it}),
		new FieldTransformDef(from: 'p_create_share', to: 'p_create_share', fn: {it})
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
			SELECT
			cp.parentid as project_id,
			'1' as p_proj_creator,
			'0' as p_subproject_of,
			null as p_business_space_id,
			'Global' as p_project_visibility,
			cp.parent_title || '10' as p_proj_name,
			
			cp.description as p_proj_desc,
			'200' as p_proj_status,
			'100' as p_proj_color_code,
			'schedule' as p_calculation_method,
			null as p_percent_complete, 
			
			date_added as p_start_date,
			null as p_end_date,
			null as p_serial,
			null as p_project_logo_id,
			'USD' as p_default_currency_code,
			
			null as p_sponsor,
			200 as p_improvement_code_id,
			null as p_current_status_description,
			null as p_financial_stat_color_code_id,
			200 as p_financial_stat_imp_code_id,
			
			null as p_budgeted_total_cost_value,
			null as p_budgeted_total_cost_cc,
			null as p_current_est_total_cost_value,
			null as p_current_est_total_cost_cc,
			null as p_actual_to_date_cost_value,
			
			null as p_actual_to_date_cost_cc,
			null as p_estimated_roi_cost_value,
			null as p_estimated_roi_cost_cc,
			null as p_cost_center,
			null as p_schedule_stat_color_code_id,
			
			200 as p_schedule_stat_imp_code_id,
			null as p_resource_stat_color_code_id,
			200 as p_resource_stat_imp_code_id,
			null as p_priority_code_id,
			null as p_risk_rating_code_id,
			
			300 as p_visibility_id,
			1 as p_autocalc_schedule,
			cp.parent_title || ' Schedule' as p_plan_name,
			0 as p_create_share
			FROM ctfeature_parent cp
			WHERE cp.parentid = ${projectId}
	        	    		'''),
	idGenerator: "default",
	idGeneratorType: IdGeneratorType.INCREMENT,
	writer: [method: WriteMethod.PROC, 
	         statement: "{call PROJECT.CREATE_PROJECT(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)}",
	         outputParams: [Sql.INTEGER, Sql.INTEGER, Sql.INTEGER],
	         outputParamsLocation: OutputParamsLocation.POST]),
			
	//link the project to appadmin
	new TableTransformDef(to: "pn_space_has_space", fieldTransformDefs: [
		new FieldTransformDef(from: "parent_space_id", to: "parent_space_id", fn: {it}),
		new FieldTransformDef(from: "child_space_id", to: "child_space_id", lookup: 'pn_project_space'),
		new FieldTransformDef(from: "created_by", to: "created_by", fn: {it}),
		new FieldTransformDef(from: 'date_created', to: 'date_created', fn: {it}),
		new FieldTransformDef(from: 'relationship_parent_to_child', to: 'relationship_parent_to_child', fn: {it}),
		new FieldTransformDef(from: 'relationship_child_to_parent', to: 'relationship_child_to_parent', fn: {it}),
		new FieldTransformDef(from: 'record_status', to: 'record_status', fn: {it}),
		new FieldTransformDef(from: 'parent_space_type', to: 'parent_space_type', fn: {it}),
		new FieldTransformDef(from: 'child_space_type', to: 'child_space_type', fn: {it})
		],
		sourceSql: new SimpleTemplateEngine().createTemplate('''
				SELECT 
				1 as parent_space_id,
				cp.parentid as child_space_id,
				1 as created_by,
				sysdate as date_created,
				'owns' as relationship_parent_to_child,
				'owned_by' as relationship_child_to_parent,
				'A' as record_status,
				'person' as parent_space_type,
				'project' as child_space_type
				FROM ctfeature_parent cp
				WHERE cp.parentid = ${projectId}
                	    		''')),
                	    		
	//bring the features back
	new TableTransformDef(to: "pn_project_space", fieldTransformDefs: [
		new FieldTransformDef(from: 'subproject_id', to: 'project_id', useGenerator: true, includeInInsert: false),                                                              
		new FieldTransformDef(from: 'p_proj_creator', to: 'p_proj_creator', fn: {it}),
		new FieldTransformDef(from: "p_subproject_of", to: "p_subproject_of", fn: {it}),
		new FieldTransformDef(from: 'p_business_space_id', to: 'p_business_space_id', lookup: "pn_project_space"),
		new FieldTransformDef(from: 'p_project_visibility', to: 'p_project_visibility', fn: {it}),
		new FieldTransformDef(from: 'p_proj_name', to: 'p_proj_name', fn: {it}),
		
		new FieldTransformDef(from: 'p_proj_desc', to: 'p_proj_desc', fn: {it}),
		new FieldTransformDef(from: 'p_proj_status', to: 'p_proj_status', fn: {
			if (it == "Open") {
				200
			} else {
				400
			}
		}),
		new FieldTransformDef(from: 'p_proj_color_code', to: 'p_proj_color_code', fn: {
			if (it == "Green") {
				100
			} else if (it == "Yellow") {
				200
			} else {
				300
			}
		}),
		new FieldTransformDef(from: 'p_calculation_method', to: 'p_calculation_method', fn: {it}),
		new FieldTransformDef(from: 'p_percent_complete', to: 'p_percent_complete', fn: {it}),
		
		new FieldTransformDef(from: 'p_start_date', to: 'p_start_date', fn: {it}),
		new FieldTransformDef(from: 'p_end_date', to: 'p_end_date', fn: {it}),
		new FieldTransformDef(from: 'p_serial', to: 'p_serial', fn: {it}),
		new FieldTransformDef(from: 'p_project_logo_id', to: 'p_project_logo_id', fn: {it}),
		new FieldTransformDef(from: 'p_default_currency_code', to: 'p_default_currency_code', fn: {it}),
		
		new FieldTransformDef(from: 'p_sponsor', to: 'p_sponsor', fn: {it}),
		new FieldTransformDef(from: 'p_improvement_code_id', to: 'p_improvement_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_current_status_description', to: 'p_current_status_description', fn: {it}),
		new FieldTransformDef(from: 'p_financial_stat_color_code_id', to: 'p_financial_stat_color_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_financial_stat_imp_code_id', to: 'p_financial_stat_imp_code_id', fn: {it}),
		
		new FieldTransformDef(from: 'p_budgeted_total_cost_value', to: 'p_budgeted_total_cost_value', fn: {it}),
		new FieldTransformDef(from: 'p_budgeted_total_cost_cc', to: 'p_budgeted_total_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_current_est_total_cost_value', to: 'p_current_est_total_cost_value', fn: {it}),
		new FieldTransformDef(from: 'p_current_est_total_cost_cc', to: 'p_current_est_total_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_actual_to_date_cost_value', to: 'p_actual_to_date_cost_value', fn: {it}),
		
		new FieldTransformDef(from: 'p_actual_to_date_cost_cc', to: 'p_actual_to_date_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_estimated_roi_cost_value', to: 'p_estimated_roi_cost_value', fn: {it}),
		new FieldTransformDef(from: 'p_estimated_roi_cost_cc', to: 'p_estimated_roi_cost_cc', fn: {it}),
		new FieldTransformDef(from: 'p_cost_center', to: 'p_cost_center', fn: {it}),
		new FieldTransformDef(from: 'p_schedule_stat_color_code_id', to: 'p_schedule_stat_color_code_id', fn: {it}),
		
		new FieldTransformDef(from: 'p_schedule_stat_imp_code_id', to: 'p_schedule_stat_imp_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_resource_stat_color_code_id', to: 'p_resource_stat_color_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_resource_stat_imp_code_id', to: 'p_resource_stat_imp_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_priority_code_id', to: 'p_priority_code_id', fn: {it}),
		new FieldTransformDef(from: 'p_risk_rating_code_id', to: 'p_risk_rating_code_id', fn: {it}),
		
		new FieldTransformDef(from: 'p_visibility_id', to: 'p_visibility_id', fn: {it}),
		new FieldTransformDef(from: 'p_autocalc_schedule', to: 'p_autocalc_schedule', fn: {it}),
		new FieldTransformDef(from: 'p_plan_name', to: 'p_plan_name', fn: {it}),
		new FieldTransformDef(from: 'p_create_share', to: 'p_create_share', fn: {it})
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
		    SELECT
		    c.itemid as subproject_id,
			'1' as p_proj_creator,
			to_char(cp.parentid) as p_subproject_of,
			null as p_business_space_id,
			'Global' as p_project_visibility,
			c.feature as p_proj_name,
			
			c.description as p_proj_desc,
			c.state as p_proj_status,
			c.status as p_proj_color_code,
			'schedule' as p_calculation_method,
			null as p_percent_complete, 
			
			null as p_start_date,
			c.close_date as p_end_date,
			null as p_serial,
			null as p_project_logo_id,
			'USD' as p_default_currency_code,
			
			null as p_sponsor,
			200 as p_improvement_code_id,
			null as p_current_status_description,
			null as p_financial_stat_color_code_id,
			200 as p_financial_stat_imp_code_id,
			
			null as p_budgeted_total_cost_value,
			null as p_budgeted_total_cost_cc,
			null as p_current_est_total_cost_value,
			null as p_current_est_total_cost_cc,
			null as p_actual_to_date_cost_value,
			
			null as p_actual_to_date_cost_cc,
			null as p_estimated_roi_cost_value,
			null as p_estimated_roi_cost_cc,
			null as p_cost_center,
			null as p_schedule_stat_color_code_id,
			
			200 as p_schedule_stat_imp_code_id,
			null as p_resource_stat_color_code_id,
			200 as p_resource_stat_imp_code_id,
			null as p_priority_code_id,
			null as p_risk_rating_code_id,
			
			300 as p_visibility_id,
			1 as p_autocalc_schedule,
			c.feature || ' Schedule' as p_plan_name,
			0 as p_create_share
			FROM ctfeature_parent cp
      		INNER JOIN ctfeaturenew c ON c.parentid = cp.parentid
			WHERE cp.parentid = ${projectId}
				        	    		'''),
	idGenerator: "default",
	idGeneratorType: IdGeneratorType.INCREMENT,
	writer: [method: WriteMethod.PROC, 
	statement: "{call PROJECT.CREATE_PROJECT(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)}",
	outputParams: [Sql.INTEGER, Sql.INTEGER, Sql.INTEGER],
	outputParamsLocation: OutputParamsLocation.POST]),
	
	//link the features to appadmin
	new TableTransformDef(to: "pn_space_has_space", fieldTransformDefs: [
	new FieldTransformDef(from: "parent_space_id", to: "parent_space_id", fn: {it}),
	new FieldTransformDef(from: "child_space_id", to: "child_space_id", lookup: 'pn_project_space'),
	new FieldTransformDef(from: "created_by", to: "created_by", fn: {it}),
	new FieldTransformDef(from: 'date_created', to: 'date_created', fn: {it}),
	new FieldTransformDef(from: 'relationship_parent_to_child', to: 'relationship_parent_to_child', fn: {it}),
	new FieldTransformDef(from: 'relationship_child_to_parent', to: 'relationship_child_to_parent', fn: {it}),
	new FieldTransformDef(from: 'record_status', to: 'record_status', fn: {it}),
	new FieldTransformDef(from: 'parent_space_type', to: 'parent_space_type', fn: {it}),
	new FieldTransformDef(from: 'child_space_type', to: 'child_space_type', fn: {it})
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
			SELECT 
			1 as parent_space_id,
			c.itemid as child_space_id,
			1 as created_by,
			sysdate as date_created,
			'owns' as relationship_parent_to_child,
			'owned_by' as relationship_child_to_parent,
			'A' as record_status,
			'person' as parent_space_type,
			'project' as child_space_type
			FROM ctfeature_parent cp
			INNER JOIN ctfeaturenew c ON c.parentid = cp.parentid
			WHERE cp.parentid = ${projectId}
                	    		''')),
                	    		
	//link the features to their containing project
	new TableTransformDef(to: "pn_space_has_space", fieldTransformDefs: [
		new FieldTransformDef(from: "parent_space_id", to: "parent_space_id", lookup: 'pn_project_space'),
		new FieldTransformDef(from: "child_space_id", to: "child_space_id", lookup: 'pn_project_space'),
		new FieldTransformDef(from: "created_by", to: "created_by", fn: {it}),
		new FieldTransformDef(from: 'date_created', to: 'date_created', fn: {it}),
		new FieldTransformDef(from: 'relationship_parent_to_child', to: 'relationship_parent_to_child', fn: {it}),
		new FieldTransformDef(from: 'relationship_child_to_parent', to: 'relationship_child_to_parent', fn: {it}),
		new FieldTransformDef(from: 'record_status', to: 'record_status', fn: {it}),
		new FieldTransformDef(from: 'parent_space_type', to: 'parent_space_type', fn: {it}),
		new FieldTransformDef(from: 'child_space_type', to: 'child_space_type', fn: {it})
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
			SELECT 
			cp.parentid as parent_space_id,
			c.itemid as child_space_id,
			1 as created_by,
			sysdate as date_created,
			'superspace' as relationship_parent_to_child,
			'subspace' as relationship_child_to_parent,
			'A' as record_status,
			'project' as parent_space_type,
			'project' as child_space_type
			FROM ctfeature_parent cp
			INNER JOIN ctfeaturenew c ON c.parentid = cp.parentid
			WHERE cp.parentid = ${projectId}
                	    		''')),

	//begin task creation, object definitions first
	new TableTransformDef(to: "pn_object", fieldTransformDefs: [
		new FieldTransformDef(from: "object_id", to: "object_id", useGenerator: true),
		new FieldTransformDef(from: "object_type", to: "object_type", fn: {it}),
		new FieldTransformDef(from: "date_created", to: "date_created", fn: {it}),
		new FieldTransformDef(from: 'created_by', to: 'created_by', fn: {it}),
		new FieldTransformDef(from: 'record_status', to: 'record_status', fn: {it})
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
			SELECT 
			t.task_id as object_id,
			'task' as object_type,
			sysdate as date_created,
			1 as created_by,
			'A' as record_status
			FROM ctfeature_parent cp
			INNER JOIN ctfeaturenew c ON c.parentid = cp.parentid
			INNER JOIN taskss t on t.itemid = c.itemid
			WHERE cp.parentid = ${projectId}
                	    		'''),
	idGenerator: 'pn_object_sequence',
	idGeneratorType: IdGeneratorType.SEQUENCE),
	
	//begin task creation
	new TableTransformDef(to: "pn_task", fieldTransformDefs: [
		new FieldTransformDef(from: "task_id", to: "task_id", lookup: 'pn_object'),
		new FieldTransformDef(from: "task_name", to: "task_name", fn: {it}),
		new FieldTransformDef(from: "task_desc", to: "task_desc", fn: {it}),
		new FieldTransformDef(from: 'task_type', to: 'task_type', fn: {it}),
		new FieldTransformDef(from: 'priority', to: 'priority', fn: {it}),
		new FieldTransformDef(from: 'date_created', to: 'date_created', fn: {it}),
		new FieldTransformDef(from: 'date_modified', to: 'date_modified', fn: {it}),
		new FieldTransformDef(from: 'modified_by', to: 'modified_by', fn: {it}),
		new FieldTransformDef(from: 'record_status', to: 'record_status', fn: {it}),
		new FieldTransformDef(from: 'critical_path', to: 'critical_path', fn: {it}),
		new FieldTransformDef(from: 'seq', to: 'seq', fn: {it}),
		new FieldTransformDef(from: 'ignore_times_for_dates', to: 'ignore_times_for_dates', fn: {it}),
		new FieldTransformDef(from: 'is_milestone', to: 'is_milestone', fn: {it}),
		new FieldTransformDef(from: 'calculation_type_id', to: 'calculation_type_id', fn: {it}),
		new FieldTransformDef(from: 'duration', to: 'duration', fn: {it}),
		new FieldTransformDef(from: 'duration_units', to: 'duration_units', fn: {it}),
		new FieldTransformDef(from: 'work', to: 'work', fn: {it}),
		new FieldTransformDef(from: 'work_units', to: 'work_units', fn: {it}),
		new FieldTransformDef(from: 'work_complete', to: 'work_complete', fn: {it})
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
			SELECT 
			t.task_id,
			t.task_name,
			t.task_desc,
			'task' as task_type,
			20 as priority,
			t.task_create_date as date_created,
			t.modified_date as date_modified,
			1 as modified_by,
			'A' as record_status,
			1 as critical_path,
			1 as seq,
			0 as ignore_times_for_dates,
			0 as is_milestone,
			10 as calculation_type_id,
			nvl2(sum(5 * ta_percentage / 100), sum(5 * ta_percentage / 100), 0) as duration,
			avg(8) as duration_units,
			nvl2(sum(5 * ta_percentage / 100 * 8), sum(5 * ta_percentage / 100 * 8), 0) as work,
			avg(4) as work_units,
			avg(0) as work_complete
			FROM ctfeature_parent cp
			INNER JOIN ctfeaturenew c ON c.parentid = cp.parentid
			INNER JOIN taskss t on t.itemid = c.itemid
			LEFT JOIN task_assignments ta ON ta.task_id = t.task_id
			WHERE cp.parentid = ${projectId}
			GROUP BY t.task_id, t.task_name, t.task_desc, 'task', 20, t.task_create_date, t.modified_date, 1, 'A', 1, 1, 0, 0, 10
                	    		''')),

    //link project ids to plan ids
	new DynamicLookupExpanderDef(
		sourceKeyName : 'pn_project_space', 
		createKeyName : 'project_to_plan',
		lookupSql : '''
			SELECT plan_id 
			FROM pn_space_has_plan 
			WHERE space_id = ?
			'''
	),
	
	//finish task creation, associate the tasks with a plan
	new TableTransformDef(to: "pn_plan_has_task", fieldTransformDefs: [
		new FieldTransformDef(from: "plan_id", to: "plan_id", lookup: 'project_to_plan'),
		new FieldTransformDef(from: "task_id", to: "task_id", lookup: 'pn_object')
	],
	sourceSql: new SimpleTemplateEngine().createTemplate('''
			SELECT
			t.itemid as plan_id,
			t.task_id as task_id
			FROM ctfeature_parent cp
			INNER JOIN ctfeaturenew c ON c.parentid = cp.parentid
			INNER JOIN taskss t on t.itemid = c.itemid
			WHERE cp.parentid = ${projectId}
                	    		'''))
	]
	
	def dbSource = [url: "cfdb04qa.vip.its.ebay.com", sid: "cfdb04", loginName: "bugsuser", password: "bugs_test"]
	def dbTarget = [url: "localhost", sid: "orcl", loginName: "pnet", password: "pnet"]
	
	def lookups = []
	
	def merges = []
}
