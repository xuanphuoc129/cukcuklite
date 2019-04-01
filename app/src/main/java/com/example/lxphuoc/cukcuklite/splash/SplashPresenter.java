package com.example.lxphuoc.cukcuklite.splash;

/**
‐ Presenter trong mô hình MVP cho màn hình Splash
*
‐ @created_by lxphuoc on 3/23/2019
*/
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.Log;

import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.data.ProductRespository;
import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.io.IOException;
import java.util.ArrayList;

public class SplashPresenter implements ISplashContract.IPresenter {

    private ISplashContract.IView mView;

    public SplashPresenter(ISplashContract.IView mView) {
        this.mView = mView;

    }

    @Override
    public void onLoadData(Context context) {
        getAssetsThumbnail(context);
        getProductList();
        getUnit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.openMainActivity();
            }
        }, 1000);
    }

    /**
     * Phương thức load danh sách thumbnail của sản phẩm
     * @param context - Context
     */
    private void getAssetsThumbnail(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            // for assets/subFolderInAssets add only subfolder name
            String[] filelistInSubfolder = assetManager.list(AppConstant.THUMBNAIL_ASSETS);
            if (filelistInSubfolder != null) {
                ProductRespository.getInstance().setIconDefaults(filelistInSubfolder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức load danh sách sản phẩm
     */
    private void getProductList() {
        ProductRespository.getInstance().getListProducts();
    }

    /**
     * Phương thức load danh sách đơn vị tính
     */
    private void getUnit() {
        ArrayList<Units> units =  UnitRespository.getInstance().getListUnits();

        if(units == null || units.size() == 0){  // Thêm giá trị mặc định cho đơn vị tính
            createDefaultValue();
        }
    }


    /**
     * Khởi tạo giá trị mặc định cho bảng unit
     * @created_by lxphuoc on 3/25/2019
     */
    private void createDefaultValue() {

        String[] units = AppConstant.UNIT_DEFAULT;
        for (String unit : units) {
            UnitRespository.getInstance().addNewUnit(new Units.Builder().setUnitName(unit).build());
        }
    }
}