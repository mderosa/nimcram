package com.fisher.extract.defs;

import static org.junit.Assert.*
import org.junit.Test
import com.fisher.extract.ConfigElementFactory

class TableTransformationDefTest {
	
	/**
	 * If a table transform definition specifies an idGenerator than it should also specify
	 * the type of that generator
	 */
	@Test(expected=AssertionError.class)
	public final void testTableTransformationConfiguration1() {
		TableTransformDef ttDef = new TableTransformDef(
				to: 'id',
				fieldTransformDefs: [new FieldTransformDef(from: "parentid", to: "id", useGenerator: true)],
				sourceSql: 'SELECT * FROM project_space',
				idGenerator: 'default'			
			)
		ttDef.assertInvariant()
	}
	
	@Test
	public final void testTargetPrimaryKey() {
		def ttDef = new ConfigElementFactory().getStdTableTransformDef()
		def actual = ttDef.targetPrimaryKey()
		assert "id" == actual
	}
}
