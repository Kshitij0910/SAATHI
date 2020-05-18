package com.example.saathi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.UploadViewHolder> {
    private Context mContext;
    private List<Upload> mUpload;
    private List<Prescription> mPrescription;

    public UploadAdapter(Context context, List<Upload> uploads, List<Prescription> prescriptions){
        mContext=context;
        mUpload=uploads;
        mPrescription=prescriptions;
    }
    @NonNull
    @Override
    public UploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.medicines_item, parent, false);
        return new UploadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadViewHolder holder, int position) {
        Upload uploadCurrent=mUpload.get(position);
        Prescription prescriptionCurrent=mPrescription.get(position);
        holder.textViewPrescription.setText(prescriptionCurrent.getPrescriptionTxt());
        Picasso.get().load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.pill_time).fit().centerCrop().into(holder.imgUpload);
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class UploadViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewPrescription;
        public ImageView imgUpload;

        public UploadViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPrescription=itemView.findViewById(R.id.view_prescription);
            imgUpload=itemView.findViewById(R.id.image_view_upload);

        }
    }
}
