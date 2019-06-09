#!/bin/bash

for i in {1..1000};
do
    cat server_test.bin | nc 127.0.0.1 8787 &
done
