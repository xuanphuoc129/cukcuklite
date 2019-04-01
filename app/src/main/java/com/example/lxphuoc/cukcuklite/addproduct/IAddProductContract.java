package com.example.lxphuoc.cukcuklite.addproduct;

import com.example.lxphuoc.cukcuklite.data.model.Products;

/**
 * ‐ View trong mô hình MVP Thêm mới và chỉnh sửa sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 */

public interface IAddProductContract {

    interface IView {

        void setProduct(Products product);

        void onAddNewSuccess();

        void onAddNewError();

        void showKeyboardDialog();

        void showColorDialog();

        void showIconDialog();

        void openUnitActivity();

        void showNameIsEmptyError();

        void showNameIsExistError();
    }

    interface IPresenter {

        void addProduct(Products product);

        void setDefaultCreate();
    }

}
