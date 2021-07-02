package com.example.fooddelivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private Context ctx;
    private List<DataHistory> listHistory;
    private List<DataMenu> listDataMenu;

    public HistoryAdapter(Context ctx, List<DataHistory> lHistory, List<DataMenu> dataMenu) {
        this.ctx = ctx;
        this.listHistory = lHistory;
        this.listDataMenu = dataMenu;
    }



    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.history_item, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        Log.i("pos", String.valueOf(position));
        DataHistory dataHistory = listHistory.get(position);
        DataMenu dataMenu = listDataMenu.get(position);

        String tanggal = dataHistory.getTanggalPembelian();
        String[] parts = tanggal.split(" ");

        String jumlahBarang = String.valueOf(dataHistory.getIdCart().size());
        jumlahBarang = jumlahBarang + " Total Items";

        if (dataHistory.getDeliverStatus().equals("Shipping")) {
            Picasso.with(ctx).load(R.drawable.shipping).into(holder.status);
        } else if (dataHistory.getDeliverStatus().equals("Delivered")) {
            Picasso.with(ctx).load(R.drawable.succees).into(holder.status);
        } else if (dataHistory.getDeliverStatus().equals("Failed")) {
            Picasso.with(ctx).load(R.drawable.failed).into(holder.status);
        }

        holder.tvStatus.setText(dataHistory.getDeliverStatus());
        holder.tvTanggal.setText(parts[0]);
        holder.tvId.setText(String.valueOf(dataHistory.getIdTransaksi()));
        holder.tvHarga.setText(dataHistory.getTotalHarga());
        holder.tvJumlah.setText(jumlahBarang);

        holder.tvNama.setText(dataMenu.getNama());
        Picasso.with(ctx).load(dataMenu.getImgUrl()).into(holder.fotomakanan);

    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }


    public class HistoryHolder extends RecyclerView.ViewHolder {
        public ImageView status, fotomakanan;
        public TextView tvNama, tvJumlah, tvHarga, tvTanggal, tvId, tvStatus;
        public Button detail;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.imgstatus);
            fotomakanan = itemView.findViewById(R.id.imgHistory);
            tvNama = itemView.findViewById(R.id.namamakanan_history);
            tvJumlah = itemView.findViewById(R.id.jumlahmakanan_history);
            tvHarga = itemView.findViewById(R.id.totalhargahistory);
            tvId = itemView.findViewById(R.id.idtransaksi);
            tvTanggal = itemView.findViewById(R.id.tanggalhistory);
            detail = itemView.findViewById(R.id.button_detail);
            tvStatus = itemView.findViewById(R.id.statustext);

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, DetailHistory.class);
                    intent.putExtra("statusDeliver", tvStatus.getText());
                    intent.putExtra("idTransaksi", tvId.getText());
                    intent.putExtra("tanggalPembelian", tvTanggal.getText());
                    ctx.startActivity(intent);
                    ((Activity)ctx).overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                }
            });


        }
    }
}
