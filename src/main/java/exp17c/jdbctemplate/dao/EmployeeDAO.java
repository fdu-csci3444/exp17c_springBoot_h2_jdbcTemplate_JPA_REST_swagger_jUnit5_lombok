/**
 * 
 */
package exp17c.jdbctemplate.dao;

import java.util.List;

import exp17c.jdbctemplate.model.Employee;

/**
 * @author ilker
 *
 */
public interface EmployeeDAO extends JpaRepositoryDAO<Employee, Long> {
	public List<Employee> findByFirstNameAndLastName(String firstName, String lastName);
	public List<Employee> findByFirstNameOrLastName(String firstName, String lastName);
	public Employee add(Employee employee, boolean updateIfExists);
	public Employee update(Employee employee);
	public boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
