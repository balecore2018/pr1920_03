package com.example.uicomponents;

import android.content.Context;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BthBig extends BthCustom{

    public BthBig(@NotNull Context context){
        super(context);
    }

    public BthBig(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BthBig(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Integer idLayout){
        super.init(R.layout.button);
    }

}
