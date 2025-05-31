#!/bin/sh

# https://gluonhq.com/products/javafx/

sdkpath=$(find . -name 'javafx-sdk-*' -type d | sed q)
java \
    --module-path ${sdkpath}/lib/ \
    --add-modules javafx.controls \
    --add-modules javafx.base \
    --add-modules javafx.swing \
    -jar build/Paint.jar
