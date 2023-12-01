
module pll_12_100(input clki, output clko);
    (* ICP_CURRENT="12" *) (* LPF_RESISTOR="8" *) (* MFG_ENABLE_FILTEROPAMP="1" *) (* MFG_GMCREF_SEL="2" *)
    EHXPLLL #(
        .PLLRST_ENA("DISABLED"),
        .INTFB_WAKE("DISABLED"),
        .STDBY_ENABLE("DISABLED"),
        .DPHASE_SOURCE("DISABLED"),
        .CLKOP_FPHASE(0),
        .CLKOP_CPHASE(11),
        .OUTDIVIDER_MUXA("DIVA"),
        .CLKOP_ENABLE("ENABLED"),
        .CLKOP_DIV(12),
        .CLKFB_DIV(25),
        .CLKI_DIV(3),
        .FEEDBK_PATH("CLKOP")
    ) pll_i (
        .CLKI(clki),
        .CLKFB(clko),
        .CLKOP(clko),
        .RST(1'b0),
        .STDBY(1'b0),
        .PHASESEL0(1'b0),
        .PHASESEL1(1'b0),
        .PHASEDIR(1'b0),
        .PHASESTEP(1'b0),
        .PLLWAKESYNC(1'b0),
        .ENCLKOP(1'b0),
    );
endmodule


module top (
    input clk12,
    input gsrn,
    output [7:0] ledn,
    input scl0,
    output sda0
);
    wire [7:0] led;
    wire clk100;
    pll_12_100 pll100(.clki(clk12), .clko(clk100));
    AOC aoc (
        .clock(clk100),
        .reset(!gsrn),
        .io_led_0(led[0]),
        .io_led_1(led[1]),
        .io_led_2(led[2]),
        .io_led_3(led[3]),
        .io_led_4(led[4]),
        .io_led_5(led[5]),
        .io_led_6(led[6]),
        .io_led_7(led[7]),
        .io_uart_tx(sda0),
        .io_uart_rx(scl0)
    );


    assign ledn = ~0;
endmodule
