package com.fisher.extract.defs;

import groovy.util.GroovyTestCase;
import static org.junit.Assert.*
import org.junit.Test
import org.junit.Before

class FieldTransformDefTest /*extends GroovyTestCase*/ {
	
	/**
	 * The invariant should check to make sure that the lookup if it exists is equal to a table name
	 */
	@Test
	public void testFieldTransforDefAssert() {
		def td = new FieldTransformDef(from: 'id', to: 'diff_id', lookup: "table");
		td.assertInvariant();
		assertTrue (true)
	}
	
	/**
	 * A field that contains json metadata should always have an associated transformation function
	 * associated with it 
	 */
	@Test(expected=AssertionError.class)
	public void testFieldTransformDefAssertJson() {
		def td = new FieldTransformDef(from: 'id', to: 'other_id', isMeta: true, lookup: "table_name")
		td.assertInvariant()
	}
	
	/**
	 * A FieldTransformDef.fn should take only one parameter if it takes two it should be marked as
	 * isMeta
	 */
	@Test(expected=AssertionError.class)
	public void testFieldTransformDefAssertFunctionArgs() {
		def td = new FieldTransformDef(from: 'old', to: 'new', fn: {rec, elmt ->
			return rec.one + rec.two
		})
		td.assertInvariant()
	}
}
