package com.example.lxphuoc.cukcuklite.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;


public class Products implements ISqlObject {

    private int productId;

    private String productName;

    private int productUnit;

    private float productPrice;

    private String productColor;

    private String productThumbnail;

    private int productStatus;

    public Products() {
    }

    public void fromProducts(Products product) {
        setProductId(product.productId);
        setProductColor(product.productColor);
        setProductName(product.productName);
        setProductPrice(product.productPrice);
        setProductUnit(product.productUnit);
        setProductThumbnail(product.productThumbnail);
        setProductStatus(product.productStatus);
    }

    public Products(Builder builder) {
        setProductId(builder.productId);
        setProductColor(builder.productColor);
        setProductName(builder.productName);
        setProductPrice(builder.productPrice);
        setProductUnit(builder.productUnit);
        setProductThumbnail(builder.productThumbnail);
        setProductStatus(builder.productStatus);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(int productUnit) {
        this.productUnit = productUnit;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if(!productName.isEmpty()){
            values.put(SqlConstant.PRODUCT_NAME, getProductName());
        }
        if(productUnit > 0){
            values.put(SqlConstant.PRODUCT_UNIT, getProductUnit());
        }
        if(productPrice > 0){
            values.put(SqlConstant.PRODUCT_PRICE, getProductPrice());
        }
        if(!productColor.isEmpty()){
            values.put(SqlConstant.PRODUCT_COLOR, getProductColor());
        }
        if(!getProductThumbnail().isEmpty()){
            values.put(SqlConstant.PRODUCT_THUMBNAIL, getProductThumbnail());
        }
        if(getProductStatus() > 0){
            values.put(SqlConstant.PRODUCT_STATUS, getProductStatus());
        }
        return values;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_ID) > -1) {
            setProductId(cursor.getInt(cursor.getColumnIndex(SqlConstant.PRODUCT_ID)));
        }
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_NAME) > -1) {
            setProductName(cursor.getString(cursor.getColumnIndex(SqlConstant.PRODUCT_NAME)));
        }
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_PRICE) > -1) {
            setProductPrice(cursor.getFloat(cursor.getColumnIndex(SqlConstant.PRODUCT_PRICE)));
        }
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_UNIT) > -1) {
            setProductUnit(cursor.getInt(cursor.getColumnIndex(SqlConstant.PRODUCT_UNIT)));
        }
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_COLOR) > -1) {
            setProductColor(cursor.getString(cursor.getColumnIndex(SqlConstant.PRODUCT_COLOR)));
        }
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_THUMBNAIL) > -1) {
            setProductThumbnail(cursor.getString(cursor.getColumnIndex(SqlConstant.PRODUCT_THUMBNAIL)));
        }
        if (cursor.getColumnIndex(SqlConstant.PRODUCT_STATUS) > -1) {
            setProductStatus(cursor.getInt(cursor.getColumnIndex(SqlConstant.PRODUCT_STATUS)));
        }
    }

    public static class Builder {
        private int productId;

        private String productName;

        private int productUnit;

        private float productPrice;

        private String productColor;

        private String productThumbnail;

        private int productStatus;

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setProductUnit(int productUnit) {
            this.productUnit = productUnit;
            return this;
        }

        public Builder setProductPrice(float productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder setProductColor(String productColor) {
            this.productColor = productColor;
            return this;
        }

        public Builder setProductThumbnail(String productThumbnail) {
            this.productThumbnail = productThumbnail;
            return this;
        }

        public Builder setProductStatus(int productStatus) {
            this.productStatus = productStatus;
            return this;
        }

        public Products build() {
            return new Products(this);
        }
    }
}
