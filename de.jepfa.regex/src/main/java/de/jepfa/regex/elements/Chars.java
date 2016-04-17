package de.jepfa.regex.elements;

import static de.jepfa.regex.helper.Checker.*;

import java.util.LinkedList;
import java.util.List;

import de.jepfa.regex.RegexBuilderException;
import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.CharClass;
import de.jepfa.regex.components.Regexable;


/**
 * This is a Character Set also called <i>character class</i>. It contains all characters, that could match. 
 * All characters are combined with logical <i>OR</i>, so the ordering is unimportant.
 * So, this element matches if at least one character of this Character Set matches.
 *
 * @author Jens Pfahl
 */
public class Chars extends ChangeableElement {

	/**
	 * A set of characters. 
	 */
	private static class CharSet implements Regexable {

		private Character ch;
		private Character cfrom;
		private Character cto;
		private CharClass charClass;

		public CharSet(CharClass charClass) {
			checkNotNull(charClass);
			this.charClass = charClass;
		}
		
		public CharSet(Character ch) {
			checkNotNull(ch);
			this.ch = ch;
		}

		public CharSet(Character cfrom, Character cto) {
			checkNotNull(cfrom);
			checkNotNull(cto);
			this.cfrom = cfrom;
			this.cto = cto;
		}

		@Override
		public String toString() {
			return "Data [ch=" + ch + ", cfrom=" + cfrom + ", cto=" + cto + "]";
		}

		@Override
		public String toRegex() {
			StringBuilder sb = new StringBuilder();
			if (charClass != null) {
				sb.append(charClass.toRegex());
			}
			else if (ch != null) {
				sb.append(getQuotedChar(ch));
			}
			else if (cfrom != null && cto != null) {
				sb.append(getQuotedChar(cfrom));
				sb.append('-');
				sb.append(getQuotedChar(cto));
			}
			else {
				throw new RegexBuilderException("Empty or incorrect CharData:" + this + " Programming error?");
			}
			
			return sb.toString();
		}
		
		private String getQuotedChar(char ch) {
			if ((ch >= 'a' && ch <= 'z')
					|| (ch >= 'A' && ch <= 'Z')
					|| (ch >= '0' && ch <= '9')) {
				return String.valueOf(ch);
			}
			else {
				return "\\" + String.valueOf(ch);
			}
		}

	}


	protected List<Chars.CharSet> charSet = new LinkedList<>();
	protected boolean not;
	protected boolean and;
	/**
	 * With this list you can combine Chars into Chars.
	 */
	protected List<Chars> otherChars = new LinkedList<>();


	/**
	 * Matches predefined {@link Char}.
	 * 
	 * @param charClass a character class. 
	 */
	public Chars(CharClass charClass) {
		charSet.add(new CharSet(charClass));
	}
	
	/**
	 * Matches the character <code>ch</code>.
	 * 
	 * @param ch a character to match. 
	 */
	public Chars(char ch) {
		charSet.add(new CharSet(ch));
	}


	/**
	 * Matches at least one of the given characters in <code>chars</code>.
	 * 
	 * @param chars String of characters to match, not <code>null</code>.
	 */
	public Chars(String chars) {
		checkNotNull(chars);
		for (Character ch : chars.toCharArray()) {
			charSet.add(new CharSet(ch));
		}
	}

	/**
	 * Matches at least one of the given characters between <code>from</code> and <code>to</code>, both inclusive.
	 * 
	 * @param from the first character of the range. 
	 * @param to the last character of the range. 
	 */
	public Chars(char from, char to) {
		charSet.add(new CharSet(from, to));
	}

	
	/**
	 * Matches a predefined {@link CharClass} in addition to the current Character Set.
	 * 
	 * @param charClass a character class. 
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T add(CharClass charClass) {
		return cloneAndCall(e -> e.charSet.add(new CharSet(charClass)));
	}
	
	/**
	 * Matches the character <code>ch</code> in addition to the current Character Set.
	 * 
	 * @param ch a character to match. 
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T add(char ch) {
		return cloneAndCall(e -> e.charSet.add(new CharSet(ch)));
	}

	/**
	 * Matches at least one of the given characters in <code>chars</code> in addition to the current Character Set.
	 * 
	 * @param chars String of characters to match, not <code>null</code>.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T add(String chars) {
		return cloneAndCall(e -> {
			for (Character ch : chars.toCharArray()) {
				e.charSet.add(new CharSet(ch));
			}
		});
	}

	/**
	 * Matches at least one of the given characters between <code>from</code> and <code>to</code>, both inclusive,
	 * in addition to the current Character Set.
	 * 
	 * @param from the first character of the range. 
	 * @param to the last character of the range.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T range(char from, char to) {
		return cloneAndCall(e -> e.charSet.add(new CharSet(from, to)));
	}

	/**
	 * Defines the whole Character Set as <i>Excluded</i> Character Set.
	 * That means, this element matches if at least one character of all possible characters 
	 * expect the current characters matches.
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T not() {
		return cloneAndCall(e -> e.not = true);
	}

	
	/**
	 * Collates the <code>other</code> Character Set to the current Character Set.
	 * Matches at least one of the given characters in the current Character Set OR the <code>other</code> 
	 * Character Set. 
	 *
	 * @param other the other Character Set, not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T union(Chars other) {
		checkNotNull(other);
		return cloneAndCall(e -> e.otherChars.add(other));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(char)} and passes the created instance to
	 * {@link #union(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(char)}
	 * @see #union(Chars)
	 */
	public <T extends Chars> T union(char ch) {
		return union(new Chars(ch));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(String)} and passes the created instance to
	 * {@link #union(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(String)}
	 * @see #union(Chars)
	 */
	public <T extends Chars> T union(String chars) {
		return union(new Chars(chars));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(char, char)} and passes the created instance to
	 * {@link #union(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(char, char)}
	 * @see #union(Chars)
	 */
	public <T extends Chars> T union(char from, char to) {
		return union(new Chars(from, to));
	}
	
	
	
	/**
	 * Collates the <code>other</code> Character Set to the current Character Set in a special way.
	 * Matches at least one of the given characters in the current Character Set AND the <code>other</code> 
	 * Character Set. That means the matched character have to be in both Character Sets.
	 *
	 * @param other the other Character Set, not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T intersect(Chars other) {
		return cloneAndCall(e -> e.otherChars.add(other.and()));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(char)} and passes the created instance to
	 * {@link #intersect(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(char)}
	 * @see #intersect(Chars)
	 */
	public <T extends Chars> T intersect(char ch) {
		return intersect(new Chars(ch));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(String)} and passes the created instance to
	 * {@link #intersect(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(String)}
	 * @see #intersect(Chars)
	 */
	public <T extends Chars> T intersect(String chars) {
		return intersect(new Chars(chars));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(char, char)} and passes the created instance to
	 * {@link #intersect(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(char, char)}
	 * @see #intersect(Chars)
	 */
	public <T extends Chars> T intersect(char from, char to) {
		return intersect(new Chars(from, to));
	}

	
	/**
	 * Collates the <code>other</code> Character Set to the current Character Set in a special way.
	 * Matches at least one of the given characters in the current Character Set that are NOT in the <code>other</code> 
	 * Character Set. That means the matched character have to be in the current Character Sets, but not in the 
	 * other Character Set.
	 *
	 * @param other the other Character Set, not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 */
	public <T extends Chars> T subtract(Chars other) {
		return cloneAndCall(e -> e.otherChars.add(other.and().not()));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(char)} and passes the created instance to
	 * {@link #subtract(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(char)}
	 * @see #subtract(Chars)
	 */	
	public <T extends Chars> T subtract(char ch) {
		return subtract(new Chars(ch));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(String)} and passes the created instance to
	 * {@link #subtract(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(String)}
	 * @see #subtract(Chars)
	 */	
	public <T extends Chars> T subtract(String chars) {
		return subtract(new Chars(chars));
	}
	
	/**
	 * Creates a new Character Set with {@link #Chars(char, char)} and passes the created instance to
	 * {@link #subtract(Chars)}.
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 *
	 * @see {@link #Chars(char, char)}
	 * @see #subtract(Chars)
	 */	
	public <T extends Chars> T subtract(char from, char to) {
		return subtract(new Chars(from, to));
	}
	
	
	/**
	 * @return <code>true</code> if this Character Set is a <i>Excluded</i> Character Set.
	 * 
	 * @see #not()
	 */
	public boolean isNot() {
		return not;
	}

	
	@Override
	protected String elementToRegex() {
		if (charSet.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (not) {
			sb.append("^");
		}
		for (Chars.CharSet c : charSet) {
			sb.append(c.toRegex());
		}

		for (Chars other : otherChars) {
			if (other.and) {
				sb.append("&&");
			}
			sb.append(other.toRegex());
		}

		sb.append("]");
		return sb.toString();
	}
	
	
	@Override
	public Chars clone() throws CloneNotSupportedException {
		Chars clone = (Chars) super.clone();
		//Do not clone the elements of this list! All is functional, so it makes no sense.
		clone.charSet = new LinkedList<>(charSet);
		clone.otherChars = new LinkedList<>(otherChars);
		return clone;
	}
	
	/**
	 * Defines the current Character Set as <i>Intersection</i>.
	 * This has only an effect for {@link #otherChars}-members.
	 */
	protected <T extends Chars> T and() {
		return cloneAndCall(e -> e.and = true);
	}



}