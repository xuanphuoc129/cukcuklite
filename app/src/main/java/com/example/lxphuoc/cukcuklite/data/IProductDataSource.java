package com.example.lxphuoc.cukcuklite.data;


import com.example.lxphuoc.cukcuklite.data.model.Products;

import java.util.ArrayList;

public interface IProductDataSource {

    long addNewProduct(Products product);

    Products getProductInfo(int productId);

    Products getProductInfoFromProductName(String productName);

    boolean updateProductInfo(int productId, Products product);

    boolean deleteProduct(int productId);

    ArrayList<Products> getListProducts();

}
