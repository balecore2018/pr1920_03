package com.example.pr1920_03.presentations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProductsFragment extends Fragment {

    private static final String TAG = "PRODUCT GET USER";
    public static final String TOKEN = Settings.DEMO_TOKEN;

    private View bthOpenAddProduct;
    private LinearLayout llContent;
    private List<Product> Products;
    Context context;
    OnTabClickListner listner;

    public ProductsFragment(Context context, OnTabClickListner listner) {

        this.context = context;
        this.listner = listner;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        PermissionManager.GetPermission(context, MainActivity.init);

        bthOpenAddProduct = view.findViewById(R.id.bthOpenAddProduct);
        llContent = view.findViewById(R.id.llContent);

        bthOpenAddProduct.setOnClickListener(v ->
                listner.onTabClick((-1))
        );

        ProductGetUser();
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        ProductGetUser();

    }

    public void ProductGetUser() {
        ProductGetUser RequestProductGetUser = new ProductGetUser(
                MainActivity.TOKEN,
                new MyResponseCallback() {
            @Override
            public void onCompile(String result) {
                Log.d(TAG, result);
                Products = new GsonBuilder().create().fromJson(
                        result,
                        new TypeToken<ArrayList<Product>>() {
                        }.getType()
                );
                CreateElements();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
            }
        });

        RequestProductGetUser.execute();
    }

    public void CreateElements() {
        llContent.removeAllViews();

        for (Product product : Products) {
            View itemProduct = LayoutInflater.from(context).inflate(R.layout.tovar, llContent, false);

            BthBig bthOpen = itemProduct.findViewById(R.id.bthOpenAddProduct);
            TextView tvName = itemProduct.findViewById(R.id.tvName);
            TextView tvPrice = itemProduct.findViewById(R.id.tvPrice);

            bthOpen.init("Открыть", BthCustom.TypeButton.PRIMARY);
            bthOpen.Bth.setTextSize(16);
            tvName.setText(product.name);
            tvPrice.setText(product.price + " ₽");

            llContent.addView(itemProduct);
        }
    }
}
