package com.hashtech.tenx.fooddistribution;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    EditText et_username,et_password;
    Button btnLogin;
    TextView tvCreate_acc;
    FirebaseFirestore db;
    CollectionReference userColRef;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvCreate_acc = findViewById(R.id.tv_create_acc);
        db = FirebaseFirestore.getInstance();
        userColRef = db.collection("users");
        mAuth = FirebaseAuth.getInstance();

        tvCreate_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSignUp(LogInActivity.this);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(view);
            }
        });


    }
    public void dialogSignUp(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View  v = LayoutInflater.from(context).inflate(R.layout.activity_sign_up, null, false);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText etUsername, etPassword, etEmail;
        Button btnSignUp;
        etUsername = v.findViewById(R.id.et_username);
        etPassword = v.findViewById(R.id.et_password);
        etEmail = v.findViewById(R.id.et_email);
        btnSignUp = v.findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etUsername.getText().toString();
                String pass = etPassword.getText().toString();
                final String email = etEmail.getText().toString();
                if(!email.equals("") && !name.equals("") && !pass.equals("")){

                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Map<String, String> data = new HashMap<>();
                                data.put("username", name);
                                data.put("email", email);

                                db.collection("users").document(email).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(context, "New User Created!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error, Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });


    }

    public void logIn(View view){
        final String email = et_username.getText().toString();
        String password = et_password.getText().toString();
           mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   goNextActivity();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(LogInActivity.this, "Error, Try again", Toast.LENGTH_SHORT).show();
               }
           });





    }

    public void goNextActivity(){
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
    }
}
