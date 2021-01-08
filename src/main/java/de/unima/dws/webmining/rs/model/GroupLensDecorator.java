package de.unima.dws.webmining.rs.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;
import org.apache.mahout.common.iterator.FileLineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupLensDecorator implements DataModel {

	private DataModel dataModel;
	
	private static final String COLON_DELIMTER = "::";
	private static final String ITEM_CATEGORY_DELIMITER = "\\|";

	private static final Logger log = LoggerFactory
			.getLogger(GroupLensDecorator.class);

	private static final long serialVersionUID = 1L;

	private static boolean tokenIsValid(String token) {
		if (token != null && token.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Holds UserInfos
	 */
	private HashMap<Long, UserData> userInfo = new HashMap<Long, UserData>();
	/**
	 * Holds ItemInfos
	 */
	private HashMap<Long, ItemData> itemInfo = new HashMap<Long, ItemData>();

	

	public GroupLensDecorator(File ratingsFile, File userFile,
			File movieFile) throws IOException {
		
		dataModel = new GroupLensDataModel(ratingsFile);
		addItemInfo(movieFile);
		addUserInfo(userFile);
	}

	public GroupLensDecorator(FastByIDMap<PreferenceArray> userData, File userFile,
			File movieFile) throws IOException {
		
		dataModel = new GenericDataModel(userData);
		addItemInfo(movieFile);
		addUserInfo(userFile);
	}
	
	
	
	/**
	 * Adds (Overwrites) information about the item like categories. File lines
	 * are supposed to have the form
	 * ITEMID::SOMETHING::CATEGORIE|CATEGORIE|CATEGORIE|...:: SOMETHING
	 * 
	 * @param itemInfoFile
	 * @throws IOException
	 */
	private void addItemInfo(File itemInfoFile) throws IOException {
		@SuppressWarnings("resource")
		FileLineIterator iterator = new FileLineIterator(itemInfoFile, false);
		while (iterator.hasNext()) {
			String line = iterator.next();
			String[] tokens = line.split(COLON_DELIMTER);
			Long itemId = null;
			ItemData itemData = new ItemData(tokens[1]);
			if (tokenIsValid(tokens[0])) {
				try {
					itemId = Long.parseLong(tokens[0].trim());
				} catch (NumberFormatException ex) {
					log.warn("Could not read item id: " + tokens[0]);
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
			itemInfo.put(itemId, itemData);
		}
	}

	/**
	 * Adds (Overwrites) information about the user like gender and age. Input
	 * file has to be formated like USERID::GENDER::AGE(::...)*
	 * 
	 * @param userInfoFile
	 * @throws IOException
	 */
	private void addUserInfo(File userInfoFile) throws IOException {
		@SuppressWarnings("resource")
		FileLineIterator iterator = new FileLineIterator(userInfoFile, false);
		while (iterator.hasNext()) {
			String line = iterator.next();
			String[] tokens = line.split(COLON_DELIMTER);
			Long userId = null;
			UserData userData = new UserData();
			// id
			if (tokenIsValid(tokens[0])) {
				try {
					userId = Long.parseLong(tokens[0].trim());
				} catch (NumberFormatException ex) {
					log.warn("Could not parse user id: " + tokens[0]);
					continue;
				}
			}
			// gender
			if (tokenIsValid(tokens[1])) {
				if (tokens[1].trim().equals("M")) {
					userData.setGender(Gender.MALE);
				} else if (tokens[1].trim().equals("F")) {
					userData.setGender(Gender.FEMALE);
				}
			}
			// age
			if (tokenIsValid(tokens[2])) {
				try {
					userData.setAge(Integer.parseInt(tokens[2].trim()));
				} catch (NumberFormatException ex) {
					// do nothing
				}
			}
			if (tokenIsValid(tokens[3])) {
				try {
					userData.setOccupation(Integer.parseInt(tokens[3].trim()));
				} catch (NumberFormatException ex) {
					// do nothing
				}
			}
			if (tokenIsValid(tokens[4])) {
				userData.setZipCode(tokens[4]);
			}
			userInfo.put(userId, userData);
		}
	}

	public ItemData getItemData(Long itemId) {
		return itemInfo.get(itemId);
	}

	public String getItemDataLabel(Long itemId) {
		ItemData itemData = itemInfo.get(itemId);
		if (itemData != null) {
			return itemData.getLabel();
		} else {
			return null;
		}
	}

	public UserData getUserData(Long userId) {
		return userInfo.get(userId);
	}

	@Override
	public String toString() {
		return "ExtendedGroupLensDataModel";
	}

	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		dataModel.refresh(alreadyRefreshed);
		
	}

	public LongPrimitiveIterator getUserIDs() throws TasteException {
		return dataModel.getUserIDs();
	}

	public PreferenceArray getPreferencesFromUser(long userID)
			throws TasteException {
		return dataModel.getPreferencesFromUser(userID);
	}

	public FastIDSet getItemIDsFromUser(long userID) throws TasteException {
		return dataModel.getItemIDsFromUser(userID);
	}

	public LongPrimitiveIterator getItemIDs() throws TasteException {
		return dataModel.getItemIDs();
	}

	public PreferenceArray getPreferencesForItem(long itemID)
			throws TasteException {
		return dataModel.getPreferencesForItem(itemID);
	}

	public Float getPreferenceValue(long userID, long itemID)
			throws TasteException {
		return dataModel.getPreferenceValue(userID, itemID);
	}

	public Long getPreferenceTime(long userID, long itemID)
			throws TasteException {
		return dataModel.getPreferenceTime(userID, itemID);
	}

	public int getNumItems() throws TasteException {
		return dataModel.getNumItems();
	}

	public int getNumUsers() throws TasteException {
		return dataModel.getNumUsers();
	}

	public int getNumUsersWithPreferenceFor(long itemID) throws TasteException {
		return dataModel.getNumUsersWithPreferenceFor(itemID);
	}

	public int getNumUsersWithPreferenceFor(long itemID1, long itemID2)
			throws TasteException {
		return dataModel.getNumUsersWithPreferenceFor(itemID1, itemID2);
	}

	public void setPreference(long userID, long itemID, float value)
			throws TasteException {
		dataModel.setPreference(userID, itemID, value);
	}

	public void removePreference(long userID, long itemID)
			throws TasteException {
		dataModel.removePreference(userID, itemID);
		
	}

	public boolean hasPreferenceValues() {
		return dataModel.hasPreferenceValues();
	}

	public float getMaxPreference() {
		return dataModel.getMaxPreference();
	}

	public float getMinPreference() {
		return dataModel.getMinPreference();
	}

}
