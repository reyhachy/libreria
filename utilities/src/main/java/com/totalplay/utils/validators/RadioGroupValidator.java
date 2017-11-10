package com.totalplay.utils.validators;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class RadioGroupValidator extends FormValidator.Validator {

    private final int errorMessage;
    private final RadioGroup radioGroup;

    public RadioGroupValidator(RadioGroup radioGroup, int errorMessage) {
        this.radioGroup = radioGroup;
        this.errorMessage = errorMessage;
    }

    @Override
    boolean isValid() {
        int count = radioGroup.getChildCount();
        boolean atLeastOneChecked = false;
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                atLeastOneChecked = ((RadioButton) o).isChecked() || atLeastOneChecked;
            }
        }
        return atLeastOneChecked;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    void showError() {
//        int count = radioGroup.getChildCount();
//        for (int i = 0; i < count; i++) {
//            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
//            radioButton.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
//            radioButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_red_dark)));
//        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    void stopError() {

    }
}
