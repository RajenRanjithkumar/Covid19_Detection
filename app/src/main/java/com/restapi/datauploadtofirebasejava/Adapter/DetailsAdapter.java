package com.restapi.datauploadtofirebasejava.Adapter;

import android.content.Context;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.restapi.datauploadtofirebasejava.Model.Upload;

import com.restapi.datauploadtofirebasejava.R;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private Context context;
    private List<Upload> patientsList; //mUploads
    //private  List<Upload> fullPatientsList;


    private OnItemClickListner mListner;



    public DetailsAdapter(Context context, List<Upload> patientsList) {
        this.context = context;
        this.patientsList = patientsList;
        //this.fullPatientsList = fullPatientsList;
        //fullPatientsList = new ArrayList<>(patientsList);

    }


    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.DetailsViewHolder holder, int position) {

        //Get the Data from Firebase

        Upload uploadCurrent = patientsList.get(position);
        holder.patient_name.setText(uploadCurrent.getName());
        holder.covid_result.setText(uploadCurrent.getCovidResult());

        Glide.with(context)
                .load(uploadCurrent.getImageUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.patient_imageView);
        //Toast.makeText(context, "list1:"+patientsList.size()+"\nlist2:"+fullPatientsList.size(), Toast.LENGTH_SHORT).show();


        /*Intent to PatientDetailsActivity
        holder.single_row_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context.getApplicationContext(), "heool",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PatientDetailsActivity.class);
                intent.putExtra("patientDetails", uploadCurrent);
                context.startActivity(intent);


            }
        });*/

    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }


    public void filteredList(ArrayList<Upload> filterList) {

        patientsList = filterList;
        notifyDataSetChanged();

    }


    public class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView patient_imageView;
        TextView patient_name, covid_result;
        CardView single_row_item;


        public DetailsViewHolder(@NonNull  View itemView) {
            super(itemView);

            single_row_item = itemView.findViewById(R.id.single_row);
            patient_imageView = itemView.findViewById(R.id.patient_imageView);
            patient_name = itemView.findViewById(R.id.patient_name);
            covid_result = itemView.findViewById(R.id.patient_result);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mListner != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    mListner.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Delete Patient?");
            MenuItem doWhatever = menu.add(Menu.NONE,1,1,"Yes");
            MenuItem delete = menu.add(Menu.NONE,2,2,"No");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (mListner != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    switch (item.getItemId()){
                        case 1:
                            mListner.onWhatEverClick(position);
                            return true;

                        case 2:
                            mListner.onDeleclick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListner{
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleclick(int position);

    }

    public void OnItemClickListner(OnItemClickListner listner)
    {
        mListner = listner;
    }


}
