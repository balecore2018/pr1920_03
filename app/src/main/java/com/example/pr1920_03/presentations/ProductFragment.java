package com.example.pr1920_03.presentations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.network.datas.products.ProductCreate;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.example.pr1920_03.R;
import com.example.pr1920_03.domains.callbacks.OnTabClickListner;

import java.io.File;
import java.io.IOException;

public class ProductFragment extends Fragment {

    public static final String TOKEN = Settings.DEMO_TOKEN;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final String TAG = "PRODUCT CREATE";

    private Context context;
    private OnTabClickListner listener;
    private EditText etName;
    private EditText etDescription;
    private EditText etExpenditure;
    private EditText etPrice;
    private Spinner sCategory;
    private AppCompatButton bthCreate;
    private ImageView bthImageSelect;
    private BottomSheetHelper bottomSheetHelper;
    private Uri imageUri;

    public ProductFragment() {
    }

    public ProductFragment(Context context, OnTabClickListner listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (this.context == null) {
            this.context = context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        etName = view.findViewById(R.id.etProductName);
        etDescription = view.findViewById(R.id.etProductDescription);
        etExpenditure = view.findViewById(R.id.etProductExpenditure);
        etPrice = view.findViewById(R.id.etProductPrice);
        sCategory = view.findViewById(R.id.spProductCategory);
        bthCreate = view.findViewById(R.id.bthProductConfirm);
        bthImageSelect = view.findViewById(R.id.ivProductImage);

        bottomSheetHelper = new BottomSheetHelper(
                requireContext(),
                v -> openGallery(),
                v -> openCamera()
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

        return view;
    }

    private void createProduct() {
        int price;
        try {
            price = Integer.parseInt(etPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Цена должна быть числом", Toast.LENGTH_SHORT).show();
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
                requireContext(),
                TOKEN,
                product,
                imageUri,
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d(TAG, result);
                        Toast.makeText(requireContext(), "Товар создан", Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onTabClick(2);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, error);
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
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

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), REQUEST_GALLERY);
    }

    public void openCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = File.createTempFile(
                    "my_photo_card",
                    ".jpg",
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    photoFile
            );

            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pictureIntent, REQUEST_CAMERA);
        } catch (IOException e) {
            Log.e(TAG, "Camera file error", e);
            Toast.makeText(requireContext(), "Не удалось открыть камеру", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
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
