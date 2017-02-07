/* Actors.sql*/
/* This is .sql batch script created to
 create, load, and query a table in MySQL
 */
/* 
1.Create a table called Actors in the TEST database 
(since this is just a warm-up project, we will be using TEST, 
not CS144). The Actors table should have the following schema:
Actors(Name:VARCHAR(40), Movie:VARCHAR(80), Year:INTEGER, Role:VARCHAR(40))

2.Load the actors.csv file into the Actors table.

3.Retrieve the movie 'Die Another Day' from the Actors table.
Give me the names of all the actors in the movie 'Die Another Day'.

4.Once you are done, drop the Actors table from MySQL,
so that it will not stay in the database for later parts of our project.
*/

/* Create a table called Actors in the TEST database */
CREATE TABLE Actors(
	Name VARCHAR(40),
	Movie VARCHAR(80),
	Year INTEGER,
	Role VARCHAR(40)
);

/* Load the actors.csv file into the Actors table. */
LOAD DATA LOCAL INFILE '~/data/actors.csv'  INTO TABLE Actors
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

/* Give the names of all the actors in the movie 'Die Another Day'. */
SELECT Name FROM Actors WHERE Movie = 'Die Another Day';

/* Once done, drop the Actors table from MySQL */
DROP TABLE Actors;

