package com.example.fooddelivery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KurirAdapter extends RecyclerView.Adapter<KurirAdapter.KurirHolder> {
    private Context ctx;


    @NonNull
    @Override
    public KurirHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull KurirHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class KurirHolder extends RecyclerView.ViewHolder {

        public KurirHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
