package cubesystem.kurimabank.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyContact {
    private final static String 
            MY_CONTACT_PREF_ROOT_KEY_INTRODUCE = "my_contact_introduce_";
    public String introduce;
    private String mSubKey;

    public MyContact(String subKey) {
        mSubKey = subKey;
        SharedPreferences preferences = 
                PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        this.introduce = 
                preferences.getString(MY_CONTACT_PREF_ROOT_KEY_INTRODUCE + mSubKey, "");
    }

    public void saveToPreference() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MY_CONTACT_PREF_ROOT_KEY_INTRODUCE + mSubKey, introduce);
        editor.commit();
    }
}
