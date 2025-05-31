#!/bin/sh

javaver=$(java -version 2>&1 | awk '/openjdk version/ { print $3 }' | sed 's/"//g')

arch=""
case $(uname -m) in
    arm64) arch=aarch64 ;;
esac

os=""
case $(uname) in
    Darwin) os=osx ;;
esac

curl -LO https://download2.gluonhq.com/openjfx/${javaver}/openjfx-${javaver}_${os}-${arch}_bin-sdk.zip
