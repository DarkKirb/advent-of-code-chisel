#!/bin/sh
set -ex

sbt run

mkdir -p target

mv AOC.* target/

cd target
yosys -p "synth_ecp5 -top top -json AOC.json" AOC.sv ../top.v
nextpnr-ecp5 --json AOC.json --textcfg AOC_out.config --um5g-85k --package CABGA381 --speed 8 --freq 100 --router router2 --parallel-refine --lpf ../ecp5evn.lpf
ecppack --svf AOC.svf AOC_out.config AOC.bit --compress
