package com.example.lxphuoc.cukcuklite.data;


import android.util.Log;

import com.example.lxphuoc.cukcuklite.data.dao.ProductDAO;
import com.example.lxphuoc.cukcuklite.data.model.Products;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class được tạo ra cung cấp các phương thức thực hiện trao đổi dữ liệu danh sách sản phẩm với các presenter
 * (Các phương thức được implements từ interface IProductDataSource)
 */

public class ProductRespository implements IProductDataSource {

    private static final String TAG = "ProductRespository";

    private static ProductRespository ourInstance = null;

    private ProductDAO mProductDAO;

    private Map<Integer, Products> mProductCache;

    private String mIconDefaults [];

    public static ProductRespository getInstance() {
        if(ourInstance == null){
            ourInstance = new ProductRespository();
        }
        return ourInstance;
    }

    private ProductRespository() {
        mProductDAO = new ProductDAO();
        mProductCache = new LinkedHashMap<>();
        mIconDefaults = new String[]{};
    }

    public String[] getIconDefaults() {
        return mIconDefaults;
    }

    /**
    * Phương thức gán giá trị cho mảng cache danh sách icon mặc định cho sản phẩm
    *
    * @param icons Mảng dạng chuỗi danh sách đường dẫn icon mặc định lấy từ file assets
    * @created_by lxphuoc on 3/25/2019
    */
    public void setIconDefaults(String[] icons){
        mIconDefaults = icons;
    }

    /**
     * Phương thức thêm mới sản phẩm
     *
     * @param product - Thông tin sản phẩm thêm mới
     * @return - Trả về id của dòng vừa thêm mới
     * (Có thể trả về - 1 khi có lỗi xảy ra )
     */
    @Override
    public long addNewProduct(Products product) {
        if(product == null || product.getProductName().isEmpty()){
            return -1;
        }

        long rowId = mProductDAO.addNewProduct(product);

        if(rowId > 0){
            Log.d(TAG, "addNewProduct: " + rowId);
            Products productDatabase = getProductInfo((int)rowId);
            if(productDatabase != null){
                addProductToCache(productDatabase);
            }

            return rowId;
        }

        return -1;
    }

    /**
     * Phương thức lấy thông tin sản phẩm từ mã sản phẩm
     *
     * @param productId - Mã sản phẩm
     * @return - Trả về thông tin sản phẩm tại
     * (Có thể trả về null khi thao tác với cơ sở dữ liệu xả ra lỗi hoặc mã sản phẩm không tồn )
     */
    @Override
    public Products getProductInfo(int productId) {
        if(productId < 1){
            return null;
        }
        try {
            if(mProductCache.size() > 0 && mProductCache.containsKey(productId)){ // Lấy đối tượng trong cache
                return mProductCache.get(productId);
            }else {
                Products productDatabase = mProductDAO.getProductInfo(productId);
                if(productDatabase != null){
                    addProductToCache(productDatabase);
                    return productDatabase;
                }else{
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức cập nhật thông tin sản phẩm
     *
     * @param productId - mã sản phẩm cần cập nhật
     * @param product - Thông tin sản phẩm muốn cập nhật
     * @return Trả về TRUE khi cập nhật thành công và FALSE khi có lỗi xảy ra
     */
    @Override
    public boolean updateProductInfo(int productId, Products product) {
        if(productId < 1 || product == null){
            return false;
        }

        try {

            boolean respone = mProductDAO.updateProductInfo(productId,product);

            if(respone) {
                Products productDatabase = mProductDAO.getProductInfo(productId);
                if (mProductCache.containsKey(productId)) {
                    Products p = mProductCache.get(productId);
                    if (p != null) {
                        p.fromProducts(productDatabase);
                    }
                } else {
                    addProductToCache(productDatabase);
                }
            }

            return respone;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức xoá 1 sản phẩm trong cơ sở dữ liệu
     *
     * @param productId - mã sản phẩm
     * @return - Trả về TRUE khi cập nhật thành công và FALSE khi có lỗi xảy ra
     */
    @Override
    public boolean deleteProduct(int productId) {
        if(productId < 1){
            return false;
        }

        try {
            boolean response = mProductDAO.deleteProduct(productId);

            if(response){
                mProductCache.remove(productId);
            }
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    /**
     * Phương thức lấy danh sách sản phẩm từ cơ sở dữ liệu
     *
     * @return - Trả về 1 danh sách sản phẩm
     * (Có thể trả về null khi danh sách rỗng hoặc thao tác với cơ sở dữ liệu xảy ra lỗi)
     */
    @Override
    public ArrayList<Products> getListProducts() {
        if(mProductCache.size() > 0){
            return new ArrayList<>(mProductCache.values());
        }else{
            try {
                ArrayList<Products> products = mProductDAO.getListProducts();

                if(products != null && products.size() > 0){
                    mProductCache.clear();
                    for (Products p: products) {
                        addProductToCache(p);
                    }
                }
                return products;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }
    }
    /**
     * Phương thức lưu 1 sản phẩm vào bộ nhớ đệm
     * @param product - Sản phẩm
     *             @created_by lxphuoc on 3/28/2019
     */
    private void addProductToCache(Products product){
        mProductCache.put(product.getProductId(),product);
    }

    /**
     * Phương thức thông tin sản phẩm từ tên sản phẩm
     * @param productName - tên sản phẩm
     * @return Trả về 1 đối tượng products - Có thể trả về null nếu đối tượng không tồn tại
     * (Có thể trả về null khi có lỗi truy vấn hoặc danh sách rỗng)
     */

    @Override
    public Products getProductInfoFromProductName(String productName) {
        if(productName.isEmpty()){
            return null;
        }
        return mProductDAO.getProductInfoFromProductName(productName);
    }
}
