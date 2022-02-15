package com.example.muolibdemo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private TextView textViewWelcom, textViewFullname, textViewEmail, textViewAdminNo;
    private Toolbar setup_toolbar;
    private Button upload_pic;
    private String fullName, email, admNumber;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        setup_toolbar = findViewById(R.id.setup_toolbar);
        setSupportActionBar(setup_toolbar);
        getSupportActionBar().setTitle("Profile Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        textViewWelcom = findViewById(R.id.textview_show_welcome);
        textViewFullname = findViewById(R.id.textview_show_fullname);
        textViewEmail = findViewById(R.id.textview_show_email);
        textViewAdminNo = findViewById(R.id.textview_show_adm);
        upload_pic = findViewById(R.id.upload_pic);
        progressBar = findViewById(R.id.progressbar10);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){

            Toast.makeText(UserProfile.this, "User's details are not available", Toast.LENGTH_LONG).show();

        }else{
            progressBar.setVisibility(View.VISIBLE);

            showUserProfile(firebaseUser);
        }

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User's");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users  readUserDetails = snapshot.getValue(Users.class);

                if (readUserDetails != null){

                    fullName = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    admNumber = readUserDetails.admnNo;

                    textViewWelcom.setText("Welcome, " + fullName + "!");
                    textViewFullname.setText(fullName);
                    textViewEmail.setText(email);
                    textViewAdminNo.setText(admNumber);

                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UserProfile.this, "Someth'n went wrong!", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
            }
        });

    }
}