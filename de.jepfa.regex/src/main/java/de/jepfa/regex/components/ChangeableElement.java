package de.jepfa.regex.components;

import de.jepfa.regex.helper.Changer;

/**
 * This class is e special {@link Element} that is also imutable but gives you the posibility to break out 
 * of this restriction. To change the behaviour of an ChangeableElement without creating a new instance, 
 * use the {@link Changer} or use the methods in {@link Changeable}. 
 * <p>
 *
 * @author Jens Pfahl
 */
public abstract class ChangeableElement extends Element implements Changeable {

	@Override
	public boolean isChangeable() {
		return ! cloneMode;
	}
	
	@Override
	public void setChangeable() {
		cloneMode = false;
	}
	
	@Override
	public void unsetChangeable() {
		cloneMode = true;
	}


}