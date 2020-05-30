# Cassper

![cassper-original](https://user-images.githubusercontent.com/65799952/82756510-53b45980-9df8-11ea-9e9c-215639d6e0b1.png)

---

## About Cassper

`Cassper` is a schema migration tool for `Apache Cassandra`. It can be used as as Apache Cassandra schema change management tool and schema version controller in `JVM` based applications. Cassper has written with `Scala` and designed based on `Flayway` schema migration tool(Flyway mainly targetted for relational databases). `Cassper` schema migrations can be used for `DDL` changes(`CREATE/ALTER/DROP` statements of `tables`, `udts`, `keyspaces`) and Simple data changes(`CRUD` operations in `tables`).

---

## Cassper migration scripts

Cassper schema migrations scripts are written in `CQL`. This makes it easy to get started and leverage any existing scripts, tools and skills. It gives you access to the full set of capabilities of your Cassandra database and eliminates the need to understand any intermediate translation layer. Cassper schema migration scripts need to put in the `src/main/resources/cassper` directory in your sbt project. The schema migration script needs to follow a specific format(the format is similar to `Flyway` migration script format). Following is the format of schema migration script `V_1_2__create_table_account.cql`.

![cassper-schema-format](https://user-images.githubusercontent.com/2450752/83220095-1295af80-a140-11ea-82d2-05fc2f333cab.png)

When running the migration, Cassper will executes each and every migration scripts in the src/main/resources/cassper director. The migration script executing order is determined by the version number. For an example `1.0`, `1.1`, `1.2` etc. Following are some example migration scripts that can be put in `src/main/resources/cassper` directory.

```
❯❯ ls -al src/main/resources/cassper
V_1_1__create_udt_trans.cql
V_1_2__create_table_accounts.cql
V_1_3__alter_table_accounts.cql
```

`V_1_1__create_udt_trans.cql` migration script

```
CREATE TYPE IF NOT EXISTS rahasak.trans (
  execer TEXT,
  id TEXT,
  actor TEXT,
  message TEXT,
  digsig TEXT,
  timestamp TIMESTAMP
);
```

`V_1_2__create_table_accounts.cql` migration script

```
CREATE TABLE IF NOT EXISTS rahasak.accounts (
  id TEXT,
  name TEXT,
  phone TEXT,
  email TEXT,
  disabled BOOLEAN,
  timestamp TIMESTAMP,

  PRIMARY KEY(id)
);
```

`V_1_3__alter_table_accounts.cql` migration script

```
ALTER TABLE rahasak.accounts ADD address TEXT;
```

---

## Cassper dependency

`Maven` dependency for `Java` applications

```
<dependency>
  <groupId>io.github.dataoperandz</groupId>
  <artifactId>cassper</artifactId>
  <version>0.4</version>
</dependency>
```

`Sbt` dependency for `Scala` applications

```
libraryDependencies += "io.github.dataoperandz" % "cassper" % "0.4"
```

---

## Run migrations

When running the migration, Cassper scans the migration scripts in the `src/main/resources/cassper` directory and execute them. Cassper stores executed migration script information in `schema_version` table. This table reside in the Cassandra keyspace where migration is running. Once migration script executed, Cassper saves the migration script information in `schema_version` table. When next time executing the migration it checks the schema_version table and finds the executed migration script information. If migration script executed one time, it won’t execute it again. Cassper not allowed to changes the previously executed migration scripts. It stores hash of the executed migration scripts on schema_version table and compare them when next time executing the migration. If script altered(that means hash is changed) it will raise an exception. Following is the way to run the migration. `Cassper().builder()` required `com.datastax.driver.core.Session`. You can pass the `com.datastax.driver.core.Session` instance which is using in your application to the Cassper.

```
// session - com.datastax.driver.core.Session
// rahasak - keyspace name
val builder = new Cassper().build("rahasak", session)
builder.migrate("rahasak")
```

---

## Test migrations

You can test the Cassper migration in your local environment by running `Main` method of the application(e.g with `IntelliJ IDEA`). In production environment you can build a `.jar` file of the application and run it with `Docker` kind of service. Then Cassper scans the `.jar` file and finds the migration scripts on `src/main/resources/cassper` directory and execute them.
