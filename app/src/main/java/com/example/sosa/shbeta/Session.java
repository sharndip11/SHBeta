package com.example.sosa.shbeta;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sosa on 19/01/17.
 */

public class Session {
    private SharedPreferences preferences;

    public Session(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setIndexRegistration(boolean _index) {
        preferences.edit().putBoolean("_indexRegistration", _index).commit();
    }

    public boolean getIndexRegistration() {
        boolean _index = preferences.getBoolean("_indexRegistration", false);

        return _index;
    }

    public void setIdUtilisateur(int _id) {
        preferences.edit().putInt("_idUtilisateur", _id).commit();
    }

    public int getIdUtilisateur() {
        int _id = preferences.getInt("_idUtilisateur", 0);

        return _id;
    }

    public void setIdSujetFAQ(int _id) {
        preferences.edit().putInt("_idSujetFAQ", _id).commit();
    }

    public int getIdSujetFAQ() {
        int _id = preferences.getInt("_idSujetFAQ", 0);

        return _id;
    }
}
