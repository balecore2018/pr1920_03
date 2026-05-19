package com.example.uicomponents.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uicomponents.R;

public class EditTextCustom extends LinearLayout {

    private TextView tvLabel;
    private EditText etInput;
    private TextView tvError;
    private String errorMessage = "";

    public EditTextCustom(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.edit_text, this, true);

        tvLabel = findViewById(R.id.tvLabel);
        etInput = findViewById(R.id.etInput);
        tvError = findViewById(R.id.tvError);

        // Установка стиля для EditText
        etInput.setBackgroundResource(R.drawable.edit_text_background);

        // Обработчик изменения текста для проверки ошибок
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // При изменении текста скрываем ошибку
                if (tvError.getVisibility() == VISIBLE) {
                    setErrorState(false, "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Обработчик фокуса для изменения фона
        etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etInput.setBackgroundResource(R.drawable.edit_text_hover);
                } else {
                    if (tvError.getVisibility() == VISIBLE) {
                        etInput.setBackgroundResource(R.drawable.edit_text_error);
                    } else {
                        etInput.setBackgroundResource(R.drawable.edit_text_default);
                    }
                }
            }
        });

        // Чтение атрибутов из XML
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextCustom);
            String labelText = a.getString(R.styleable.EditTextCustom_label);
            String hintText = a.getString(R.styleable.EditTextCustom_hint);
            String valueText = a.getString(R.styleable.EditTextCustom_value);

            if (labelText != null) {
                tvLabel.setText(labelText);
            }
            if (hintText != null) {
                etInput.setHint(hintText);
            }
            if (valueText != null) {
                etInput.setText(valueText);
            }

            a.recycle();
        }
    }

    public void init(String label, String hint, String value) {
        tvLabel.setText(label);
        etInput.setHint(hint);
        etInput.setText(value);
    }

    public void setErrorState(boolean isError, String message) {
        if (isError) {
            tvError.setText(message);
            tvError.setVisibility(VISIBLE);
            etInput.setBackgroundResource(R.drawable.edit_text_error);
        } else {
            tvError.setVisibility(GONE);
            if (etInput.isFocused()) {
                etInput.setBackgroundResource(R.drawable.edit_text_hover);
            } else {
                etInput.setBackgroundResource(R.drawable.edit_text_default);
            }
        }
    }

    /**
     * Получение введенного текста
     */
    public String getText() {
        return etInput.getText().toString();
    }

    public boolean isValid() {
        String text = getText();
        if (text == null || text.trim().isEmpty()) {
            setErrorState(true, "Поле не может быть пустым");
            return false;
        }
        return true;
    }

    public EditText getEditText() {
        // верните внутренний EditText из вашего LinearLayout
        return etInput;
    }
}