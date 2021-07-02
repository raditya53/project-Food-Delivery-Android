package com.example.fooddelivery;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailHolder> {
    private Context ctx;
    private List<DataHistory> dataHistoryList;

    public HistoryDetailAdapter(Context ctx, List<DataHistory> lHistory) {
        this.ctx = ctx;
        this.dataHistoryList = lHistory;
    }


    @NonNull
    @Override
    public HistoryDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.detailhistory_item,parent, false);
        return new HistoryDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HistoryDetailHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView namamakanan,jumlahbarang, totalharga;
        public HistoryDetailHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgHistorydetail);
            namamakanan = itemView.findViewById(R.id.detailnama_history);
            jumlahbarang = itemView.findViewById(R.id.detailjumlah_history);
            totalharga = itemView.findViewById(R.id.detailharga_history);
        }
    }
}
