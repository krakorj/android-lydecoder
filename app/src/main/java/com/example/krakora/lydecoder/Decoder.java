package com.example.krakora.lydecoder;

/**
 * Created by KRAKORA on 30.01.2018.
 */

public class Decoder {

    public String code;
    public String password;

    public Decoder(String code, String password) {
        this.code = code;
        this.password = password;
        // TODO: Open decrypted CSV file, https://www.callicoder.com/java-read-write-csv-file-apache-commons-csv/
        // TODO: Encrypt the the CSV file => f(password), https://stackoverflow.com/a/22695880 
        // TODO: Read the CSV content
        // TODO: Provide data to user
    }

    public String decode() {
        String[] x = this.code.split(",");
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
