package net.ecuatecnologia.golosovaniye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity  extends AppCompatActivity {
    private EditText etUser;
    private EditText etPassword;
    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        etUser = (EditText) findViewById(R.id.user);
        etPassword = (EditText) findViewById(R.id.password);
        regButton = (Button) findViewById(R.id.regButton);


    }
}
