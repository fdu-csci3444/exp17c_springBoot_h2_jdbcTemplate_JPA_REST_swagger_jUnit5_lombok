/**
 * 
 */
package exp17c.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * NOTE ilker, with below "Entity" class, jpa(hibernate) will generate below SQL
 *   create table staff (
 *      id bigint not null,
 *       first_name varchar(255),
 *       last_name varchar(255),
 *       primary key (id)
 *   )
 *  and a "HIBERNATE_SEQUENCE" to make up id values
 * NOTE above id column is not "IDENTITY" (i.e auto_increment of h2), hence using of sequence is outside of db insert, meaning data.sql inserts for this table has to have id specified 
 * @author ilker
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Entity(name="STAFF")	// NOTE ilker, if name is not specified class name camel cased to table name in DB, like STAFF_ENTITY if class name is StaffEntity
@Entity					// NOTE ilker, equivalent to above line. By default class name is assumed for the table name
//@Table(name="staff")	// NOTE ilker, this line is optional, by default this is what happens(class name is taken as table name)
@ApiModel(description = "Details about the staff") // NOTE ilker optional line to add swagger model info
public class Staff {
	@ApiModelProperty(notes = "Unique id of the staff, which will be auto incremented by hibernate upon add(save)") // NOTE ilker optional line to add swagger model attribute info
	@Id
	// NOTE ilker, GenerationType.AUTO means a "HIBERNATE_SEQUENCE" will be created in h2 that starts with 0 value and use that for id values
	@GeneratedValue		// NOTE ilker GenerationType.AUTO is the default strategy, so this line is equivalent to below one.
//	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long 	id;
//	@Column(name="last_name")	// NOTE ilker using @Column is optional. By default it is assumed to be there with attribute name as the column name
	private String	firstName;
	private String	lastName;
	
	public Staff(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
