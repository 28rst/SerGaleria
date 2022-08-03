package com.example.rafae.myappv4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by rafae on 18/04/2018.
 */

public class loginSession {

    private static final String NAME = "prolserhumLoginSession";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_AVATAR = "keyavatar";
    private static final String KEY_NIVEL_ACESSO = "keynivelacesso";
    private static final String KEY_NIVEL_PREMIUM = "keynicelpremium";
    private static final String KEY_IMAGEM_BACK = "keyimagemback";
    private static final String KEY_ID = "keyid";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_ID_ANIMAL= "keyidanimal";

    private static loginSession mInstance;
    private static Context mCtx;

    private loginSession(Context context) {
        mCtx = context;
    }

    public static synchronized loginSession getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new loginSession(context);
        }
        return mInstance;
    }

    public void userLogin(Utilizador user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getNomeUtilizador());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_AVATAR, user.getAvatar());
        editor.putInt(KEY_NIVEL_ACESSO, user.getNivelAcesso());
        editor.putInt(KEY_NIVEL_PREMIUM, user.getNivelPremium());
        editor.putString(KEY_IMAGEM_BACK, user.getAvatar());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.apply();
    }



    //Verificar se esta loginado
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public Utilizador getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return new Utilizador(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_AVATAR, null),
                sharedPreferences.getInt(KEY_NIVEL_ACESSO, -1),
                sharedPreferences.getInt(KEY_NIVEL_PREMIUM, -1),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_IMAGEM_BACK, null),
                sharedPreferences.getString(KEY_PASSWORD, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }
}
