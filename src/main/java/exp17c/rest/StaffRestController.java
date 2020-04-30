/**
 * 
 */
package exp17c.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exp17c.jpa.model.Staff;
import exp17c.jpa.repo.StaffRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author ilker
 *
 */
@RestController
//@RequestMapping("/rest/${my.app.values.api.version}/staff")	// NOTE ilker equivalent to below line
//@RequestMapping(path = "/rest/${my.app.values.api.version}/staff")	// TODO ilker makeMeWork
@RequestMapping(path = "/rest/v1/staff")
public class StaffRestController {
	private static final Logger logger = LoggerFactory.getLogger(StaffRestController.class);
	
	@Autowired
	private StaffRepository staffRepository;

	// curl -i -X GET http://localhost:8888/rest/v1/staff/echoMessage?msg=Hi
	@RequestMapping(value="/echoMessage", method=RequestMethod.GET)
//	@RequestMapping("/echoMessage") // NOTE ilker, equivalent to above line since default is GET. NOTE ilker WRONG to use when you enable swagger, because swagger thinks all operations(GET,POST,PUT,DELETE,PATH) are there
//	@GetMapping("/echoMessage")		// NOTE ilker, equivalent to above line as well
	/**
	 * http://localhost:8888/rest/v1/staff/echoMessage?msg=Hi
	 */
	// NOTE ilker below optional swagger annotation @ApiOperation can be used to better describe(instead of default of providing method name) what the api end point(resource) does
	@ApiOperation(value = "To test this REST end point is alive via echo",
				  notes = "An optional message value can be passed into this echo service, via msg query param, to use in echoed String",
				  response = String.class)
	public String echoMessage(@ApiParam(value = "optional message value to pass", required = false) @RequestParam(value="msg", defaultValue="Hello ilker") String message) {
	// NOTE ilker below is without optional swagger api param info. Above is equivalent to below enhanced with swagger param info
//	public String echoMessage(@RequestParam(value="msg", defaultValue="Hello ilker") String message) {
		logger.debug("echoMessage with message:{}", message);
		return "echoMessage echoed: " + message;
	}

	// curl -i http://localhost:8888/rest/v1/staff
	// curl -i http://localhost:8888/rest/v1/staff?page=2
	// curl -i http://localhost:8888/rest/v1/staff?rowsPerPage=3
	// curl -i "http://localhost:8888/rest/v1/staff?page=2&rowsPerPage=3"
	@GetMapping("")
	public Page<Staff> findAll(@RequestParam(defaultValue="0") int page, @RequestParam(value="rowsPerPage", defaultValue="5") int size) {
//		PageRequest pr = new PageRequest()
		Page<Staff> staffPage = staffRepository.findAll(PageRequest.of(page, size));
		return staffPage;
//		return null;
	}

	// curl -i http://localhost:8888/rest/v1/staff/all
	@GetMapping("/all")
	public  List<Staff> findAll() {
		List<Staff> staffs = staffRepository.findAll();
		return staffs;
	}

//	curl -X POST -H "Content-Type: application/json" -i  -d '{"firstName":"ilker_0", "lastName":"kiris_0"}' http://localhost:8888/rest/v1/staff
//	curl -X POST -H "Content-Type: application/json"     -d '{"firstName":"ilker_1", "lastName":"kiris_1"}' http://localhost:8888/rest/v1/staff
//	curl -X POST -H "Content-Type: application/json" --data '{"firstName":"ilker_2", "lastName":"kiris_2"}' http://localhost:8888/rest/v1/staff	
	@PostMapping("")
	public  Optional<Staff> save(@RequestBody final Staff staff) {
		Staff savedStaff = staffRepository.save(staff);
		return staffRepository.findById(savedStaff.getId());
	}
	
	// curl -X DELETE -i http://localhost:8888/rest/v1/staff/2
	@DeleteMapping("/{id}")
	public  void delete(@PathVariable("id") Long id) {
		staffRepository.deleteById(id);
	}
	
	// curl -i http://localhost:8888/rest/v1/staff/1
	// curl -i http://localhost:8888/rest/v1/staff/findById/1
	// NOTE ilker in below assigning multiple URLs(paths) to single method. Also NOTE 1st url is more correct REST syntax
	@GetMapping({"/{id}", "/findById/{id}"})
	public  Optional<Staff> findById(@PathVariable Long id) {
		Optional<Staff> staff = staffRepository.findById(id);
		return staff;
	}
	
	// curl -i http://localhost:8888/rest/v1/staff/findByFirstNameIgnoreCaseQuery2JPQL/ilker
	// curl -i http://localhost:8888/rest/v1/staff/findByFirstNameIgnoreCaseQuery2JPQL/ILKer
	@GetMapping("/findByFirstNameIgnoreCaseQuery2JPQL/{firstName}")
	public  Optional<List<Staff>> findByFirstNameIgnoreCaseQuery2JPQL(@PathVariable String firstName) {
		Optional<List<Staff>> staffs = staffRepository.findByFirstNameIgnoreCaseQuery2JPQL(firstName);
		return staffs;
	}
	
	// curl -i http://localhost:8888/rest/v1/staff/findByFirstNameIgnoreCaseQuery2Native/ilker
	// curl -i http://localhost:8888/rest/v1/staff/findByFirstNameIgnoreCaseQuery2Native/ILKer
	@GetMapping("/findByFirstNameIgnoreCaseQuery2Native/{firstName}")
	public  Optional<List<Staff>> findByFirstNameIgnoreCaseQuery2Native(@PathVariable String firstName) {
		Optional<List<Staff>> staffs = staffRepository.findByFirstNameIgnoreCaseQuery2JPQL(firstName);
		return staffs;
	}
	
}
