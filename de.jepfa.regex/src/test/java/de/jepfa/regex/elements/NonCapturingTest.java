package de.jepfa.regex.elements;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.components.Element;



public class NonCapturingTest {
	
	private NonCapturing nonCapturing;
	
	@Before
	public final void setup() {
		nonCapturing = new NonCapturing();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedNonCapturing = nonCapturing.clone();
		
		Assert.assertNotSame(nonCapturing, clonedNonCapturing);
		Assert.assertEquals("(?:)", nonCapturing.toRegex());
		Assert.assertEquals("(?:)", clonedNonCapturing.toRegex());
	}
	
	@Test
	public final void testNonCapturing() throws Exception {
		TestElement newElem = new TestElement();
		NonCapturing addedNonCapturing = nonCapturing.add(newElem);
		
		
		Assert.assertNotSame(nonCapturing, addedNonCapturing);
		Assert.assertEquals(0, nonCapturing.getElements().size());
		Assert.assertEquals("(?:)", nonCapturing.toRegex());
		
		Assert.assertEquals(1, addedNonCapturing.getElements().size());
		Assert.assertSame(newElem, addedNonCapturing.getElements().get(0));
		Assert.assertEquals("(?:nop)", addedNonCapturing.toRegex());
	}
	
	@Test
	public final void testIndependent() throws Exception {
		TestElement newElem = new TestElement();
		NonCapturing independentNonCapturing = nonCapturing.independent().add(newElem);
		
		
		Assert.assertNotSame(nonCapturing, independentNonCapturing);
		Assert.assertEquals(0, nonCapturing.getElements().size());
		Assert.assertEquals("(?:)", nonCapturing.toRegex());
		
		Assert.assertEquals(1, independentNonCapturing.getElements().size());
		Assert.assertSame(newElem, independentNonCapturing.getElements().get(0));
		Assert.assertEquals("(?>nop)", independentNonCapturing.toRegex());
	}
	
	@Test
	public final void testIsIndexable() {
		Assert.assertFalse(nonCapturing.isIndexable());
	}
	
	@Test
	public final void testGetIndex() {
				
		Assert.assertNull(nonCapturing.getIndex());
		NonCapturing addedNonCapturing = nonCapturing.add(new TestElement());
		Assert.assertNull(addedNonCapturing.getIndex());
				
	}
	

}
