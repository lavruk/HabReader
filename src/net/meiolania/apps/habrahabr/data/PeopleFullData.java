/*
Copyright 2012 Andrey Zaytsev

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

package net.meiolania.apps.habrahabr.data;

public class PeopleFullData extends PeopleData{
    protected String fullname;
    protected String birthday;
    protected String summary;
    protected String interests;

    public String getFullname(){
        return fullname;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getSummary(){
        return summary;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }

    public String getInterests(){
        return interests;
    }

    public void setInterests(String interests){
        this.interests = interests;
    }

}