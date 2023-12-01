package aoc.day1

import chisel3._
import uart._
import aoc.utils.ZeroExtend
import chisel3.util._

class DigitParser extends Module {
  val io = IO(new Bundle {
    val char = Input(UInt(8.W))
    val digit = Output(UInt(4.W))
    val found = Output(Bool())
  })
  io.found := (io.char >= 0x30.U && io.char <= 0x39.U)
  io.digit := io.char - 0x30.U
}

class DigitParser2 extends Module {
  val io = IO(new Bundle {
        val in = Flipped(new UartIO());
        val out = new DecoupledIO(UInt(4.W));
  })
  io.in.ready := true.B;
  val outReg = RegInit(0.U(4.W))
  val validReg = RegInit(false.B)
  
  io.out.bits := outReg
  io.out.valid := validReg

  when (io.in.valid) {
    outReg := io.in.bits - 0x30.U
    validReg := io.in.bits >= 0x30.U && io.in.bits <= 0x39.U
  }

  when (validReg && io.out.ready) {
    validReg := false.B
  }
}

class DigitNameParser extends Module {
    val io = IO(new Bundle {
        val in = Flipped(new UartIO());
        val out = new DecoupledIO(UInt(4.W));
    })
    object State extends ChiselEnum {
        val initial, s_o, s_on, s_one, s_t, s_tw, s_two, s_th, s_thr, s_thre, s_three, s_f, s_fo, s_fou, s_four, s_fi, s_fiv, s_five, s_s, s_si, s_six, s_se, s_sev, s_seve, s_seven, s_e, s_ei, s_eig, s_eigh, s_eight, s_n, s_ni, s_nin, s_nine = Value
    }
    import State._

    io.in.ready := true.B;

    val stateReg = RegInit(initial)
    val outReg = RegInit(0.U(4.W))
    val validReg = RegInit(false.B)

    io.out.valid := validReg
    io.out.bits := outReg

    when(stateReg === s_one || stateReg === s_two || stateReg === s_three || stateReg === s_four || stateReg === s_five || stateReg === s_six || stateReg === s_seven || stateReg === s_eight || stateReg === s_nine) {
        validReg := true.B
        outReg := MuxLookup(stateReg, 0.U)(Seq(
        s_one -> 1.U,
        s_two -> 2.U,
        s_three -> 3.U,
        s_four -> 4.U,
        s_five -> 5.U,
        s_six -> 6.U,
        s_seven -> 7.U,
        s_eight -> 8.U,
        s_nine -> 9.U,
    ))
    }

    when(validReg && io.out.ready) {
        validReg := false.B
    }

    when(io.in.valid) {
        switch (stateReg) {
            is (initial, s_four, s_six) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n
                ))
            }
            is (s_o, s_two) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_on
                ))
            }
            is (s_on) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_one,
                    0x6e.U -> s_n,
                    0x69.U -> s_ni
                ))
            }
            is (s_t, s_eight) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x77.U -> s_tw,
                    0x68.U -> s_th,
                ))
            }
            is (s_tw) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_two,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n
                ))
            }
            is (s_th) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x72.U -> s_thr,
                ))
            }
            is (s_thr) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_thre,
                    0x6e.U -> s_n
                ))
            }
            is (s_thre) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_three,
                    0x6e.U -> s_n,
                    0x69.U -> s_ei
                ))
            }
            is (s_f) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_fo,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x69.U -> s_fi,
                ))
            }
            is (s_fo) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_on,
                    0x75.U -> s_fou,
                ))
            }
            is (s_fou) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x72.U -> s_four,
                ))
            }
            is (s_fi) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x76.U -> s_fiv,
                ))
            }
            is (s_fiv) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_five,
                    0x6e.U -> s_n,
                ))
            }
            is (s_s) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_se,
                    0x6e.U -> s_n,
                    0x69.U -> s_si,
                ))
            }
            is (s_si) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x78.U -> s_six,
                ))
            }
            is (s_se) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x76.U -> s_sev,
                    0x69.U -> s_ei,
                ))
            }
            is (s_sev) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_seve,
                    0x6e.U -> s_n,
                ))
            }
            is (s_seve) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_seven,
                    0x69.U -> s_ei,
                ))
            }
            is (s_e, s_one, s_three, s_five, s_nine) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x69.U -> s_ei,
                ))
            }
            is (s_ei) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x67.U -> s_eig,
                ))
            }
            is (s_eig) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x68.U -> s_eigh,
                ))
            }
            is (s_eigh) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x74.U -> s_eight,
                ))
            }
            is (s_n, s_seven) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_n,
                    0x69.U -> s_ni,
                ))
            }
            is (s_ni) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_e,
                    0x6e.U -> s_nin,
                ))
            }
            is (s_nin) {
                stateReg := MuxLookup(io.in.bits, initial)(Seq(
                    0x6F.U -> s_o,
                    0x74.U -> s_t,
                    0x66.U -> s_f,
                    0x73.U -> s_s,
                    0x65.U -> s_nine,
                    0x6e.U -> s_n,
                    0x69.U -> s_ni
                ))
            }
        }
    }
}

class Part2Parser extends Module {
    val io = IO(new Bundle {
        val in = Flipped(new UartIO());
        val out = new DecoupledIO(UInt(4.W));
    })

    val outReg = RegInit(0.U(4.W))
    val validReg = RegInit(false.B)
    val digitNameParserReady = RegInit(true.B)
    val digitParserReady = RegInit(true.B)

    io.out.bits := outReg
    io.out.valid := validReg

    val digitParser = Module(new DigitParser2)
    val digitNameParser = Module(new DigitNameParser)

    digitNameParser.io.out.ready := io.in.ready && digitNameParserReady;
    digitNameParser.io.in <> io.in

    digitParser.io.out.ready := io.in.ready && digitParserReady;
    digitParser.io.in <> io.in

    when(validReg && io.out.ready) {
        validReg := false.B
        digitParserReady := true.B
        digitNameParserReady := true.B
    }

    when(digitNameParser.io.out.valid && digitNameParserReady) {
        digitParserReady := false.B
        validReg := true.B
        outReg := digitNameParser.io.out.bits
    }

    when(digitParser.io.out.valid && digitParserReady) {
        digitNameParserReady := false.B
        validReg := true.B
        outReg := digitParser.io.out.bits
    }
}

class SendWord extends Module {
  val io = IO(new Bundle {
    val data = Input(UInt(32.W))
    val enable = Input(Bool())
    val tx = new UartIO();
  });
  val data = RegInit(VecInit(0.U(8.W), 0.U(8.W), 0.U(8.W), 0.U(8.W)))
  val cntReg = RegInit(0.U(3.W))
  val enabled = RegInit(false.B)

  io.tx.valid := enabled && (cntReg =/= 4.U)
  io.tx.bits := data(cntReg)

  when(io.enable) {
    data(0) := VecInit(io.data.asBools.slice(0, 8)).asUInt
    data(1) := VecInit(io.data.asBools.slice(8, 16)).asUInt
    data(2) := VecInit(io.data.asBools.slice(16, 24)).asUInt
    data(3) := VecInit(io.data.asBools.slice(24, 32)).asUInt
    enabled := true.B
    cntReg := 0.U
  }.otherwise {
    when(enabled && io.tx.ready && cntReg =/= 4.U) {
      cntReg := cntReg + 1.U;
    }
  }
}

class Part1 extends Module {
  val io = IO(new Bundle {
    val tx = new UartIO();
    val rx = Flipped(new UartIO());
  })

  val firstDigit = RegInit(0.U(4.W));
  val firstDigitSet = RegInit(false.B);
  val lastDigit = RegInit(0.U(4.W));
  val sum = RegInit(0.U(17.W));

  val digitParser = Module(new DigitParser)

  val resultWriter = Module(new SendWord())

  digitParser.io.char := io.rx.bits;

  io.rx.ready := io.tx.ready

  resultWriter.io.tx <> io.tx
  resultWriter.io.data := sum
  resultWriter.io.enable := (io.rx.bits === 0x24.U) && io.rx.valid

  when(io.rx.valid) {
    when(io.rx.bits =/= 0x0d.U) {
      when(io.rx.bits === 0x24.U) {
        sum := 0.U
      }.otherwise {
        when(digitParser.io.found) {
          when(!firstDigitSet) {
            firstDigit := digitParser.io.digit
            firstDigitSet := true.B
          }
          lastDigit := digitParser.io.digit
        }
      }
    }.otherwise {
      sum := sum + firstDigit * 10.U(8.W) + lastDigit;
      firstDigitSet := false.B
      firstDigit := 0.U
      lastDigit := 0.U
    }
  }
}

class Part2 extends Module {
  val io = IO(new Bundle {
    val tx = new UartIO();
    val rx = Flipped(new UartIO());
  })

  val firstDigit = RegInit(0.U(4.W));
  val firstDigitSet = RegInit(false.B);
  val lastDigit = RegInit(0.U(4.W));
  val sum = RegInit(0.U(17.W));

  val digitParser = Module(new Part2Parser)

  val resultWriter = Module(new SendWord())

  digitParser.io.in <> io.rx
  digitParser.io.out.ready := true.B

  val delayReg = RegInit(false.B)

  io.rx.ready := io.tx.ready

  resultWriter.io.tx <> io.tx
  resultWriter.io.data := sum
  resultWriter.io.enable := (io.rx.bits === 0x24.U) && io.rx.valid  

  when(delayReg) {
    delayReg := false.B
  }.otherwise {
    when(digitParser.io.out.valid) {
    when(!firstDigitSet) {
        firstDigit := digitParser.io.out.bits
        firstDigitSet := true.B
    }
    lastDigit := digitParser.io.out.bits
  }
    when(io.rx.valid) {
    when(io.rx.bits =/= 0x0d.U) {
      when(io.rx.bits === 0x24.U) {
        sum := 0.U
      }
    }.otherwise {
      sum := sum + firstDigit * 10.U(8.W) + lastDigit;
      firstDigitSet := false.B
      firstDigit := 0.U
      lastDigit := 0.U
      delayReg := true.B
    }
  }
}
}
