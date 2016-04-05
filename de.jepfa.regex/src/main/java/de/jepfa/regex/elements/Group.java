package de.jepfa.regex.elements;

import static de.jepfa.regex.helper.Checker.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import de.jepfa.regex.RegexBuilder;
import de.jepfa.regex.RegexBuilderException;
import de.jepfa.regex.components.ChangeableElement;
import de.jepfa.regex.components.Element;
import de.jepfa.regex.helper.Changer;


/**
 * This is a group, also called <i>Capturing Group</i>. A group can contains multiple {@link Element Elements}.
 * The group matches if and only if all elements inside matches in the same order as they were added.
 *
 * @author Jens Pfahl
 */
public class Group extends ChangeableElement {

	protected List<Element> elems = new LinkedList<>();
	private Integer index = isIndexable() ? 0 : null;
	private int lastIndex = 0;

	/**
	 * Create a new {@link Group} with a set of {@link Element Elements}.
	 * You can use {@link #add(Element...)} instead.
	 *
	 * @param elements, not <code>null</code>, no <code>null</code>-elements
	 */
	public Group(Element... elements) {
		checkNoNullElements(elements);
		addInternal(elements);
	}
	

	/**
	 * Adds all given {@link Element elements} to this group.
	 *
	 * @param elements, not <code>null</code>, no <code>null</code>-elements
	 */
	public <T extends Group> T add(Element... elements) {
		checkNoNullElements(elements);
		return cloneAndCall(e -> e.addInternal(elements));
	}
	
	/**
	 * Returns all {@link Element elements} of this group.<br>
	 * It is not intended to modify already added elements!
	 * 
	 * @return all {@link Element elements} of this group, not <code>null</code>.
	 */
	public List<Element> getElements() {
		return Collections.unmodifiableList(elems);
	}
	
	
	/**
	 * Returns <code>true</code>, if this {@link Group} is indexable, means it is a <i>Capturing-Group</i>.
	 * Returns <code>false</code>, if this {@link Group} is NOT indexable, means it is a <i>Non-Capturing-Group</i>.
	 * <p>
	 * Only Capturing-Groups can be accessed with {@link Matcher#group(int)}.
	 *
	 * @return <code>true</code> or <code>false</code>
	 * @see #getIndex()
	 */
	public boolean isIndexable() {
		return true;
	}
	
	/**
	 * Returns the index of this {@link Group} if the Group is {@link Group#isIndexable() indexable}. 
	 * <p>
	 * Use this method to access groups of the {@link Matcher} with {@link Matcher#group(int)}.
	 * <p>
	 * If you use {@link Group#setChangeable()} or the {@link Changer} it is recommend to 
	 * use this method after {@link RegexBuilder#buildPattern()} or {@link RegexBuilder#runGroupIndexer()} 
	 * was running.
	 *
	 * @return the index or <code>null</code>
	 * @see #isIndexable()
	 */
	public Integer getIndex() {
		return index;
	}
	

	@Override
	public Group clone() throws CloneNotSupportedException {
		Group clone = (Group) super.clone();
		clone.elems = new LinkedList<>(elems);
		//clone.index = 0; // TODO reset index to be on the safe side to avoid duplicate indexes.
		//clone.lastIndex = 0;
		
		//Do not clone the whole tree! All is functional, so it makes no sense.
//		for (Element element : elems) {
//			clone.elems.add(element.clone());
//		}
		
		return clone;
	}
	
	@Override
	protected String elementToRegex() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBegin());
		sb.append(getPrefix());
		boolean firstDone = false;
		for (Element element : getElemsForRegex()) {
			if (firstDone) {
				sb.append(getOperator());
			}
			sb.append(element.toRegex());
			firstDone = true;
		}
		sb.append(getEnd());
		return sb.toString();
	}

	/**
	 * Gets a list of all elementy of this construct.
	 *
	 * @return
	 */
	protected List<Element> getElemsForRegex() {
		return elems;
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [index=" + index + ", q=" + getQuantifier() + ", flags=" + getFlags() + "] = " + toRegex();
	}
	
	/**
	 * @return the start token of a group
	 */
	protected String getBegin() {
		return "(";
	}
	
	/**
	 * @return the stop token of a group
	 */
	protected String getEnd() {
		return ")";
	}
	

	/**
	 * @return the token directly after the start token
	 */
	protected String getPrefix() {
		return "";
	}
	
	/**
	 * @return the token between th elements
	 */
	protected String getOperator() {
		return "";
	}
	
	protected void addInternal(Element... elements) {
		for (Element e : elements) {
			if (e instanceof Group) {
				Group group = (Group) e;
				check(this, group);
				setIndex(this, group);
			}
			elems.add(e);
		}
	}

	protected void runIndexer() {
		lastIndex = 0;
		for (Element e : elems) {
			if (e instanceof Group) {
				setIndex(this, (Group) e);
			}
		}
	}

	private static void setIndex(Group root, Group subGroup) {
		
		if (subGroup.isIndexable()) {
		
			int rootIndex = root.index != null ? root.index : 0;
			int newIndex = root.lastIndex + rootIndex + 1;
			
			subGroup.index = newIndex;
			root.lastIndex = newIndex;
			
		}
		else {
			subGroup.index = null;
		}
		
		for (Element e : subGroup.getElements()) {
			if (e instanceof Group) {
				setIndex(root, (Group) e);
			}
		}
	}


	private void check(Group search, Group other) {
		for (Element otherElement : other.getElements()) {
			if (otherElement == search) {
				throw new RegexBuilderException("Cycle detected! Cannot add a group that contains This to This.");
			}
			if (otherElement instanceof Group) {
				check(search, (Group)otherElement);
			}
		}

		
	}
}