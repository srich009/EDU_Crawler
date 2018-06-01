#!/bin/bash

# how to query localhost with string using curl
# curl http://localhost:8080/search?query=STRING

STEM="http://localhost:8080/search?query="

while true;
do
    echo 
    echo -n "QUERY>> "
    read QUERY
    curl $STEM$QUERY
done