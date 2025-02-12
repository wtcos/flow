#!/bin/bash
TIMESTAMP=`date +%Y%m%d%H%M%S`
BRANCH=`git rev-parse --abbrev-ref HEAD`
IFS="@" read -r NAME EMAIL <<< `git config user.email`
printf "%s_%s.flot\n" $BRANCH $NAME
printf "%s_%s_%s.flol" $BRANCH $EMAIL $TIMESTAMP