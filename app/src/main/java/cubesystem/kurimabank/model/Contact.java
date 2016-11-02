package cubesystem.kurimabank.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kurimabank-kurima on 2016/03/22.
 */
public class Contact {

    private final static String CONTACT_PREF_ROOT_KEY_CC = "contact_sendtype_";
    private final static String CONTACT_PREF_ROOT_KEY_NAME = "contact_name_";
    private final static String CONTACT_PREF_ROOT_KEY_EMAIL = "contact_email_";

    public SendType sendType;
    public String name;
    public String email;
    private String mSubKey;

    public Contact(String subKey, Contact.SendType st){
        mSubKey = subKey;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        this.sendType = SendType .toSendType(preferences.getString(CONTACT_PREF_ROOT_KEY_CC + mSubKey,st.name()));
        this.name = preferences.getString(CONTACT_PREF_ROOT_KEY_NAME + mSubKey, "");
        this.email = preferences.getString(CONTACT_PREF_ROOT_KEY_EMAIL + mSubKey, "");
    }

    public  enum SendType {
        TO, CC;

        public static SendType toSendType(String value){
            try{
                return valueOf(value);
            }
            catch (Exception e){
                return SendType.TO;
            }
        }
    }

    public void saveToPreference() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CONTACT_PREF_ROOT_KEY_CC + mSubKey, sendType.name());
        editor.putString(CONTACT_PREF_ROOT_KEY_NAME + mSubKey, name);
        editor.putString(CONTACT_PREF_ROOT_KEY_EMAIL + mSubKey, email);
        editor.commit();
    }


}


