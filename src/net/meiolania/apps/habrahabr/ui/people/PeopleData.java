/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
        http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.meiolania.apps.habrahabr.ui.people;

public class PeopleData{
    private String name;
    private String karma;
    private String rating;
    private String link;
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setKarma(String karma){
        this.karma = karma;
    }
    
    public String getKarma(){
        return karma;
    }
    
    public void setRating(String rating){
        this.rating = rating;
    }
    
    public String getRating(){
        return rating;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public String getLink(){
        return link;
    }
    
}