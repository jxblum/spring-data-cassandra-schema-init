CREATE KEYSPACE IF NOT EXISTS test WITH replication = { 'class':'SimpleStrategy','replication_factor':'1' };
USE test;
CREATE TABLE IF NOT EXISTS "Users" (id INT PRIMARY KEY, name TEXT);
CREATE INDEX IF NOT EXISTS UsersNameIdx ON "Users"(name);
