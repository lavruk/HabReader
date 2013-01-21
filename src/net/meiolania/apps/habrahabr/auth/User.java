package net.meiolania.apps.habrahabr.auth;

import net.meiolania.apps.habrahabr.Preferences;
import android.content.Context;

public class User {
    private static User instance = null;
    private String login;
    private String phpsessid;
    private String hsecid;
    private boolean isLogin = false;

    private User(Context context) {
	Preferences preferences = Preferences.getInstance(context);
	login = preferences.getLogin();
	phpsessid = preferences.getPHPSessionId();
	hsecid = preferences.getHSecId();
	
	//TOOD: think more about this?
	if(login != null && phpsessid != null && hsecid != null)
	    isLogin = true;
    }

    public static User getInstance(Context context) {
	if (instance == null)
	    instance = new User(context);
	return instance;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getPhpsessid() {
	return phpsessid;
    }

    public void setPhpsessid(String phpsessid) {
	this.phpsessid = phpsessid;
    }

    public String getHsecid() {
	return hsecid;
    }

    public void setHsecid(String hsecid) {
	this.hsecid = hsecid;
    }

    public boolean isLogin() {
	return isLogin;
    }

    public void setLogin(boolean isLogin) {
	this.isLogin = isLogin;
    }

}