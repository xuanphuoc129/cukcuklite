package com.example.lxphuoc.cukcuklite.menulist;

import com.example.lxphuoc.cukcuklite.data.ProductRespository;
import com.example.lxphuoc.cukcuklite.data.model.Products;

import java.util.ArrayList;

/**
 * ‐ Presenter trong mô hình MVP cho màn hình Danh sách thực đơn
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 */

public class MenuListPresenter implements IMenuListContract.IPresenter {

    private IMenuListContract.IView mView;

    MenuListPresenter(IMenuListContract.IView mView) {
        this.mView = mView;
    }

    /**
     * Phương thức lấy danh sách sản phẩm
     *
     * @created_by lxphuoc on 3/25/2019
     */
    @Override
    public void getListProduct() {
        ArrayList<Products> products = ProductRespository.getInstance().getListProducts();
        if (products != null && products.size() > 0) {
            mView.setProducts(products);
        } else {
            mView.setProducts(new ArrayList<Products>());
            mView.showEmptySate();
        }
    }
}
