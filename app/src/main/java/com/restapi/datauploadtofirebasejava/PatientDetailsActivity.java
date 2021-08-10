package com.restapi.datauploadtofirebasejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.restapi.datauploadtofirebasejava.Model.Upload;

public class PatientDetailsActivity extends AppCompatActivity {

    private ImageView XrayImage;
    private TextView patientName;
    private TextView patientPhone;
    private TextView modelsResult;
    private TextView testDate;
    private Button fullImageBt,smsBt;
    private Upload upload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.toolbar_patients_details), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);*/


        //Toolbar toolbar = findViewById(R.id.toolbar_patients_details);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        XrayImage = findViewById(R.id.detailsImageview);
        patientName = findViewById(R.id.detailsName);
        patientPhone = findViewById(R.id.detailsNo);
        modelsResult = findViewById(R.id.modelResult);
        testDate = findViewById(R.id.testDate);
        fullImageBt = findViewById(R.id.fullImagebt);
        smsBt = findViewById(R.id.sendSmsBt);

        getDataFromIntent();

        fullImageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageViewActivity.class);
                intent.putExtra("patientDetails", upload);
                //shared Animation for imageView
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(PatientDetailsActivity.this,XrayImage, ViewCompat.getTransitionName(XrayImage));
                startActivity(intent, optionsCompat.toBundle());

            }
        });

        smsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(upload);
            }
        });

    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra("patientDetails"))
        {
            upload = getIntent().getParcelableExtra("patientDetails");

            Glide.with(getApplicationContext())
                    .load(upload.getImageUrl())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(XrayImage);

            patientName.setText(upload.getName());
            patientPhone.setText(upload.getPhoneNo());
            modelsResult.setText(upload.getCovidResult());
            testDate.setText("tested on "+upload.getDate());


        }

    }

    private void sendSMS(Upload upload) {

        try {
            //check permission
            if(ContextCompat.checkSelfPermission(PatientDetailsActivity.this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED){

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(upload.getPhoneNo(),null, "Hello "+upload.getName()+","+"\nYou have tested "+upload.getCovidResult().toUpperCase()+" for Covid", null, null);
                Toast.makeText(this, "SMS send successfully", Toast.LENGTH_SHORT).show();


            }else {

                ActivityCompat.requestPermissions(PatientDetailsActivity.this,new String[]{Manifest.permission.SEND_SMS}, 100);
            }
        }  catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}