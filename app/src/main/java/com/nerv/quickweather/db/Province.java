package com.nerv.quickweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by NERV on 2017/12/4.
 * Province表
 */

public class Province extends DataSupport{

    private int id;

    private String provinceName;

    private int provinceCode;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getProvinceName(){
        return provinceName;
    }

    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }

    public int getProvinceCode(){
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode){
        this.provinceCode=provinceCode;
    }
}
