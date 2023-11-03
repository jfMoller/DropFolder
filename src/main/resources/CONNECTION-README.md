### How to connect

* Create an application.properties file in this folder
* Paste the content below into the file
* Add the provided connection string to spring.datasource.url
* Run Application.java

#### application.properties content:

```
### Establishes the connection to NeonDB
spring.datasource.url=YOUR_CONNECTION_STRING

### Auto create or update of tables based on the defined data-model
spring.jpa.hibernate.ddl-auto= update

```