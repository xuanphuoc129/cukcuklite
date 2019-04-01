package com.example.lxphuoc.cukcuklite.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.lxphuoc.cukcuklite.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * ‐ Class được tạo ra để cài đặt sẵn 1 vài phương thức thường được sử dụng phổ biến
 * trong tất cả ứng dụng
 * <p>
 * ‐ @created_by lxphuoc on 3/18/2019
 * ‐ @modified_by lxphuoc on 3/18/2019 ‐ Diễn giải thay đổi
 */

public class CommonUtils {

    private static final CommonUtils ourInstance = new CommonUtils();

    private static final String USER_NAME = "^[a-z0-9_-]{3,15}$";

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,15}$";

    private ProgressDialog mProgressDialog;

    /**
     * Phương thức khởi tạo đối tượng theo singleton design pattern
     *
     * @return trả về 1 đối tượng CommonUtils
     * @created_by lxphuoc on 3/18/2019
     * @modified_by lxphuoc on 3/18/2019 ‐ Cập nhật, bổ sung
     */
    public static CommonUtils getInstance() {
        return ourInstance;
    }


    private CommonUtils() {

    }

    /**
     * Kiểm tra điều kiện tên đăng nhập người dùng
     * Điều kiện: Tên đăng nhập có từ 3 đến 15 ký tự gồm: chữ cái, số, dấu gạch dưới.
     *
     * @param username - Tên đăng nhập người dùng
     * @return giải thích hàm này trả về
     * @created_by lxphuoc on 3/18/2019
     * @modified_by lxphuoc on 3/18/2019 ‐ Cập nhật, bổ sung
     */
    public boolean isVaLidUsername(String username) {
        return username.matches(USER_NAME);
    }

    /**
     * Kiểm tra điều kiện tên đăng nhập người dùng
     * Điều kiện: Mật khẩu có từ 6 đến 15 ký tự gồm: chữ cái, số, chữ viết hoa, không chứa dấu cách.
     *
     * @param password - Mật khẩu đăng nhập người dùng
     * @return giải thích hàm này trả về
     * @created_by lxphuoc on 3/18/2019
     * @modified_by lxphuoc on 3/18/2019 ‐ Cập nhật, bổ sung
     */
    public boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    /**
     * Phương thức chuyển activity
     *
     * @param startActivity - Activity đang chạy
     * @param endActitity   - Activity muốn chuyển đến
     * @param params        - Params muốn gửi sang
     *                      - @created_by lxphuoc on 3/13/2019
     *                      - @modified_by lxphuoc on 3/13/2019 ‐ Cập nhật, bổ sung
     */
    public void navigateToActivity(Context startActivity, Class endActitity, @Nullable Bundle params) {
        try {
            Intent intent = new Intent(startActivity, endActitity);
            if (params != null) {
                intent.putExtras(params);
            }

            startActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Chuyển sang activity khác và nhận thông điệp trả về trừ activity đã chuyển tới
     *
     * @param startActivity - Activity đang chạy
     * @param endActitity   - Activity muốn chuyển đến
     * @param params        - Params muốn gửi sang
     *                      - @created_by lxphuoc on 3/13/2019
     *                      - @modified_by lxphuoc on 3/13/2019 ‐ Cập nhật, bổ sung
     */
    public void navigateToActivityReceiveResult(Activity startActivity, Class endActitity, int reuestCode, @Nullable Bundle params) {
        try {
            Intent intent = new Intent(startActivity, endActitity);
            if (params != null) {
                intent.putExtras(params);
            }
            startActivity.startActivityForResult(intent, reuestCode, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức dùng để đọc file Json từ thư mục assets
     *
     * @param context  - Context
     * @param fileName - Tên file
     *                 - @created_by lxphuoc on 3/13/2019
     *                 - @modified_by lxphuoc on 3/13/2019 ‐ Cập nhật, bổ sung
     */
    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);

            int size = inputStream.available();

            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            inputStream.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

//    /**
//     * Hiển thị toast lên màn hình
//     *
//     * @param context -  Màn hình muốn hiển thị
//     * @param message - Thông điệp muốn hiển thị
//     *                - @created_by lxphuoc on 3/14/2019
//     *                - @modified_by lxphuoc on 3/14/2019 ‐ Cập nhật, bổ sung
//     */
//    public void showToast(Context context, String message) {
//        try {
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Hiển thị loading lên màn hình
//     *
//     * @param context Màn hình muốn hiển thị
//     * @created_by lxphuoc on 3/19/2019
//     * @modified_by lxphuoc on 3/19/2019 ‐ Cập nhật, bổ sung
//     */
//    public void showProgressLoadding(Context context, @Nullable String message) {
//        try {
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//                mProgressDialog = null;
//            }
//            mProgressDialog = new ProgressDialog(context);
//            mProgressDialog.setMessage(message == null ? context.getString(R.string.loading_message) : message);
//            mProgressDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Ẩn dialog loading trên màn hình
//     *
//     * @created_by lxphuoc on 3/19/2019
//     * @modified_by lxphuoc on 3/19/2019 ‐ Cập nhật, bổ sung
//     */
//    public void hideLoading() {
//        try {
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//                mProgressDialog = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
