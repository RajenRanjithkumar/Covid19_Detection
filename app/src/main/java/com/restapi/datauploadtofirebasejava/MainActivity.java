package com.restapi.datauploadtofirebasejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.restapi.datauploadtofirebasejava.Model.Upload;

import com.restapi.datauploadtofirebasejava.ml.CovidXrayUpdated;
import com.restapi.datauploadtofirebasejava.ml.OptimizedCovidModel;
import com.restapi.datauploadtofirebasejava.ml.XrayOrNotModel2;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextPatirntName;
    private EditText mEditTextPatirntPhoneNo;
    private ImageView mImageView;
    private TextView progressPercent;
    private ProgressBar mProgressBar;
    private Uri mImageUri, firebaseImageUrl, beforeCropUri;
    private Bitmap xrayImageBitmap, xrayImageBitmapResized;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private RelativeLayout rootLayout;
    private Upload userDetails;
    //private final String API_KEY = "NzE2YTZmNGE1MzZhNjczMzUwNDYzODUzNTEzNzRlMzU=";


    BottomNavigationView bottomNavigationView;

    // SMS api key
    //NzE2YTZmNGE1MzZhNjczMzUwNDYzODUzNTEzNzRlMzU=
    //Next part7
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar toolbar = findViewById(R.id.add_activity_toolbar);
        //setSupportActionBar(toolbar);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);

        mEditTextPatirntName = findViewById(R.id.edit_text_patient_name);
        mEditTextPatirntPhoneNo = findViewById(R.id.edit_text_phone_no);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        progressPercent = findViewById(R.id.progress_barPecentage);
        rootLayout = findViewById(R.id.rootLayoutMainActivity);

        //bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_bar_add);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS}, 100);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_bar_list:
                        Intent intent = new Intent(getApplicationContext(),PatientsListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_bar_add:
                        return true;

                    case R.id.nav_bar_info:
                        Intent intent1 = new Intent(getApplicationContext(),AboutActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFileChooser();
                CropImage.startPickImageActivity(MainActivity.this);
            }
        });


        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternet();

                //validation();
            }
        });

        //set color for progress bar
        mProgressBar.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void checkInternet() {
        //check internet connection
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (activeNetwork==null)
        {
            Snackbar.make(rootLayout,"No Internet", Snackbar.LENGTH_LONG)
                    .setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Perform any action
                            checkInternet();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.snackBar))
                    .show();
        }else
            {
                validation();
            }

    }

    private void validation() {

        if(mEditTextPatirntName.getText().toString().equals(""))
        {
            mEditTextPatirntName.setError("Enter Patient's name");
        }if (mEditTextPatirntPhoneNo.getText().toString().equals("")){
            mEditTextPatirntPhoneNo.setError("Enter Patient's phone number");
        } else if (mEditTextPatirntPhoneNo.getText().length() != 10)
        {
            mEditTextPatirntPhoneNo.setError("Enter a 10 digit phone number");
        } else if (mUploadTask != null && mUploadTask.isInProgress())
        {
            Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            if(mImageUri != null)
            {
                //uploadFile();
                checkXrayOrNot(xrayImageBitmap);
            }else {

                Snackbar.make(rootLayout,"Please select an X-ray image", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Open image picker
                                CropImage.startPickImageActivity(MainActivity.this);

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.snackBar))
                        .show();

                //Toast.makeText(getApplicationContext(),"Please select an X-ray Image",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            beforeCropUri = CropImage.getPickImageResultUri(this,data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this,beforeCropUri))
            {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
            }else {
                startCrop(beforeCropUri);
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                mImageUri = result.getUri();
                try {
                    // convert the Image to bitmap
                    xrayImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),mImageUri);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Bitmap Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Glide.with(getApplicationContext())
                        .load(mImageUri)
                        .into(mImageView);

            }
        }
        /*
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            //mImageUri = data.getData();

            startCrop(mImageUri);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK)
                {
                    mImageUri = result.getUri();

                    //imageView.setImageURI(xrayImageUri);
                    Glide.with(getApplicationContext())
                            .load(mImageUri)
                            .into(mImageView);

                }
            }

            //Glide.with(getApplicationContext())
              //      .load(mImageUri)
                //    .into(mImageView);

        }*/
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                //.setAspectRatio(224,224)
                .start(this);
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(String covidResult) {
        if (mImageUri != null) {

            //Set the visibility for Progress bar and percentage textview
            mProgressBar.setVisibility(View.VISIBLE);
            progressPercent.setVisibility(View.VISIBLE);

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                   + "." + getFileExtension(beforeCropUri));

            String currentDate = getcurrentDate();
            //StorageReference fileReference = mStorageRef.child(mImageUri.toString());

                fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            
                            //to add delay for progress bar
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(MainActivity.this, "Uploaded successful on "+currentDate, Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    firebaseImageUrl = uri;
                                    Upload upload = new Upload(
                                            mEditTextPatirntName.getText().toString().trim(),
                                            firebaseImageUrl.toString(),
                                            mEditTextPatirntPhoneNo.getText().toString().trim(),
                                            covidResult,
                                            currentDate);
                                    
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    progressPercent.setVisibility(View.INVISIBLE);

                                    userDetails = upload;

                                    //sendSMS(upload);

                                    //intent to details activity
                                    upload.setImageUrl(mImageUri.toString());
                                    Intent intent = new Intent(getApplicationContext(),PatientDetailsActivity.class);
                                    intent.putExtra("patientDetails", upload);
                                    startActivity(intent);

                                }
                            });


                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int)(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressPercent.setText(String.valueOf(progress)+"%");
                            mProgressBar.setProgress((int) progress);
                        }
                    });



        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(Upload upload) {

        //check permission
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(upload.getPhoneNo(),null, "You have tested "+upload.getCovidResult()+" for Covid", null, null);
            Toast.makeText(this, "SMS send successfully", Toast.LENGTH_SHORT).show();


        }else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS}, 100);
        }

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

            validation();


        }else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            validation();
        }
    }*/

    private String getcurrentDate() {

        Calendar calendar = Calendar.getInstance();
        return DateFormat.getDateInstance().format(calendar.getTime());
    }

    private void checkXrayOrNot(Bitmap xrayImageBitmap){

            xrayImageBitmapResized = Bitmap.createScaledBitmap(xrayImageBitmap,224,224,true);

        try {
            XrayOrNotModel2 model = XrayOrNotModel2.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

            TensorImage tensorImage =new TensorImage(DataType.UINT8);
            tensorImage.load(xrayImageBitmapResized);

            ByteBuffer byteBuffer =tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            XrayOrNotModel2.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // Releases model resources if no longer used.
            model.close();

            int[] OutputArray = new int[]{outputFeature0.getIntValue(0)/255 ,outputFeature0.getIntValue(1)/255};
            //String finalOtput = Arrays.toString(new int[]{outputFeature0.getIntValue(0)});

            if (OutputArray[0] == 1)
            {
                //Toast.makeText(getApplicationContext(),"its an xray", Toast.LENGTH_SHORT).show();
                //uploadFile();
                optimizedCovidModel(xrayImageBitmapResized);


            }else if(OutputArray[1] == 1)
            {
                Toast.makeText(getApplicationContext(),"Selected Image is not an Xray", Toast.LENGTH_SHORT).show();
            }

            //int[] finalOp = outputFeature0.getIntArray();

            //Toast.makeText(getApplicationContext(),"Model output"+ Arrays.toString(finalOp), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Xray or not Model error:"+e.getMessage() , Toast.LENGTH_SHORT).show();
        }


    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void covidModel(Bitmap xrayImageBitmap)
    {

        try {
            CovidXrayUpdated model = CovidXrayUpdated.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            TensorImage tensorImage =new TensorImage(DataType.FLOAT32);
            tensorImage.load(xrayImageBitmap);

            ByteBuffer byteBuffer = tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            CovidXrayUpdated.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // Releases model resources if no longer used.
            model.close();

            int modelPrediction = (int)outputFeature0.getFloatArray()[0];

            if (modelPrediction ==1)
            {
                uploadFile("Negative");
            }else if (modelPrediction == 0)
            {
                uploadFile("Positive");
            }

            Toast.makeText(this, ""+modelPrediction, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext()," Model error:"+e.getMessage() , Toast.LENGTH_SHORT).show();
        }



    }

    private void optimizedCovidModel(Bitmap xrayImageBitmap) {
        try {
            OptimizedCovidModel model = OptimizedCovidModel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            TensorImage tensorImage =new TensorImage(DataType.UINT8);
            tensorImage.load(xrayImageBitmapResized);

            ByteBuffer byteBuffer =tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            OptimizedCovidModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // Releases model resources if no longer used.
            model.close();

            //0 -> positive
            //1 -> negative
            int[] predictionArray = new int[]{outputFeature0.getIntValue(0)/255 ,outputFeature0.getIntValue(1)/255};

            if (predictionArray[0] == 1)
            {
                uploadFile("Positive");
                //Toast.makeText(getApplicationContext(),"Positive", Toast.LENGTH_SHORT).show();
                //uploadFile();
                //covidModel(xrayImageBitmapResized);

            }else if(predictionArray[1] == 1)
            {
                uploadFile("Negative");
                //Toast.makeText(getApplicationContext(),"Negative", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Covid Model error:"+e.getMessage() , Toast.LENGTH_SHORT).show();
        }

    }




}