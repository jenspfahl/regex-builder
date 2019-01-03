package de.jepfa.regex.constructs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.TestElement;



public class LineStartsWithTest {
	
	private LineStartsWith lineStartsWith;
	
	@Before
	public final void setup() {
		lineStartsWith = new LineStartsWith(new TestElement("one"));
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = lineStartsWith.clone();
		
		Assert.assertNotSame(lineStartsWith, clonedElement);
		Assert.assertEquals(lineStartsWith.toRegex(), clonedElement.toRegex());
	}
	
	@Test
	public final void testNormal() throws Exception {
		lineStartsWith = lineStartsWith.add(new TestElement("two"));
		
		Assert.assertEquals("(^(onetwo).*$)", lineStartsWith.toRegex());
	}
	
	@Test
	public final void testNot() throws Exception {
		lineStartsWith = lineStartsWith.not().add(new TestElement("two"));
		
		Assert.assertEquals("(^(?!onetwo).*$)", lineStartsWith.toRegex());
	}


}
