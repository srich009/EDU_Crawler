#!/bin/bash

# run from inside Phase2

# how to query localhost with string using curl
# curl http://localhost:8080/search?query=STRING

STEM="http://localhost:8080/search?query="

# run maven then jar file
function launch () {
    cd backend
    mvn package
    java -jar target/lucene-searcher-0.0.1-SNAPSHOT.jar
}

launch

while true;
do
   echo -n "QUERY>> "
   read QUERY
   curl $STEM$QUERY
done

