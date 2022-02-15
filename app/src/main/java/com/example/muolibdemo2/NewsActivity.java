package com.example.muolibdemo2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class NewsActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 200;
    private Toolbar newsToolbar;
    private ImageView newsimageView;
    private EditText newPost_description;
    private Button post_btn;
    private Uri postimageUri;
    private ProgressBar newPostprogressBar;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_uid = FirebaseAuth.getInstance().getUid();

        newsimageView = findViewById(R.id.newsimageView);
        newPost_description = findViewById(R.id.newPost_description);
        post_btn = findViewById(R.id.post_btn);
        newPostprogressBar = findViewById(R.id.progressbarnew);

        newsToolbar = findViewById(R.id.news_toolbar);
        setSupportActionBar(newsToolbar);
        getSupportActionBar().setTitle("News Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newsimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1,1)
                        .start(NewsActivity.this);

            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = newPost_description.getText().toString();

                if (!TextUtils.isEmpty(description) && postimageUri != null){

                    String randName = random();
                    StorageReference filePath = storageReference.child("post_image").child(randName + "jpg");
                    filePath.putFile(postimageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()){


                                String downloadUri = task.getResult().toString();

                                Map<String, Object> postMap = new HashMap<>();
                                postMap.put("image_url", downloadUri);
                                postMap.put("description", description);
                                postMap.put("uid", current_uid);
                                postMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(NewsActivity.this, " Status update succesful " , Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }else{

                                            String error = task.getException().getMessage();
                                            Toast.makeText(NewsActivity.this, " FireStore ERROR : " + error, Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });

                            }else {

                                String error = task.getException().getMessage();
                                Toast.makeText(NewsActivity.this, " FireStore ERROR : " + error, Toast.LENGTH_LONG).show();
                                newPostprogressBar.setVisibility(View.INVISIBLE);

                            }

                        }
                    });

                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postimageUri = result.getUri();
                newsimageView.setImageURI(postimageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public static String random(){

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int randomLen = random.nextInt(MAX_LENGTH);
        char tempChar;

        for(int i = 0; i < randomLen; i++){

            tempChar = (char) ((char)random.nextInt(96) + 32);
            stringBuilder.append(tempChar);

        }
        return stringBuilder.toString();
  }
}