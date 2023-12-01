package aoc.utils

import chisel3._

class ZeroExtend(inBits: Int, outBits: Int) extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(outBits.W))
    val in = Input(UInt(inBits.W))
  })

  val bools = Wire(Vec(outBits, Bool()))

  for (i <- 0 until outBits) {
    if (i >= inBits) {
      bools(i) := false.B;
    } else {
      bools(i) := io.in(i);
    }
  }

  io.out := bools.asUInt
}
