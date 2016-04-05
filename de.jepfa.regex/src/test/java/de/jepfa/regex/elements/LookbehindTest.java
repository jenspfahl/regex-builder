package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class LookbehindTest {
	
	private Lookbehind lookbehind;
	
	@Before
	public final void setup() {
		lookbehind = new Lookbehind();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = lookbehind.clone();
		
		Assert.assertNotSame(lookbehind, clonedElement);
		Assert.assertEquals("(?<=)", lookbehind.toRegex());
		Assert.assertEquals("(?<=)", clonedElement.toRegex());
	}
	
	@Test
	public final void testLookbehind() throws Exception {
		TestElement newElem = new TestElement();
		Lookbehind addedLookbehind = lookbehind.add(newElem);
		
		
		Assert.assertNotSame(lookbehind, addedLookbehind);
		Assert.assertEquals(0, lookbehind.getElements().size());
		Assert.assertEquals("(?<=)", lookbehind.toRegex());
		
		Assert.assertEquals(1, addedLookbehind.getElements().size());
		Assert.assertSame(newElem, addedLookbehind.getElements().get(0));
		Assert.assertEquals("(?<=nop)", addedLookbehind.toRegex());
	}
	
	@Test
	public final void testLookbehind_Negative() throws Exception {
		TestElement newElem = new TestElement();
		Lookbehind addedLookbehind = lookbehind.not().add(newElem);
		
		
		Assert.assertNotSame(lookbehind, addedLookbehind);
		Assert.assertEquals(0, lookbehind.getElements().size());
		Assert.assertEquals("(?<=)", lookbehind.toRegex());
		
		Assert.assertEquals(1, addedLookbehind.getElements().size());
		Assert.assertSame(newElem, addedLookbehind.getElements().get(0));
		Assert.assertEquals("(?<!nop)", addedLookbehind.toRegex());
	}
	
	@Test
	public final void testIsIndexable() {
		Assert.assertFalse(lookbehind.isIndexable());
	}
	

}
