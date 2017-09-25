#!/usr/bin/env sh

mkdir target
cd target/
cmake -DCMAKE_TOOLCHAIN_FILE=../../../../toolchain/linux_x86_64.cmake ../../../../
cd -
