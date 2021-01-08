package de.unima.dws.webmining.rs.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing an item (in this particular implementation an item with a
 * label and a set of categories, like a movie)
 * 
 * 
 */
public class ItemData {

	/**
	 * Label of the item
	 */
	private String label;

	/**
	 * Set of categories
	 */
	private Set<String> categories = new HashSet<String>();

	/**
	 * Create new {@link ItemData} for a given label
	 * 
	 * @param label
	 *            - the label of the item
	 */
	public ItemData(String label) {
		this.label = label;
	}

	/**
	 * Add one categorie to the item data. Please note a set is internally used,
	 * so you cannot have the same categor< twice. Which actually does not make
	 * any sense.
	 * 
	 * @param category
	 */
	public void addCategory(String category) {
		categories.add(category);
	}

	/**
	 * Retrieves all categories of this item.
	 * 
	 * @return {@link Set} of Strings
	 */
	public Set<String> getCategories() {
		return categories;
	}

	/**
	 * Retrieves the label of the item.
	 * 
	 * @return the label as string
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Deletes a category out of the set of categories. After calling this
	 * method the category will not be included in the set any more, no matter
	 * if it initially was not there or was really removed.
	 * 
	 * @param category
	 */
	public void removeCategory(String category) {
		categories.remove(category);
	}

	/**
	 * Sets the label of the item.
	 * @param label as string
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Retrieves the string representation of an item.
	 */
	@Override
	public String toString() {
		return "ItemData [label=" + label + ", categories=" + categories + "]";
	}

}
