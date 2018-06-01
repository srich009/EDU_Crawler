#!/bin/bash

# from inside Phase2 folder
# run maven then jar file
function launch () {
    cd backend
    mvn package
    java -jar target/lucene-searcher-0.0.1-SNAPSHOT.jar
}

launch