package dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import domain.Call;
import domain.Customer;

public class CustomerDaoJdbcTemplate implements CustomerDao {
	
	private static final String SELECT_CALL_SQL = "SELECT * FROM TBL_CALL WHERE CUSTOMER_ID=?";
	private static final String INSERT_CALL_SQL = "INSERT INTO TBL_CALL(NOTES, TIME_AND_DATE, CUSTOMER_ID) VALUES (?, ?, ?)";
	private static final String SELECT_ALL_CUSTOMER_SQL = "SELECT * FROM CUSTOMER";
	private static final String DELETE_CUSTOMER_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
	private static final String UPDATE_CUSTOMER_SQL = "UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID=?";
	private static final String SELECT_CUSTOMER_BY_ID_SQL = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=?";
	private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?, ?, ?, ?, ?)";
	private static final String CREATE_CALL_TABLE_SQL = "CREATE TABLE TBL_CALL(NOTES VARCHAR(255), TIME_AND_DATE DATE, CUSTOMER_ID VARCHAR(20))";
	private static final String CREATE_CUSTOMER_TABLE_SQL = "CREATE TABLE CUSTOMER(CUSTOMER_ID VARCHAR(20), COMPANY_NAME VARCHAR(50), EMAIL VARCHAR(50), TELEPHONE VARCHAR(20), NOTES VARCHAR(255))";
	
	private JdbcTemplate template;
	
	public CustomerDaoJdbcTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	private void createTables() {
		//create customer table
		try {
			template.update(CREATE_CUSTOMER_TABLE_SQL);
		}catch(BadSqlGrammarException e) {
			System.out.println("The Customer table already exists");
		}
		//create call table
		try {
			template.update(CREATE_CALL_TABLE_SQL);
		}catch(BadSqlGrammarException e) {
			System.out.println("The Call table already exists");
		}
	}

	@Override
	public void create(Customer customer) { 
		
		template.update(INSERT_CUSTOMER_SQL, customer.getCustomerId(), customer.getCompanyName(), customer.getEmail(), customer.getTelephone(), customer.getNotes());

	}

	@Override
	public Customer getById(String customerId) throws RecordNotFoundException {
		try {
			return template.queryForObject(SELECT_CUSTOMER_BY_ID_SQL, new CustomerRowMapper(), customerId);
		}catch(IncorrectResultSizeDataAccessException e) {
			throw new RecordNotFoundException();
		}
	}

	@Override
	public List<Customer> getByName(String name) {
		return template.query("SELECT * FROM CUSTOMER WHERE COMPANY_NAME=?", new CustomerRowMapper(), name);
	}

	@Override
	public void update(Customer customerToUpdate) throws RecordNotFoundException {
		int rowsAffected = template.update(UPDATE_CUSTOMER_SQL, customerToUpdate.getCompanyName(), customerToUpdate.getEmail(), customerToUpdate.getTelephone(), customerToUpdate.getNotes(), customerToUpdate.getCustomerId());
		if(rowsAffected != 1) {
			throw new RecordNotFoundException();
		}	
	}

	@Override
	public void delete(Customer oldCustomer) throws RecordNotFoundException {
		int rowsAffected = template.update(DELETE_CUSTOMER_SQL, oldCustomer.getCustomerId());
		if(rowsAffected != 1) {
			throw new RecordNotFoundException();
		}
	}

	@Override
	public List<Customer> getAllCustomers() {
		return template.query(SELECT_ALL_CUSTOMER_SQL, new CustomerRowMapper());
	}

	//get customer and its all call details
	@Override
	public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
		
		Customer customer = this.getById(customerId);
		
		List<Call> allCallsForTheCustomer = template.query(SELECT_CALL_SQL, new CallRowMapper(), customer.getCustomerId());
		
		customer.setCalls(allCallsForTheCustomer);
		
		return customer;
	}

	@Override
	public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
		Customer newCustomer = this.getById(customerId);
		template.update(INSERT_CALL_SQL, newCall.getNotes(), newCall.getTimeAndDate(), customerId);

	}

}

class CustomerRowMapper implements RowMapper<Customer>{

	@Override
	public Customer mapRow(ResultSet rs, int rowNumber) throws SQLException {
		String customerID = rs.getString("CUSTOMER_ID");
		String companyName = rs.getString("COMPANY_NAME");
		String email = rs.getString("EMAIL");
		String telephone = rs.getString("TELEPHONE");
		String notes = rs.getString("NOTES");
		
		Customer newCustomer = new Customer(customerID, companyName, email, telephone, notes);
		return newCustomer;
		
	}
	
}

class CallRowMapper implements RowMapper<Call>{

	@Override
	public Call mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		String notes = rs.getString("NOTES");
		Date date = rs.getDate("TIME_AND_DATE");
		
		//CUSTOMER_ID is foreign key. We don't need that as the query will only return call details for that particular CUSTOMER_ID
		return new Call(notes, date);
		
	}
	
}
