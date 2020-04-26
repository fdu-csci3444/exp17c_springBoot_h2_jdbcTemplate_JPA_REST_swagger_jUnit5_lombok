/**
 * 
 */
package exp17c.jpa.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import exp17c.jpa.model.Staff;

/**
 * 
 * Couple of example queries
 *   https://www.baeldung.com/spring-data-jpa-query
 * 	 // "native" query with "named parameters" 
 *   @Query(value="select s from STAFF s where lower(s.first_name) = lower(:staffFirstName)", nativeQuery=true)
 *   public Staff findByFirstNameIgnoreCaseQuery(@Param("staffFirstName") String staffFirstName);
 *   
 * 	 // JPQL query with "named parameters" 
 *      NOTE ilker in below "Staff" is NOT the table name (although table's name is same as entity name in this case), it is the entity class Staff used by JPQL
 *   @Query("select s from Staff s where lower(s.firstName) = lower(:staffFirstName)")
 *   public Staff findByFirstNameIgnoreCaseQuery(@Param("staffFirstName") String staffFirstName);
 * 
 * 	 // JPQL query with "indexed parameters" 
 *   @Query("select s from Staff s where s.firstName = ?1")
 *   public Staff findByFirstNameQueryPositionalParam(String staffFirstName);
 * 
 * @author ilker
 *
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {
	// NOTE ilker an example "Query method". Below link has many more examples of it
	//   https://docs.spring.io/spring-data/jpa/docs/1.6.0.RELEASE/reference/html/jpa.repositories.html#jpa.query-methods
	public Optional<List<Staff>> findByFirstName(String firstName);
	public Optional<List<Staff>> findByFirstNameOrLastName(String firstName, String lastName);
	
	// NOTE ilker an example JPQL query using @Query with named parameters
	//      in below "Staff" is NOT the table name (although table's name is same as entity name in this case), it is the entity class Staff used by JPQL
	@Query("select s from Staff s where lower(s.firstName) = lower(:staffFirstName)")
	public Optional<List<Staff>> findByFirstNameIgnoreCaseQuery2JPQL(@Param("staffFirstName") String staffFirstName);
	
	// NOTE ilker an example "native" query using @Query with named parameters
	@Query(value = "select s from STAFF s where lower(s.first_name) = lower(:staffFirstName)", nativeQuery = true)
	public Optional<List<Staff>> findByFirstNameIgnoreCaseQuery2Native(@Param("staffFirstName") String staffFirstName);
	
}
