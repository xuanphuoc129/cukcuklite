package com.example.lxphuoc.cukcuklite.data;

import com.example.lxphuoc.cukcuklite.data.dao.UnitDAO;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class được tạo ra cung cấp các phương thức truy vấn vào bảng units trong cơ sở dữ liệu
 * (Các phương thức được implements từ interface IUnitDataSource)
 */

public class UnitRespository implements IUnitDataResource {

    private static final String TAG = "UnitRespository";

    private static UnitRespository ourInstance = null;

    private UnitDAO mUnitDAO;

    private Map<Integer, Units> mUnitCaches;


    /**
     * Phương thức viết theo design patter singleton
     *
     * @return - trả về chính dối tượng UnitRespository
     */
    public static UnitRespository getInstance() {
        if (ourInstance == null) {
            ourInstance = new UnitRespository();
        }
        return ourInstance;
    }

    private UnitRespository() {
        // Khởi tạo giá trị mặc định cho các biến toàn cục
        mUnitDAO = new UnitDAO();
        mUnitCaches = new LinkedHashMap<>();
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
            long rowId = mUnitDAO.addNewUnit(unit);

            if (rowId > 0) {
                Units unitDatabase = mUnitDAO.getUnitInfo((int) rowId);
                if (unitDatabase != null) {
                    addUnitToCache(unitDatabase);
                }
                return rowId;
            }

            return rowId;

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
            if (mUnitCaches.size() > 0 && mUnitCaches.containsKey(unitId)) { // Lấy đối tượng trong cache
                return mUnitCaches.get(unitId);
            } else {
                Units unitDatabase = mUnitDAO.getUnitInfo(unitId);
                if (unitDatabase != null) {
                    addUnitToCache(unitDatabase);
                    return unitDatabase;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
    * Phương thức lấy thông tin đơn vị tính từ tên đơn vị tính
    *
    * @param unitName Giải thích cho biến này
    * @created_by lxphuoc on 3/28/2019
    */
    @Override
    public Units getUnitInfoFromUnitName(String unitName) {
        if (unitName.isEmpty()) {
            return null;
        }
        return mUnitDAO.getUnitInfoFromUnitName(unitName);
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
            boolean respone = mUnitDAO.updateUnitInfo(unitId, unit);
            if (respone) {
                Units unitDatabase = mUnitDAO.getUnitInfo(unitId);
                if (mUnitCaches.containsKey(unitId)) {
                    Units u = mUnitCaches.get(unitId);
                    if (u != null) {
                        u.fromUnit(unitDatabase);
                    }
                } else {
                    addUnitToCache(unitDatabase);
                }
            }
            return respone;
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
            boolean response = mUnitDAO.deleteUnit(unitId);

            if (response) {
                mUnitCaches.remove(unitId);
            }
            return response;
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

        if (mUnitCaches.size() > 0) {
            return new ArrayList<>(mUnitCaches.values());
        } else {
            try {
                ArrayList<Units> units = mUnitDAO.getListUnits();

                if (units != null && units.size() > 0) {
                    mUnitCaches.clear();
                    for (Units u : units) {
                        addUnitToCache(u);
                    }
                }

                return units;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>();
    }

    /**
     * Phương thức lưu 1 đơn vị tính vào bộ nhớ đệm
     * @param unit - Đơn vị tính
     *             @created_by lxphuoc on 3/28/2019
     */
    private void addUnitToCache(Units unit) {
        mUnitCaches.put(unit.getUnitId(), unit);
    }


}
