#!/bin/sh

mkdir -pv build
sdkpath=$(find . -name 'javafx-sdk-*' -type d | sed q)
javac \
    --module-path ${sdkpath}/lib \
    --add-modules javafx.controls \
    --add-modules javafx.base \
    --add-modules javafx.swing \
    -d ./build src/paint/*.java
cd build
jar cvfm Paint.jar ../manifest.txt *
