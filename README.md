# Introduction
This is a set of small but very functional tools suitable for in small to medium sized projects.

## Db-extract Overview
Db-extract is a very flexible ETL tool.  Unlike a lot of ETL tools which take structured data from one source and often denormalize that data into a second source.  This tool is designed to do specifically for schema to schema transformation.  The original purpose for which this tool was developed was to take data from a legacy application database and move that data into a new, different database schema.  It is able to do that move, transforming data along the way, and assembling all of the proper inter-table relationships in the target.

## Picominmin Overview
The picominmin application (now named Hokulea) is a work progress tracking application designed for teams that are doing continuous deployment.  It tracks and shows variations in the time for work start to work deployed, what I call the loop time.  For CD and actually for any team that is doing short loops there is no other metric that has to be tracked - the ability of a team to accelerate over time, with safety, is a linear function of a teams capablility.

## Picominmin Installation
- install maven 3
- install leiningen from https://github.com/technomancy/leiningen
- install the database following the directions in ./picominmin-db/ReadMe
- install the application following the dirctions in ./pelrapeire/ReadMe