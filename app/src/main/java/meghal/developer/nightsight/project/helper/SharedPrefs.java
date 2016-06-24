package meghal.developer.nightsight.project.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Meghal on 6/9/2016.
 */
public class SharedPrefs {

    // Shared preferences file name
    public static final String PREF_NAME = "NiceEyesData";
    public static final String KEY_BRIGHTNESS = "brighteness";
    public static final String KEY_IS_START = "ServiceStart";
    public static final String KEY_SHOW_NOTIFICATION = "KeyNotification";
    public static final String KEY_COLOR_ID = "ColorIdKey";

    public static String TAG = "Shared Preference";
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SharedPrefs(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setShowNotification(boolean isLoggedIn) {
        editor.putBoolean(KEY_SHOW_NOTIFICATION, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean showNotification() {
        return pref.getBoolean(KEY_SHOW_NOTIFICATION, false);
    }

    public void setService(boolean service) {
        editor.putBoolean(KEY_IS_START, service);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isService() {
        return pref.getBoolean(KEY_IS_START, false);
    }

    public int getColor() {
        return pref.getInt(KEY_COLOR_ID, 0);
    }

    public void setColorId(int colorId) {

        editor.putInt(KEY_COLOR_ID, colorId);
        editor.commit();
    }

    public int getBrightness() {
        return pref.getInt(KEY_BRIGHTNESS, 0);
    }

    public void setBrightness(int brightness) {

        editor.putInt(KEY_BRIGHTNESS, brightness);
        editor.commit();
    }
}
