package de.jepfa.regex.components;

import static de.jepfa.regex.helper.Checker.*;

import java.util.function.Consumer;

import de.jepfa.regex.RegexBuilder;
import de.jepfa.regex.RegexBuilder.Flag;
import de.jepfa.regex.RegexBuilderException;
import de.jepfa.regex.components.Quantifier.Strategy;

/**
 * This class is the base for all elements that can be matched, also called as <i>atoms</i>.
 * All elements have a quantification ({@link #getQuantifier()}) and optionally some match flags ({@link #getFlags()}),
 * that can overwrite the match flags set on the {@link RegexBuilder}.
 * <p>
 * All elements are imutable as default, that means any change of an element returns a new instance of them
 * with the changed value. The original will be untouched. The reason is to avoid side-effects, when you
 * use the same object several times with other configurations. 
 * To change the behaviour of an element without creating a new instance, see {@link ChangeableElement}. 
 *
 * @author Jens Pfahl
 */
public abstract class Element implements Regexable, Cloneable {

	private Quantifier q = new Quantifier();
	private Flags flags = new Flags();
	
	/**
	 * Implementors of {@link Element} can access this flag to control the mode of {@link #cloneAndCall(Consumer...)}. 
	 */
	protected boolean cloneMode = true;

	/**
	 * Returns the Reguler Expression of the current element without {@link Quantifier} and {@link Flags}. 
	 *
	 * @return the Regular Expression, not <code>null</code>
	 */
	abstract protected String elementToRegex();
	
	
	/**
	 * Changes the matching {@link Strategy} of the {@link Quantifier}. 
	 *
	 * @param strategy not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getStrategy()
	 */
	public <T extends Element> T strategy(Quantifier.Strategy strategy) {
		checkNotNull(strategy);
		return cloneAndCall(e -> e.getQuantifier().setStrategy(strategy));
	}

	/**
	 * Sets the minimum value of the {@link Quantifier} to the given value 
	 * and the maximum value to {@link Quantifier#UNBOUND}.
	 *
	 * @param least the minimum value
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMin()
	 * @see Quantifier#getMax()
	 */
	public <T extends Element> T least(int least) {
		return cloneAndCall(e -> e.getQuantifier().setLeast(least));
	}

	/**
	 * Sets the maximum value of the {@link Quantifier} to the given value 
	 * and the minimum value to <code>0</code>.
	 *
	 * @param most the maximum value
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMin()
	 * @see Quantifier#getMax()
	 */
	public <T extends Element> T most(int most) {
		return cloneAndCall(e -> e.getQuantifier().setMost(most));
	}

	/**
	 * Sets the minimum value and the maximum value of the {@link Quantifier} to the given values.
	 * In a other meaning, the current Element can occures between <i>min</i> and <i>max</i> times.
	 *
	 * @param min the minimum value
	 * @param max the maximum value
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMin()
	 * @see Quantifier#getMax()
	 */
	public <T extends Element> T range(int min, int max) {
		return cloneAndCall(e -> e.getQuantifier().setMin(min), e -> e.getQuantifier().setMax(max));
	}

	/**
	 * Sets the minimum value of the {@link Quantifier} to <code>0</code>.
	 * The maximum will be untouched. 
	 * In a other meaning, the current Element is <i>optional</i> (can be absent).
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMin()
	 */
	public <T extends Element> T optional() {
		return range(0, getQuantifier().getMax());
	}

	/**
	 * Sets the maximum value of the {@link Quantifier} to {@link Quantifier#UNBOUND}.
	 * The minimum will be untouched. 
	 * In a other meaning, the current Element can occures <i>many</i> (more than once).
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMax()
	 */
	public <T extends Element> T many() {
		return range(getQuantifier().getMin(), Quantifier.UNBOUND);
	}
	
	/**
	 * Sets the minimum value of the {@link Quantifier} to <code>0</code> 
	 * and the maximum value to {@link Quantifier#UNBOUND}.
	 * In a other meaning, the current Element is <i>optional</i> (can be absent) 
	 * or can occures <i>many</i> (more than once).
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMax()
	 */
	public <T extends Element> T arbitrary() {
		return range(0, Quantifier.UNBOUND);
	}

	/**
	 * Sets the minimum value and the maximum value of the {@link Quantifier} to the same given value.
	 * In a other meaning, the current Element can occures exact <i>n</i> times.
	 *
	 * @param value the value
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Quantifier#getMin()
	 * @see Quantifier#getMax()
	 */
	public <T extends Element> T count(int value) {
		return range(value, value);
	}


	/**
	 * Switch the given flags <i>on</i>, but only in the context of the current element (and its sub-elements).
	 * Use this method to change the regex engine - behaviour temporary.
	 *
	 * @param flags the flags to switch on, not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Flags#getEnabledFlags()
	 */
	public <T extends Element> T switchOn(Flag... flags) {
		checkNotNull(flags);
		return cloneAndCall(e -> e.getFlags().switchOn(flags));
	}

	/**
	 * Switch the given flags <i>off</i>, but only in the context of the current element (and its sub-elements).
	 * Use this method to change the regex engine - behaviour temporary.
	 *
	 * @param flags the flags to switch off, not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see Flags#getDisabledFlags()
	 */
	public <T extends Element> T switchOff(Flag... flags) {
		checkNotNull(flags);
		return cloneAndCall(e -> e.getFlags().switchOff(flags));
	}
	
	/**
	 * Restores the given flags to the defaults or to the configuration of the {@link RegexBuilder}. 
	 * Use this method, if you want to restore temporary changes applied with 
	 * {@link #switchOn(Flag...)} or {@link #switchOff(Flag...)}.
	 *
	 * @param flags the flags to restore, not <code>null</code>
	 * 
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see RegexBuilder#getFlags()
	 */
	public <T extends Element> T restoreFlags(Flag... flags) {
		checkNotNull(flags);
		return cloneAndCall(e -> e.getFlags().restoreDefaults(flags));
	}
	
	/**
	 * Restores all flags to the defaults or to the configuration of the {@link RegexBuilder}. 
	 * Use this method, if you want to restore all temporary changes applied with 
	 * {@link #switchOn(Flag...)} or {@link #switchOff(Flag...)}.
	 *
	 * @return a changed clone, see {@link ChangeableElement}
	 * 
	 * @see RegexBuilder#getFlags()
	 */
	public <T extends Element> T restoreAllFlags() {
		return cloneAndCall(e -> e.getFlags().restoreAll());
	}


	/**
	 * Returns the quantification of this element.
	 *
	 * @return not <code>null</code>
	 */
	public Quantifier getQuantifier() {
		return q;
	}

	/**
	 * Returns the match flags of this element.
	 *
	 * @return not <code>null</code>
	 */
	public Flags getFlags() {
		return flags;
	}
	

	/**
	 * Helper to change the state of an Element "stateless".
	 * If {@link #cloneMode} == <code>true</code>, this method firstly clones the current 
	 * instance and apply the changes at the clone and not at the original instance.  
	 * Clients should do their method calls on the <code>consumers</code>!
	 * 
	 * @param consumers not <code>null</code>
	 * @return a clone or <code>this</code>.
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	final protected <T extends Element> T cloneAndCall(Consumer<T>... consumers) {
		checkNotNull(consumers);
		try {
			T e = (T)this;
			if (cloneMode) {
				e = (T) this.clone();
			}
			for (Consumer<T> c : consumers) {
				c.accept(e);
			}
			return e;
		}
		catch (CloneNotSupportedException e) {
			throw new RegexBuilderException(
					this.getClass().getName() + " is not cloneable, but should be. Programming error?", e);
		}
	}
	
	@Override
	public String toRegex() {
		StringBuilder sb = new StringBuilder();
		
		if (!getFlags().isEmpty()) {
			// pseudo Non-capturing-group
			sb.append("(?");
			sb.append(getFlags().toRegex());
			sb.append(":");
		}
		sb.append(elementToRegex());
		sb.append(getQuantifier().toRegex());
		if (!getFlags().isEmpty()) {
			sb.append(")");
		}
		
		return sb.toString();
	}



	@Override
	public Element clone() throws CloneNotSupportedException {
		Element clone = (Element) super.clone();
		clone.q = q.clone();
		clone.flags = flags.clone();
		clone.cloneMode = true;
		return clone;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [q=" + q + ", flags=" + flags + "] = " + toRegex();
	}

}