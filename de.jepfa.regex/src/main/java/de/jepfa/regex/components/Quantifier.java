package de.jepfa.regex.components;

import static de.jepfa.regex.helper.Checker.*;

import de.jepfa.regex.RegexBuilderException;


/**
 * This class describes the quantification of an {@link Element}. 
 * The default quantification is "there is precise one", the default strategy is {@link Strategy#GREEDY}.
 * 
 *
 * @author Jens Pfahl
 */
public class Quantifier implements Regexable, Cloneable {
	
	/**
	 * Constant of unlimited cardinality.
	 */
	public static final int UNBOUND = -1;

	/**
	 * This enum descripes types of strategies of quantification.
	 */
	public enum Strategy {
		/**
		 * Match as many characters as possible.
		 */
		GREEDY, 
		/**
		 * Match as few characters as possible.
		 */
		LAZY, 
		/**
		 * Like {@link #GREEDY}, but don't use backtracking to prevent the regex engine 
		 * from trying all permutations of an {@link Element} to accomplish a match.
		 * So this is more performant than {@link #GREEDY}.
		 */
		POSSESSIVE
	};


	private int min = 1, max = 1;
	private Quantifier.Strategy strategy = Strategy.GREEDY;



	/**
	 * Returns the minimal cardinality.
	 *
	 * @return
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Returns the maximal cardinality, {@link #UNBOUND} means unlimited.
	 *
	 * @return
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Returns the match strategy of this quantifier.
	 *
	 * @return
	 */
	public Quantifier.Strategy getStrategy() {
		return strategy;
	}

	@Override
	public String toRegex() {
		String s = null;

		if (min == 0 && max == 1) { // ?
			s = "?";
		}
		if (min == 1 && max == UNBOUND) { // +
			s = "+";
		}
		if (min == 0 && max == UNBOUND) { // *
			s = "*";
		}
		if (min == 1 && max == 1) { //
			s = "";
		}
		if (min > 1 && min == max) { // {n}
			s = "{" + min + "}";
		}
		if (min > 1 && min < max) { // {min,}
			s = "{" + min + "," + max + "}";
		}
		if (min == 0 && max > 1) { // {min,max}
			s = "{0," + max + "}";
		}
		if (min > 1 && max == UNBOUND) { // {0,max}
			s = "{" + min + ",}";
		}
		if (s == null) {
			throw new RegexBuilderException("unsupported constellation: min=" + min + 
					", max=" + getMaxAsString());
		}
		if (!s.isEmpty()) {
			switch (strategy) {
				case LAZY:
					s = s + "?";
					break;
				case POSSESSIVE:
					s = s + "+";
					break;

				default:
					break;
			}
		}

		return s;
	}


	@Override
	public Quantifier clone() throws CloneNotSupportedException {
		return (Quantifier) super.clone();
	}

	@Override
	public String toString() {
		return "[strategy=" + strategy + ", min=" + min + ", max=" + getMaxAsString() + "]";
	}

	
	
	void setStrategy(Quantifier.Strategy strategy) {
		this.strategy = strategy;
	}

	void setMin(int min) {
		checkCondition("min should not be less than zero", () -> (min < 0));
		this.min = min;
	}
	
	void setMax(int max) {
		checkCondition("max should not be less than zero", () -> (max < 0 && max != Quantifier.UNBOUND));
		this.max = max;
	}
	
	void setLeast(int least) {
		setMin(least);
		this.max = UNBOUND;
	}
	
	void setMost(int most) {
		this.min = 0;
		setMax(most);
	}
	
	
	
	private Object getMaxAsString() {
		return max == UNBOUND ? "unbound" : max;
	}

}