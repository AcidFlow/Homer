#!/usr/bin/env sh

mkdir target
cd target/
cmake -DCMAKE_TOOLCHAIN_FILE=../../../../toolchain/linux_armv7l.cmake ../../../../
cd -
