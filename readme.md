# Regular Expression Builder for Java #

## Introduction ##

Writing Regular Expressions can be very difficult and exhausting.
A lot of brackets and special characters hinder the readability. And a single misplaced token can make your expression corrupted. 
So I had the idea to create an API for creating Regular Expressions with predefined Java classes.

## Objective ##

This API is appropiated to build complex Regular Expressions with emphasis on readability and comprehensibility. It is not appropiated to create performant and fancy Regular Expression strings. The readability comes with the API, not with the built Regular Expression string.

Another intention is to understand and learn Regular Expressions. If you understand it, you are free to use Regular Expressions directly. It is meaningful to comment your expression explizitly, even it's complex.

## Disclaimer ##

The current implementation is in beta state. There is no warranty at all. Feel free to improve the current implementation.





## Usage ##
### Overview ###

The core is the `RegexBuilder` and a bunch of `Element`s.
Here is a sample how it basically works:

	Pattern p = new RegexBuilder(new Chars("ace")).buildPattern();
	Matcher m = p.matcher("abcdef");
	...

This example creates a `Pattern` that matches all characters *'a'*, *'c'* and *'e'* in the String *"abcdef"*.

### Functional design ###

Any modification on an `Element` applies to a new instance and let the original Element unchanged. This is to avoid side effects.
The following sample demonstrates the purpose of this design decision:


    Element string = new StringElement("foo"); 
    new RegexBuilder(string, string.many());

The method `many()` return a new instance of type `StringElement` with the same state like the `string`-instance **plus** the modification done by this method. So, this manipulation doesn't *contanimate* the state of the `string`-instance.
 

----------

### Elements ###

#### Manipulation ####

As descriped before, all elements are **immutable** and any modification results in a cloned instance of the original. If you want to change an instance without cloning, you can activate the *change-mode* on an element with `setChangeable()`. To deactivate this mode use `unsetChangeable()`.

#### Group elements ####

Java Regular Expression offers **backreferences**, that can be accessed directly with the `Matcher.group(int)`-method. All Elements that supports backreferences are *indexable* and inherits from `Group`. That means there is a `isIndexable()` and `getIndex()` method.

Here is a sample how you can access groups:

	Group g1 = new Group(Char.WORD.many());
	Group g2 = new Group(Char.SPACE.many());
	Pattern p = new RegexBuilder(g1, g2).buildPattern();
	Matcher m = p.matcher("foo bar");
	m.group(g1.getIndex()); // matches "foo"
	m.group(g2.getIndex()); // matches " " after "foo"


#### Overview of elements ####

All elements are contained in the package `de.jepfa.regex.elements`.

<table>
    <tr>
		<th>Element</th>
		<th>Description</th>
		<th>Regex meaning as example</th>
		<th>Changeable</th>
		<th>Indexable</th>
	</tr>

	<tr>
		<td><pre>Chars</pre></td>
		<td>A Character Set, a set of characters</td>
		<td><code>[ac-z\s]</code></td>
		<td>Yes</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Group</pre></td>
		<td>A group of Elements. Match results of Groups can be accessed with Matcher.group(int)</td>
		<td><code>(foo*[bB]ar)</code></td>
		<td>Yes</td>
		<td>Yes</td>
    </tr>
	<tr>
        <td><pre>Choice</pre></td>
		<td>A Group of Elements that are joined with logical ORs</td>
		<td><code>(foo*|[bB]ar|bear)</code></td>
		<td>Yes</td>
		<td>Yes</td>
    </tr>
	<tr>
        <td><pre>StringElement</pre></td>
		<td>A quoted common String.</td>
		<td><code>\Qfoo\E</code></td>
		<td>Yes</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Strings</pre></td>
		<td>A Choice of common Strings.</td>
		<td><code>(foo|bar|bear)</code></td>
		<td>Yes</td>
		<td>Yes</td>
    </tr>
	<tr>
        <td><pre>Word</pre></td>
		<td>A Group of Elements that is wrapped with word-boundaries</td>
		<td><code>(\bfoo*[bB]ar\b)</code></td>
		<td>Yes</td>
		<td>Yes</td>
    </tr>
	<tr>
        <td><pre>Words</pre></td>
		<td>A Choice of common Strings, wrapped with word-boundaries</td>
		<td><code>(\bfoo\b|\bbar\b|\bbear\b)</code></td>
		<td>Yes</td>
		<td>Yes</td>
    </tr>
	<tr>
        <td><pre>NonCapturing</pre></td>
		<td>A Group of Elements that cannot be accessed with Matcher.group(int)</td>
		<td><code>(?:foo*[bB]ar)</code></td>
		<td>Yes</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Lookahead</pre></td>
		<td>A non-capturing lookahead instruction</td>
		<td><code>(?=foo*[bB]ar)</code></td>
		<td>Yes</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Lookbehind</pre></td>
		<td>A  non-capturing lookbehind instruction</td>
		<td><code>(?&lt;=foo*[bB]ar)</code></td>
		<td>Yes</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>PlainElement</pre></td>
		<td>A plain, not modified or enriched Regex element as a common String.</td>
		<td><code>al\l ((is po?ss[ib]le</code></td>
		<td>Yes</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Any</pre></td>
		<td>Predefined expressions for wildcards</td>
		<td><code>.</code></td>
		<td>No, use <code>changeable()</code> to become a changeable element of it</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Boundary</pre></td>
		<td>Predefined expressions of boundary matches</td>
		<td><code>^</code></td>
		<td>No, use <code>changeable()</code> to become a changeable element of it</td>
		<td>No</td>
    </tr>
	<tr>
        <td><pre>Char</pre></td>
		<td>Predefined expressions of character classes</td>
		<td><code>\d</code></td>
		<td>No, use <code>changeable()</code> to become a changeable element of it</td>
		<td>No</td>
    </tr>
</table>



----------

#### Quantifiers ####

Each `Element` has its own `Quantifier`. A Quantifier defines the quantification of an element and the matching strategy.

<table>
    <tr>
		<th>Element method</th>
		<th>Description</th>
		<th>Regex meaning</th>
		<th>Min</th>
		<th>Max</th>
	</tr>

	<tr>
		<td><pre>optional()</pre></td>
		<td>Indicates zero occurrences of the corresponding element.</td>
		<td><code>?</code> or <code>*</code></td>
		<td>0</td>
		<td>?</td>
    </tr>
	<tr>
		<td><pre>many()</pre></td>
		<td>Indicates infinity occurrences of the corresponding element.</td>
		<td><code>+</code> or <code>*</code></td>
		<td>?</td>
		<td>&infin;</td>
    </tr>
	<tr>
		<td><pre>arbitrary()</pre></td>
		<td>Indicates zero or infinity occurrences of the corresponding element.</td>
		<td><code>*</code></td>
		<td>0</td>
		<td>&infin;</td>
    </tr>
	<tr>
		<td><pre>least(n)</pre></td>
		<td>Indicates at least <i>n</i> occurrences of the corresponding element.</td>
		<td><code>{n,}</code></td>
		<td>n</td>
		<td>&infin;</td>
    </tr>
	<tr>
		<td><pre>most(n)</pre></td>
		<td>Indicates at most <i>n</i> occurrences of the corresponding element.</td>
		<td><code>{0,n}</code></td>
		<td>0</td>
		<td>n</td>
    </tr>
	<tr>
		<td><pre>count(n)</pre></td>
		<td>Indicates extactly <i>n</i> occurrences of the corresponding element.</td>
		<td><code>{n}</code></td>
		<td>n</td>
		<td>n</td>
    </tr>
	<tr>
		<td><pre>range(m, n)</pre></td>
		<td>Indicates <i>m</i> till <i>n</i> occurrences of the corresponding element.</td>
		<td><code>{m,n}</code></td>
		<td>m</td>
		<td>n</td>
    </tr>
</table>

Matching strategies are defined in the enum `Quantifier.Strategy`.

<table>
    <tr>
		<th>Matching strategy</th>
		<th>Description</th>
		<th>Regex extention to quantifer</th>
		<th>Regex example</th>
	</tr>

	<tr>
		<td><pre>GREEDY</pre></td>
		<td>Match as many characters as possible.</td>
		<td><i>none</i></td>
		<td><code>a{2}</code></td>
    </tr>
	<tr>
		<td><pre>LAZY</pre></td>
		<td>Match as few characters as possible.</td>
		<td><code>?</code></td>
		<td><code>a{2}?</code></td>
    </tr>
	<tr>
		<td><pre>POSSESSIVE</pre></td>
		<td>Match as many characters as possible but don't use backtracking.</td>
		<td><code>+</code></td>
		<td><code>a{2}+</code></td>
    </tr>
</table>



----------

#### Flags ####

Every `Element` can enable or disable `Flags` in the scope of itself with `switchOn(Flag...)` or  `switchOff(Flag...)`.
These flags override the flags set on the RegexBuilder with `new RegexBuilder(Flag...)`.

All flags are defined in the enum `RegexBuilder.Flag`. The flags go with the constants defined in the class `java.util.regex.Pattern`.

Usual flags are `IGNORE_CASE_SENSITIVE` or `MULTILINE`.



----------

### Constructs ###

Constructs are predefined Groups of Elements with dynamic content. For example creating a Regular Expression for matching lines that **not contains** certain strings is very tough. To reuse such Regular Expressions, Constructs are common.

Predefined constructs are contained in the package `de.jepfa.regex.constructs`.

You are welcome to create your own constructs.


## Examples ##

### Disassemble a URL ###

Example to disassemble a URL in its following terms:

- transfer protocol
- domain
- optionally a port
- endpoint path 

URL examples:

- `http://my-example.org`
- `http://my-example.org/path/to/endpoint`
- `https://my.example.org:8080/test/regex?WSD`
- `https://my.2nd.complex-example.org:8080/test/&SESSION=2323?TOKEN=#664344`

This is a possible Regular Expression for that challenge.

`^(http[s]?)://((\w(\w|[-.])+)\.\p{Alpha}+).?(\d+)?/?(.*)$`


With the RegexBuilder, it looks like follows:

		RegexBuilder builder = new RegexBuilder(Flag.MULTILINE);
		
		
		Group protocolGroup = new Group(					// (http[s]?)
				new StringElement("http"),					//  http
				new Chars('s').optional()					//      [s]?
		);
		
		Group domainGroup = new Group(						// ((\w(\w|[-.])+)\.\p{Alpha}+)
				new Group(									//  (\w(\w|[-.])+)
						Char.WORD_CHAR,						//   \w
						new Choice(							//     (\w|[-.])
								Char.WORD_CHAR,				//      \w
								new Chars("-.")				//         [-.]
						).many()							//     (\w|[-.])+
				),
				new Chars('.'),								//                \.
				Char.ALPHA_CHAR.many()						//                  \p{Alpha}+
		);
		
		Group portGroup = new Group(						// (\d+)
				Char.NUMBER									//  \d+
		).optional();										// (\d+)?
		
		Group endpointPathGroup = new Group(				// (.*)									
				Any.ANY										//  .*					
		);
		
		
		builder.add( 										// ^(http[s]?)://((\w(\w|[-.])+)\.\p{Alpha}+).?(\d+)?/?(.*)$
				Boundary.LINE_START,						// ^
				protocolGroup, 								//  (http[s]?)
				new StringElement("://"),					//            ://
				domainGroup, 								//               ((\w(\w|[-.])+)\.\p{Alpha}+)
				Any.ANY_CHAR.optional(),					//                                           .?
				portGroup,									//                                             (\d+)?
				new Chars('/').optional(),					//                                                   /?
				endpointPathGroup,							//                                                     (.*)
				Boundary.LINE_END							//                                                         $
		);

The resulting regex string differs a little bit, but the result should be the same:

`^(\Qhttp\E[s]?)\Q://\E((\w(\w|[\-\.])+)[\.]\p{Alpha}+).?(\d+)?[\/]?(.*)$`


To see the whole test code, look here: `RegexBuilderTest.testURL()`