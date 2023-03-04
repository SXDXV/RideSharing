package com.example.ridesharing;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ClassResizeAnimation extends Animation {
    final int startWidth;
    final int targetWidth;
    final int startHeight;
    final int targetHeight;
    View view;

    public ClassResizeAnimation(View view, int targetWidth, int targetHeight) {
        this.view = view;
        this.targetWidth = targetWidth;
        startWidth = view.getWidth();

        this.targetHeight = targetHeight;
        startHeight = view.getHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
        view.getLayoutParams().width = newWidth;

        int newHeight = (int) (startHeight + (targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;

        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}