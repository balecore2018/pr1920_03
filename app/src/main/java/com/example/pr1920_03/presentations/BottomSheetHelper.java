package com.example.pr1920_03.presentations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pr1920_03.R;
import com.example.uicomponents.BthBig;
import com.example.uicomponents.BthCustom;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheetHelper extends BottomSheetDialog {

    public BottomSheetHelper(Context context) {
        this(context, null, null);
    }

    public BottomSheetHelper(
            Context context,
            View.OnClickListener galleryClickListener,
            View.OnClickListener cameraClickListener
    ) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.bs_select_image, null, false);

        BthBig bthGallery = view.findViewById(R.id.bthGallery);
        BthBig bthCamera = view.findViewById(R.id.bthCamera);

        bthGallery.init("Выбрать из галереи", BthCustom.TypeButton.SECONDARY);
        bthCamera.init("Сфотографировать", BthCustom.TypeButton.SECONDARY);

        if (galleryClickListener != null) {
            bthGallery.Bth.setOnClickListener(v -> {
                galleryClickListener.onClick(v);
                dismiss();
            });
        }

        if (cameraClickListener != null) {
            bthCamera.Bth.setOnClickListener(v -> {
                cameraClickListener.onClick(v);
                dismiss();
            });
        }

        setContentView(view);
    }
}
