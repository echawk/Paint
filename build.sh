#!/bin/sh

mkdir -pv build
javac -d ./build src/paint/*.java
cd build
jar cvfm Paint.jar ../manifest.txt *
