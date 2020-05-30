package com.example.saathi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UploadAdapter2 extends RecyclerView.Adapter<UploadAdapter2.UploadViewHolder2> {

     //RecyclerView recyclerView;
    private Context mContext;
    //ArrayList<String> reportItems;
    //ArrayList<String> urls;
    private List<UploadReport> mReport;
    private OnItemClickListener mListenerRep;



    public UploadAdapter2(Context context, List<UploadReport> uploadReports) {
        mContext = context;
        mReport = uploadReports;
    }




    @NonNull
    @Override
    public UploadViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.reports_items, parent, false);
        return new UploadViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadViewHolder2 holder, int position) {
        UploadReport uploadRepCurrent=mReport.get(position);
        holder.textViewReportFile.setText(uploadRepCurrent.getFileName());



    }



    @Override
    public int getItemCount() {
        return mReport.size();
    }

    public class UploadViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewReportFile;
        public ImageView bG;


        public UploadViewHolder2(@NonNull View itemView) {
            super(itemView);
            textViewReportFile = itemView.findViewById(R.id.download_filename);



            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListenerRep!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListenerRep.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("SELECT ACTION");
            //MenuItem notify=menu.add(Menu.NONE, 1, 1, "Set Reminder");
            MenuItem delete=menu.add(Menu.NONE, 1, 1, "Delete");

            //notify.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListenerRep!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){

                        case 1:
                            mListenerRep.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        //void onNotifyClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(UploadAdapter2.OnItemClickListener listenerRep){
        mListenerRep=listenerRep;
    }
}
