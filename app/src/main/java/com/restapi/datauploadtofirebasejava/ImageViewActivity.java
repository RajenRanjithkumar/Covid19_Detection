package com.restapi.datauploadtofirebasejava;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import com.restapi.datauploadtofirebasejava.Model.Upload;
import com.restapi.datauploadtofirebasejava.R;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView fullImageView;
    private Upload upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        /*Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.toolbar_patients_details), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);*/

        fullImageView = findViewById(R.id.fullImageView);

        getDataFromIntent();

    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra("patientDetails"))
        {
            upload = getIntent().getParcelableExtra("patientDetails");

            Glide.with(getApplicationContext())
                    .load(upload.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(fullImageView);







        }

    }
}