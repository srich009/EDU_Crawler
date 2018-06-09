#!/bin/bash

# script to run .edu search engine indexer
# Example use:
# [user@server]$ ./indexer.sh <curr-dir> <num-docs>
#------------------------------------------------------------------------------

#input parameters
FOLDER=$1
DOCS=$2

# error checking
if   [ $# -lt 2 ] ; then
     echo "Error: too few arguments"
     echo "usage: ./indexer.sh <curr-dir> <num-docs>"
     exit 1
elif [ $# -ge 3 ] ; then
     echo "Error: too many arguments"
     echo "usage: ./indexer.sh <curr-dir> <num-docs>"
     exit 1
elif ! [ -e exe/indexer.jar ] ; then
     echo "Error: crawler executable not found"
     exit 1
elif ! [ $DOCS -ne 0 -o $DOCS -eq 0 ] ; then
     echo "Error: number of pages must be integer"
     exit 1
elif ! [ -e $FOLDER ] ; then
     echo "Error: output directory not found"
     exit 1
fi
#------------------------------------------------------------------------------

# do some directory resolving
# the shell automatically converts: "~"  into  "$HOME"
# otherwise user should type full path
if   [ $FOLDER == "." ] ; then
     FOLDER=$PWD
fi
#------------------------------------------------------------------------------

date
echo "starting indexer"
echo "curr-dir  : $FOLDER"
echo "num-docs  : $DOCS"

# calling the java executable
java -jar exe/indexer.jar $FOLDER $DOCS
