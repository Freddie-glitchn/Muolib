package com.example.muolibdemo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText email_add;
    private Button resetPassword;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_add = findViewById(R.id.emailAddReset);
        resetPassword = findViewById(R.id.reset_password);
        progressBar = findViewById(R.id.progressbar2);
        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPassword();
            }
        });
    }

    private void ResetPassword() {
        String email = String.valueOf(email_add).trim();

        if (email.isEmpty()){
            email_add.setError("Email address missing");
            email_add.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_add.setError("Insert a valid email address");
            email_add.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Check your email to reset password", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Retry! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}