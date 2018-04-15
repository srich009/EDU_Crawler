#!/bin/bash

# script to run .edu webcrawler
# Example use:
# [user@server]$ ./crawler.sh <seed-file: seed.txt> <num-pages: 10000> <hops-away: 6> <output-dir>

# error checking input parameters
if   [ $# -lt 4 ] ; then
    echo "Error: too few arguments"
    exit 1
elif [ $# -ge 5 ] ; then
    echo "Error: too many arguments"
    exit 1
fi

SEED=$1
PAGES=$2
HOPS=$3
OUTPUT=$4

echo "starting crawler"
echo "seed-file: $SEED"
echo "num-pages: $PAGES"
echo "hops-away: $HOPS"
echo "output-dir $OUTPUT"

# insert code here
