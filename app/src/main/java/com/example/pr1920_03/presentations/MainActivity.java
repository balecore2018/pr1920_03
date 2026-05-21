package com.example.pr1920_03.presentations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.network.datas.products.ProductGetUser;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.example.pr1920_03.R;
import com.example.pr1920_03.domains.callbacks.OnTabClickListner;
import com.example.pr1920_03.domains.managers.PermissionManager;
import com.example.uicomponents.BthBig;
import com.example.uicomponents.BthCustom;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PRODUCT GET USER";
    public static final String TOKEN = Settings.DEMO_TOKEN;

    public static MainActivity init;
    public Fragment openFragment;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init = this;
        context = this;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NavigationMenu menu = new NavigationMenu(this, MenuItemSelect);
        ft.add(R.id.menu_navigation, menu);
        ft.commit();

    }

    OnTabClickListner MenuItemSelect = new OnTabClickListner() {
        @Override
        public void onTabClick(Integer position) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (openFragment != null)
                ft.remove(openFragment);

            if (position == -1) {

                openFragment = new ProductFragment(context);
                ft.add(R.id.content, openFragment);

            } else if (position == 2) {

                openFragment = new ProductsFragment(context, MenuItemSelect);
                ft.add(R.id.content, openFragment);

            }

            ft.commit();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        ProductFragment productFragment = (ProductFragment) openFragment;
        productFragment.onActivityResult(requestCode, resultCode, data);

    }
}
