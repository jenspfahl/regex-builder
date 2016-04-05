package de.jepfa.regex;

import static de.jepfa.regex.TestHelper.*;
import static de.jepfa.regex.elements.SystemElement.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jepfa.regex.RegexBuilder.Flag;
import de.jepfa.regex.components.Element;
import de.jepfa.regex.components.Quantifier;
import de.jepfa.regex.components.Quantifier.Strategy;
import de.jepfa.regex.elements.Chars;
import de.jepfa.regex.elements.Choice;
import de.jepfa.regex.elements.Group;
import de.jepfa.regex.elements.Lookahead;
import de.jepfa.regex.elements.Lookbehind;
import de.jepfa.regex.elements.NonCapturing;
import de.jepfa.regex.elements.StringElement;
import de.jepfa.regex.elements.Strings;
import de.jepfa.regex.elements.SystemElement;
import de.jepfa.regex.elements.Word;
import de.jepfa.regex.elements.Words;
import de.jepfa.regex.helper.Changer;
import de.jepfa.regex.helper.Printer;


/**
 * @author Jens Pfahl
 */
public class RegexBuilderTest {
	
	private RegexBuilder builder;
	
	
	@Before
	public final void setup() {
		builder = new RegexBuilder();
	}

	@Test
	public final void testURL() {
		
		RegexBuilder builder = new RegexBuilder(Flag.MULTILINE);
		
		
		Group protocolGroup = new Group(					// (http[s]?)
				new StringElement("http"),					// http
				new Chars('s').optional()					// [s]?
		);
		
		Group domainGroup = new Group(						// ((\w(\w|[-.])+)\.\p{Alpha}+)
				new Group(									// (\w(\w|[-.])+)
						SystemElement.WORD_CHAR,			// \w
						new Choice(							// (\w|[-.])
								SystemElement.WORD_CHAR,	// \w
								new Chars("-.")				// [-.]
						).many()							// (\w|[-.])+
				),
				new StringElement("."),						// \.
				SystemElement.ALPHA_CHAR.many()				// \p{Alpha}+
		);
		
		Group portGroup = new Group(						// (\d+)
				SystemElement.DIGIT.many()					// \d+
		).optional();										// (\d+)?
		
		Group endpointPathGroup = new Group(				// (.*)									
				SystemElement.ANY							// .*					
		);
		
		
		builder.add( 
				LINE_START,									// ^
				protocolGroup, 								// (http[s]?)
				new StringElement("://"),					// ://
				domainGroup, 								// ((\w(\w|[-.])+)\.\p{Alpha}+)
				SystemElement.ANY_CHAR.optional(),			// .?
				portGroup,									// (\d+)?
				new Chars('/').optional(),					// /?
				endpointPathGroup,							// (.*)
				LINE_END									// $
		);
		
		Assert.assertEquals("^(\\Qhttp\\E[s]?)\\Q://\\E((\\w(\\w|[\\-\\.])+)\\Q.\\E\\p{Alpha}+).?(\\d+)?[\\/]?(.*)$", 
				builder.toRegex());
		
		Pattern pattern = builder.buildPattern();
		
		Matcher matcher = pattern.matcher(
			  "http://my-example.org" + RegexBuilder.NEW_LINE
			+ "http://my-example.org/path/to/endpoint" + RegexBuilder.NEW_LINE
			+ "https://my.example.org:8080/test/regex?WSD" + RegexBuilder.NEW_LINE
			+ "https://my.2nd.complex-example.org:8080/test/&SESSION=2323?TOKEN=#664344");
		
		Assert.assertTrue(matcher.find());
		
		Assert.assertEquals("http", matcher.group(protocolGroup.getIndex()));
		Assert.assertEquals("my-example.org", matcher.group(domainGroup.getIndex()));
		Assert.assertEquals(null, matcher.group(portGroup.getIndex()));
		Assert.assertEquals("", matcher.group(endpointPathGroup.getIndex()));
		
		Assert.assertTrue(matcher.find());
		
		Assert.assertEquals("http", matcher.group(protocolGroup.getIndex()));
		Assert.assertEquals("my-example.org", matcher.group(domainGroup.getIndex()));
		Assert.assertEquals(null, matcher.group(portGroup.getIndex()));
		Assert.assertEquals("path/to/endpoint", matcher.group(endpointPathGroup.getIndex()));
		
		Assert.assertTrue(matcher.find());
		
		Assert.assertEquals("https", matcher.group(protocolGroup.getIndex()));
		Assert.assertEquals("my.example.org", matcher.group(domainGroup.getIndex()));
		Assert.assertEquals("8080", matcher.group(portGroup.getIndex()));
		Assert.assertEquals("test/regex?WSD", matcher.group(endpointPathGroup.getIndex()));
		
		Assert.assertTrue(matcher.find());
		
		Assert.assertEquals("https", matcher.group(protocolGroup.getIndex()));
		Assert.assertEquals("my.2nd.complex-example.org", matcher.group(domainGroup.getIndex()));
		Assert.assertEquals("8080", matcher.group(portGroup.getIndex()));
		Assert.assertEquals("test/&SESSION=2323?TOKEN=#664344", matcher.group(endpointPathGroup.getIndex()));
		
		
	}
	
	@Test
	public final void testExamples_1() {
		doIt("^((\\b(\\Qhallo Jens\\E)\\b)*?)$", 
				LINE_START, 
				new Group(
						new Word(
								new Strings("hallo Jens")).arbitrary().strategy(Quantifier.Strategy.LAZY)
						), 
				LINE_END
				);
	}
	
	@Test
	public final void testExamples_2() {
		doIt("((\\Qbanane\\E)|(?:(\\Qdg\\E)))", 
				new Choice(
						new Strings("banane"), 
						new NonCapturing(new Strings("dg")))
		);
	}
	
	@Test
	public final void testExamples_3() {
		doIt("(\\Qtest\\E)+?", 
				new Strings("test").many().strategy(Quantifier.Strategy.LAZY)
		);
	}
	
	@Test
	public final void testExamples_4() {
		doIt("[a-z123&&[^d-f]]+", 
				new Chars('a', 'z').add("123").subtract(new Chars('d', 'f')).many()
		);
	}
	
	@Test
	public final void testExamples_5() {
		Group group = new Group(new Strings("Jens")).count(7);
		group.add(WORD_CHAR.many()); // has no effect because a new instance is created and thereon added
		Changer.change(group, e -> e.add(WORD_CHAR.many())); // now it HAS effect because clone mode is disabled
		
		doIt("((\\QJens\\E)\\w+){2}+.*((\\QJens\\E)\\w+){4}", 
				group.count(2).strategy(Quantifier.Strategy.POSSESSIVE), 
				ANY_CHAR.optional().many(), 
				group.count(4)
		);
	}
	
	@Test
	public final void testExamples_6() {
		
		Chars selection = new Chars("abc");
		Chars not = selection.not();
		doIt("[abc][^abc]", 
				selection, 
				not
		);
	}
	
	@Test
	public final void testExamples_7() {
		doIt(Arrays.asList("(?ix-m:(?>(\\Qfgh\\E))?)", "(?xi-m:(?>(\\Qfgh\\E))?)"), 
				new NonCapturing(
						new Strings("fgh")).independent().switchOn(Flag.IGNORE_CASE_SENSITIVE, Flag.COMMENTS).switchOff(Flag.MULTILINE).optional()
		);
	}
	
	@Test
	public final void testExamples_8() {
		doIt("(\\Q[\\E).(\\Q]\\E)", 
				new Strings("["), 
				ANY_CHAR, 
				new Strings("]")
		);
	}
	
	@Test
	public final void testExamples_9() {
		
		doIt("(?<!\\w\\d(\\w|\\d))", 
				new Lookbehind(
						WORD_CHAR, 
						DIGIT, 
						new Choice(
								WORD_CHAR, 
								DIGIT
						)
				).not()
		);
	}
	
	@Test
	public final void testExamples_A() {
		doIt("(?i:[asdfX])", 
				new Chars("asdfX").switchOn(Flag.IGNORE_CASE_SENSITIVE)
		);
	}
	
	@Test
	public final void testExamples_B() {
		doIt("(\\b\\QJe\\E|\\QHa\\E\\b)", 
				new Words("Je", "Ha")
		);
	}
	
	@Test
	public final void testExamples_C() {
		doIt("(?m:((\\QCompany\\E)\\s*(\\Q()\\E)\\s*(\\Q==\\E|\\Q!=\\E)\\s*+(?!(\\QCompanyCode\\E))(?!(\\Qnull\\E))))",
				new Group(
					new Strings("Company"), 
					SPACE_CHAR.arbitrary(), 
					new Strings("()"), 
					SPACE_CHAR.arbitrary(), 
					new Strings("==", "!="),
					SPACE_CHAR.arbitrary().strategy(Strategy.POSSESSIVE), 
					new Lookahead(new Strings("CompanyCode")).not(),
					new Lookahead(new Strings("null")).not()
				).switchOn(Flag.MULTILINE)
		);
	}
	
	@Test
	public final void testExamples_D() {
		doIt("(?m:((?!(\\QCompanyCode\\E))(?!(\\Qnull\\E))))(?m:((?!(\\QCompanyCode\\E))(?!(\\Qnull\\E))))",
				new Group(
						new Lookahead(new Strings("CompanyCode")).not(),
						new Lookahead(new Strings("null")).not()
						).switchOn(Flag.MULTILINE),
				new Group(
						new Lookahead(new Strings("CompanyCode")).not(),
						new Lookahead(new Strings("null")).not()
						).switchOn(Flag.MULTILINE)
				);
	}
	
	@Test
	public final void testGroupIndex_WithRepetition() {
		Strings s_a = new Strings("a");
		Strings s_b = new Strings("b");
		Group group_with_b = new Group(s_b);
		
		builder.add(s_a, group_with_b, s_a);
		Pattern pattern = builder.buildPattern();
		Matcher matcher = pattern.matcher("aba");
		assertEquals("(\\Qa\\E)((\\Qb\\E))(\\Qa\\E)", pattern.pattern());
		assertTrue(matcher.matches());
		assertEquals("aba", matcher.group());
		assertEquals(4, matcher.groupCount());
		
		assertEquals("a", matcher.group(s_a.getIndex()));
		assertEquals("a", matcher.group(1));
		assertEquals("a", matcher.group(4));
		
		assertEquals("b", matcher.group(s_b.getIndex()));
		assertEquals("b", matcher.group(2));
		assertEquals("b", matcher.group(3));
		assertEquals("b", matcher.group(group_with_b.getIndex()));
	}
	
	@Test
	public final void testGroupIndex_WithRunGroupIndexer() {
		Group a = new Group(new StringElement("a")); 
		Group b = new Group(new StringElement("b")); 
		Group c = new Group(new StringElement("c")); 
		Group d = new Group(new StringElement("d")); 
		Group e = new Group(new StringElement("e")); 
		Group f = new Group(new StringElement("f")); 
		
		
		builder.add(d, a);
		
		Changer.setChangeable(a, b ,d ,e);
		a.add(b.add(c));
		d.add(e.add(f));
		Changer.unsetChangeable(a, b, d, e);
		
		Printer.showTree = false;
		builder.runGroupIndexer();
		System.out.println(Printer.toString(a, d));
		Printer.showTree = true;

		Pattern pattern = builder.buildPattern();
		
		Matcher matcher = pattern.matcher("defabc");
		assertEquals("(\\Qd\\E(\\Qe\\E(\\Qf\\E)))(\\Qa\\E(\\Qb\\E(\\Qc\\E)))", pattern.pattern());
		assertTrue(matcher.matches());
		assertEquals(6, matcher.groupCount());
		assertEquals("defabc", matcher.group());
		
		assertEquals("def", matcher.group(d.getIndex()));
		assertEquals("def", matcher.group(1));
		assertEquals("ef", matcher.group(e.getIndex()));
		assertEquals("ef", matcher.group(2));
		assertEquals("f", matcher.group(f.getIndex()));
		assertEquals("f", matcher.group(3));
		
		assertEquals("abc", matcher.group(a.getIndex()));
		assertEquals("abc", matcher.group(4));
		assertEquals("bc", matcher.group(b.getIndex()));
		assertEquals("bc", matcher.group(5));
		assertEquals("c", matcher.group(c.getIndex()));
		assertEquals("c", matcher.group(6));
	}
	
	@Test
	public final void testGroupIndex_WithOneNonCapturing() {
		Group a = new Group(new StringElement("a")); 
		Group b = new Group(new StringElement("b")); 
		NonCapturing c = new NonCapturing(new StringElement("c")); 
		
		builder.add(a, Changer.change(b, Group::optional), c);
		Pattern pattern = builder.buildPattern();
		
		Matcher matcher = pattern.matcher("abc");
		assertEquals("(\\Qa\\E)(\\Qb\\E)?(?:\\Qc\\E)", pattern.pattern());
		assertTrue(matcher.matches());
		assertEquals(2, matcher.groupCount());
		assertEquals("abc", matcher.group());
		
		assertEquals("a", matcher.group(a.getIndex()));
		assertEquals("a", matcher.group(1));
		assertEquals("b", matcher.group(b.getIndex()));
		assertEquals("b", matcher.group(2));
		assertNull(c.getIndex());
	}
	
	
	private void doIt(String expectedRegex, Element ...elems) {
		doIt(Collections.singletonList(expectedRegex), elems);
	}
	
	private void doIt(List<String> expectedRegexs, Element ...elems) {
		builder.add(elems);
		Assert.assertEquals(elems.length, builder.getElements().size());
		
		String regex = builder.toRegex();
		System.out.println(regex);
		System.out.println(builder);
		
		orAssertEquals(regex, expectedRegexs);
		Assert.assertNotNull(regex);
		
		Pattern pattern = builder.buildPattern();
		
		Assert.assertNotNull(pattern);
		
	}

}
