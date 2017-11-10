package com.totalplay.utils.validators;

import android.widget.CheckBox;

/**
 * Created by jorge.hernandez on 22/09/2016.
 * :v
 */
public class CheckBoxGroupValidator extends FormValidator.Validator {

    private final CheckBox checkBox;
    private final FormValidator.Validator[] validators;
    private final boolean showAllErrors;

    public CheckBoxGroupValidator( CheckBox checkBox, FormValidator.Validator[] validators,
                                  boolean showAllErrors) {
        this.checkBox = checkBox;
        this.validators = validators;
        this.showAllErrors = showAllErrors;
    }

    @Override
    boolean isValid() {
        boolean valid = true;
        if (checkBox.isChecked()){
            for (FormValidator.Validator validator : validators) {
                validator.setContext(context);
                if (!validator.isValid()){
                    valid = false;
                }
            }
        }
        return valid;
    }

    @Override
    void showError() {
        if(showAllErrors){
            for (FormValidator.Validator validator : validators) {
                if (!validator.isValid()){
                   validator.showError();
                }
            }
        } else {
            for (FormValidator.Validator validator : validators) {
                if (!validator.isValid()){
                    validator.showError();
                    break;
                }
            }
        }
    }

    @Override
    void stopError() {
        for (FormValidator.Validator validator : validators) {
            if (validator.isValid()){
                validator.stopError();
            }
        }
    }
}
