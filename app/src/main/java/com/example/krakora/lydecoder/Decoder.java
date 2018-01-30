package com.example.krakora.lydecoder;

/**
 * Created by KRAKORA on 30.01.2018.
 */

public class Decoder {

    public Decoder(String password) {
        //
    }

    public String decode(String input) {
        String[] x = input.split(",");
        int[] code = {0, 0};
        try {
            code[0] = Integer.parseInt(x[0]);
            code[1] = Integer.parseInt(x[1]);
        } catch (NumberFormatException e) {
            // pass
        }
        String output = String.valueOf(code[0]) + "|" + String.valueOf(code[1]);
        return output;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
