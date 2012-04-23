package net.meiolania.apps.habrahabr.data;

public class PeopleData{
    protected String rating;
    protected String karma;
    protected String url;
    protected String avatar;
    protected String name;
    protected String lifetime;

    public String getRating(){
        return rating;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public String getKarma(){
        return karma;
    }

    public void setKarma(String karma){
        this.karma = karma;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLifetime(){
        return lifetime;
    }

    public void setLifetime(String lifetime){
        this.lifetime = lifetime;
    }

}