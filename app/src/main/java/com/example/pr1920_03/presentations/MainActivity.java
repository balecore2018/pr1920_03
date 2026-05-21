package com.example.pr1920_03.presentations;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(1,101, Menu.NONE,"Изменить");
        menu.add(2,102, Menu.NONE,"Удалить");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getGroupId() == 1)
            Toast.makeText(this, "Изменение элемента", Toast.LENGTH_SHORT).show();
        else if (item.getGroupId() == 2)
            Toast.makeText(this, "Удаление элемента", Toast.LENGTH_SHORT).show();

        return true;

    }
}
