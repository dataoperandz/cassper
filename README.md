# Cassper
Cassper is a schema change management tool **(version controller)** and tool for Apache Cassandra that can be used with applications running on the JVM.

##Why?
Versioned migrations have a version, a description and a checksum. The version must be unique. The description is purely informative for you to be able to remember what each migration does. The checksum is there to detect accidental changes. Versioned migrations are the most common type of migration. They are applied in order exactly once.

 Fortunately, there are tool for managing schema changes like Liquibase, Flyway, and Active Record for Ruby on Rails applications. These tools however, are designed specifically for relational databases.  Cassper is designed only for use with Cassandra, not for any other database systems.

Cassper is written in Scala. 
