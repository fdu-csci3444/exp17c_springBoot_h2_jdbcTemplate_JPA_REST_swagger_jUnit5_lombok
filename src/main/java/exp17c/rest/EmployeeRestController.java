/**
 * 
 */
package exp17c.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exp17c.jdbctemplate.dao.EmployeeDAO;
import exp17c.jdbctemplate.model.Employee;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author ilker
 *
 */
@RestController
//@RequestMapping("/rest/${my.app.values.api.version}/employee")	// NOTE ilker equivalent to below line
//@RequestMapping(path = "/rest/${my.app.values.api.version}/employee")	// TODO ilker makeMeWork
@RequestMapping(path = "/rest/v1/employee")
public class EmployeeRestController {
	@Autowired
	@Qualifier("employeeDAOImpl")
	private EmployeeDAO employeeDAO;

	// curl -i -X GET http://localhost:8888/rest/v1/employee/echoMessage?msg=Hi
	@RequestMapping(value="/echoMessage", method=RequestMethod.GET)
//	@RequestMapping("/echoMessage") // NOTE ilker, equivalent to above line since default is GET. NOTE ilker WRONG to use when you enable swagger, because swagger thinks all operations(GET,POST,PUT,DELETE,PATH) are there
//	@GetMapping("/echoMessage")		// NOTE ilker, equivalent to above line as well
	/**
	 * http://localhost:8888/rest/v1/employee/echoMessage?msg=Hi
	 */
	// NOTE ilker below optional swagger annotation @ApiOperation can be used to better describe(instead of default of providing method name) what the api end point(resource) does
	@ApiOperation(value = "To test this REST end point is alive via echo",
				  notes = "An optional message value can be passed into this echo service, via msg query param, to use in echoed String",
				  response = String.class)
	public String echoMessage(@ApiParam(value = "optional message value to pass", required = false) @RequestParam(value="msg", defaultValue="Hello ilker") String message) {
	// NOTE ilker below is without optional swagger api param info. Above is equivalent to below enhanced with swagger param info
//	public String echoMessage(@RequestParam(value="msg", defaultValue="Hello ilker") String message) {
		return "echoMessage echoed: " + message;
	}

	// curl -i http://localhost:8888/rest/v1/employee
	// curl -i http://localhost:8888/rest/v1/employee/all
	// NOTE ilker in below assigning multiple URLs(paths) to single method
	@GetMapping({"", "/all"})
	public  List<Employee> findAll() {
		List<Employee> employees = employeeDAO.findAll();
		return employees;
	}

//	curl -X POST -H "Content-Type: application/json" -i  -d '{"firstName":"ilker_0", "lastName":"kiris_0"}' http://localhost:8888/rest/v1/employee
//	curl -X POST -H "Content-Type: application/json"     -d '{"firstName":"ilker_1", "lastName":"kiris_1"}' http://localhost:8888/rest/v1/employee
//	curl -X POST -H "Content-Type: application/json" --data '{"firstName":"ilker_2", "lastName":"kiris_2"}' http://localhost:8888/rest/v1/employee	
	@PostMapping("")
	public  Optional<Employee> save(@RequestBody final Employee employee) {
		Employee savedEmployee = employeeDAO.save(employee);
		return employeeDAO.findById(savedEmployee.getId());
	}
	
	// curl -X DELETE -i http://localhost:8888/rest/v1/employee/2
	@DeleteMapping("/{id}")
	public  void delete(@PathVariable("id") Long id) {
		employeeDAO.deleteById(id);
	}
	
	// curl -i http://localhost:8888/rest/v1/employee/1
	// curl -i http://localhost:8888/rest/v1/employee/findById/1
	// NOTE ilker in below assigning multiple URLs(paths) to single method. Also NOTE 1st url is more correct REST syntax
	@GetMapping({"/{id}","/findById/{id}"})
	public  Optional<Employee> findById(@PathVariable Long id) {
		Optional<Employee> employee = employeeDAO.findById(id);
		return employee;
	}
	
	// curl -i http://localhost:8888/rest/v1/employee/findByFirstNameAndLastName/asli/sutcuoglu
	@GetMapping("/findByFirstNameAndLastName/{firstName}/{lastName}")
	public  List<Employee> findByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
		List<Employee> employees = employeeDAO.findByFirstNameAndLastName(firstName, lastName);
		return employees;
	}
	
	// curl -i http://localhost:8888/rest/v1/employee/findByFirstNameOrLastName/tahsin/sutcuoglu
	@GetMapping("/findByFirstNameOrLastName/{firstName}/{lastName}")
	public  List<Employee> findByFirstNameOrLastName(@PathVariable String firstName, @PathVariable String lastName) {
		List<Employee> employees = employeeDAO.findByFirstNameOrLastName(firstName, lastName);
		return employees;
	}
	
}
