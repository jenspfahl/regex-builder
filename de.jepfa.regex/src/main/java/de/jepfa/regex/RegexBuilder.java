package de.jepfa.regex;

import static de.jepfa.regex.helper.Checker.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.components.Flags;
import de.jepfa.regex.components.Regexable;
import de.jepfa.regex.elements.Group;
import de.jepfa.regex.elements.Strings;
import de.jepfa.regex.helper.Printer;


/**
 * This class is responsible to collect {@link Element}s and to build a complete Regular Expression.
 * <p>
 * Not supported:
 * <li>Named capturing groups (use the index getting from {@link Group#getIndex()})
 *
 * @author Jens Pfahl
 */
public final class RegexBuilder implements Regexable {
	
	public static final String NEW_LINE = System.getProperty("line.separator");

	/**
	 * All flags to control the Regex engine.
	 */
	public enum Flag implements Regexable {
		IGNORE_CASE_SENSITIVE(Pattern.CASE_INSENSITIVE, "i"), 
		UNIX_LINES(Pattern.UNIX_LINES, "d"), 
		MULTILINE(Pattern.MULTILINE, "m"), 
		DOTALL(Pattern.DOTALL, "s"), 
		UNICODE(Pattern.UNICODE_CASE, "u"), 
		COMMENTS(Pattern.COMMENTS, "x"), 
		UNICODE_CHARS(Pattern.UNICODE_CHARACTER_CLASS, "U"), 
		CANON_EQUALS(Pattern.CANON_EQ, ""), LITERAL(Pattern.LITERAL, "");

		private int id;
		private String regex;


		Flag(int id, String regex) {
			this.id = id;
			this.regex = regex;
		}


		/**
		 * @return the corresponding constant from {@link Pattern}.
		 */
		public int getId() {
			return id;
		}

		@Override
		public String toRegex() {
			return regex;
		}
	};


	
	private class Root extends Group {
		
		public Root() {
			setChangeable();
		}
		
		@Override
		public void runIndexer() {
			// make it public for the RegexBuilder
			super.runIndexer();
		}
		
		@Override
		public String toRegex() {
			StringBuilder sb = new StringBuilder();
			for (Element element : elems) {
				sb.append(element.toRegex());
			}
			return sb.toString();
		}
		
		@Override
		public String toString() {
			return Printer.toString(getElements());
		}
		
	}
	
	private Root root = new Root();
	private Set<Flag> enabledFlags = new HashSet<>();


	/**
	 * Create a new RegexBuilder without any {@link Flags flags}.
	 */
	public RegexBuilder() {

	}

	/**
	 * Create a new RegexBuilder with {@link Flags flags}.
	 *
	 * @param enabledFlags, not <code>null</code>
	 */
	public RegexBuilder(Flag ...enabledFlags) {
		checkNotNull(enabledFlags);
		this.enabledFlags.addAll(Arrays.asList(enabledFlags));
	}
	
	/**
	 * Create a new RegexBuilder without any {@link Flags flags} but with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elems, not <code>null</code>
	 */
	public RegexBuilder(Element... elems) {
		add(elems);
	}
	
	/**
	 * Adds all given {@link String strings} to this RegexBuilder instance.
	 * The strings are converted to a {@link Strings}-element.
	 *
	 * @param strings, not <code>null</code>
	 */
	public RegexBuilder add(String... strings) {
		checkNotNull(strings);
		root.add(new Strings(strings));
		return this;
	}


	/**
	 * Adds all given {@link Element elements} to this RegexBuilder instance.
	 *
	 * @param elems, not <code>null</code>
	 */
	public RegexBuilder add(Element... elems) {
		checkNotNull(elems);
		root.add(elems);
		return this;
	}
	

	/**
	 * Returns all {@link Element elements} of this builder instance.<br>
	 * It is not intended to modify already added elements!
	 * 
	 * @return all {@link Element elements} of this builder instance, not <code>null</code>.
	 */
	public List<Element> getElements() {
		return root.getElements();
	}
	
	
	/**
	 * @return all enabled {@link Flag flags} of this builder instance, not <code>null</code>.
	 */
	public Set<Flag> getFlags() {
		return Collections.unmodifiableSet(enabledFlags);
	}
	
	


	@Override
	public String toRegex() {
		return root.toRegex();
	}
	
	/**
	 * Indexes all {@link Group}s in this builder. After that, and only after that,
	 * you can use {@link Group#getIndex()}.
	 *
	 */
	public void runGroupIndexer() {
		root.runIndexer();
	}
	


	/**
	 * Builds an Regular Expression with {@link #getElements() all elements} and
	 * {@link #getFlags() all match flags} and creates a compiled {@link Pattern}.
	 * Also runs the {@link #runGroupIndexer()}-method.
	 *
	 * @return not <code>null</code>
	 */
	public Pattern buildPattern() {
		try {
			Pattern pattern = Pattern.compile(toRegex(), getFlagsAsInt());
			runGroupIndexer();
			return pattern;
		}
		catch (Exception e) {
			throw new RegexBuilderException("Compile fails for regex=" + toRegex(), e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("enabledFlags=");
		
		sb.append(enabledFlags).append(NEW_LINE);
		sb.append(root.toString());
		
		return sb.toString();
	}

	
	
	private int getFlagsAsInt() {
		int i = 0;
		for (Flag flag : enabledFlags) {
			i |= flag.getId();
		}
		return i;
	}


}
