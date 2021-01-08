package de.unima.dws.webmining.rs.similarity;


import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.similarity.PreferenceInferrer;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.google.common.base.Preconditions;

import de.unima.dws.webmining.rs.model.GroupLensDecorator;
import de.unima.dws.webmining.rs.model.UserData;

/**
 * Really simple user data comparator. If user data is equal than similarity is
 * highest. Uses Average Jaccard Similarity
 * 
 * 
 */
public class UserContentSimilarity implements UserSimilarity {

	private final GroupLensDecorator dataModel;

	public UserContentSimilarity(GroupLensDecorator dataModel) {
		this.dataModel = Preconditions.checkNotNull(dataModel);
	}

	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
		RefreshHelper.maybeRefresh(alreadyRefreshed, dataModel);
	}

	public double userSimilarity(long userID1, long userID2)
			throws TasteException {
		UserData userData1 = dataModel.getUserData(userID1);
		UserData userData2 = dataModel.getUserData(userID2);
		double score = 0.0;
		if (userData1 != null && userData2 != null) {
			if (userData1.getAge() == userData2.getAge()) {
				score++;
			}
			// in case of unknown gender, there is no similarity
			if (userData1.getGender() != null
					&& userData1.getGender().equals(userData2.getGender())) {
				score++;
			}
			// there should be no "null" occupation as 0 is unknown or others
			if (userData1.getOccupation() == userData2.getOccupation()) {
				score++;
			}
			score = score / 3;
		}
		return score;
	}

	public void setPreferenceInferrer(PreferenceInferrer inferrer) {
		throw new UnsupportedOperationException();
	}

}
