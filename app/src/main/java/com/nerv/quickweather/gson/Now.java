package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NERV on 2017/12/5.
 */

public class Now {

    //温度
    @SerializedName("tmp")
    public String temperature;

    //体感温度
    @SerializedName("fl")
    public String feelTemperature;


    @SerializedName("cond")
    public Cond cond;
    public class Cond{
        //天气
        @SerializedName("txt")
        public String condInfo;
    }


    @SerializedName("wind")
    public Wind wind;
    public class Wind{
        //风力
        @SerializedName("sc")
        public String windlv;
        //风速
        @SerializedName("spd")
        public String windSpd;
        //风向
        @SerializedName("dir")
        public String windDir;
    }
}
