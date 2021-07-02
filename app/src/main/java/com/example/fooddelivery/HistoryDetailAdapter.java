package com.example.fooddelivery;

import android.content.Context;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import java.util.List;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailHolder> {
    private Context ctx;
    private List<DataCart> dataCartList;
    private List<DataMenu> dataMenuList;

    public HistoryDetailAdapter(Context ctx, List<DataCart> lHistory, List<DataMenu> dataMenuList) {
        this.ctx = ctx;
        this.dataCartList = lHistory;
        this.dataMenuList = dataMenuList;
    }


    @NonNull
    @Override
    public HistoryDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.detailhistory_item,parent, false);
        return new HistoryDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailHolder holder, int position) {
        DataCart dataCart = dataCartList.get(position);
        DataMenu dataMenu = dataMenuList.get(position);

        holder.namamakanan.setText(dataCart.getNamaMenu());
        holder.totalharga.setText("IDR " + dataCart.getHargaMenu());
        holder.jumlahbarang.setText(dataCart.getQuantityMenu() + " Items");

        Picasso.with(ctx).load(dataMenu.getImgUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataCartList.size();
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

