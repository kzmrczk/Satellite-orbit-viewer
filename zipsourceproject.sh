#!/bin/bash
# This script zips the content of the src/ folder, ready to be returned to your teacher.
TOCOMPRESS=$(dirname $(readlink -f $0))
DIRNAME=$(basename $TOCOMPRESS)
projectname=$(cat $TOCOMPRESS/.project | grep "<name>" | head -1 | sed -e "s#<.\?name>##g" | sed -e "s#\s##g"); echo $name
ZIPNAME=$projectname-$(whoami).project.zip
TODIR=$(dirname $TOCOMPRESS)
cd $TODIR
[ -f $ZIPNAME ] && rm -f $ZIPNAME
zip -r $ZIPNAME $DIRNAME/src $DIRNAME/.classpath  $DIRNAME/.project
echo "Created: $TODIR/$ZIPNAME"

