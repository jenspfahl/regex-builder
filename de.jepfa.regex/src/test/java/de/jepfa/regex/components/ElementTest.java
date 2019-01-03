package de.jepfa.regex.components;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.RegexBuilder.Flag;
import de.jepfa.regex.components.Quantifier.Strategy;
import de.jepfa.regex.elements.TestElement;



public class ElementTest {
	
	private Element element;
	
	@Before
	public final void setup() {
		element = new TestElement();
	}

	
	@Test
	public final void testClone() throws Exception {
		
		Element clonedElement = element.clone();
		
		Assert.assertNotSame(element, clonedElement);
		Assert.assertNotSame(element.getQuantifier(), clonedElement.getQuantifier());
		Assert.assertNotSame(element.getFlags(), clonedElement.getFlags());
		Assert.assertEquals("nop", element.toRegex());
		Assert.assertEquals("nop", clonedElement.toRegex());
	}
	
	@Test
	public final void testStrategy() throws Exception {
		Element lazyElement = element.strategy(Quantifier.Strategy.LAZY);
		
		Assert.assertNotSame(element, lazyElement);
		Assert.assertEquals(Strategy.GREEDY, element.getQuantifier().getStrategy());
		Assert.assertEquals(Strategy.LAZY, lazyElement.getQuantifier().getStrategy());
	}
	
	@Test
	public final void testDefault() throws Exception {
		Assert.assertEquals(1, element.getQuantifier().getMin());
		Assert.assertEquals(1, element.getQuantifier().getMax());
	}
	
	@Test
	public final void testOptional() throws Exception {
		Element optionalElement = element.optional();
		
		Assert.assertNotSame(element, optionalElement);
		Assert.assertEquals(0, optionalElement.getQuantifier().getMin());
		Assert.assertEquals(1, optionalElement.getQuantifier().getMax());
	}
	
	@Test
	public final void testMany() throws Exception {
		Element manyElement = element.many();
		
		Assert.assertNotSame(element, manyElement);
		Assert.assertEquals(1, manyElement.getQuantifier().getMin());
		Assert.assertEquals(Quantifier.UNBOUND, manyElement.getQuantifier().getMax());
	}
	
	@Test
	public final void arbitrary() throws Exception {
		Element arbitraryElement = element.arbitrary();
		
		Assert.assertNotSame(element, arbitraryElement);
		Assert.assertEquals(0, arbitraryElement.getQuantifier().getMin());
		Assert.assertEquals(Quantifier.UNBOUND, arbitraryElement.getQuantifier().getMax());
	}
	
	@Test
	public final void testCount() throws Exception {
		Element countElement = element.count(5);
		
		Assert.assertNotSame(element, countElement);
		Assert.assertEquals(5, countElement.getQuantifier().getMin());
		Assert.assertEquals(5, countElement.getQuantifier().getMax());
	}
	
	@Test
	public final void testRange() throws Exception {
		Element rangeElement = element.range(2,6);
		
		Assert.assertNotSame(element, rangeElement);
		Assert.assertEquals(2, rangeElement.getQuantifier().getMin());
		Assert.assertEquals(6, rangeElement.getQuantifier().getMax());
	}
	
	@Test
	public final void testLeast() throws Exception {
		Element leastElement = element.least(8);
		
		Assert.assertNotSame(element, leastElement);
		Assert.assertEquals(8, leastElement.getQuantifier().getMin());
		Assert.assertEquals(Quantifier.UNBOUND, leastElement.getQuantifier().getMax());
	}
	
	@Test
	public final void testMost() throws Exception {
		Element mostElement = element.most(8);
		
		Assert.assertNotSame(element, mostElement);
		Assert.assertEquals(0, mostElement.getQuantifier().getMin());
		Assert.assertEquals(8, mostElement.getQuantifier().getMax());
	}
	
	
	
	@Test
	public final void testMostLeast_LeastWins() throws Exception {
		Element newElement = element.most(16).least(8);
		
		Assert.assertNotSame(element, newElement);
		Assert.assertEquals(8, newElement.getQuantifier().getMin());
		Assert.assertEquals(Quantifier.UNBOUND, newElement.getQuantifier().getMax());
	}
	
	
	@Test
	public final void testLeastMost_MostWins() throws Exception {
		Element newElement = element.least(8).most(16);
		
		Assert.assertNotSame(element, newElement);
		Assert.assertEquals(0, newElement.getQuantifier().getMin());
		Assert.assertEquals(16, newElement.getQuantifier().getMax());
	}
	
	
	@Test
	public final void testSwitchOn() throws Exception {
		Element newElement = element.switchOn(Flag.IGNORE_CASE_SENSITIVE, Flag.DOTALL);
		
		Assert.assertNotSame(element, newElement);
		Assert.assertTrue(element.getFlags().isEmpty());
		Assert.assertFalse(element.getFlags().getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertFalse(element.getFlags().getEnabledFlags().contains(Flag.DOTALL));
		
		Assert.assertFalse(newElement.getFlags().isEmpty());
		Assert.assertEquals(2, newElement.getFlags().getEnabledFlags().size());
		Assert.assertTrue(newElement.getFlags().getEnabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(newElement.getFlags().getEnabledFlags().contains(Flag.DOTALL));
	}
	
	@Test
	public final void testSwitchOff() throws Exception {
		Element newElement = element.switchOff(Flag.IGNORE_CASE_SENSITIVE, Flag.DOTALL);
		
		Assert.assertNotSame(element, newElement);
		Assert.assertTrue(element.getFlags().isEmpty());
		Assert.assertFalse(element.getFlags().getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertFalse(element.getFlags().getDisabledFlags().contains(Flag.DOTALL));
		
		Assert.assertFalse(newElement.getFlags().isEmpty());
		Assert.assertEquals(2, newElement.getFlags().getDisabledFlags().size());
		Assert.assertTrue(newElement.getFlags().getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(newElement.getFlags().getDisabledFlags().contains(Flag.DOTALL));
	}
	
	@Test
	public final void testRestoreFlags() throws Exception {
		Element offElement = element.switchOff(Flag.IGNORE_CASE_SENSITIVE, Flag.DOTALL);
		Element restoredElement = offElement.restoreFlags(Flag.IGNORE_CASE_SENSITIVE);
		
		
		Assert.assertNotSame(element, offElement);
		Assert.assertFalse(offElement.getFlags().isEmpty());
		Assert.assertEquals(2, offElement.getFlags().getDisabledFlags().size());
		Assert.assertTrue(offElement.getFlags().getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(offElement.getFlags().getDisabledFlags().contains(Flag.DOTALL));
		
		Assert.assertNotSame(offElement, restoredElement);
		Assert.assertFalse(restoredElement.getFlags().isEmpty());
		Assert.assertFalse(restoredElement.getFlags().getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(restoredElement.getFlags().getDisabledFlags().contains(Flag.DOTALL));
		Assert.assertEquals(1, restoredElement.getFlags().getDisabledFlags().size());
	}
	
	@Test
	public final void testRestoreAllFlags() throws Exception {
		Element offElement = element.switchOff(Flag.IGNORE_CASE_SENSITIVE, Flag.DOTALL);
		Element restoredElement = offElement.restoreAllFlags();
		
		
		Assert.assertNotSame(element, offElement);
		Assert.assertFalse(offElement.getFlags().isEmpty());
		Assert.assertEquals(2, offElement.getFlags().getDisabledFlags().size());
		Assert.assertTrue(offElement.getFlags().getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertTrue(offElement.getFlags().getDisabledFlags().contains(Flag.DOTALL));
		
		Assert.assertNotSame(offElement, restoredElement);
		Assert.assertTrue(restoredElement.getFlags().isEmpty());
		Assert.assertFalse(restoredElement.getFlags().getDisabledFlags().contains(Flag.IGNORE_CASE_SENSITIVE));
		Assert.assertFalse(restoredElement.getFlags().getDisabledFlags().contains(Flag.DOTALL));
	}
	
	
	
}
