package com.example.segura;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name, phone;
    private ItemClickListener itemClickListener;
    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        name=(TextView)itemView.findViewById(R.id.cname);
        phone=(TextView)itemView.findViewById(R.id.cphone);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}

