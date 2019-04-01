package com.example.lxphuoc.cukcuklite.data.dao;

import android.database.Cursor;

import com.example.lxphuoc.cukcuklite.data.IProductDataSource;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqliteUtils;
import com.example.lxphuoc.cukcuklite.data.model.Products;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

/**
 * Class được tạo ra để thực hiện các thao tác truy vấn vào bảng products trong cơ sở dữ liệu
 * (Các phương thức được implements từ interface IProductDataSource)
 */

public class ProductDAO implements IProductDataSource {

    public static final String TABLE_NAME = "products";

    public static final String CREATE_TABLE_SQL =
            String.format("CREATE TABLE " + TABLE_NAME + "(" +
                            "%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT NOT NULL," +
                            "%s REAL DEFAULT 0," +
                            "%s INTEGER NOT NULL," +
                            "%s TEXT," +
                            "%s TEXT," +
                            "%s INTEGER DEFAULT 1)"
                    ,
                    SqlConstant.PRODUCT_ID,
                    SqlConstant.PRODUCT_NAME,
                    SqlConstant.PRODUCT_PRICE,
                    SqlConstant.PRODUCT_UNIT,
                    SqlConstant.PRODUCT_COLOR,
                    SqlConstant.PRODUCT_THUMBNAIL,
                    SqlConstant.PRODUCT_STATUS
            );


    public ProductDAO() {

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
        if (product == null) {
            return -1;
        }
        try {
            return SqliteUtils.getInstance().addNewRecord(TABLE_NAME, product.toContentValues());
        } catch (Exception e) {
            e.printStackTrace();
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
        if (productId < 1) {
            return null;
        }
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +SqlConstant.PRODUCT_ID + " = '" +productId+ "' LIMIT 1";
            Cursor cursor = SqliteUtils.getInstance().rawQuery(sql, null);
            if (cursor != null) {
                cursor.moveToFirst();
                Products product = new Products();
                product.fromCursor(cursor);
                return product;
            } else {
                return null;
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
     * @param product   - Thông tin sản phẩm muốn cập nhật
     * @return Trả về TRUE khi cập nhật thành công và FALSE khi có lỗi xảy ra
     */
    @Override
    public boolean updateProductInfo(int productId, Products product) {
        if (product == null || productId < 1) {
            return false;
        }
        try {
            int rowId = SqliteUtils.getInstance().updateRecord(
                    TABLE_NAME, product.toContentValues(),
                    SqlConstant.PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

            // Cập nhật thành công trả về id của hàng đã cập nhật
            return rowId > 0;

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
        if (productId < 1) {
            return false;
        }
        try {
            int rowId = SqliteUtils.getInstance().deleteRecord(TABLE_NAME, SqlConstant.PRODUCT_ID + " =?", new String[]{String.valueOf(productId)});
            // Xoá thành công trả về id của hàng đã cập nhật
            return rowId > 0;
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
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;

            Cursor cursor = SqliteUtils.getInstance().rawQuery(sql, null);

            if (cursor != null) {
                cursor.moveToFirst();
                ArrayList<Products> products = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    Products product = new Products();
                    product.fromCursor(cursor);
                    products.add(product);
                    cursor.moveToNext();
                }
                return products;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Products getProductInfoFromProductName(String productName) {
        if (productName.isEmpty()) {
            return null;
        }
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE lower(" + SqlConstant.PRODUCT_NAME + ") = '" + productName.toLowerCase() + "'";

            Cursor cursor = SqliteUtils.getInstance().rawQuery(sql,null);

            if(cursor != null){
                cursor.moveToFirst();
                Products product = new Products();
                product.fromCursor(cursor);
                return product;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
