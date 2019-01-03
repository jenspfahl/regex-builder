package de.jepfa.regex.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jepfa.regex.components.Element;
import de.jepfa.regex.elements.Group;

/**
 * @author Jens Pfahl
 */
public class Printer {
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	public static boolean showTree = true;
	
	public static String toString(Element ...elements) {
		if (elements == null) {
			return "null";
		}
		return toString(Arrays.asList(elements));
	}
	
	public static String toString(List<Element> elements) {
		if (elements == null) {
			return "null";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("elements={").append(NEW_LINE);
		
		Map<Integer, Boolean> lastPerLevel = new HashMap<>();
		toStringInternal(1, lastPerLevel, sb, elements);
		
		sb.append("}");
		
		return sb.toString();
	}
	
	private static void toStringInternal(int level, Map<Integer, Boolean> lastPerLevel, StringBuilder sb, List<Element> elems) {
		if (elems.isEmpty()) {
			return;
		}
		
		Element last = elems.get(elems.size()-1);
		for (Element element : elems) {
			boolean isLast = (element == last);

			if (isLast) {
				lastPerLevel.put(level-1, true);
			}
			indent(level, lastPerLevel, sb);
			sb.append(element).append(NEW_LINE);
			if (element instanceof Group) {
				Group group = (Group) element;
				toStringInternal(level + 1, lastPerLevel, sb, group.getElements());
			}
			lastPerLevel.remove(level-1); 
			
		}
	}
	
	private static void indent(int level, Map<Integer, Boolean> lastPerLevel, StringBuilder sb) {
		if (level > 0)  {
			if (!showTree) {
				char[] indent = new char[level * 2];
				Arrays.fill(indent, ' ');
				sb.append(indent);
			}
			else {
			
				if (level == 1) {
					sb.append("  --");
				}
				else 
				for (int i = 0; i < level; i++) {
					if (i>0){
						if ( i+1==level) {
							if (lastPerLevel.containsKey(i)) {
								sb.append("\u2514-- ");
							}
							else {
								sb.append("|-- ");
							}
							
						}
						else {
							if (lastPerLevel.containsKey(i)) {
								sb.append("    ");
							}
							else {
								sb.append("|   ");
							}
							
						}
					}
					else{
						sb.append("    ");
					}
				}
			}
		}
	}
	
}
