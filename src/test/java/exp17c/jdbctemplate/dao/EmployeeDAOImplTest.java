package exp17c.jdbctemplate.dao;

//import static org.junit.jupiter.api.Assertions.*;	// NOTE ilker wild char import not suggested

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import exp17c.jdbctemplate.model.Employee;

/**
 * NOTE ilker this is jUnit5
 * @author ilker
 *
 */
//@RunWith(SpringRunner.class)			// WRONG - NOTE ilker this was used with jUnit4+SpringBoot to hook them up. Not there anymore with jUnit5
//@ExtendWith(SpringExtension.class)	// OK to have - NOTE ilker this is introduced with jUnit5+SpringBoot is not really needed in this case. But does not hurt anything
@SpringBootTest							// CORRECT - NOTE ilker this jUnit5+SpringBoot hook, enables this jUnit5 TC to hook up to Spring context Beans(i.e so that @Autowired beans will be injected to this class)
class EmployeeDAOImplTest {
	private static final int countOfRows4dataSql = 4;
	
	@Autowired
	@Qualifier("employeeDAOImpl")
	private EmployeeDAO employeeDAO;
//	private EmployeeDAO employeeDAOImpl;	// NOTE ilker if variable name was employeeDAOImpl, then due to auto matching of Class for variable name, would not need above @Qualifier("employeeDAOImpl")
	
	/**
	 * NOTE ilker executes BEFORE any TCs(Test Cases) in this class executes
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * NOTE ilker executes AFTER all TCs(Test Cases) in this class executes
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * NOTE ilker executes BEFORE each TC(Test Cases) in this class executes
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * NOTE ilker executes AFTER each TC(Test Cases) in this class executes
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

//	@Disabled	// TODO ilker to disable(so that it is not executed) a TC
	@Test
	void testFindAll() {
		List<Employee> employees = employeeDAO.findAll();
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(countOfRows4dataSql, employees.size());
	}

	@Test
	void testFindById() {
		Optional<Employee> oEmployee = employeeDAO.findById(1L);
		Assertions.assertNotNull(oEmployee);
		Assertions.assertTrue(oEmployee.isPresent());
		Assertions.assertFalse(StringUtils.isEmpty(oEmployee.get().getFirstName()));
		Assertions.assertEquals("asli", oEmployee.get().getFirstName().toLowerCase());
	}

	@Test
	void testFindOne_byId() {
		Employee employee4asli = new Employee(1L, "asli", "sutcuoglu");
		Optional<Employee> oEmployee = employeeDAO.findOne(employee4asli);
		Assertions.assertNotNull(oEmployee);
		Assertions.assertTrue(oEmployee.isPresent());
		Assertions.assertFalse(StringUtils.isEmpty(oEmployee.get().getFirstName()));
		Assertions.assertEquals("asli", oEmployee.get().getFirstName().toLowerCase());
	}

	@Test
	void testFindOne_byFirstNameAndLastName() {
		Employee employee4asli = new Employee("asli", "sutcuoglu");
		Optional<Employee> oEmployee = employeeDAO.findOne(employee4asli);
		Assertions.assertNotNull(oEmployee);
		Assertions.assertTrue(oEmployee.isPresent());
		Assertions.assertFalse(StringUtils.isEmpty(oEmployee.get().getFirstName()));
		Assertions.assertEquals("asli", oEmployee.get().getFirstName().toLowerCase());
	}

	@Test
	void testSave_withId() {
		Employee employee5 = new Employee(5L, "ilker_5", "kiris");
		Employee employee = employeeDAO.save(employee5);
		Assertions.assertNotNull(employee);
		Assertions.assertFalse(StringUtils.isEmpty(employee.getFirstName()));
		Assertions.assertEquals("ilker_5", employee.getFirstName().toLowerCase());
		employeeDAO.delete(employee5); // clean up to make sure this TC don't interfere with other TCs data in DB
	}

	@Test
	void testSave_withoutId() {
		Employee employee5 = new Employee("ilker_5", "kiris");
		Employee employee = employeeDAO.save(employee5);
		Assertions.assertNotNull(employee);
		Assertions.assertFalse(StringUtils.isEmpty(employee.getFirstName()));
		Assertions.assertEquals("ilker_5", employee.getFirstName().toLowerCase());
		Assertions.assertEquals(5L, employee.getId());
		employeeDAO.delete(employee5); // clean up to make sure this TC don't interfere with other TCs data in DB
	}

	@Test
	void testDeleteAll() {
		// pre TC body - fetch the rows BEFORE this TC executes
		List<Employee> employeesBeforeTC = employeeDAO.findAll();
		
		// TC body
		long countBeforeDeleteAll = employeeDAO.count();
		Assertions.assertTrue(countBeforeDeleteAll > 0);
		employeeDAO.deleteAll();
		long countAfterDeleteAll = employeeDAO.count();
		Assertions.assertEquals(0, countAfterDeleteAll);

		// post TC body - restore the same rows that were there before this TC executed
		if (!CollectionUtils.isEmpty(employeesBeforeTC)) {
			for (Employee employee : employeesBeforeTC) {
				employeeDAO.save(employee);
			}
		}
	}

	@Test
	void testDeleteById() {
		Employee employee = new Employee("ilker", "kiris");
		employeeDAO.save(employee);
		
		// TC body
		long countBeforeDelete = employeeDAO.count();
		employeeDAO.deleteById(employee.getId());
		long countAfterDelete = employeeDAO.count();
		Assertions.assertTrue(countBeforeDelete - countAfterDelete == 1);
	}

	@Test
	void testDelete() {
		Employee employee = new Employee("ilker", "kiris");
		employeeDAO.save(employee);
		
		// TC body
		long countBeforeDelete = employeeDAO.count();
		employeeDAO.delete(employee);
		long countAfterDelete = employeeDAO.count();
		Assertions.assertTrue(countBeforeDelete - countAfterDelete == 1);
	}

	@Test
	void testExistsById() {
		Assertions.assertTrue(employeeDAO.existsById(1L));
	}

	@Test
	void testExists_withId() {
		Employee employee1 = new Employee(1L, "asli", "sutcuoglu");
		Assertions.assertTrue(employeeDAO.exists(employee1));
	}

	@Test
	void testExists_withFirstNameAndLastName() {
		Employee employee1 = new Employee("asli", "sutcuoglu");
		Assertions.assertTrue(employeeDAO.exists(employee1));
	}

	@Test
	void testCount() {
		Assertions.assertEquals(4, employeeDAO.count());
	}

	@Test
	void testFindByFirstNameAndLastName() {
		Employee employee1 = new Employee("asli", "sutcuoglu");
		List<Employee> employees = employeeDAO.findByFirstNameAndLastName(employee1.getFirstName(), employee1.getLastName());
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(1, employees.size());
	}

	@Test
	void testFindByFirstNameOrLastName_1() {
		Employee employee2 = new Employee("burhan", "altintop");
		List<Employee> employees = employeeDAO.findByFirstNameOrLastName(employee2.getFirstName(), employee2.getLastName());
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(1, employees.size());
	}

	@Test
	void testFindByFirstNameOrLastName_2() {
		Employee employee1 = new Employee("asli_volkan", "sutcuoglu");	// NOTE ilker there will be 2 matches for last_name only
		List<Employee> employees = employeeDAO.findByFirstNameOrLastName(employee1.getFirstName(), employee1.getLastName());
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(2, employees.size());
	}
	
	@Test
	void testAdd_wasThereSoNotSaved() {
		// pre TC body
		long countBeforeSave = employeeDAO.count();
		Employee employee = new Employee("ilker", "kiris");
		Employee employeeSaved = employeeDAO.save(employee);
		Long id2employeeSaved = employeeSaved.getId();
		long countAfterSave = employeeDAO.count();
		Assertions.assertTrue(countAfterSave - countBeforeSave == 1);
		
		// TC body
		boolean updateIfExists = false;
		Employee employeeWasAlreadyThereSoNotSaved = employeeDAO.add(employee, updateIfExists);
		Long id2employeeWasAlreadyThereSoNotSaved = employeeWasAlreadyThereSoNotSaved.getId();
		Assertions.assertEquals(id2employeeSaved, id2employeeWasAlreadyThereSoNotSaved);
		
		// post TC body
		employeeDAO.delete(employeeWasAlreadyThereSoNotSaved);
	}
	
	@Test
	void testAdd_wasThereForceSaved() {
		// pre TC body
		long countBeforeSave = employeeDAO.count();
		Employee employee = new Employee(101L,"ilker", "kiris");
		Employee employeeSaved = employeeDAO.save(employee);
		Long id2employeeSaved = employeeSaved.getId();
		long countAfterSave = employeeDAO.count();
		Assertions.assertTrue(countAfterSave - countBeforeSave == 1);
		
		// TC body
		boolean updateIfExists = true;
		Employee employeeNew = new Employee(101L,"ilker_new", "kiris");
		Employee employeeWasAlreadyThereForceSaved = employeeDAO.add(employeeNew, updateIfExists);
		Long id2employeeWasAlreadyThereForceSaved = employeeWasAlreadyThereForceSaved.getId();
		Assertions.assertEquals(id2employeeSaved, id2employeeWasAlreadyThereForceSaved);
		Assertions.assertEquals("ilker_new", employeeWasAlreadyThereForceSaved.getFirstName().toLowerCase());
		
		// post TC body
		employeeDAO.delete(employeeWasAlreadyThereForceSaved);
	}

	@Test
	void testUpdate() {
		// pre TC body
		long countBeforeSave = employeeDAO.count();
		Employee employee = new Employee(101L,"ilker", "kiris");
		Employee employeeSaved = employeeDAO.save(employee);
		Long id2employeeSaved = employeeSaved.getId();
		long countAfterSave = employeeDAO.count();
		Assertions.assertTrue(countAfterSave - countBeforeSave == 1);
		
		// TC body
		Employee employeeNew = new Employee(101L,"ilker_new", "kiris");
		Employee employeeNewUpdated = employeeDAO.update(employeeNew);
		Long id2employeeNewUpdated = employeeNewUpdated.getId();
		Assertions.assertEquals(id2employeeSaved, id2employeeNewUpdated);
		Assertions.assertEquals("ilker_new", employeeNewUpdated.getFirstName().toLowerCase());
		
		// post TC body
		employeeDAO.delete(employeeNewUpdated);
	}

	@Test
	void testExistsByFirstNameAndLastName() {
		Employee employee4asli = new Employee("asli", "sutcuoglu");
		Assertions.assertTrue(employeeDAO.existsByFirstNameAndLastName(employee4asli.getFirstName(), employee4asli.getLastName()));
	}

}
