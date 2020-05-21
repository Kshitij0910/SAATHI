package com.example.saathi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UploadAdapter2 extends RecyclerView.Adapter<UploadAdapter2.UploadViewHolder2> {

     RecyclerView recyclerView;
     Context mContext;
    ArrayList<String> reportItems;
    ArrayList<String> urls;

    public void update(String name, String url){
        reportItems.add(name);
        urls.add(url);
        notifyDataSetChanged();
    }

    public UploadAdapter2(RecyclerView recyclerView, Context Context, ArrayList<String> reportItems, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.reportItems=reportItems;
        this.urls=urls;

    }

    @NonNull
    @Override
    public UploadViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.reports_items, parent, false);
        return new UploadViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadViewHolder2 holder, int position) {

        holder.textViewReportFile.setText(reportItems.get(position));


    }



    @Override
    public int getItemCount() {
        return reportItems.size();
    }

    public class UploadViewHolder2 extends RecyclerView.ViewHolder {
        public TextView textViewReportFile;

        public UploadViewHolder2(@NonNull View itemView) {
            super(itemView);
            textViewReportFile = itemView.findViewById(R.id.download_filename);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=recyclerView.getChildLayoutPosition(v);
                    Intent pdfIntent=new Intent();
                    pdfIntent.setType(Intent.ACTION_VIEW);
                    pdfIntent.setData(Uri.parse(urls.get(position)));
                    mContext.startActivity(pdfIntent);
                }
            });
        }

    }
}
