package de.unima.dws.webmining.rs.helper;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import de.unima.dws.webmining.rs.model.ItemDataDisplayHelper;

public class Printer {

	/**
	 * Simply prints out {@link IRStatistics} to the console.
	 * 
	 * @param stats
	 *            {@link IRStatistics}
	 */
	public static void printIRStatsToConsole(IRStatistics stats) {
		System.out.println("Recall: " + stats.getRecall());
		System.out.println("Precision: " + stats.getPrecision());
		System.out.println("F1 Measure: " + stats.getF1Measure());
		System.out.println("Fallout: " + stats.getFallOut());
		System.out.println("Gain: "
				+ stats.getNormalizedDiscountedCumulativeGain());
		System.out.println("Reach:" + stats.getReach());
	}

	/**
	 * Simply prints recommendations out to the console including the label of a
	 * movie, the id and the calculated value of the preference.
	 * 
	 * @param recommendations
	 *            List of {@link RecommendedItem}
	 */
	public static void printRecommendationsToConsole(
			List<RecommendedItem> recommendations, String itemFile) {
		ItemDataDisplayHelper helper = null;
		try {
			helper = new ItemDataDisplayHelper(itemFile);
		} catch (IOException ex) {
			System.out.println("Could not read item data.");
		}
		System.out.println("User's recommendations: ");
		for (RecommendedItem recommendation : recommendations) {
			String label = "unknown";
			if (helper != null) {
				label = helper.getItemLabel(recommendation.getItemID());
			}
			System.out.println(recommendation.getValue() + ": " + label
					+ " (ID: " + recommendation.getItemID() + ")");
		}
	}

	/**
	 * Prints out preferences using {@link ItemDataDisplayHelper} to console
	 * 
	 * @param preferences
	 */
	public static void printPreferencesFromArray(PreferenceArray preferences,
			String itemFile) {
		ItemDataDisplayHelper helper = null;
		try {
			helper = new ItemDataDisplayHelper(itemFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Could not read item data.");
		}
		String label = "unknown";
		for (Preference pref : preferences) {
			if (helper != null) {
				label = helper.getItemLabel(pref.getItemID());
			}
			System.out.println(pref.getValue() + ": " + label + " (ID: "
					+ pref.getItemID() + ")");
		}
	}

}
