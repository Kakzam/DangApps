package dang.program.id.dangapps;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created .
 */

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sharedPreference";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    //data yg mau disimpan.
    private static final String EMAIL = "email";
    //njajal
    private static final String IMAGE_ITEM = "image_item";
    private static final String TITLE_ITEM = "title_item";
    private static final String PRICE_ITEM = "price_item";
    private static final String QUANTITY = "quantity";

    public PrefManager(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(EMAIL, "");
    }

    //image
    public void setImageItem(String img_item) {
        editor.putString(IMAGE_ITEM, img_item);
        editor.commit();
    }

    public String getImageItem() {
        return pref.getString(IMAGE_ITEM, "");
    }

    //title
    public void setTitleItem(String title_item) {
        editor.putString(TITLE_ITEM, title_item);
        editor.commit();
    }

    public String getTitleItem() {
        return pref.getString(TITLE_ITEM, "");
    }

    //price
    public void setPriceItem(String price_item) {
        editor.putString(PRICE_ITEM, price_item);
        editor.commit();
    }

    public String getPriceItem() {
        return pref.getString(PRICE_ITEM, "");
    }

    //quantity
    public void setQuantity(String quantity) {
        editor.putString(QUANTITY, quantity);
        editor.commit();
    }

    public String getQuantity() {
        return pref.getString(QUANTITY, "");
    }
}
