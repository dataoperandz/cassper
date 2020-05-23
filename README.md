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



Simple bulk data changes (CRUD in regular data tables)