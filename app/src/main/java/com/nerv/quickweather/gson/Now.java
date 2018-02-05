package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NERV on 2017/12/5.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public Cond cond;

    public class Cond{

        @SerializedName("txt")
        public String condInfo;


    }

    @SerializedName("wind")
    public Wind wind;

    public class Wind{

        @SerializedName("sc")
        public String windInfo;
    }
}
