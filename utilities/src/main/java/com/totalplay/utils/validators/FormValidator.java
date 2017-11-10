package com.totalplay.utils.validators;

import android.content.Context;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class FormValidator {

    ArrayList<Validator> mValidators;
    private Context mContext;
    private boolean showAllErrors;

    public FormValidator(Context context){
        this.mContext = context;
        mValidators = new ArrayList<>();
        showAllErrors = false;
    }

    public FormValidator(Context context, boolean showAllErrors){
        this.mContext = context;
        this.showAllErrors = showAllErrors;
        mValidators = new ArrayList<>();
    }

    public <T extends Validator>void addValidator(T validator){
        validator.setContext(mContext);
        mValidators.add(validator);
    }

    public <T extends Validator>void addValidators(Validator... validators){
        for(Validator validator : validators) {
            validator.setContext(mContext);
            mValidators.add(validator);
        }
    }

    public boolean isEdit(){
        boolean edit = false;
        for(Validator v : mValidators){
            edit |= v.isValid();
        }
        return edit;
    }

    public boolean isValid(){
        boolean valid = true;
        if(showAllErrors){
            for(Validator v : mValidators){
                if(!v.isValid()) {
                    v.showError();
                }else{
                    v.stopError();
                }
                valid &= v.isValid();
            }
        }else{
            for(Validator v : mValidators){
                if(!v.isValid()){
                    v.showError();
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    public abstract static class Validator{

        Context context;

        public Validator(Context context){
            this.context = context;
        }

        public Validator(){

        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        abstract boolean isValid();
        abstract void showError();
        abstract void stopError();
    }

}
