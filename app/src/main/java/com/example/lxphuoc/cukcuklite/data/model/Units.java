package com.example.lxphuoc.cukcuklite.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;


public class Units implements ISqlObject {

    private int unitId;

    private String unitName;

    public Units() {
    }

    public Units(Builder builder) {
        setUnitId(builder.unitId);
        setUnitName(builder.unitName);
    }

    public void fromUnit(Units unit){
        setUnitId(unit.getUnitId());
        setUnitName(unit.getUnitName());
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(SqlConstant.UNIT_NAME, getUnitName());
        return values;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor.getColumnIndex(SqlConstant.UNIT_ID) > -1){
            setUnitId(cursor.getInt(cursor.getColumnIndex(SqlConstant.UNIT_ID)));
        }
        if(cursor.getColumnIndex(SqlConstant.UNIT_NAME) > -1){
            setUnitName(cursor.getString(cursor.getColumnIndex(SqlConstant.UNIT_NAME)));
        }
    }

    public static  class Builder{

        private int unitId;

        private String unitName;


        public Builder setUnitId(int unitId) {
            this.unitId = unitId;
            return this;
        }

        public Builder setUnitName(String unitName) {
            this.unitName = unitName;
            return this;
        }

        public Units build(){
            return new Units(this);
        }
    }
}
