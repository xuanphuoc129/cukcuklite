package com.example.lxphuoc.cukcuklite.editproduct;

import com.example.lxphuoc.cukcuklite.data.ProductRespository;
import com.example.lxphuoc.cukcuklite.data.model.Products;


/**
 * ‐ Presenter trong mô hình MVP Thêm mới và chỉnh sửa sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 * ‐ @modified_by lxphuoc on 3/23/2019 ‐ Diễn giải thay đổi
 */

public class EditProductPresenter implements IEditProductContract.IPresenter {

    private IEditProductContract.IView mView;

    private Products mProduct;

    EditProductPresenter(IEditProductContract.IView mView) {
        this.mView = mView;
        mProduct = new Products();
    }

    /**
     * Phương thức xóa sản phẩm
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void deleteProduct() {
        if (mProduct.getProductId() > 0) {
            boolean response = ProductRespository.getInstance().deleteProduct(mProduct.getProductId());
            if (response) {
                mView.onDeleteSuccess();
            } else {
                mView.onDeleteError();
            }
        } else {
            mView.onDeleteError();
        }
    }
    /**
     * Phương thức lấy thông tin sản phẩm từ mã sản phẩm
     *
     * @param productId Mã sản phẩm
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void getProductInfo(int productId) {
        if (productId > 0) {
            mProduct.setProductId(productId);
            Products product = ProductRespository.getInstance().getProductInfo(productId);
            if (product != null) {
                mProduct.fromProducts(product);
                mView.setProduct(product);
            } else {
                mView.showCantLoadProduct();
            }
        } else {
            mView.showCantLoadProduct();
        }

    }

    /**
     * Phương thức cập nhật thông tin sản phẩm
     *
     * @param product Thông tin sản phẩm gửi vào từ giao diện
     * @created_by lxphuoc on 3/27/2019
     */
    public void updateProduct(Products product) {
        if (product == null || product.getProductName().isEmpty()) {
            mView.onUpdateError();
            return;
        }
        try {
            if (checkChangeData(product)) {
                Products productUpdate = new Products.Builder()
                        .setProductName(!product.getProductName().equals(mProduct.getProductName()) ? product.getProductName() : "")
                        .setProductUnit(product.getProductUnit() != mProduct.getProductId() ? product.getProductUnit() : 0)
                        .setProductPrice(product.getProductPrice() != mProduct.getProductPrice() ? product.getProductPrice() : 0)
                        .setProductColor(!product.getProductColor().equals(mProduct.getProductColor()) ? product.getProductColor() : "")
                        .setProductThumbnail(!product.getProductThumbnail().equals(mProduct.getProductThumbnail()) ? product.getProductThumbnail() : "")
                        .setProductStatus(product.getProductStatus() != mProduct.getProductStatus() ? product.getProductStatus() : 0)
                        .build();
                if(!productUpdate.getProductName().isEmpty()){
                    Products productCheck = ProductRespository.getInstance().getProductInfoFromProductName(productUpdate.getProductName());
                    if(productCheck != null && mProduct.getProductId() != productCheck.getProductId()){
                        mView.showNameIsExistError();
                    }else{
                        boolean response = ProductRespository.getInstance().updateProductInfo(product.getProductId(), productUpdate);
                        if (response) {
                            mView.onUpdateSuccess();
                        } else {
                            mView.onUpdateError();
                        }
                    }
                }else{
                    boolean response = ProductRespository.getInstance().updateProductInfo(product.getProductId(), productUpdate);
                    if (response) {
                        mView.onUpdateSuccess();
                    } else {
                        mView.onUpdateError();
                    }
                }

            } else {
                mView.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức kiểm tra xem thông tin sản phẩm có thay đổi hay không
     *
     * @param product Thông tin sản phẩm gửi vào từ giao diện
     * @return True - Nếu người dùng đã thay đổi - False nếu người dùng chưa thay đổi gì
     * @created_by lxphuoc on 3/27/2019
     */
    private boolean checkChangeData(Products product) {
        return !product.getProductName().equals(mProduct.getProductName()) || !product.getProductColor().equals(mProduct.getProductColor()) || !product.getProductThumbnail().equals(mProduct.getProductThumbnail()) || product.getProductUnit() != mProduct.getProductId() || product.getProductPrice() != mProduct.getProductPrice() || product.getProductStatus() != mProduct.getProductStatus();
    }


}
