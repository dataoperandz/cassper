# Cassper Quickstart Guide



Cassper is a schema change management tool **(version controller)** and tool for Apache Cassandra that can be used with applications running on the JVM.

## Why
Versioned migrations have a version, a description and a checksum. The version must be unique. The description is purely informative for you to be able to remember what each migration does. The checksum is there to detect accidental changes. Versioned migrations are the most common type of migration. They are applied in order exactly once.

 Fortunately, there are tool for managing schema changes like Liquibase, Flyway, and Active Record for Ruby on Rails applications. These tools however, are designed specifically for relational databases.  Cassper is designed only for use with Cassandra, not for any other database systems.

Cassper is written in Scala. 


## CQL Based Migration
Migrations are most commonly written in CQL. This makes it easy to get started and leverage any existing scripts, tools and skills. It gives you access to the full set of capabilities of your cassandra database and eliminates the need to understand any intermediate translation layer.

CQL-based migrations are typically used for

- DDL changes (CREATE/ALTER/DROP statements for TABLES,TYPES,KEYSPACES,…)
- Simple reference data changes (CRUD in reference data tables)

#Naming
In order to be picked up by Flyway, SQL migrations must comply with the following naming pattern:

![naming](https://user-images.githubusercontent.com/9468378/82719687-6f214680-9cca-11ea-9119-8abedadd4846.png)

The file name consists of the following parts:
- **Prefix**- V for versioned.
 - **Version**- Cassper has two versions.
  
 Examples- 1_1 , 1_2, 2_1, 2_2 ………….. 400_1
 - **Seperator1**- This is used to seperate version and prefix.
  - **Seperator2**- This is used to seperate version and description.
 -  **Description**-  Underscores separate the words.
  
  Examples- 
  ```
 Add_new_keyspace
initialization
Create_table
  ```
  - **Suffix**- .cql
  
  Examples for naming- 
  - V_1_1__my_create_keyspace.cql
  - V_1_2__my_create_udt.cql
  - V_1_3__my_create_tables.cql
  
  # Discovery
  Cassper discovers all the migration files within cassper directory inside resources.
  
  ![Screenshot from 2020-05-24 08-39-34](https://user-images.githubusercontent.com/65799952/82746857-ed0c4d00-9db1-11ea-8141-abbc5bd83860.png)

#Usage

Cassper can be used in Java and scala

**i. Use keyspace name as parameter**
  ```

val builder = new Cassper().build("keyspace", session)
builder.migrate("keyspace")
```
- session- com.datastax.driver.core.Session
- keyspace- keyspace name

**ii . Uses keyspace of session**
```
val builder = new Cassper().build(session)
builder.migrate("keyspace")
```
#How it works
When we use Cassper, it is created table called "schema_version"

![Screenshot from 2020-05-24 10-07-43](https://user-images.githubusercontent.com/65799952/82746912-6efc7600-9db2-11ea-82e1-bba8d630590e.png)

Scripts are running ascending order by using version of the file. So we must use correct version when adding the new script file. Always it should be greater than existing latest version.

![Screenshot from 2020-05-24 10-13-48](https://user-images.githubusercontent.com/65799952/82746922-863b6380-9db2-11ea-8da3-2ed8a5203773.png)

We can not change existing script files in Cassper. It will end up with exception.
