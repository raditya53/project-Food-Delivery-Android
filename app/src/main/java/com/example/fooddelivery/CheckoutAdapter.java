package com.example.fooddelivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CartHolder> {

    private Context ctx;
    private List<DataCart> cartList;
    private List<DataMenu> menuList;

    public CheckoutAdapter(Context ctx, List<DataCart> cartList, List<DataMenu> dataMenus) {
        this.ctx = ctx;
        this.cartList = cartList;
        this.menuList = dataMenus;
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        public TextView tvNama, tvQuantity, tvHarga;
        public ImageView ivFoto;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.txtNama);
            tvQuantity = itemView.findViewById(R.id.txtQuantity);
            tvHarga = itemView.findViewById(R.id.txtHarga);
            ivFoto = itemView.findViewById(R.id.fotomenucart);
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
        DataMenu dataMenu = menuList.get(position);

        if(dataMenu.getKategori().equals("Chicken")) {
            Picasso.with(ctx).load(R.drawable.chicken).into(holder.ivFoto);
        } else if(dataMenu.getKategori().equals("Desert")) {
            Picasso.with(ctx).load(R.drawable.desert).into(holder.ivFoto);
        } else if(dataMenu.getKategori().equals("Side Dish")) {
            Picasso.with(ctx).load(R.drawable.sidedish).into(holder.ivFoto);
        } else if(dataMenu.getKategori().equals("Burger")) {
            Picasso.with(ctx).load(R.drawable.berger).into(holder.ivFoto);
        } else if(dataMenu.getKategori().equals("Drink")) {
            Picasso.with(ctx).load(R.drawable.minumanlogo).into(holder.ivFoto);
        } else if(dataMenu.getKategori().equals("Coffee")) {
            Picasso.with(ctx).load(R.drawable.coffeee).into(holder.ivFoto);
        } else if(dataMenu.getKategori().equals("Pasta")) {
            Picasso.with(ctx).load(R.drawable.pastaa).into(holder.ivFoto);
        }

        holder.tvHarga.setText(dataCart.getHargaMenu());
        holder.tvQuantity.setText(dataCart.getQuantityMenu());
        holder.tvNama.setText(dataCart.getNamaMenu());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


}
