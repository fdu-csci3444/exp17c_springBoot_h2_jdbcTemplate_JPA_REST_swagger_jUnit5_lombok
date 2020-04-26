/**
 * 
 */
package exp17c.jdbctemplate.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exp17c.jdbctemplate.model.Employee;
import exp17c.jdbctemplate.model.EmployeeRowMapper;


/**
 * @author ilker
 *
 */
@Repository
@Transactional	// NOTE ilker means "for any failure in insert or update operation, complete operation will be rolled back"
public class EmployeeDAOImpl implements EmployeeDAO {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Employee> findAll() {
		String sql = "SELECT e.id, e.first_name, e.last_name FROM employee e";
		RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);	// NOTE ilker since there is 1 to 1 match btw attributes of Employee and employee table, can use this line instead of below one
//		RowMapper<Employee> rowMapper = new EmployeeRowMapper();
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public Optional<Employee> findById(Long id) {
		Optional<Employee> oEmployee = Optional.ofNullable(null);
		String sql = "SELECT e.id, e.first_name, e.last_name FROM employee e WHERE e.id = ?";
		if (id != null) {
			RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);	// NOTE ilker since there is 1 to 1 match btw attributes of Employee and employee table, can use this line instead of below one
//			RowMapper<Employee> rowMapper = new EmployeeRowMapper();
			Employee employee = jdbcTemplate.queryForObject(sql, rowMapper, id);
			oEmployee = Optional.ofNullable(employee);
		} else {
			logger.warn("findById unExpected null input, will do nothing, will return nulled Optional");			
		}
		return oEmployee;
	}

	@Override
	public Optional<Employee> findOne(Employee employee) {
		Optional<Employee> oEmployee = Optional.ofNullable(null);
		if (employee != null) {
			Long id = employee.getId();
			if (id != null) {
				oEmployee = findById(id);
			} else {
				String firstName = employee.getFirstName();
				String lastName = employee.getLastName();
				List<Employee> employees = findByFirstNameAndLastName(firstName, lastName);
				if (employees == null) {
					logger.info("findOne could not find Employee for firstName:{}, lastName:{}", firstName, lastName);
				} else if (employees.size() >= 1) {
					logger.info("findOne will return 1st Employee of {} employees for firstName:{}, lastName:{}", employees.size(), firstName, lastName);
					oEmployee = Optional.of(employees.get(0));
				}
			}
		} else {
			logger.warn("findOne unExpected null input, will do nothing, will return nulled Optional");			
		}
		return oEmployee;
	}

	@Override
	public Employee save(Employee employee) {
		String sqlInsertWithId = "INSERT INTO employee (id, first_name, last_name) values (?, ?, ?)";
		String sqlInsertWithoutId = "INSERT INTO employee (first_name, last_name) values (?, ?)";
		String sql4id = "SELECT e.id FROM employee e WHERE e.first_name = ? AND e.last_name = ?";

		Long id = null;	
		if (employee != null) {
			// insert employee
			id = employee.getId();
			if (id != null) {
				jdbcTemplate.update(sqlInsertWithId, id, employee.getFirstName(), employee.getLastName());
			} else {
				jdbcTemplate.update(sqlInsertWithoutId, employee.getFirstName(), employee.getLastName());				
				// fetch and set id of employee after the insert
				// NOTE ilker using Long.class to parse id value from resultSet of SQL query
				id = jdbcTemplate.queryForObject(sql4id, Long.class, employee.getFirstName(), employee.getLastName());
				employee.setId(id);
			}
		} else {
			logger.warn("save unExpected null input, will do nothing, will just return original employee");			
		}
		return employee;
	}

	@Override
	public void deleteAll() {
		String sql = "DELETE FROM employee";
		jdbcTemplate.update(sql);
	}

	@Override
	public void deleteById(Long id) {
		if (id != null) {
			String sql = "DELETE FROM employee e WHERE e.id = ?";
			jdbcTemplate.update(sql, id);
		} else {
			logger.warn("deleteById unExpected null input, will do nothing");
		}
	}

	@Override
	public void delete(Employee employee) {
		if (employee != null) {
			Long id = employee.getId();
			if (id != null) {
				deleteById(id);
			} else {
				Optional<Employee> oEmployee = findOne(employee);
				id = oEmployee.isPresent() ? oEmployee.get().getId() : null;
				if (id != null) deleteById(id);
			}
		} else {
			logger.warn("delete unExpected null input, will do nothing");
		}
	}

	@Override
	public boolean existsById(Long id) {
		int count = 0;
		String sql = "SELECT count(*) FROM employee e WHERE e.id = ?";
		if (id != null) {
			count = jdbcTemplate.queryForObject(sql, Integer.class, id);
		}
		return count == 0 ? false : true;
	}

	@Override
	public boolean exists(Employee employee) {
		boolean exists = false;
		if (employee != null) {
			Long id = employee.getId();
			if (id != null) {
				exists = existsById(id);
			} else {
				exists = existsByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
			}
		}
		return exists;
	}

	@Override
	public long count() {
		String sql = "SELECT count(*) FROM employee";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public List<Employee> findByFirstNameAndLastName(String firstName, String lastName) {
		String sql = "SELECT e.id, e.first_name, e.last_name FROM employee e WHERE e.first_name = ? AND e.last_name = ?";
//		RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);
		RowMapper<Employee> rowMapper = new EmployeeRowMapper();
		return jdbcTemplate.query(sql, rowMapper, firstName, lastName);
	}

	@Override
	public List<Employee> findByFirstNameOrLastName(String firstName, String lastName) {
		String sql = "SELECT e.id, e.first_name, e.last_name FROM employee e WHERE e.first_name = ? OR e.last_name = ?";
//		RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);
		RowMapper<Employee> rowMapper = new EmployeeRowMapper();
		return jdbcTemplate.query(sql, rowMapper, firstName, lastName);
	}

//	@Transactional	// NOTE ilker could have declared "at method level" here instead of "at class level"
	@Override
	public Employee add(Employee employee, boolean updateIfExists) {
		if (employee != null) {
			if (exists(employee)) {
				if (updateIfExists) {
					delete(employee);
					save(employee);
				} else {
					logger.info("add will do nothing since employee {} exists in Db", employee.toString());
				}
			} else {
				save(employee);
			}
		} else {
			logger.warn("add unExpected null input, will do nothing");			
		}
		return employee;
	}

	@Override
	public Employee update(Employee employee) {
		if (employee != null) {
			if (exists(employee)) delete(employee);
			save(employee);
		} else {
			logger.warn("update unExpected null input, will do nothing");			
		}
		return employee;
	}

	@Override
	public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
		int count = 0;
		String sql = "SELECT count(*) FROM employee e WHERE e.first_name = ? AND e.last_name = ?";
		if (firstName != null || lastName != null) {
			count = jdbcTemplate.queryForObject(sql, Integer.class, firstName, lastName);
		}
		return count == 0 ? false : true;
	}

}
