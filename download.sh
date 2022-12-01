#! /bin/bash

if [ -z "$1" ] ; then
  echo "Please pass the day number, e.g.: "
  echo "./download.sh 6"
  exit 1
fi

if [ -z "$AOC_SESSION" ] ; then
  echo "Make sure AOC_SESSION is set in your environment"
  exit 2
fi


curl "https://adventofcode.com/2022/day/$1/input" \
  --compressed \
  --cookie "session=$AOC_SESSION" \
  > input/day_$(printf "%02d" $1).txt
