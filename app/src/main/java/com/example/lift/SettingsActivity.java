package com.example.lift;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
//import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    public static final String KEY_PREF_LIFT_ADDRESS = "address_key";
    public static final String KEY_PREF_LIFT_NUMBER = "lift_number_key";
    public static final String KEY_PREF_FLOOR_NUMBER = "lift_floor_number_key";
    public static final String KEY_PREF_USER_NAME = "user_name_key";

}
