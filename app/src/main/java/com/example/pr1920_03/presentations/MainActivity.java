package com.example.pr1920_03.presentations;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.network.domains.common.Settings;
import com.example.pr1920_03.R;
import com.example.pr1920_03.domains.callbacks.OnTabClickListner;

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

                openFragment = new ProductFragment(context, MenuItemSelect);
                ft.add(R.id.content, openFragment);

            } else if (position == 2) {

                openFragment = new ProductsFragment(context, MenuItemSelect);
                ft.add(R.id.content, openFragment);

            }

            ft.commit();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (openFragment instanceof ProductFragment) {
            openFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
