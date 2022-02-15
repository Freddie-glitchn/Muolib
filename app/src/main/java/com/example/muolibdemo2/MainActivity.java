package com.example.muolibdemo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar main_toolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currentUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setTitle("Muolib");

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, main_toolbar
                                        , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notes:
                Intent intent = new Intent(MainActivity.this, ViewPdfFiles.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.comSci:
                intent = new Intent(MainActivity.this, UploadNotes.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.prof_settings:
                intent = new Intent(MainActivity.this, UserProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.post:
                intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.logout_menu:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else{

            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){


            currentUid = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Toast.makeText(MainActivity.this, " Welcome " , Toast.LENGTH_LONG).show();

                        }
                    }else{

                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, " FireStore ERROR : " + error, Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.logout_menu:
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//
//            case R.id.comSci:
//
//                Intent intent1 = new Intent(MainActivity.this, UploadNotes.class);
//                startActivity(intent1);
//                finish();
//
//            case R.id.notes:
//
//                Intent intent2 = new Intent(MainActivity.this, ViewPdfFiles.class);
//                startActivity(intent2);
//                finish();
//
////            case R.id.newsimageView1:
////
////                Intent intent3 = new Intent(MainActivity.this, NewsActivity.class);
////                startActivity(intent3);
////                finish();
////
////            case R.id.newsimageView3:
////
////                intent3 = new Intent(MainActivity.this, NewsActivity.class);
////                startActivity(intent3);
////                finish();
////
////            case R.id.newsimageView2:
////
////                intent3 = new Intent(MainActivity.this, NewsActivity.class);
////                startActivity(intent3);
////                finish();
////
////            case R.id.newsimageView4:
////
////                intent3 = new Intent(MainActivity.this, NewsActivity.class);
////                startActivity(intent3);
////                finish();
//
//        }
//
//        return true;
//    }
}