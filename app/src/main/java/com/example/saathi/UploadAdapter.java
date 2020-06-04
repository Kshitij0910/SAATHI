package com.example.saathi;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.UploadViewHolder> {
    private Context mContext;
    private List<Upload> mUpload;

    private OnItemClickListener mListener;


    public UploadAdapter(Context context, List<Upload> uploads){
        mContext=context;
        mUpload=uploads;

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
        //Prescription prescriptionCurrent=mPrescription.get(position);
        holder.textViewPrescription.setText(uploadCurrent.getPrescription());
        Picasso.get().load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.pill_time).fit().centerCrop().into(holder.imgUpload);

        //boolean isExpanded=uploadCurrent.isExpanded();
        //holder.textViewPrescription.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class UploadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewPrescription;
        public ImageView imgUpload;
        //public Button expandBtn;

        public UploadViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPrescription=itemView.findViewById(R.id.view_prescription);
            imgUpload=itemView.findViewById(R.id.image_view_upload);
            //expandBtn=itemView.findViewById(R.id.expandable_btn);

            /*expandBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Upload upload=mUpload.get(getAdapterPosition());
                    upload.setExpanded(!upload.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    //expandBtn.setText("HIDE PRESCRIPTION");
                }
            });*/


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("SELECT ACTION");
            MenuItem delete=menu.add(Menu.NONE, 1, 1, "Delete");


            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                   switch (item.getItemId()){
                       case 1:
                           mListener.onDeleteClick(position);
                           return true;
                   }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);


        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}
