package services.customers;

import java.util.List;

import domain.Call;
import domain.Customer;

/**
 * This interface defines the functionality that Customer Management Service
 * to offer.
 */
public interface CustomerManagementService 
{
	/**
	 * Takes a customer domain object and saves it in the database
	 */
	public void newCustomer(Customer newCustomer);
	
	/**
	 * The specified customer is updated in the database.
	 * @throws CustomerNotFoundException 
	 */
	public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException;
	
	/**
	 * The specified customer is removed from the database
	 * @throws CustomerNotFoundException 
	 */
	public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException;
	
	/**
	 * Finds the customer by Id
	 */
	public Customer findCustomerById(String customerId) throws CustomerNotFoundException;
	
	public List<Customer> findCustomersByName (String name);

	public List<Customer> getAllCustomers();

	public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException;
	
	
	public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException;
}
