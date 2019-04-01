package com.example.lxphuoc.cukcuklite.unitlist;

import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

/**
‐ View trong mô hình MVP cho màn hình Danh sách đơn vị tính
*
‐ @created_by lxphuoc on 3/23/2019
*/

public interface IUnitListContract {

    interface IView{

        void setUnits(ArrayList<Units> units);

        void addNewSuccess(int unitId);

        void addNewError();

        void deleteSuccess();

        void deleteError();

        void updateSuccess(int unitId);

        void updateError();

        void showAddDialog();

        void showEditDialog(int unitId);

        void showNameIsExistError();

    }

    interface IPresenter{

        void onLoadUnits();

        void addNewUnits(String name);

        void deleteUnits(int unitId);

        void updateUnits(String name, int unitId);
    }
}
