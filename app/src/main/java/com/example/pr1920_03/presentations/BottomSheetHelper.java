package com.example.pr1920_03.presentations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pr1920_03.R;
import com.example.uicomponents.BthBig;
import com.example.uicomponents.BthCustom;

public class BottomSheetHelper {

    public BottomSheetHelper dialog;


    public BottomSheetHelper(Context context){

        dialog = new BottomSheetHelper(context);

        View view = LayoutInflater.from(context).inflate(R.layout.bs_select_image, null);

        BthBig bthGallery = view.findViewById(R.id.bthGallery);
        BthBig bthCamera = view.findViewById(R.id.bthCamera);

        bthGallery.init(

                "\uD83D\uDDBC\uFE0F Выбрать из галереи",
                BthCustom.TypeButton.SECONDARY

        );

        bthCamera.init(

                "\uD83D\uDCF8 Сфотографировать",
                BthCustom.TypeButton.SECONDARY

        );

        bthGallery.Bth.setOnClickListener(v -> {

            ProductActivity.init.OpenGallery();

        });

        bthCamera.Bth.setOnClickListener(v -> {

            ProductActivity.init.OpenCamera();

        });

        dialog.setContentView(view);

    }

}
