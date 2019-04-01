package com.example.lxphuoc.cukcuklite.menulist;

import com.example.lxphuoc.cukcuklite.data.model.Products;

import java.util.ArrayList;

/**
 * ‐ View trong mô hình MVP cho màn hình Danh sách thực đơn
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 */

public interface IMenuListContract {

    interface IView{
        void setProducts(ArrayList<Products> products);

        void showEmptySate();
    }

    interface IPresenter{
        void getListProduct();
    }
}
