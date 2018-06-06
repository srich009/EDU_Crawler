#!/bin/bash

# how to query localhost with string using curl
# curl http://localhost:8080/search?query=STRING&withPR=BOOL&count=10
# STRING = query to search for
# BOOL   = true/false , ude pagerank or not
# count  = number of pages to show

FRONT="http://localhost:8080/search?query="
BACK="&withPR=false&count=10"

while true;
do
    echo ""
    echo -n "QUERY>> "
    read QUERY
    curl $FRONT$QUERY$BACK
done
