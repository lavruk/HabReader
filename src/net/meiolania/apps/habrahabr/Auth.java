package net.meiolania.apps.habrahabr;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Auth{
    public final static String LOG_TAG = "Auth";
    public final static String AUTH_URL = "http://habrahabr.ru/ajax/auth/";

    public static String doAuth(Context context, String login, String password, String captcha){
        try{
            Connection.Response auth = Jsoup.connect(AUTH_URL).
                                       data("act", "login").
                                       data("redirect_url", "http://habrahabr.ru").
                                       data("login", login).
                                       data("password", password).
                                       data("captcha", captcha).
                                       referrer("http://habrahabr.ru/login/").
                                       method(Method.POST).
                                       execute();
            Document document = auth.parse();
            Element error = document.select("error").first();
            if(error == null){
                Log.i(LOG_TAG, "Logged in");
                
                Preferences preferences = Preferences.getInstance(context);
                preferences.setAuthData(auth.cookie(Preferences.HSEC_ID), auth.cookie(Preferences.SESSION_ID));
                Toast.makeText(context, R.string.logged_in, Toast.LENGTH_SHORT).show();
            }else{
                Log.d(LOG_TAG, "<error> element: " + error.text());
                Log.d(LOG_TAG, "<act> element: " + document.select("act").first().text());
                
                return error.text();
            }    
        }
        catch(IOException e){
            return e.getMessage();
        }
        return "None";
    }

}