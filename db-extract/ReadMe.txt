
Summary:
Does extract - transform - load to bring data from a source database to a target database

Operation:
The etl operation is controlled by a configuration file written in Groovy.  There are two
example configuration files that are included in this project.  One is Config2To3 which is
a working example of how to bring data from the current tracker 2.0 database into the
new Tracker 3.0 database.  The configuration file Config2To2 is a test example which
demonstates how to transfer a set of data between two tracker 3.0 databases

Dependencies:
Requires a tracker db version of at least 81

Building:
--Install Ant and Groovy 
--From the project root input the build command:
groovy build.groovy jar
to build ./tracker/tracker-etl-0.1.jar
--Run tracker-etl.bat (Windows) or tracker-etl.sh (Linux)

Troubleshooting:
On error as dump of final application state will be made to the console.  For detailed 
progress of the application during its execution consult tracker-etl.log in the project
base directory. 

 