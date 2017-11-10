package com.totalplay.utils.validators;


public class ViewGroupValidator extends FormValidator.Validator {

    private final FormValidator.Validator validator;
    private final FormValidator.Validator[] validators;
    private final boolean showAllErrors;
    private final boolean valueActivator;

    public ViewGroupValidator(FormValidator.Validator validator, FormValidator.Validator[] validators,
                              boolean showAllErrors, boolean valueActivator) {
        this.validator = validator;
        this.validators = validators;
        this.showAllErrors = showAllErrors;
        this.valueActivator = valueActivator;
    }

    @Override
    boolean isValid() {
        boolean valid = true;
        boolean result = validator.isValid();
        if (result == valueActivator){
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
