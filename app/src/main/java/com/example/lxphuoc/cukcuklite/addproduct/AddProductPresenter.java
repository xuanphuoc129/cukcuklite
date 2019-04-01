package com.example.lxphuoc.cukcuklite.addproduct;

import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.data.ProductRespository;
import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.model.Products;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

/**
 * ‐ Presenter trong mô hình MVP Thêm mới sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 * ‐ @modified_by lxphuoc on 3/23/2019 ‐ Diễn giải thay đổi
 */

public class AddProductPresenter implements IAddProductContract.IPresenter {

    private IAddProductContract.IView mView;

    AddProductPresenter(IAddProductContract.IView mView) {
        this.mView = mView;
    }

    /**
     * Phương thức thêm sản phẩm mới
     *
     * @param product Thông tin sản phẩm gửi vào từ giao diện
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void addProduct(Products product) {
        if (product == null) {
            mView.onAddNewError();
            return;
        }

        if(product.getProductName().isEmpty()){
            mView.showNameIsEmptyError();
            return;
        }

        try {
            Products productDatabase = ProductRespository.getInstance().getProductInfoFromProductName(product.getProductName());
            if(productDatabase == null){
                long rowId = ProductRespository.getInstance().addNewProduct(product);
                if (rowId > 0) {
                    mView.onAddNewSuccess();
                } else {
                    mView.onAddNewError();
                }
            }else{
                mView.showNameIsExistError();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức thiết lập thông tin mặc định cho sản phẩm khi bắt đầu thêm mới
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void setDefaultCreate() {
        Products product = new Products.Builder()
                .setProductColor(AppConstant.COLOR_ARRAY[4])
                .setProductPrice(0)
                .setProductStatus(1)
                .build();
        ArrayList<Units> units = UnitRespository.getInstance().getListUnits();
        if (units != null && units.size() > 0) {
            product.setProductUnit(units.get(0).getUnitId());
        }

        String icons[] = ProductRespository.getInstance().getIconDefaults();
        if (icons != null && icons.length > 0) {
            product.setProductThumbnail(icons[icons.length - 1]);
        }
        mView.setProduct(product);
    }



}
