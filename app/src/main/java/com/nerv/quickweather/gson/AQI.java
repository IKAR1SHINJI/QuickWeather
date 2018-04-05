package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NERV on 2017/12/5.
 */

public class AQI {

//    public AQICity city;
//
//    public class AQICity{
//        public String aqi;
//
//        public String pm25;
//
//        public String pm10;
//    }

    public String status;

    @SerializedName("air_now_city")
    public AirQuality airquality;

    public class AirQuality {

        @SerializedName("aqi")
        public String aqi_value;

        @SerializedName("qlty")
        public String aqi_text;

        public String pm25;

        public String pm10;


    }

}
