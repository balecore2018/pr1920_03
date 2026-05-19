package com.example.pr1920_03.presentations;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.network.datas.products.ProductCreate;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.example.pr1920_03.R;

import java.io.File;
import java.io.IOException;

public class ProductActivity extends AppCompatActivity {

    public static ProductActivity init;
    public static final String TOKEN = Settings.DEMO_TOKEN;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final String TAG = "PRODUCT CREATE";

    private EditText etName;
    private EditText etDescription;
    private EditText etExpenditure;
    private EditText etPrice;
    private Spinner sCategory;
    private AppCompatButton bthCreate;
    private ImageView bthImageSelect;
    private BottomSheetHelper bottomSheetHelper;
    private Uri imageUri;

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

        init = this;

        etName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etProductDescription);
        etExpenditure = findViewById(R.id.etProductExpenditure);
        etPrice = findViewById(R.id.etProductPrice);
        sCategory = findViewById(R.id.spProductCategory);
        bthCreate = findViewById(R.id.bthProductConfirm);
        bthImageSelect = findViewById(R.id.ivProductImage);

        bottomSheetHelper = new BottomSheetHelper(
                this,
                v -> OpenGallery(),
                v -> OpenCamera()
        );

        bthCreate.setEnabled(false);
        bthImageSelect.setOnClickListener(v -> bottomSheetHelper.show());
        bthCreate.setOnClickListener(v -> createProduct());

        TextWatcher formWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCreateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        etName.addTextChangedListener(formWatcher);
        etDescription.addTextChangedListener(formWatcher);
        etExpenditure.addTextChangedListener(formWatcher);
        etPrice.addTextChangedListener(formWatcher);
    }

    private void createProduct() {
        int price;
        try {
            price = Integer.parseInt(etPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Цена должна быть числом", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(
                etName.getText().toString().trim(),
                etDescription.getText().toString().trim(),
                sCategory.getSelectedItemPosition(),
                etExpenditure.getText().toString().trim(),
                price
        );

        ProductCreate requestProductCreate = new ProductCreate(
                this,
                TOKEN,
                product,
                imageUri,
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d(TAG, result);
                        Toast.makeText(ProductActivity.this, "Товар создан", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, error);
                        Toast.makeText(ProductActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestProductCreate.execute();
    }

    private void updateCreateButtonState() {
        boolean hasText = !isEmpty(etName)
                && !isEmpty(etDescription)
                && !isEmpty(etExpenditure)
                && !isEmpty(etPrice);
        bthCreate.setEnabled(hasText && imageUri != null);
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    public void OpenGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), REQUEST_GALLERY);
    }

    public void OpenCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = File.createTempFile(
                    "my_photo_card",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    photoFile
            );

            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pictureIntent, REQUEST_CAMERA);
        } catch (IOException e) {
            Log.e(TAG, "Camera file error", e);
            Toast.makeText(this, "Не удалось открыть камеру", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_GALLERY && data != null && data.getData() != null) {
            imageUri = data.getData();
            bthImageSelect.setImageURI(imageUri);
        } else if (requestCode == REQUEST_CAMERA && imageUri != null) {
            bthImageSelect.setImageURI(imageUri);
        }

        bthImageSelect.setBackgroundResource(R.drawable.product_field_background);
        bthImageSelect.setPadding(0, 0, 0, 0);
        updateCreateButtonState();
    }
}
