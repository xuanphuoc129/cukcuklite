package com.example.lxphuoc.cukcuklite.splash;


import android.os.Bundle;

import com.example.lxphuoc.cukcuklite.BaseActivity;
import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.menulist.MenuListActivity;

/**
 * ‐ Màn hình loading khi vào ứng dụng. Thực hiện lấy trước dữ liệu mặc định từ cơ sở dữ liệu
 * Ví dụ: Danh sách thực đơn, danh sách order ..
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 */

public class SplashActivity extends BaseActivity implements ISplashContract.IView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ISplashContract.IPresenter mSplashPresenter = new SplashPresenter(this);

        mSplashPresenter.onLoadData(this);
    }

    /**
     * Phương thức chuyển sang màn hình chính
     * @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void openMainActivity() {
        navigateToActivity(this, MenuListActivity.class,null);
        finish();
    }

//    void setColor(int color){
//        try {
//            Drawable drawable = getResources().getDrawable(R.drawable.bg_circle);
//            if(color == 1){
//                drawable.setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC);
//                rlColor1.setBackground(drawable);
//            }else{
//                drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC);
//                rlColor2.setBackground(drawable);
//            }
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        }
//    }




}
