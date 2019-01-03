# Regular Expression Builder for Java #

## Introduction ##

Writing Regular Expressions can be very difficult and exhausting.
A lot of brackets and special characters hinder the readability. And a single misplaced token can corrupt your whole expression. 
Therefor I had the idea to create an API for creating Regular Expressions with predefined Java classes.

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

Every modification of an `Element` applies to a new instance and let the original Element unchanged. This is to avoid side effects.
The following sample demonstrates the purpose of this design decision:


    Element string = new StringElement("foo"); 
    new RegexBuilder(string, string.many());

The method `many()` returns a new instance of type `StringElement` with the same data copied from the `string`-instance **plus** the modification to apply many matches. So, this manipulation doesn't *contanimate* the state of the `string`-instance.
 

----------

### Elements ###

#### Manipulation ####

As descriped before, all elements are **immutable** and every modification results in a cloned instance of the original. If you want to change an instance without cloning, you can activate the *change-mode* on an element with `setChangeable()`. To deactivate this mode use `unsetChangeable()`.

#### Group elements ####

Java Regular Expression offers **backreferences**, that can be accessed directly with the `Matcher.group(int)`-method. All Elements that supports backreferences are *indexable* and inherits from `Group`. That means there is a `isIndexable()` and `getIndex()` method.

Here is a sample how you can access groups:

	Group g1 = new Group(Char.WORD);
	Group g2 = new Group(Char.SPACE);
	Pattern p = new RegexBuilder(g1, g2).buildPattern();
	Matcher m = p.matcher("foo bar");
	m.group(g1.getIndex()); // matches "foo" and "bar"
	m.group(g2.getIndex()); // matches " "


#### Overview of elements ####

All elements are contained in the package `de.jepfa.regex.elements`.

|Element|Description|Regex meaning as example|Changeable|Indexable|
|-------|-----------|------------------------|----------|---------|
|`Chars`|A set of characters, also called Character classes|`[ac-z\s]`|Yes|No|
 |`Group`|A group of elements. Match results of Groups can be accessed with `Matcher.group(int)`|`(foo*[bB]ar)`|Yes|Yes|
|`Choice`|A `Group` of elements joined by logical ORs|`(foo*|[bB]ar|bear)`|Yes|Yes|
|`StringElement`|A quoted common String|`\Qfoo\E`|Yes|No|
|`Strings`|A `Choice` of many `StringElement`|`(\Qfoo\E|\Qbar\E|\Qbear\E)`|Yes|Yes|
|`Word`|A `Group` of elements wrapped by word-boundaries|`(\bfoo*[bB]ar\b)`|Yes|Yes|
|`Words`|A `Choice` of many `Word`, wrapped by word-boundaries|`(\bfoo\b|\bbar\b|\bbear\b)`|Yes|Yes|
|`NonCapturing`|A `Group` of elements that cannot be accessed by `Matcher.group(int)`|`(?:foo*[bB]ar)`|Yes|No|
|`Lookahead`|A non-capturing lookahead instruction|`(?=foo*[bB]ar)`|Yes|No|
|`Lookbehind`|A  non-capturing lookbehind instruction|`(?&lt;=foo*[bB]ar)`|Yes|No|
|`PlainElement`|A plain, not modified or enriched Regex element as a common String.|`al\l ((is po?ss[ib]le`|Yes|No|
|`Any`|Predefined expression for wildcards|`.`|No, use `changeable()` to get a changeable element|No|
|`Boundary`|Predefined expressions of boundary matches|`^`|No, use `changeable()` to get a changeable element|No|
|`Char`|Predefined expressions of character classes|`\d`|No, use `changeable()` to get a changeable element|No|



----------

#### Quantifiers ####

Each `Element` has its own `Quantifier`. A Quantifier defines the quantification of an element and the matching strategy.

|Element method|Description|Regex meaning|Min|Max|
|------------------------|----------------|---------------------|-----|------|
|`optional()`|Indicates possible zero occurrences of the corresponding element|`?` or `*`|0|?|
|`many()`|Indicates many occurrences of the corresponding element|`+` or `*`|?|&infin;|
|`arbitrary()`|Indicates zero or infinity occurrences of the corresponding element|`*`|0|&infin;|
|`least(n)`|Indicates at least *n* occurrences of the corresponding element|`{n,}`|n|&infin;|
|`most(n)`|Indicates at most *n* occurrences of the corresponding element|`{0,n}`|0|n|
|`count(n)`|Indicates extactly *n* occurrences of the corresponding element|`{n}`|n|n|
|`range(m, n)`|ndicates *m* til *n* occurrences of the corresponding element|`{m,n`|m|n|

Matching strategies are defined in the enum `Quantifier.Strategy`.

|Matching strategy|Description|Regex extention to quantifer|Regex example|
|--------------------------|----------------|----------------------------------------|----------------------|
|`GREEDY`|Match as many characters as possible|*none*|`a{2}`|
|`LAZY`|Match as few characters as possible|`?`|`a{2}?`|
|`POSSESSIVE`|Match as many characters as possible but don't use backtracking|`+`|`a{2}+`|



----------

#### Flags ####

Every `Element` can enable or disable `Flags` in the scope of itself with `switchOn(Flag...)` or  `switchOff(Flag...)`.
These flags override the flags set on the RegexBuilder with `new RegexBuilder(Flag...)`.

All flags are defined in the enum `RegexBuilder.Flag`. The flags come with the constants defined in the class `java.util.regex.Pattern`.

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