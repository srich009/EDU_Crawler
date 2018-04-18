#!/bin/bash

# script to run .edu webcrawler
# Example use:
# [user@server]$ ./crawler.sh <seed-file> <num-pages> <hops-away> <output-dir>

# error checking
if   [ $# -lt 4 ] ; then
    echo "Error: too few arguments"
    exit 1
elif [ $# -ge 5 ] ; then
    echo "Error: too many arguments"
    exit 1
elif ! [ -e exe/web_crawler.jar ] ; then
    echo "Error: crawler executable not found"
    exit 1
elif ! [ -e $1 ] ; then
    echo "Error: seed-file not found"
    exit 1
fi

# input parameters
SEED=$1
PAGES=$2
HOPS=$3
OUTPUT=$4

date
echo "starting crawler"
echo "seed-file  : $SEED"
echo "num-pages  : $PAGES"
echo "hops-away  : $HOPS"
echo "output-dir : $OUTPUT"

# calling the java executable
java -jar exe/web_crawler.jar $SEED $PAGES $HOPS $OUTPUT
