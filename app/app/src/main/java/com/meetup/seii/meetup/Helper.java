package com.meetup.seii.meetup;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;

public class Helper {
    public static String getButtonText(Activity activity, int id) {
        EditText editText = (EditText)activity.findViewById(id);
        return editText.getText().toString();
    }

    public static boolean getRadioChecked(Activity activity, int id) {
        RadioButton radio = (RadioButton)activity.findViewById(id);
        return radio.isChecked();
    }

    public static boolean uslessString(String s) {
        return s == null || s.equals("");
    }
}
