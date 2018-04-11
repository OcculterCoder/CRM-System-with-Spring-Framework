package client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import domain.Action;
import domain.Call;
import domain.Customer;
import services.calls.CallHandlingService;
import services.customers.CustomerManagementService;
import services.customers.CustomerNotFoundException;
import services.diary.DiaryManagementService;

public class SimpleClientTest {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("Application.xml");
		
		try {
			CustomerManagementService customerService = container.getBean(CustomerManagementService.class);
			CallHandlingService callService = container.getBean(CallHandlingService.class);
			DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);
			
			//Customer newCustomer = new Customer("CS039", "Bex", "Top Company");
			//customerService.newCustomer(newCustomer);
			
			try {
				Customer updateCustomer = customerService.findCustomerById("CS039");
				updateCustomer.setEmail("hello@bex.com");
				updateCustomer.setTelephone("09812653");
				customerService.updateCustomer(updateCustomer);
			} catch (CustomerNotFoundException e1) {
				System.out.println("Sorry customer not found");
			}
			
			Call newCall = new Call("Joseph Called from Bex Ltd.");
			
			Action action1 = new Action("Call back Joseph", new GregorianCalendar(2019,10,19), "jsph");
			Action action2 = new Action("Call back Maria", new GregorianCalendar(2018,7,10), "mar");
			List<Action> actions = new ArrayList<Action>();
			actions.add(action1);
			actions.add(action2);
			
			try {
				callService.recordCall("CS039", newCall, actions);
			} catch (CustomerNotFoundException e) {
				System.out.println("The Customer does not exist");
			}
			
			System.out.println("Printing below the incomplete Action(s) of a user from Diary Service\n");
			
			Collection<Action> incompleteActions = diaryService.getAllIncompleteActions("jsph");
			for(Action action : incompleteActions) {
				System.out.println(action);
			}
		}
		
		finally {
			container.close();
		}
		

	}

}
