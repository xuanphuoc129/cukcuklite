package com.example.lxphuoc.cukcuklite;

import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * ‐ Class được tạo ra dể người dùng đăng nhập vào hệ thống
 * <p>
 * ‐ @created_by lxphuoc on 3/25/2019.
 * ‐ @modified_by lxphuoc on 3/25/2019.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

//    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
     * Phương thức chuyển ảnh thành bitmap từ đường dẫn file trong assets
     *
     * @param fileName Tên file ảnh
     * @return giải thích hàm này trả về
     * @created_by lxphuoc on 3/25/2019
     */
    public Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(AppConstant.THUMBNAIL_ASSETS + "/" + fileName);
//                istr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(istr);
    }

    /**
     * Hiển thị toast lên màn hình
     *
     * @param message - Thông điệp muốn hiển thị
     *                - @created_by lxphuoc on 3/14/2019
     *                - @modified_by lxphuoc on 3/14/2019 ‐ Cập nhật, bổ sung
     */
    public void showToast(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
