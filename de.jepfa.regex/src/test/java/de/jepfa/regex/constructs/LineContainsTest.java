package de.jepfa.regex.constructs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.TestElement;



public class LineContainsTest {
	
	private LineContains lineContains;
	
	@Before
	public final void setup() {
		lineContains = new LineContains(new TestElement("one"));
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = lineContains.clone();
		
		Assert.assertNotSame(lineContains, clonedElement);
		Assert.assertEquals(lineContains.toRegex(), clonedElement.toRegex());
	}
	
	@Test
	public final void testNormal() throws Exception {
		lineContains = lineContains.add(new TestElement("two"));
		
		Assert.assertEquals("(^.*(onetwo).*$)", lineContains.toRegex());
	}
	
	@Test
	public final void testNot() throws Exception {
		lineContains = lineContains.not().add(new TestElement("two"));
		
		Assert.assertEquals("(^((?!onetwo).)*$)", lineContains.toRegex());
	}

}
