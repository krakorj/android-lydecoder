package com.example.krakora.lydecoder;

import android.provider.Settings;
import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by KRAKORA on 30.01.2018.
 */

public class Decoder {

    public int[] ids;
    public String password;
    public String code;
    private String[] MASQUERADE_CODE = {"N1C", "VOE"};

    public Decoder(String text, String password) {
        // Map attributes
        try {
            this.ids = this.parseIds(text);
        } catch (Exception e) {
            // Log
            Log.d("LyDecoder", e.getMessage());
            // Make masquerade
            this.code = this.codePresenter(this.getMasqueradeCode());
            return;
        }
        this.password = password;
        // TODO: Open decrypted CSV file
        //new FileReader("yourfile.csv")
        // TODO: Encrypt the CSV file => f(password), https://stackoverflow.com/a/22695880
        String data = "id,code\r\n" +
                "1,ABC\r\n" +
                "2,BBC\r\n" +
                "3,CBC\r\n" +
                "4,DBC\r\n" +
                "5,EBC\r\n" +
                "6,FBC\r\n" +
                "7,GBC";
        Reader in = new StringReader(data);
        // Read the CSV content - https://stackoverflow.com/a/43055945
        try {
            // Parse CSV
            CSVParser parser = new CSVParser(in, CSVFormat.RFC4180);
            List<CSVRecord> list = parser.getRecords();
            Log.d("LyDecoder", "CSV data parsed");
            // Get Codes
            String[] codes = this.getCodes(list);
            // Ready to use
            this.code = this.codePresenter(codes);
            Log.d("LyDecoder", "Code: " + this.code);
        }
        catch (Exception e) {
            // Log exception
            Log.d("LyDecoder", e.getMessage());
            // Make masquerade
            this.code = this.codePresenter(this.getMasqueradeCode());
            return;
        }
    }

    public String codePresenter(String[] codes) {
        return codes[0] + "|" + codes[1];
    }

    private int[] parseIds(String text) throws Exception {
        int[] ids = {65000, 65000};
        try {
            String[] x = text.split(",");
            ids[0] = Integer.parseInt(x[0].trim());
            ids[1] = Integer.parseInt(x[1].trim());
        } catch (NumberFormatException e) {
            throw new Exception("Wrong input IDs!");
        }
        return ids;
    }

    private String[] getMasqueradeCode() {
        return MASQUERADE_CODE;
    }

    private String[] getCodes(List<CSVRecord> list) throws Exception {

        String[] code = {"", ""};

        // Assign codes from CSV to ids
        int id = 0;
        for (CSVRecord i : list) {
            // Ignore header
            if (i.get(0).trim().equals("id")) { continue; };
            // Get id from CSV
            try {
                // Get ID
                id = Integer.parseInt(i.get(0));
                // Check code 1
                if(id == this.ids[0]) { code[0] = i.get(1); };
                if(id == this.ids[1]) { code[1] = i.get(1); };
            } catch (Exception e) {
                // Id is not integer - exception
                throw new Exception("CSV IDs invalid!");
            }
        }
        // Validate output
        if ( code[0].equals("") || code[1].equals("")) {
            throw new Exception("IDs invalid!");
        }
        // Return codes
        return code;
    }

    public String decode() {
        return this.code;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
