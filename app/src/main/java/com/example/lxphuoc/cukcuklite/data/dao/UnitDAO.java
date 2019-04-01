package com.example.lxphuoc.cukcuklite.data.dao;

import android.database.Cursor;

import com.example.lxphuoc.cukcuklite.data.IUnitDataResource;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqliteUtils;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

/**
 * Class được tạo ra để thực hiện các thao tác truy vấn vào bảng units trong cơ sở dữ liệu
 * (Các phương thức được implements từ interface IUnitDataResource)
 */

public class UnitDAO implements IUnitDataResource {

    private static final String TABLE_NAME = "units";

    public static final String CREATE_TABLE_SQL = String.format("CREATE TABLE " + TABLE_NAME + "(" +
                    "%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL)",
            SqlConstant.UNIT_ID,
            SqlConstant.UNIT_NAME
    );

    public UnitDAO() {

    }

    /**
     * Phương thức thêm mới đơn vị tính vào cơ sở dữ liệu
     *
     * @param unit - Thông tin đơn vị tính muốn thêm mới
     * @return - Trả về id của hàng vừa được thêm vào bảng
     * (Có thể trả về -1 khi thao tác truy vấn có lỗi xảy ra)
     */
    @Override
    public long addNewUnit(Units unit) {
        if (unit == null) {
            return -1;
        }
        try {
            Units unitDatabase = getUnitInfoFromUnitName(unit.getUnitName());
            if(unitDatabase != null){ // Đã có dối tượng trùng tên trong bảng
                return 0;
            }else{
                return SqliteUtils.getInstance().addNewRecord(TABLE_NAME, unit.toContentValues());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Phương thức lấy thông tin đơn vị tính từ mã đơn vị tính
     *
     * @param unitId - mã đơn vị tính
     * @return - Trả về thông tin đơn vị tính
     * (Có thể trả về null khi thao tác truy vấn có lỗi xảy ra)
     */
    @Override
    public Units getUnitInfo(int unitId) {
        if (unitId < 1) {
            return null;
        }

        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + SqlConstant.UNIT_ID + " = " + "'" + unitId + "'" + " LIMIT 1";
            Cursor cursor = SqliteUtils.getInstance().rawQuery(sql, null);

            if (cursor != null) {
                cursor.moveToFirst();
                Units unit = new Units();
                unit.fromCursor(cursor);
                return unit;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức cập nhật thông tin đơn vị tính
     *
     * @param unitId - mã đơn vị tính cần cập nhật
     * @param unit   - Thông tin đơn vị tính muốn cập nhật
     * @return - Trả về TRUE khi cập nhật thành công và FALSE khi có lỗi xảy ra
     */

    @Override
    public boolean updateUnitInfo(int unitId, Units unit) {
        if (unit == null || unitId < 1) {
            return false;
        }
        try {
            Units unitDatabase = getUnitInfoFromUnitName(unit.getUnitName());
            if(unitDatabase == null){
                int rowId = SqliteUtils.getInstance().updateRecord(
                        TABLE_NAME, unit.toContentValues(),
                        SqlConstant.UNIT_ID + " = ?", new String[]{String.valueOf(unitId)});
                // Cập nhật thành công trả về id của hàng đã cập nhật
                return rowId > 0;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức xoá 1 đơn vị tính trong cơ sở dữ liệu từ mã đơn vị
     *
     * @param unitId - mã đơn vị tính muốn xoá
     * @return - Trả về TRUE khi xoá thành công và FALSE khi có lỗi xảy ra
     */
    @Override
    public boolean deleteUnit(int unitId) {
        if (unitId < 1) {
            return false;
        }
        try {
            int rowId = SqliteUtils.getInstance().deleteRecord(TABLE_NAME, SqlConstant.UNIT_ID + " =? ", new String[]{String.valueOf(unitId)});
            return rowId > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Phương thức lấy danh sách đơn vị tính trong cơ sở dữ liệu
     *
     * @return Trả về 1 danh sách đơn vị tính
     * (Có thể trả về null khi có lỗi truy vấn hoặc danh sách rỗng)
     */
    @Override
    public ArrayList<Units> getListUnits() {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME;

            Cursor cursor = SqliteUtils.getInstance().rawQuery(sql, null);

            if (cursor != null) {
                cursor.moveToFirst();
                ArrayList<Units> units = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    Units unit = new Units();
                    unit.fromCursor(cursor);
                    units.add(unit);
                    cursor.moveToNext();
                }
                return units;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Phương thức lấy thông tin đơn vị tính từ tên
     *
     * @param unitName - tên đơn vị tính
     * @return Thông tin đơn vị tính
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public Units getUnitInfoFromUnitName(String unitName) {
        if (unitName.isEmpty()) {
            return null;
        }
        try {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE lower(" + SqlConstant.UNIT_NAME + ") = '" + unitName.toLowerCase() + "'";

            Cursor cursor = SqliteUtils.getInstance().rawQuery(sql,null);

            if(cursor != null){
                cursor.moveToFirst();
                Units unit = new Units();
                unit.fromCursor(cursor);
                return unit;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
