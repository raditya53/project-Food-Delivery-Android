package com.example.fooddelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private Context ctx;
    private List<DataHistory> lHistory;



    public HistoryAdapter(Context ctx, List<DataHistory> lHistory) {
        this.ctx = ctx;
        this.lHistory = lHistory;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.history_item, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
//        DataHistory dataHistory = lHistory.get(position);
//        holder.tvNama.setText(dataHistory.getIdHistory().);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{

        public TextView tvNama, tvTotalHarga, tvCheck, tvItem;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
//            tvCheck = itemView.findViewById(R.id.txtCheck);
//            tvNama = itemView.findViewById(R.id.txtNamaHistory);
//            tvItem = itemView.findViewById(R.id.txtItem);
//            tvTotalHarga = itemView.findViewById(R.id.txtTotalHarga);
        }
    }
}
