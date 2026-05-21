package com.example.pr1920_03.presentations;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pr1920_03.R;
import com.example.pr1920_03.domains.MenuItem;
import com.example.pr1920_03.domains.adapters.MenuAdapter;
import com.example.pr1920_03.domains.callbacks.OnTabClickListner;

import java.util.ArrayList;
import java.util.List;


public class NavigationMenu extends Fragment {

    public RecyclerView RecyclerView;
    public MenuAdapter Adapter;
    public Context context;
    OnTabClickListner listner;
    public NavigationMenu(Context context, OnTabClickListner listner) {

        this.context = context;
        this.listner = listner;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_menu, container,false);
        List<MenuItem> Items = new ArrayList<>();
        Items.add(new MenuItem("Главная", R.drawable.ic_home));
        Items.add(new MenuItem("Каталог", R.drawable.ic_catalog));
        Items.add(new MenuItem("Продукты", R.drawable.ic_order));
        Items.add(new MenuItem("Профиль", R.drawable.ic_user));

        RecyclerView = view.findViewById(R.id.recycleView);
        RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), Items.size()));
        Adapter = new MenuAdapter(Items, listner);
        RecyclerView.setAdapter(Adapter);
        return view;

    }

}
