package de.unima.dws.webmining.rs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import de.unima.dws.webmining.rs.helper.Printer;

public class RecommendationRunner {

	/**
	 * Number of recommendations
	 */
	private final static int HOW_MANY = 50;
	/**
	 * Size of the used neighborhood
	 */
	private final static int NUM_NEIGHBORS = 10;
	/**
	 * Similarity Threshold for evaluation
	 */
	private final static double SIM_THRESHOLD = Double.NEGATIVE_INFINITY;

	/**
	 * Ratings File
	 */
	private final static String RATINGS = "ratings.dat";

	/**
	 * Movies File
	 */
	private final static String ITEMS = "movies.dat";

	/**
	 * Users File
	 */
	private final static String USERS = "users.dat";

	
	public static void main(String[] args) throws IOException, TasteException {
		getRecommendations_generic_item();
	}
	
	/**
	 * get a recommendation
	 * 
	 * @throws IOException
	 * @throws TasteException
	 */
	private static void getRecommendations() throws IOException, TasteException {
		
		//TODO Task 1
		
		// the data model based on MovieLens
		DataModel dataModel = new GroupLensDataModel(new File(RATINGS));
		// pearson similarity


		/**UserSimilarity similarity = null;*/
		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

		// the user neighborhood
		/**UserNeighborhood neighborhood = null;*/
		UserNeighborhood neighborhood =	new NearestNUserNeighborhood(NUM_NEIGHBORS, similarity, dataModel);
		// the recommender
		/**Recommender recommender = null;*/
		Recommender recommender =  new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

		// run it for a particular user
		List<RecommendedItem> recommendations = recommender.recommend(324, HOW_MANY);
		// print users preferences
		Printer.printPreferencesFromArray(dataModel.getPreferencesFromUser(324), USERS);
		// print recommendations
		Printer.printRecommendationsToConsole(recommendations, USERS);
	}

	private static void getRecommendations_generic_item() throws IOException, TasteException {
		// the data model based on MovieLens
		DataModel dataModel = new GroupLensDataModel(new File(RATINGS));
		/**Le nouveau code */
		// pearson similarity
		ItemSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
		//UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, similarity);
		// run it for a particular user
		List<RecommendedItem> recommendations = recommender.mostSimilarItems(324, HOW_MANY);
		// print users preferences
		Printer.printPreferencesFromArray(dataModel.getPreferencesFromUser(324), ITEMS);
		// print recommendations
		Printer.printRecommendationsToConsole(recommendations, ITEMS);
	}

	private static void evaluateRecommenderWithIR() throws IOException,
			TasteException {
		// the data model based on MovieLens
	//	DataModel dataModel = new GroupLensDataModel() ;
		DataModel dataModel = new GroupLensDataModel(new File(RATINGS)) ;

		// create evaluator
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		// evaluate recommender
		IRStatistics stats = evaluator.evaluate(new RecommenderBuilder() {

			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				// pearson similarity
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				// the user neighborhood
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(NUM_NEIGHBORS, SIM_THRESHOLD, similarity, dataModel);
				// the recommender
				Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
				return recommender;
			}
		}, null, dataModel, null, HOW_MANY, Double.NEGATIVE_INFINITY, 0.1);
		// print your stats
		Printer.printIRStatsToConsole(stats);
	}

	private static void evaluateRecommenderWithRMS() throws IOException,
			TasteException {

		// the data model based on MovieLens
		DataModel dataModel = new GroupLensDataModel();
		// create evaluator
		RMSRecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		// evaluate recommender
		double rms = evaluator.evaluate(new RecommenderBuilder() {

			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				// pearson similarity
				UserSimilarity similarity = new PearsonCorrelationSimilarity(
						dataModel);
				// the user neighborhood
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(
						NUM_NEIGHBORS, SIM_THRESHOLD, similarity, dataModel);
				// the recommender
				Recommender recommender = new GenericUserBasedRecommender(
						dataModel, neighborhood, similarity);
				return recommender;
			}
		}, null, dataModel, 0.7, 0.2);
		// print your stats
		System.out.println("The RMS is: " + rms);

	}

	private static void evaluateRecommenderWithMAE() throws IOException,
			TasteException {
		// the data model based on MovieLens
		DataModel dataModel = new GroupLensDataModel(new File(RATINGS));
		// create evaluator
		AverageAbsoluteDifferenceRecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		// evaluate recommender
		double MAE = evaluator.evaluate(new RecommenderBuilder() {

			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				// pearson similarity
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				// the user neighborhood
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(
						NUM_NEIGHBORS, SIM_THRESHOLD, similarity, dataModel);
				// the recommender
				Recommender recommender = new GenericUserBasedRecommender(
						dataModel, neighborhood, similarity);
				return recommender;
			}
		}, null, dataModel, 0.7, 0.2);
		// print your stats
		System.out.println("Mean Average Error: " + MAE);
	}

}
