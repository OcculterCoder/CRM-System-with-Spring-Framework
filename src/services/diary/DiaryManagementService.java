 package services.diary;

import java.util.List;

import domain.Action;

public interface DiaryManagementService 
{
	/**
	 * Records an action in the diary
	 */
	public void recordAction(Action action);
	
	public List<Action> getAllIncompleteActions(String requiredUser);
}
