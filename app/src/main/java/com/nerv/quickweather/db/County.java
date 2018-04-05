package com.nerv.quickweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by NERV on 2017/12/4.
 * County表
 */

public class County extends DataSupport{

    private int id;

    private String countyName;

    private String weatherId;

    private int cityId;

    public int getId(){
        return id;
    }

    public void setId(){
        this.id=id;
    }

    public String getCountyName(){
        return countyName;
    }

    public void setCountyName(String countyName){
        this.countyName=countyName;
    }

    public String getWeatherId(){
        return weatherId;
    }

    public void setWeatherId(String weatherId){
        this.weatherId=weatherId;
    }

    public int getCityId(){
        return cityId;
    }

    public void setCityId(int cityId){
        this.cityId=cityId;
    }



}
