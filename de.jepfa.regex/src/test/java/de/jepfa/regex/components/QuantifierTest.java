package de.jepfa.regex.components;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.RegexBuilderException;
import de.jepfa.regex.components.Quantifier;
import de.jepfa.regex.components.Quantifier.Strategy;



public class QuantifierTest {
	
	private Quantifier quantifier;
	
	
	@Before
	public final void setup() {
		quantifier = new Quantifier();
	}

	@Test
	public final void testClone() throws Exception {
		Object clonedQuantifier = quantifier.clone();
		
		Assert.assertNotSame(quantifier, clonedQuantifier);
		
	}
	
	@Test(expected=RegexBuilderException.class)
	public final void testMin_Fail() throws Exception {
		quantifier.setMin(-53);
		
	}
	
	@Test(expected=RegexBuilderException.class)
	public final void testMax_Fail() throws Exception {
		quantifier.setMax(-53);
	}
	
	
	@Test
	public final void testLeast() throws Exception {
		quantifier.setLeast(7);
		
		Assert.assertEquals(7, quantifier.getMin());
		Assert.assertEquals(Quantifier.UNBOUND, quantifier.getMax());
		Assert.assertEquals(Strategy.GREEDY, quantifier.getStrategy());
	}
	
	@Test
	public final void testMost() throws Exception {
		quantifier.setMost(7);
		
		Assert.assertEquals(0, quantifier.getMin());
		Assert.assertEquals(7, quantifier.getMax());
		Assert.assertEquals(Strategy.GREEDY, quantifier.getStrategy());
	}
	
	
	
	
	@Test
	public final void testGreedy_One() throws Exception {
		doIt(1, 1, Strategy.GREEDY, "");
	}
	
	@Test
	public final void testGreedy_ZeroOrOne() throws Exception {
		doIt(0, 1, Strategy.GREEDY, "?");
	}
	
	@Test
	public final void testGreedy_ZeroOrMore() throws Exception {
		doIt(0, Quantifier.UNBOUND, Strategy.GREEDY, "*");
	}
	
	@Test
	public final void testGreedy_OneOrMore() throws Exception {
		doIt(1, Quantifier.UNBOUND, Strategy.GREEDY, "+");
	}
	
	@Test
	public final void testGreedy_ExactN() throws Exception {
		doIt(53, 53, Strategy.GREEDY, "{53}");
	}

	@Test
	public final void testGreedy_AtLeastN() throws Exception {
		doIt(53, Quantifier.UNBOUND, Strategy.GREEDY, "{53,}");
	}
	
	@Test
	public final void testGreedy_AtLeastNNotMoreThanM() throws Exception {
		doIt(53, 54, Strategy.GREEDY, "{53,54}");
	}

	
	
	@Test
	public final void testLazy_One() throws Exception {
		doIt(1, 1, Strategy.LAZY, "");
	}
	
	@Test
	public final void testLazy_ZeroOrOne() throws Exception {
		doIt(0, 1, Strategy.LAZY, "??");
	}
	
	@Test
	public final void testLazy_ZeroOrMore() throws Exception {
		doIt(0, Quantifier.UNBOUND, Strategy.LAZY, "*?");
	}
	
	@Test
	public final void testLazy_OneOrMore() throws Exception {
		doIt(1, Quantifier.UNBOUND, Strategy.LAZY, "+?");
	}
	
	@Test
	public final void testLazy_ExactN() throws Exception {
		doIt(53, 53, Strategy.LAZY, "{53}?");
	}
	
	@Test
	public final void testLazy_AtLeastN() throws Exception {
		doIt(53, Quantifier.UNBOUND, Strategy.LAZY, "{53,}?");
	}
	
	@Test
	public final void testLazy_AtLeastNNotMoreThanM() throws Exception {
		doIt(53, 54, Strategy.LAZY, "{53,54}?");
	}
	
	
	
	
	@Test
	public final void testPossessive_One() throws Exception {
		doIt(1, 1, Strategy.POSSESSIVE, "");
	}
	
	@Test
	public final void testPossessive_ZeroOrOne() throws Exception {
		doIt(0, 1, Strategy.POSSESSIVE, "?+");
	}
	
	@Test
	public final void testPossessive_ZeroOrMore() throws Exception {
		doIt(0, Quantifier.UNBOUND, Strategy.POSSESSIVE, "*+");
	}
	
	@Test
	public final void testPossessive_OneOrMore() throws Exception {
		doIt(1, Quantifier.UNBOUND, Strategy.POSSESSIVE, "++");
	}
	
	@Test
	public final void testPossessive_ExactN() throws Exception {
		doIt(53, 53, Strategy.POSSESSIVE, "{53}+");
	}
	
	@Test
	public final void testPossessive_AtLeastN() throws Exception {
		doIt(53, Quantifier.UNBOUND, Strategy.POSSESSIVE, "{53,}+");
	}
	
	@Test
	public final void testPossessive_AtLeastNNotMoreThanM() throws Exception {
		doIt(53, 54, Strategy.POSSESSIVE, "{53,54}+");
	}
	
	
	
	
	
	
	private void doIt(int min, int max, Strategy type, Object expectedRegex) throws Exception {
		quantifier.setMin(min);
		quantifier.setMax(max);
		quantifier.setStrategy(type);
		
		Assert.assertEquals(min, quantifier.getMin());
		Assert.assertEquals(max, quantifier.getMax());
		Assert.assertEquals(type, quantifier.getStrategy());
		
		Assert.assertEquals(expectedRegex, quantifier.toRegex());
	}
	
	

	


}
