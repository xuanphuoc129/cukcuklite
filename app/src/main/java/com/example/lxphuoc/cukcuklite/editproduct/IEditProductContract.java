package com.example.lxphuoc.cukcuklite.editproduct;

import com.example.lxphuoc.cukcuklite.data.model.Products;

/**
 * ‐ View trong mô hình MVP Thêm mới và chỉnh sửa sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 */

public interface IEditProductContract {

    interface IView {
        void setProduct(Products product);

        void updateProductPrice();

        void updateProductUnit();

        void updateProductColor();

        void updateProductThumbnail();

        void onUpdateSuccess();

        void onUpdateError();

        void onDeleteSuccess();

        void onDeleteError();

        void showKeyboardDialog();

        void showColorDialog();

        void showIconDialog();

        void openUnitActivity();

        void destroy();

        void showConfirmDialog();

        void showCantLoadProduct();

        void showNameIsExistError();
    }

    interface IPresenter {

        void deleteProduct();

        void getProductInfo(int productId);

        void updateProduct(Products product);

    }

}
