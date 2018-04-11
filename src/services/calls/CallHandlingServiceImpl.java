package services.calls;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import domain.Action;
import domain.Call;
import services.customers.CustomerManagementService;
import services.customers.CustomerNotFoundException;
import services.diary.DiaryManagementService;

@Transactional
public class CallHandlingServiceImpl implements CallHandlingService {
	
	private CustomerManagementService customerService;
	private DiaryManagementService diaryService;
	
	public CallHandlingServiceImpl(CustomerManagementService customerService, DiaryManagementService diaryService) {
		this.customerService = customerService;
		this.diaryService = diaryService;
	}

	@Override
	public void recordCall(String customerId, Call newCall, Collection<Action> actions)
			throws CustomerNotFoundException {
		
		customerService.recordCall(customerId, newCall);
		
		for(Action action : actions) {
			diaryService.recordAction(action);
		}
	}

}
