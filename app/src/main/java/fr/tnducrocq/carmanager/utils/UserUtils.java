package fr.tnducrocq.carmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.tnducrocq.carmanager.model.beans.User;

/**
 * Created by tony on 09/04/2018.
 */
public class UserUtils {

    private static final String USER_PREFS_NAME = "USER_PREFS_NAME";
    private static final String USER_KEY = "USER";

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static User getCurrentUser(Context context) {
        String user = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE).getString(USER_KEY, null);
        if (user == null) {
            return null;
        }
        return gson.fromJson(user, User.class);
    }

    public static void saveCurrentUser(Context context, User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER_KEY, gson.toJson(user));
        editor.commit();
    }


}
