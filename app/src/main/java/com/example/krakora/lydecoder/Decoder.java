package com.example.krakora.lydecoder;

import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

/**
 * Created by KRAKORA on 30.01.2018.
 */

public class Decoder {

    public int[] ids;
    public String password;
    public String code;
    private String[] MASQUERADE_CODE = {"N1C", "1MT"};

    public Decoder(String text, String password, InputStream file) {
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

        // Open encrypted CSV file
        String data = "";
        try {
            // Open file
            Log.d("LyDecoder", "Opening the decripted file ...");
            StringWriter writer = new StringWriter();
            IOUtils.copy(file, writer, StandardCharsets.UTF_8);
            String input = writer.toString();
            // Decrypt
            // https://stackoverflow.com/questions/44497058/issue-with-key-and-iv-on-aes-256-cbc
            Log.d("LyDecoder", "Decrypting the file ...");
            data = Aes.decrypt(input, this.password);
            // TODO: Check if the AES output is in CSV format
            // if not valid format throw an exception
        } catch (Exception e) {
            Log.d("LyDecoder", e.getMessage());
            // Make masquerade
            this.code = this.codePresenter(this.getMasqueradeCode());
            return;
        }
        // Read the CSV content - https://stackoverflow.com/a/43055945
        Reader in = new StringReader(data);
        try {
            // Parse CSV
            Log.d("LyDecoder", "CSV file processing ...");
            CSVParser parser = new CSVParser(in, CSVFormat.RFC4180.withDelimiter(';').withRecordSeparator('\n'));
            List<CSVRecord> list = parser.getRecords();
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
        return codes[0] + "" + codes[1];
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
        String[] code = {"",""};
        code[0] = MASQUERADE_CODE[0];
        code[1] = this.getRandomString();
        return code;
    }

    private String getRandomString() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( 3 );
        for( int i = 0; i < 3; i++ )
            sb.append( CHARS.charAt( rnd.nextInt(CHARS.length()) ) );
        return sb.toString();
    }

    private String[] getCodes(List<CSVRecord> list) throws Exception {

        String[] code = {"", ""};

        // Assign codes from CSV to ids
        int id = 0;
        for (CSVRecord i : list) {
            // Get id from CSV
            try {
                // Get ID
                id = Integer.parseInt(i.get(0));
                // Check code values
                if(id == this.ids[0]) { code[0] = i.get(1); };
                if(id == this.ids[1]) { code[1] = i.get(1); };
                // Check codes filled
                if(!code[0].equals("") && !code[1].equals("")) {
                    break;
                }
            } catch (Exception e) {
                // Ignore:
                //  Row is header
                //  Id is not integer
                ;
            }
        }
        // Validate output
        if ( code[0].equals("") || code[1].equals("")) {
            throw new Exception("Invalid input IDs or invalid CSV!");
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
