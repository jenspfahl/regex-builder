package de.jepfa.regex.components;

/**
 * Interface for all objects that can be serialized to a valid Regual Expression.
 *
 * @author Jens Pfahl
 */
public interface Regexable {
	
	/**
	 * Returns a String representing the object as Regular Expression. 
	 *
	 * @return not <code>null</code>, but an empty String is possible
	 */
	public String toRegex();
}