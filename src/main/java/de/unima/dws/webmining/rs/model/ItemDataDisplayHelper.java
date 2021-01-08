package de.unima.dws.webmining.rs.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Helper Class to display movie names for ids. Read the movies.dat file
 * 
 * 
 */
public class ItemDataDisplayHelper {

	private HashMap<Long, ItemData> itemInfos = new HashMap<Long, ItemData>();
	private final String COLON_DELIMTER = "::";
	private final String ITEM_CATEGORY_DELIMITER = "\\|";

	public ItemDataDisplayHelper(String fileDestination) throws IOException {
		init(fileDestination);
	}
	
	private void init(String itemInfoFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(itemInfoFile));
		
		while (br.ready()) {
			String line = br.readLine();
			String[] tokens = line.split(COLON_DELIMTER);
			Long itemId = null;
			ItemData itemData = new ItemData(tokens[1]);
			if (tokenIsValid(tokens[0])) {
				try {
					itemId = Long.parseLong(tokens[0].trim());
				} catch (NumberFormatException ex) {
					continue;
				}
			}
			if (tokenIsValid(tokens[2])) {
				String[] categories = tokens[2].split(ITEM_CATEGORY_DELIMITER);
				for (String cat : categories) {
					if (tokenIsValid(cat)) {
						itemData.addCategory(cat.trim());
					}
				}
			}
			itemInfos.put(itemId, itemData);
		}
		br.close();
	}

	private static boolean tokenIsValid(String token) {
		if (token != null && token.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ItemData getItemData(long itemId) {
		return itemInfos.get(itemId);
	}

	public String getItemLabel(long itemId) {
		ItemData itemData = itemInfos.get(itemId);
		if (itemData == null) {
			return null;
		} else {
			return itemData.getLabel();
		}
	}
}
