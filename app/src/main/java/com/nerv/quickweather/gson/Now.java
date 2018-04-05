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


    @SerializedName("cond_txt")
    public String condInfo;

    //风力
    @SerializedName("wind_sc")
    public String windlv;
    //风速
    @SerializedName("wind_spd")
    public String windSpd;
    //风向
    @SerializedName("wind_dir")
    public String windDir;
}
