package com.example.fooddelivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CartHolder> {

    private Context ctx;
    private List<DataCart> cartList;

    public CheckoutAdapter(Context ctx, List<DataCart> cartList) {
        this.ctx = ctx;
        this.cartList = cartList;
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        public TextView tvNama, tvQuantity, tvEdit, tvHarga;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.txtNama);
            tvQuantity = itemView.findViewById(R.id.txtQuantity);
            tvHarga = itemView.findViewById(R.id.txtHarga);

        }
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.cart_item, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        DataCart dataCart = cartList.get(position);
        holder.tvHarga.setText(dataCart.getHargaMenu());
        holder.tvQuantity.setText(dataCart.getQuantityMenu());
        holder.tvNama.setText(dataCart.getNamaMenu());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


}
