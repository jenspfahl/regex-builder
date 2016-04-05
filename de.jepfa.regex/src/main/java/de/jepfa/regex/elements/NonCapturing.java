package de.jepfa.regex.elements;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.components.Quantifier.Strategy;

/**
 * This is a special group, also called <i>Non-Capturing Group</i>. Such a group works like a common {@link Group},
 * with the difference, that this group don't support back references and 
 * ({@link #isIndexable()} returns <code>false</code>.
 * 
 * @see #getIndex()
 * @author Jens Pfahl
 */
public class NonCapturing extends Group {

	protected boolean independent;
	


	public NonCapturing(Element... elements) {
		super(elements);
	}

	/**
	 * Creates a new independent Non-Captiuring-Group, also called <i>Atomic grouping.</i>.
	 * Atomic grouping means the same behavior as {@link Strategy#POSSESSIVE} behavior.
	 * So, alternatively, you can use {@link #strategy(Strategy)} with that value instead.
	 *
	 * @return a new independent Non-Captiuring-Group, if {@link #isCloneMode()} == <code>true</code>
	 * @see Strategy#POSSESSIVE
	 */
	public <T extends NonCapturing> T independent() {
		return cloneAndCall(e -> e.independent = true);
	}
	
	

	public boolean isIndependent() {
		return independent;
	}
	
	
	/**
	 * This element is not indexable!
	 */
	@Override
	public boolean isIndexable() {
		return false;
	}


	@Override
	protected String getPrefix() {

		StringBuffer sb = new StringBuffer("?");

		if (independent) {
			sb.append(">");
		}
		else {
			sb.append(":");
		}

		return sb.toString();
	}


}