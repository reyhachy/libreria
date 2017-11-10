package com.totalplay.utils.validators;

import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused")
public class DateValidator extends FormValidator.Validator {

    private static final int BEFORE_DATE_SYSTEM = 1;
    private static final int BEFORE_DATE = 2;
    private static final int AFTER_DATE = 3;
    private static final int EQUAL_DATE = 4;
    private static final int NOT_EQUAL_DATE = 5;

    private final EditText dateEditText;
    private final String dateCompare;
    private final int errorMessage;
    private final int typeValidate;

    public DateValidator(EditText dateEditText, int typeValidate, int errorMessage) {
        this.dateEditText = dateEditText;
        this.errorMessage = errorMessage;
        this.typeValidate = typeValidate;
        this.dateCompare = "";
    }

    public DateValidator(EditText dateEditText, String dateCompare, int typeValidate, int errorMessage) {
        this.dateEditText = dateEditText;
        this.errorMessage = errorMessage;
        this.typeValidate = typeValidate;
        this.dateCompare = dateCompare;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dateSecond;
            Date dateFirst;
            switch (typeValidate) {
                case BEFORE_DATE_SYSTEM:
                    dateSecond = formatDate.parse(formatDate.format(new Date()));
                    dateFirst = formatDate.parse(dateEditText.getText().toString().trim());
                    if (dateFirst.equals(dateSecond) || dateFirst.after(dateSecond)) {
                        valid = false;
                    }
                    break;
                case BEFORE_DATE:
                    dateSecond = formatDate.parse(dateCompare);
                    dateFirst = formatDate.parse(dateEditText.getText().toString().trim());
                    valid = dateFirst.before(dateSecond);
                    break;
                case AFTER_DATE:
                    dateSecond = formatDate.parse(dateCompare);
                    dateFirst = formatDate.parse(dateEditText.getText().toString().trim());
                    valid = dateFirst.after(dateSecond);
                    break;
                case EQUAL_DATE:
                    dateSecond = formatDate.parse(dateCompare);
                    dateFirst = formatDate.parse(dateEditText.getText().toString().trim());
                    valid = dateFirst.equals(dateSecond);
                    break;
                case NOT_EQUAL_DATE:
                    dateSecond = formatDate.parse(dateCompare);
                    dateFirst = formatDate.parse(dateEditText.getText().toString().trim());
                    valid = !dateFirst.equals(dateSecond);
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    @Override
    void showError() {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    void stopError() {

    }
}
