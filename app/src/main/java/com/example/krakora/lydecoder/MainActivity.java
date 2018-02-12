package com.example.krakora.lydecoder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Process text fields
        final EditText codeText = (EditText) findViewById(R.id.editText);
        final EditText passText = (EditText) findViewById(R.id.editPassword);

        // Process Text view
        final TextView outputText = (TextView) findViewById(R.id.outputText);

        // Process toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Process button
        ImageButton button = (ImageButton) findViewById(R.id.processButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check inputs filled
                if (codeText.getText().toString().trim().length() < 3 ||
                        passText.getText().toString().trim().length() < 3) {
                    outputText.setText("I like to move it :)");
                    return;
                }
                    // Hide keyboards
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                // Process file decoding
                String output = ".";
                try {
                    InputStream ins = getAssets().open("data.enc");
                    Decoder d = new Decoder(codeText.getText().toString(), passText.getText().toString(), ins);
                    output = d.decode();
                } catch (Exception e) {
                    // pass
                }
                // Present it
                outputText.setText(output);
                // Reset code & password
                codeText.setText("");
                codeText.requestFocus();
                passText.setText("");
            }
            }
        );

        // Open keyboard immediately
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
