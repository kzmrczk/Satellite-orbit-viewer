#!/bin/bash
# script to extract the project to give to students
TOCOMPRESS=$(dirname $(readlink -f $0))
DIRNAME=$(basename $TOCOMPRESS)
ZIPNAME=$DIRNAME.zip
TODIR=$(dirname $TOCOMPRESS)
cd $TODIR
[ -f $ZIPNAME ] && rm -f $ZIPNAME
zip -r $ZIPNAME $DIRNAME/src $DIRNAME/.classpath $DIRNAME/.project $DIRNAME/libs $DIRNAME/*.sh
echo "Created full: $TODIR/$ZIPNAME"

