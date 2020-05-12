# exp17c_springBoot_h2_jdbcTemplate_JPA_REST_swagger_jUnit5_lombok
Example SpringBoot project showing;

- h2 as in memory RDBMS (Relational DB Management System)
- defining (part of) RDBMS schema via schema.sql of SpringBoot (see src/main/resources/schema.sql)
- updating RDBMS schema via JPA(hybernate) via "spring.jpa.hibernate.ddl-auto=update" value in application.properties
- initializing RDBMS schema via data.sql of SpringBoot (see src/main/resources/data.sql)
- using jdbcTemplate to hand code access to RDBMS (h2) via SQL in code
- using ORM(Object Relational Mapping) to access RDBMS (h2) via JPA (via it's default implementation, hybernate). 
  Hence we write very little code. SQL 
  will be generated and executed via hybernate
- using JPA's JPQL Query and "Native" SQL Query (see exp17c.jpa.repo.StaffRepository)
- creating REST api end points (resources) via Spring Web
- swagger to generate documents for REST api end points
- jUnit5 to unit and integration test
- using lombok to instrument(auto generate) setter/getter/constructor/.. of a POJO via 
  lombok annotations
- using spring-boot-devtools (see pom.xml), which watches changes in code and triggers auto deployments of app upon saved 
code changes, will ONLY be in effect during dev runs, will NOT be there in PROD jar build

## using h2 as in memory RDBMS (Relational DB Management System)
- to access h2-console via browser with default values go to (assuming server.port=8888) 

  http://localhost:8888/h2-console                 <br>
  in "Login" page of h2, keep below default values <br>
    Driver Class: org.h2.Driver                    <br>
    JDBC URL: jdbc:h2:mem:testdb                   <br>
    User Name: sa                                  <br>
    Password:       (leave it empty)               <br>
      click "Connect"  (can 1st click "Test Connection" to get green "Test successfull" at bottom)   <br>
      then click on the table "EMPLOYEE" or "STAFF" on left hand side, click "Run" button in "SQL statement" tab to see rows in table

## using jdbctTemplate for DAO(Data Access Object) to RDBMS
- see exp17.jdbctemplate.dao.EmployeeDAOImpl

## using JPA to access RDBMS
- see exp17c.jpa.model.Staff for "Entity" class
- see exp17c.jpa.repo.StaffRepository for repo interface which guides repo impl generation 
  via JPA

## using lombok to instrument(auto generate) getter/setter/constructor/toString/.. methods 
in a class via lombok annotations
- see exp17c.jdbctemplate.model.Employee
- see exp17c.jpa.model.Staff 

### lombok setup of STS
- make sure your project's build path is using JDK (not JRE)
- First run your program as Maven Install. If Lombok is not generating anything or if you don't see lombok generated methods in the "outline" window when you look at the lombok annotated class, then you need to configure Lombok with STS following below steps?????????????;
- Locate the directory(basically local maven repository directory, ~/.m2) where Lombok-1.x.y.jar is at (1.x.y is the version)
	+ In my case, it was <br>
	  C:\Users\MyName\.m2\repository\org\projectlombok\lombok\1.18.12
	+ Open up command prompt and type: cd Path_To_Directory(Whatever the path is toLombok.jar)
	+ Then type java -jar lombok-1.x.y.jar
	+ An installer for Lombok will pop up. Hit Specify Location
	+ The location should be the Spring Tool Suite.exei. Mine was located at: <br>
      C:\Program Files\Spring Tool\sts-4.5.1.RELEASE
	+ Hit Install/Update after, and then Quit Installer
- Launch Spring Tool
	+ Right Click Project-&gt;Maven-&gt;Update Project
	+ Right Click Project-&gt;Run As-&gt;Maven Clean
	+ Right Click Project-&gt;Run As-&gt;Maven Install
- Lombok should work now in STS. You should see lombok generated methods in "outline" window located on right side of STS when you open a lombok annotated class. You should be able access those methods using instance of that Class in other parts of code.

## creating REST webservice api end points(resources)
- see exp17c.rest.EmployeeRestController
- see exp17c.rest.StaffRestController

## jUnit5 integration with SpringBoot
- see src/test/java/exp17c.jdbctemplate.dao.EmployeeDAOImplTest that unit tests src/main/java/exp17c.jdbctemplate.dao.EmployeeDAOImpl

## swagger summary
- swagger is a "specification"
- it can be applied in many languages/frameworks
- springfox is a "spring" "implementation" of swagger
- swagger is one of the ways, in fact basically an industry standard, to generate (REST) 
  API documentation
- NOTE "Spring REST Docs" is an alternative gaining popularity to swagger that annotates the api in test 
  code instead of real api code

### how to "add" swagger to your Spring Boot web app to generate documentation of your REST services APIs
- add swagger 2 Spring dependency(ies)
  - add for swagger    (to generate json documentation) add io.springfox  springfox-swagger2   dependency (to pom.xml)
  - add for swagger UI (to generate html documentation) add io.springfox  springfox-swagger-ui dependency (to pom.xml)
- enable swagger in your app
  - add @EnableSwagger2 in SpringBootApplication class or more preferably a @Configuration class
- configure swagger
  - add a "Docket" Spring Bean via @Bean either in main spring class (Exp17cSpringBootH2JdbcTemplateJpaRestJUnit5LombokApplication) 
    or a config class (see exp17c.config.SwaggerConfig)
- add API details as annotations to APIs code
  - add REST end point details using @ApiOperation, @ApiParam into code (see echoMessage 
  REST api end point of exp17c.rest.EmployeeRestController, 
  exp17c.rest.StaffRestController)
  - add model details of model classes used by REST end points using @ApiModel, @ApiModelProperty 
    (see exp17c.jpa.model.Staff)

## how to access generated swagger api documentations
- swagger generated json document link(assuming app is started on port 8888)
  
  NOTE ilker suggest installing "JSONView" extension to google Chrome browser to better view the JSON returned by below link
  
  http://localhost:8888/v2/api-docs
- swagger generated html documents link
  
  http://localhost:8888/swagger-ui.html

  
## to clone and then import project
- if you have windows laptop, make sure gitbash is installed. Then open gitbash
- if you have mac, open Terminal
- issue below to clone the projects
```bash
cd /c/fdu/csci3444/projects
git clone https://github.com/fdu-csci3444/exp17c_springBoot_h2_jdbcTemplate_JPA_REST_swagger_jUnit5_lombok.git
```

- import the project to STS
	- File -> Import -> General -> Projects from Folder or Archive
	- select above cloned project

## to directly import project from github into STS
- 1st copy the github url of the remote project
Go to github link <br>
 https://github.com/fdu-csci3444/exp17c_springBoot_h2_jdbcTemplate_JPA_REST_swagger_jUnit5_lombok.git <br>
   click "Clone or Download" pull down button, click "book" icon to the right of the github url

- import directly from github url into STS
File --> Import --> Git --> Project from Git <br>
 click Next <br>
   click Clone URI <br>
     click Next (the github url should be auto filled, if you had clicked the "book" icon above <br>
       click Next <br>
        choose remote branch (master should be auto picked) <br>
          click Next <br>
            choose "Destination" "Directory" for the project to be created in <br>
              click Finish

## to run the project in STS
- make sure maven dependencies are made available to project
	- right click on project, Maven, Update project
- to run project
	- right click on project, Run as, Java Application
	- or
	- right click on project, Run as, Spring Boot App
  