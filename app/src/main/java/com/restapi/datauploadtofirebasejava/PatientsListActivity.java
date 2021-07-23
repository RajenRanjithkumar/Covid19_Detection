package com.restapi.datauploadtofirebasejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.restapi.datauploadtofirebasejava.Adapter.DetailsAdapter;
import com.restapi.datauploadtofirebasejava.Model.Upload;

import java.util.ArrayList;
import java.util.List;

public class PatientsListActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListner{

    private RecyclerView recyclerView; //mrecyclerView
    private DetailsAdapter detailsAdapter; //mAdapter

    private FirebaseStorage mStorage;
    private DatabaseReference databaseReference;
    private List<Upload> patientsList; //mUploads
    private EditText searchBar;

    private ProgressBar progressBar;

    private TextView emptyRecord;

    private TextView noRecord;
    private SwipeRefreshLayout swipeRefreshLayout;

    RelativeLayout rootLayout;
    BottomNavigationView bottomNavigationView;

    private ValueEventListener mDBListner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar toolbar = findViewById(R.id.toolbar_patients_list);
        //setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progress_barCircle);
        emptyRecord = findViewById(R.id.emptyRecord);
        noRecord = findViewById(R.id.noRecord);
        rootLayout = findViewById(R.id.rootLayout);
        searchBar = findViewById(R.id.editTextSearch);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        patientsList = new ArrayList<>();



        //bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_bar_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_bar_list:
                        return true;

                    case R.id.nav_bar_add:
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
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

        checkInternet();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInternet();
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        //search bar function
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });


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
                            //fetchDatafromDB();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.snackBar))
                    .show();
        }

        fetchDatafromDB();



    }

    private void filter(String query) {

        ArrayList<Upload> filterList = new ArrayList<>();
        for(Upload item: patientsList){
            if (item.getName().toLowerCase().contains(query.toLowerCase()))
            {
                filterList.add(item);
            }
        }
        detailsAdapter.filteredList(filterList);
        if (filterList.size() !=0){
            noRecord.setVisibility(View.INVISIBLE);
        }else {
            noRecord.setVisibility(View.VISIBLE);
        }



    }

    private void layoutAnimation(RecyclerView recyclerView){
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void fetchDatafromDB() {

        mStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListner = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                patientsList.clear();

                for(DataSnapshot individualPatientDetails: snapshot.getChildren())
                {
                    Upload upload = individualPatientDetails.getValue(Upload.class);

                    upload.setKey(individualPatientDetails.getKey());

                    patientsList.add(upload);


                }
                //patientsListCopy = new ArrayList<>(patientsList);
                //call the Adapter
                detailsAdapter = new DetailsAdapter(PatientsListActivity.this,patientsList);
                recyclerView.setAdapter(detailsAdapter);
                layoutAnimation(recyclerView);

                //for click events
                detailsAdapter.OnItemClickListner(PatientsListActivity.this);


                if(patientsList.isEmpty()){
                    emptyRecord.setVisibility(View.VISIBLE);

                }
                //Toast.makeText(PatientsListActivity.this, ""+patientsListCopy.size(), Toast.LENGTH_SHORT).show();
                detailsAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to fetch data: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);




            }
        });

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                detailsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/

    @Override
    public void onItemClick(int position) {

        Upload upload = patientsList.get(position);
        Intent intent = new Intent(getApplicationContext(),PatientDetailsActivity.class);
        intent.putExtra("patientDetails", upload);
        startActivity(intent);



        //Toast.makeText(getApplicationContext(),"normal click" + upload.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        //Toast.makeText(getApplicationContext(),"onWhatEverClick click" + position, Toast.LENGTH_SHORT).show();
        //to get id of a patient
        Upload selectedItem = patientsList.get(position);
        String userId = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                databaseReference.child(userId).removeValue();
                Toast.makeText(PatientsListActivity.this, "Patient deleted", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientsListActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleclick(int position) {
        //Toast.makeText(getApplicationContext(),"onDeleclick click" + position, Toast.LENGTH_SHORT).show();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDBListner);
    }
}