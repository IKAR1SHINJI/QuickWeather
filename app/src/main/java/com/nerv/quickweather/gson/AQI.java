package com.nerv.quickweather.gson;

/**
 * Created by NERV on 2017/12/5.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;

        public String pm25;
    }
}