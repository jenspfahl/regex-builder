package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class LookaheadTest {
	
	private Lookahead lookahead;
	
	@Before
	public final void setup() {
		lookahead = new Lookahead();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = lookahead.clone();
		
		Assert.assertNotSame(lookahead, clonedElement);
		Assert.assertEquals("(?=)", lookahead.toRegex());
		Assert.assertEquals("(?=)", clonedElement.toRegex());
	}
	
	@Test
	public final void testLookahead() throws Exception {
		TestElement newElem = new TestElement();
		Lookahead addedLookahead = lookahead.add(newElem);
		
		
		Assert.assertNotSame(lookahead, addedLookahead);
		Assert.assertEquals(0, lookahead.getElements().size());
		Assert.assertEquals("(?=)", lookahead.toRegex());
		
		Assert.assertEquals(1, addedLookahead.getElements().size());
		Assert.assertSame(newElem, addedLookahead.getElements().get(0));
		Assert.assertEquals("(?=nop)", addedLookahead.toRegex());
	}
	
	@Test
	public final void testLookahead_Negative() throws Exception {
		TestElement newElem = new TestElement();
		Lookahead addedLookahead = lookahead.not().add(newElem);
		
		
		Assert.assertNotSame(lookahead, addedLookahead);
		Assert.assertEquals(0, lookahead.getElements().size());
		Assert.assertEquals("(?=)", lookahead.toRegex());
		Assert.assertNull(lookahead.getIndex());
		
		Assert.assertEquals(1, addedLookahead.getElements().size());
		Assert.assertSame(newElem, addedLookahead.getElements().get(0));
		Assert.assertEquals("(?!nop)", addedLookahead.toRegex());
		Assert.assertNull(addedLookahead.getIndex());
	}
	
	@Test
	public final void testIsIndexable() {
		Assert.assertFalse(lookahead.isIndexable());
	}
	

}
