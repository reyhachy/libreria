package com.totalplay.utils.validators;

import android.widget.ImageView;
import android.widget.Toast;


@SuppressWarnings("unused")
public class ImageViewValidator extends FormValidator.Validator {

    private final ImageView imageView;
    private final int errorMessage;


    public ImageViewValidator(ImageView imageView, int errorMessage) {
        this.imageView = imageView;
        this.errorMessage = errorMessage;
    }


    @Override
    public boolean isValid() {
        return imageView.getDrawable() != null;
    }

    @Override
    public void showError() {
        error(errorMessage);
    }

    private void error(Integer message) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    void stopError() {
//        error(null);
    }
}
