package com.example.ridesharing.commonClasses;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Адаптивный класс для анимации любого элемента из кода
 */
public class ClassResizeAnimation extends Animation {
    final int startWidth;
    final int targetWidth;
    final int startHeight;
    final int targetHeight;
    View view;

    /**
     * Конструктор класса
     * @param view Передать объект
     * @param targetWidth Передать новое значения ширины
     * @param targetHeight Передать новое значение высоты
     */
    public ClassResizeAnimation(View view, int targetWidth, int targetHeight) {
        this.view = view;
        this.targetWidth = targetWidth;
        startWidth = view.getWidth();

        this.targetHeight = targetHeight;
        startHeight = view.getHeight();
    }

    /**
     * Применение транформации объекта
     * @param interpolatedTime Установка времени проигрывания анмиации
     * @param t Тип транформации
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
        view.getLayoutParams().width = newWidth;

        int newHeight = (int) (startHeight + (targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;

        view.requestLayout();
    }

    /**
     * Обновление данных
     * @param width Ширина
     * @param height Высота
     * @param parentWidth Ширина контейнера родителя
     * @param parentHeight Высота контейнейра родителя
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}