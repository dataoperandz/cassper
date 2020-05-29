# Cassper

Schema change management tool(`version controller`) for `Apache Cassandra` that can be used with applications running on the `JVM`.

![cassper-original](https://user-images.githubusercontent.com/65799952/82756510-53b45980-9df8-11ea-9e9c-215639d6e0b1.png)


### Cassper backgournd

Versioned migrations have a version, a description and a checksum. The version must be unique. The description is purely informative for you to be able to remember what each migration does. The checksum is there to detect accidental changes. Versioned migrations are the most common type of migration. They are applied in order exactly once. Fortunately, there are tool for managing schema changes like `Liquibase`, `Flyway`, and `Active Record` for `Ruby on Rails` applications. These tools however, are designed specifically for relational databases.

`Cassper` is a database migration tool which maily targetted for `Apache Cassandra`. It has written with `Scala` and designed based on `Flayway` schema migration tool. Cassper schema migrations are most commonly written in `CQL`. This makes it easy to get started and leverage any existing scripts, tools and skills. It gives you access to the full set of capabilities of your cassandra database and eliminates the need to understand any intermediate translation layer. `CQL` migrations are typically used for

```
1. DDL changes (CREATE/ALTER/DROP statements for TABLES,TYPES,KEYSPACES etc)
2. Simple reference data changes (CRUD in reference data tables)
```

### Cassper migration scripts

Cassper schema migration scripts need to put in the `src/main/resources/cassper` directory in your sbt project. The schema migration script needs to follow a specific format(the format is similar to `Flyway` migration script format). Following is the format of schema migration script `V_1_2__create_table_account.cql`.

![cassper-schema-format](https://user-images.githubusercontent.com/2450752/83220095-1295af80-a140-11ea-82d2-05fc2f333cab.png)

When running the migration, Cassper will executes each and every migration scripts in the src/main/resources/cassper director. The migration script executing order is determined by the version number. For an example `1.0`, `1.1`, `1.2` etc. Following are some example migration scripts that can be put in `src/main/resources/cassper` directory.

```
❯❯ ls -al src/main/resources/cassper
-rw-r--r--  1 eranga  staff  158 May 28 00:23 V_1_1__create_udt_trans.cql
-rw-r--r--  1 eranga  staff  361 May 28 03:34 V_1_2__create_table_accounts.cql
-rw-r--r--  1 eranga  staff  361 May 28 03:34 V_1_3__alter_table_accounts.cql
```

### Cassper dependency

Cassper provides library for `Scala Sbt` applications and `Java Maven` applications. Following are the `Maven` and `Sbt` dependencies of Cassper.

```
<dependency>
  <groupId>io.github.dataoperandz</groupId>
  <artifactId>cassper</artifactId>
  <version>0.3</version>
</dependency>
```

```
libraryDependencies += "io.github.dataoperandz" % "cassper" % "0.3"
```

### Run migrations

When running the migration, Cassper scans the migration scripts in the `src/main/resources/cassper` directory and execute them. Cassper stores executed migration script information in `schema_version` table. This table reside in the Cassandra keyspace where migration is running. Once migration script executed, Cassper saves the migration script information in `schema_version` table. When next time executing the migration it checks the schema_version table and finds the executed migration script information. If migration script executed one time, it won’t execute it again. Cassper not allowed to changes the previously executed migration scripts. It stores hash of the executed migration scripts on schema_version table and compare them when next time executing the migration. If script altered(that means hash is changed) it will raise an exception. Following is the way to run the migration. `Cassper().builder` required `com.datastax.driver.core.Session`. You can pass the `com.datastax.driver.core.Session` instance which is using in your application to the Cassper.

```
// session - com.datastax.driver.core.Session
// keyspace - keyspace name
val builder = new Cassper().build("mystiko", session)
builder.migrate("mystiko")
```

### Test migrations

You can test the Cassper migration in your local environment by running `Main` method of the application(e.g with `IntelliJ IDEA`). In production environment you can build a `.jar` file of the application and run it with `Docker` kind of service. Then Cassper scans the `.jar` file and finds the migration scripts on `src/main/resources/cassper` directory and execute them.
