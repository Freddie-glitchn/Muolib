package com.example.muolibdemo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button createAccount;
    TextView login;
    EditText username, emailAdd, admNo, Muserpass, conPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressBar =findViewById(R.id.progressbar);
        login = findViewById(R.id.textlogin);
        createAccount = findViewById(R.id.createAccount);
        username = findViewById(R.id.fullName);
        emailAdd = findViewById(R.id.emailAdd);
        admNo = findViewById(R.id.admNo);
        Muserpass = findViewById(R.id.user_pass);
        conPassword = findViewById(R.id.confirm_Password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterUser();
            }
        });
    }

    private void RegisterUser() {

        String email = emailAdd.getText().toString().trim();
        String password = Muserpass.getText().toString().trim();
        String name = username.getText().toString().trim();
        String admnNo = admNo.getText().toString().trim();
        String conPass = conPassword.getText().toString().trim();

        if (email.isEmpty()){
            emailAdd.setError("Please Enter Your Email Address");
            emailAdd.requestFocus();
            return;
        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            emailAdd.setError("Please a valid Email Address");
//            emailAdd.requestFocus();
//            return;
//        }
        if (name.isEmpty()){
            username.setError("Please Enter Your Name");
            username.requestFocus();
            return;
        }
        if (admnNo.isEmpty()){
            admNo.setError("Please Enter Your Admission Number");
            admNo.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Muserpass.setError("Please enter your password");
            Muserpass.requestFocus();
            return;
        }
        if (password.length() < 6){
            Muserpass.setError("Your password is not strong");
            Muserpass.requestFocus();
            return;
        }
        if (!password.equals(conPass)){
            conPassword.setError("Password don't match");
            conPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Users users = new Users(name, email, admnNo, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Register successful!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();


                                    }else {
                                        Toast.makeText(getApplicationContext(), "Registration Failed! Try Again", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getApplicationContext(), "Registration Failed! Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}