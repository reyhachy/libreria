package com.totalplay.utils.validators;

import android.widget.RadioButton;


public class RadioButtonGroupValidator extends FormValidator.Validator {

    private final RadioButton radioButton;
    private final FormValidator.Validator[] validators;
    private final boolean showAllErrors;

    public RadioButtonGroupValidator(RadioButton radioButton, FormValidator.Validator[] validators,
                                     boolean showAllErrors) {
        this.radioButton = radioButton;
        this.validators = validators;
        this.showAllErrors = showAllErrors;
    }

    @Override
    boolean isValid() {
        boolean valid = true;
        if (radioButton.isChecked()){
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
