#!/bin/bash

# script to run .edu webcrawler
# Example use:
# [user@server]$ ./crawler.sh <seed-file> <num-pages> <hops-away> <output-dir>
#------------------------------------------------------------------------------

# input parameters
SEED=$1
PAGES=$2
HOPS=$3
OUTPUT=$4

# error checking
if   [ $# -lt 4 ] ; then
     echo "Error: too few arguments"
     echo "usage: ./crawler.sh <seed-file> <num-pages> <hops-away> <output-dir>"
     exit 1
elif [ $# -ge 5 ] ; then
     echo "Error: too many arguments"
     echo "usage: ./crawler.sh <seed-file> <num-pages> <hops-away> <output-dir>"
     exit 1
elif ! [ -e exe/web_crawler.jar ] ; then
     echo "Error: crawler executable not found"
     exit 1
elif ! [ -e $SEED ] ; then
     echo "Error: seed-file not found"
     exit 1
elif ! [ $PAGES -ne 0 -o $PAGES -eq 0 ] ; then
     echo "Error: number of pages must be integer"
     exit 1
elif ! [ $HOPS -ne 0 -o $HOPS -eq 0 ] ; then
     echo "Error: number of hops must be integer"
     exit 1
elif ! [ -e $OUTPUT ] ; then
     echo "Error: output directory not found"
     exit 1
fi
#------------------------------------------------------------------------------

# do some directory resolving
# the shell automatically converts: "~"  into  "$HOME"
# otherwise user should type full path
if   [ $OUTPUT == "." ] ; then
     OUTPUT=$PWD
fi
#------------------------------------------------------------------------------

date
echo "starting crawler"
echo "seed-file  : $SEED"
echo "num-pages  : $PAGES"
echo "hops-away  : $HOPS"
echo "output-dir : $OUTPUT"

# calling the java executable
java -jar exe/web_crawler.jar $SEED $PAGES $HOPS $OUTPUT
