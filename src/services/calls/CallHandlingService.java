package services.calls;

import java.util.Collection;

import domain.Action;
import domain.Call;
import services.customers.CustomerNotFoundException;

public interface CallHandlingService 
{
	
	/**
	 * Records a call with the customer management service, and also records
	 * any actions in the diary service
	 */
	public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException;
}
