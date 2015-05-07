# Introduction
This is a set of small, functional tools suitable for use in small to medium sized projects.

# Db-extract 
## Overview
Db-extract is a very flexible ETL tool.  Unlike a lot of ETL tools which take structured data from one source and denormalize that data into a second source.  This tool is specifically designed to do schema to schema transformations.  The original purpose for this tool was to take data from a legacy application database and move that data into a new, different database schema.  It is able to move data in a generic way regardless of the source or target, transforming data along the way, and assembling all of the proper inter-table relationships in the target.

Tags: Groovy

# Picominmin 
## Overview
An experimental project to investigate using Closure and YUI for a web application as the basis for a web
application. The application is designed for teams that are doing continuous deployment and tracks tasks and shows variations in the time from work start to work deployed, what I call the loop time.

Tags: Clojure, YUI

## Picominmin Installation
- install maven 3
- install leiningen from https://github.com/technomancy/leiningen
- install the database following the directions in ./picominmin-db/ReadMe
- install the application following the dirctions in ./pelrapeire/ReadMe
