package com.example.fooddelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KurirAdapter extends RecyclerView.Adapter<KurirAdapter.KurirHolder> {

    private Context ctx;
    private List<DataHistory> dataHistoryList;
    private List<DataUser> dataUserList;

    public KurirAdapter(Context ctx, List<DataHistory> dataHistoryList, List<DataUser> dataUserList) {
        this.ctx = ctx;
        this.dataHistoryList = dataHistoryList;
        this.dataUserList = dataUserList;
    }

    @NonNull
    @Override
    public KurirHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.activity_kurir_detail,parent, false);
        return new KurirAdapter.KurirHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KurirHolder holder, int position) {
        DataHistory dataHistory = dataHistoryList.get(position);
        DataUser dataUser = dataUserList.get(position);
        holder.alamat.setText(dataHistory.getLocation());
        holder.tanggal.setText(dataHistory.getTanggalPembelian());
        holder.idTrans.setText(dataHistory.getIdTransaksi());
        holder.namaCust.setText(dataUser.getFullname());
    }

    @Override
    public int getItemCount() {
        return dataHistoryList.size();
    }

    public class KurirHolder extends RecyclerView.ViewHolder {
        public Button sukses,cancel;
        public TextView tanggal,idTrans,namaCust,alamat;

        public KurirHolder(@NonNull View itemView) {
            super(itemView);
            sukses = itemView.findViewById(R.id.success);
            cancel = itemView.findViewById(R.id.cancel);
            tanggal = itemView.findViewById(R.id.tanggaldeliver);
            idTrans = itemView.findViewById(R.id.idDeliver);
            namaCust = itemView.findViewById(R.id.namacust);
            alamat = itemView.findViewById(R.id.lokasicust);

            sukses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctx instanceof Kurir) {
                        ((Kurir)ctx).DialogKurir(idTrans.getText().toString(), "Success");
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctx instanceof Kurir) {
                        ((Kurir)ctx).DialogKurir(idTrans.getText().toString(), "Failed");
                    }
                }
            });
        }
    }
}