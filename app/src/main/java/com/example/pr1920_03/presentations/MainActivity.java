package com.example.pr1920_03.presentations;

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

import com.example.network.datas.products.ProductGetUser;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.example.pr1920_03.R;
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

    private View bthOpenAddProduct;
    private LinearLayout llContent;
    private List<Product> products = new ArrayList<>();

    private final ActivityResultLauncher<Intent> productActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    productGetUser();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PermissionManager.GetPermission(this, this);

        bthOpenAddProduct = findViewById(R.id.bthOpenAddProduct);
        llContent = findViewById(R.id.llContent);

        bthOpenAddProduct.setOnClickListener(v ->
                productActivityLauncher.launch(new Intent(MainActivity.this, ProductActivity.class))
        );

        productGetUser();
    }

    public void productGetUser() {
        ProductGetUser requestProductGetUser = new ProductGetUser(TOKEN, new MyResponseCallback() {
            @Override
            public void onCompile(String result) {
                Log.d(TAG, result);
                products = new GsonBuilder().create().fromJson(
                        result,
                        new TypeToken<ArrayList<Product>>() {
                        }.getType()
                );

                if (products == null) {
                    products = new ArrayList<>();
                }

                createElement();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
            }
        });

        requestProductGetUser.execute();
    }

    public void createElement() {
        llContent.removeAllViews();

        for (Product product : products) {
            View itemProduct = LayoutInflater.from(this).inflate(R.layout.tovar, llContent, false);

            BthBig bthBig = itemProduct.findViewById(R.id.bthOpenAddProduct);
            TextView tvName = itemProduct.findViewById(R.id.tvName);
            TextView tvPrice = itemProduct.findViewById(R.id.tvPrice);

            bthBig.init("Открыть", BthCustom.TypeButton.PRIMARY);
            tvName.setText(product.name);
            tvPrice.setText(product.price + " ₽");

            llContent.addView(itemProduct);
        }
    }
}
