package com.hashtech.tenx.fooddistribution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditText,emailEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);






    }

    public void signUp(View view){
        switchToLogInActivity();

    }
    public void switchToLogInActivity(){
        Intent intentToMainActivity = new Intent(getApplicationContext(),LogInActivity.class);
        startActivity(intentToMainActivity);
    }

}
