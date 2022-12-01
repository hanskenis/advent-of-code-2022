#! /bin/bash

if [[ $# -ne 3 ]]; then
    echo "Illegal number of parameters" >&2
    exit 2
fi

if [[ -z $AOC_SESSION ]]; then
    echo "AOC_SESSION not set" >&2
    exit 2
fi

curl "https://adventofcode.com/2022/day/$1/answer" \
    -X POST \
    --cookie "session=$AOC_SESSION" \
    --data "level=$2&answer=$3"