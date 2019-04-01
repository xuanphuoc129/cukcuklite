package com.example.lxphuoc.cukcuklite.unitlist;

import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

/**
 * ‐ Presenter trong mô hình MVP cho màn hình Danh sách đơn vị tính
 * <p>
 * ‐ @created_by lxphuoc on 3/23/2019
 */

public class UnitListPresenter implements IUnitListContract.IPresenter {

    private IUnitListContract.IView mView;

    UnitListPresenter(IUnitListContract.IView mView) {
        this.mView = mView;
    }

    /**
     * Load danh sách đơn vị tính
     *
     * @created_by lxphuoc on 3/26/2019
     */
    @Override
    public void onLoadUnits() {
        ArrayList<Units> units = UnitRespository.getInstance().getListUnits();

        if (units != null && units.size() > 0) {
            mView.setUnits(units);
        } else {
            mView.setUnits(new ArrayList<Units>());
        }
    }

    /**
     * Phương thức Thêm mới đơn vị tính
     *
     * @param name Tên đơn vị tính
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void addNewUnits(String name) {
        if (name.isEmpty()) {
            return;
        }
        try {
            long unitId = UnitRespository.getInstance().addNewUnit(new Units.Builder().setUnitName(name).build());

            if (unitId > 0) {
                mView.addNewSuccess((int) unitId);
            } else {
                if(unitId == 0){
                    mView.showNameIsExistError();
                }else{
                    mView.addNewError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức xóa đơn vị tính
     *
     * @param unitId Mã đơn vị tính
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void deleteUnits(int unitId) {
        if (unitId < 1) {
            return;
        }
        try {
            boolean response = UnitRespository.getInstance().deleteUnit(unitId);
            if (response) {
                mView.deleteSuccess();
            } else {
                mView.deleteError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Phương thức cập nhật thông tin đơn vị tính
     *
     * @param unitId Mã đơn vị tính
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void updateUnits(String name, int unitId) {
        if (name.isEmpty() || unitId < 1) {
            return;
        }
        try {
            Units unitCheck = UnitRespository.getInstance().getUnitInfoFromUnitName(name);
            if(unitCheck == null){
                boolean response = UnitRespository.getInstance().updateUnitInfo(unitId, new Units.Builder().setUnitName(name).build());
                if (response) {
                    mView.updateSuccess(unitId);
                } else {
                    mView.updateError();
                }
            }else{
                if(unitCheck.getUnitId() != unitId){
                    mView.showNameIsExistError();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
