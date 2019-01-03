package de.jepfa.regex.components;

/**
 * Interface for all imutable objects that can be changeable with this special methods. 
 * 
 * @author Jens Pfahl
 */
public interface Changeable {

	/**
	 * @return <code>true</code> is the object currently changeable, else <code>false</code>
	 */
	public boolean isChangeable();

	/**
	 * Makes the current (imutable) object changeable so any modification will take effect to
	 * all used references of this instance. 
	 *
	 */
	public void setChangeable();
	
	/**
	 * Makes the current (mutable) object not changeable (imutable) so any modification will return a
	 * clone with the changes. The original will be untouched. 
	 *
	 */
	public void unsetChangeable();

}