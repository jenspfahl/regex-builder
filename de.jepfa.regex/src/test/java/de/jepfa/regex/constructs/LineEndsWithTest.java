package de.jepfa.regex.constructs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.TestElement;



public class LineEndsWithTest {
	
	private LineEndsWith lineEndsWith;
	
	@Before
	public final void setup() {
		lineEndsWith = new LineEndsWith(new TestElement("one"));
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = lineEndsWith.clone();
		
		Assert.assertNotSame(lineEndsWith, clonedElement);
		Assert.assertEquals(lineEndsWith.toRegex(), clonedElement.toRegex());
	}
	
	@Test
	public final void testNormal() throws Exception {
		lineEndsWith = lineEndsWith.add(new TestElement("two"));
		
		Assert.assertEquals("(^.*(onetwo)$)", lineEndsWith.toRegex());
	}
	
	@Test
	public final void testNot() throws Exception {
		lineEndsWith = lineEndsWith.not().add(new TestElement("two"));
		
		Assert.assertEquals("(^.*(?<!onetwo)$)", lineEndsWith.toRegex());
	}


}
