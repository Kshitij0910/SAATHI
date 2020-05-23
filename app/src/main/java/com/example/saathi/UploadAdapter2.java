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

   /* public void update(String name, String url){
        reportItems.add(name);
        urls.add(url);
        notifyDataSetChanged();
    }*/

    /*public UploadAdapter2(RecyclerView recyclerView, Context Context, ArrayList<String> reportItems, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.reportItems=reportItems;
        this.urls=urls;

    }*/



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
            bG=itemView.findViewById(R.id.report_background);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=recyclerView.getChildLayoutPosition(v);
                    Intent pdfIntent=new Intent();
                    pdfIntent.setType(Intent.ACTION_VIEW);
                    pdfIntent.setData(Uri.parse(urls.get(position)));
                    mContext.startActivity(pdfIntent);
                }
            });*/

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
            MenuItem doWhatever=menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete=menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListenerRep!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListenerRep.onWhateverClick(position);
                            return true;

                        case 2:
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
        void onWhateverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(UploadAdapter2.OnItemClickListener listenerRep){
        mListenerRep=listenerRep;
    }
}
