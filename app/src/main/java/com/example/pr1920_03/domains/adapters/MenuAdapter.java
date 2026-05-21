package com.example.pr1920_03.domains.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr1920_03.R;
import com.example.pr1920_03.domains.MenuItem;
import com.example.pr1920_03.domains.callbacks.OnTabClickListner;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    List<MenuItem> Items;
    Integer SelectPosition = 0;
    public OnTabClickListner listner;
    public MenuAdapter(List<MenuItem> items, OnTabClickListner listner) {

        Items = items;
        this.listner = listner;

    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {

       holder.Title.setText(Items.get(position).Title);
       holder.Image.setImageResourse(Items.get(position).IdDrawable);

       Integer SelectColor = position == SelectPosition ?
               Color.parseColor("#1A6FEE") :
               Color.parseColor("B8C1CC");

       holder.Title.setTextColor(SelectColor);
       holder.Image.setColorFilter(SelectColor);
       holder.Parent.setOnClickListener(v -> {

           notifyItemChanged(SelectPosition);
           SelectPosition = position;
           notifyItemChanged(SelectPosition);
           if (listner != null) listner.onTabClick(position);

       });
    }

    @Override
    public int getItemCount() {

        return Items.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout Parent;
        ImageView Image;
        TextView Title;

        public ViewHolder (@NonNull View itemView) {

            super(itemView);
            Image = itemView.findViewById(com.example.uicomponents.R.id.imageView);
            Title = itemView.findViewById(com.example.uicomponents.R.id.textView);
            Parent = (LinearLayout) Image.getParent();

        }

    }

}
