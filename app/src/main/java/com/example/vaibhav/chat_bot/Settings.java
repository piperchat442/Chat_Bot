package com.example.vaibhav.chat_bot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Settings extends AppCompatActivity {
public EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText=(EditText)findViewById(R.id.ip);
        String url= editText.getText().toString();
        url url1 = new url();
        url1.Send_Message_Url=url;
    }
}
