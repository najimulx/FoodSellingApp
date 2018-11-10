package com.hashtech.tenx.fooddistribution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {
    EditText usernameEditText,passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



    }


    public void switchToSignUp(View view){
        Intent intentToSignUp = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intentToSignUp);
    }

    public void logIn(View view){


    }

    public void forgotPassword(View view){


    }
}
