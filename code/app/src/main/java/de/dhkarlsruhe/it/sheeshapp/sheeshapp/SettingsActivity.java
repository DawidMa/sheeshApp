package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by d0272129 on 22.01.18.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
