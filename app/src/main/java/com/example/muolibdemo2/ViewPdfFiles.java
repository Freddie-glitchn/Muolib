package com.example.muolibdemo2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPdfFiles extends AppCompatActivity {

    public ListView myPdfListview;
    DatabaseReference databaseReference;
    public ArrayList<UploadPd> uploadPds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf_files);

        myPdfListview = findViewById(R.id.myListView);
        uploadPds = new ArrayList<>();


        viewAllFiles();

        myPdfListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                UploadPd uploadPd = uploadPds.get(position);

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadPd.getUrl()));
                startActivity(intent);
                finish();

            }
        });
    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()){

                    UploadPd uploadPd = postSnapshot.getValue(UploadPd.class);
                    uploadPds.add(uploadPd);


                }

                String[] uploads = new String[uploadPds.size()];

                for (int i = 0; i<uploads.length; i++){

                    uploads[i] = uploadPds.get(i).getName();

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewPdfFiles.this, R.layout.simple_list_item1, uploads){


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view =super.getView(position, convertView, parent);

                        TextView textView = view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }
                };



                myPdfListview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}