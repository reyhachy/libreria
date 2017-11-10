package com.totalplay.utils.validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jorgehdezvilla on 10/11/17.
 */

@SuppressWarnings("unused")
public class TextViewValidator extends FormValidator.Validator implements TextWatcher {

    private final TextView textView;
    private final int errorMessage;
    private final Pattern pattern;
    private final int minCharacters;
    private final int maxCharacters;


    public TextViewValidator(TextView textView, String regex, int errorMessage) {
        this.textView = textView;
        this.textView.addTextChangedListener(this);
        this.errorMessage = errorMessage;
        this.minCharacters = -1;
        this.maxCharacters = -1;
        this.pattern = Pattern.compile(regex);
    }

    public TextViewValidator(TextView textView, int minCharacters, int maxCharacters, int errorMessage) {
        this.textView = textView;
        this.textView.addTextChangedListener(this);
        this.errorMessage = errorMessage;
        this.minCharacters = minCharacters;
        this.maxCharacters = maxCharacters;
        if (minCharacters < 0) {
            throw new IllegalArgumentException("[EditTextValidator] Minimum number of characters is 0");
        }
        this.pattern = null;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        if (minCharacters != -1) {
            valid = textView.getText().toString().trim().length() >= minCharacters;
        }
        if (maxCharacters != -1) {
            valid &= maxCharacters >= textView.getText().toString().trim().length();
        }
        if (pattern != null) {
            Matcher matcher = pattern.matcher(textView.getText().toString().trim());
            return valid & matcher.matches();
        }

        return valid;
    }

    @Override
    public void showError() {
        error(errorMessage);
    }

    private void error(Integer message) {
        try {
            textView.setError(message != null ? context.getString(message) : null);
            textView.requestFocus();
        } catch (Exception ignored) {

        }
    }

    @Override
    void stopError() {
        error(null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        textView.setError(null);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textView.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
        textView.setError(null);
    }
}