package com.example.mobilecomp4b.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {

    private static final String PREF_NAME = "RentEaseUserSession";
    private static final String KEY_USER_ROLE = "user_role"; // "TENANT" or "OWNER"
    private static final String KEY_USER_NAME = "user_name";

    public static final String ROLE_TENANT = "TENANT";
    public static final String ROLE_OWNER = "OWNER";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        this.pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = pref.edit();
    }

    public void setSession(String role, String name) {
        editor.putString(KEY_USER_ROLE, role);
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getUserRole() != null && !getUserRole().isEmpty();
    }

    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, null);
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "isOwner".equals(getUserRole()) ? "Property Owner" : "Tenant User");
    }

    public boolean isOwner() {
        return ROLE_OWNER.equalsIgnoreCase(getUserRole());
    }

    public boolean isTenant() {
        return ROLE_TENANT.equalsIgnoreCase(getUserRole());
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
