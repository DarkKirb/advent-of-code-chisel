package aoc

import chisel3._
import chisel3.util.Counter
import circt.stage.ChiselStage
import uart._

class AOC(freq: Int) extends Module {
  val io = IO(new Bundle {
    val led = Output(Vec(8, Bool()))
    val uart_rx = Input(UInt(1.W))
    val uart_tx = Output(UInt(1.W))
  })

  val tx = Module(new BufferedTx(freq, 1_000_000));
  val rx = Module(new Rx(freq, 1_000_000));

  io.uart_tx <> tx.io.txd
  rx.io.rxd <> io.uart_rx

  val d1part2 = Module(new day1.Part2);
  tx.io.channel <> d1part2.io.tx;
  d1part2.io.rx <> rx.io.channel;

  io.led(0) := !io.uart_tx.asBool;
  io.led(1) := !io.uart_rx.asBool;
  io.led(2) := false.B;
  io.led(3) := false.B;
  io.led(4) := false.B;
  io.led(5) := false.B;
  io.led(6) := false.B;
  io.led(7) := false.B;
}

object AOC extends App {
  ChiselStage.emitSystemVerilogFile(
    new AOC(100_000_000),
    firtoolOpts = Array(
      "-disable-all-randomization",
      "-strip-debug-info",
      "--lowering-options=disallowLocalVariables,disallowPackedArrays"
    )
  )
}
