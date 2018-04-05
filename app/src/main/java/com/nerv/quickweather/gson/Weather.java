package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by NERV on 2017/12/5.
 */

public class Weather {

    public String status;

    public Basic basic;

    public Update update;

//    public AQI aqi;

    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;
}
