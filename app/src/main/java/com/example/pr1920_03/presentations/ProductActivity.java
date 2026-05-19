package com.example.pr1920_03.presentations;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pr1920_03.R;

public class ProductActivity extends AppCompatActivity {

    private EditText productName;
    private EditText productDescription;
    private EditText productExpenditure;
    private EditText productPrice;
    private Spinner productCategory;
    private ImageView productImage;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri == null) {
                    return;
                }

                selectedImageUri = uri;
                productImage.setImageURI(uri);
                productImage.setBackgroundResource(R.drawable.product_field_background);
                productImage.setPadding(0, 0, 0, 0);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productName = findViewById(R.id.etProductName);
        productDescription = findViewById(R.id.etProductDescription);
        productExpenditure = findViewById(R.id.etProductExpenditure);
        productPrice = findViewById(R.id.etProductPrice);
        productCategory = findViewById(R.id.spProductCategory);
        productImage = findViewById(R.id.ivProductImage);
        AppCompatButton confirmButton = findViewById(R.id.bthProductConfirm);

        productImage.setOnClickListener(v -> imagePicker.launch("image/*"));
        confirmButton.setOnClickListener(v -> validateProductForm());
    }

    private void validateProductForm() {
        if (isEmpty(productName) || isEmpty(productDescription)
                || isEmpty(productExpenditure) || isEmpty(productPrice)) {
            Toast.makeText(this, "Заполни поля товара", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = productCategory.getSelectedItem().toString();
        String imageStatus = selectedImageUri == null ? "без изображения" : "с изображением";
        Toast.makeText(this, "Товар готов: " + category + ", " + imageStatus, Toast.LENGTH_SHORT).show();
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}
