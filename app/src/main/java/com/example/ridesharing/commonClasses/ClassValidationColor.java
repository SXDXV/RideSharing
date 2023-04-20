package com.example.ridesharing.commonClasses;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ridesharing.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Класс создания адаптивной перекраски неправильно заполненных полей пользователем
 */
public class ClassValidationColor{
    private Context context;

    /**
     * Конструктор класса
     * @param context Передача контекста (this/getContext()/getActivity())
     */
    public ClassValidationColor(Context context) {
        this.context = context;
    }

    /**
     * Основной метод перекраски полей
     * @param backColor Исходный цвет поля
     * @param view Выбор элемента для взаимодействия
     * @param inner Входной текст элемента
     */
    public void validationColor(String backColor,TextInputLayout view, String inner){
        if (!inner.equals("")){
            view.setBoxStrokeColor(context.getResources().getColor(R.color.main_color));
            view.setHintTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.main_color)));
            if (backColor.equals("white")) view.setBoxBackgroundColor(context.getResources().getColor(R.color.white));
            else if (backColor.equals("very_light_dark")) view.setBoxBackgroundColor(context.getResources().getColor(R.color.very_light_dark));
            view.getEditText().setTextColor(context.getResources().getColor(R.color.light_dark));
        }else{
            view.setBoxStrokeColor(context.getResources().getColor(R.color.pass));
            view.setHintTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.pass)));
            view.setBoxBackgroundColor(context.getResources().getColor(R.color.semi_pass));
            view.getEditText().setTextColor(context.getResources().getColor(R.color.pass));
        }
    }
}
