package com.fisher.extract.defs;

import groovy.util.GroovyTestCase;
import static org.junit.Assert.*
import org.junit.Test
import org.junit.Before
import com.fisher.extract.ConfigElementFactory

class MergeDefTest extends GroovyTestCase {
	def mergeDef
	
	@Before
	public void setUp() {
		mergeDef = new ConfigElementFactory().getStdMergeDef()
	}
	
	@Test
	public void testAssertInvariant() {
		mergeDef.assertInvariant()
		assert true, "we should get here as the std merge def should always be valid"
	}
}
