package net.meiolania.apps.habrahabr;

public final class Preferences{
    private static Preferences preferences = null;
    
    private Preferences(){}
    
    public static Preferences getInstance(){
        return preferences != null ? preferences : (preferences = new Preferences());
    }
    
}