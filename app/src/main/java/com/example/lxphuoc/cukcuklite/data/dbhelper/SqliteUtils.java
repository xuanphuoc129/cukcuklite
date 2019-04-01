package com.example.lxphuoc.cukcuklite.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.dao.ProductDAO;
import com.example.lxphuoc.cukcuklite.data.dao.UnitDAO;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.io.IOException;

/**
 * ‐ Class được khởi tạo nhằm thực hiện các tương tác với cơ sở dữ liệu SQLite
 * <p>
 * ‐ @created_by lxphuoc on 3/16/2019
 * ‐ @modified_by lxphuoc on 3/16/2019 ‐ Diễn giải thay đổi
 */
public class SqliteUtils extends SQLiteOpenHelper implements ISqliteHelper {

    private static final String TAG = "SqliteUtils";

    private static final String DATABASE_NAME = "cukcuklite";

    private static final int DATABASE_VERSION = 1;

    private static SqliteUtils instance = null;


    /**
     * Phương thức được viết theo dạng singleton design patter.
     * Sử dụng để khởi tạo đối tượng SqliteUtils
     *
     * @return Trả về 1 đối tượng SqliteUtils
     * @created_by lxphuoc on 3/18/2019
     * @modified_by lxphuoc on 3/18/2019 ‐ Cập nhật, bổ sung
     */
    public static SqliteUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteUtils(context);
        }
        return instance;
    }

    public static SqliteUtils getInstance() {
        return instance;
    }

    /**
     * Phương thưc khởi tạo đối tượng
     *
     * @param context Context khởi tạo đối tượng
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    private SqliteUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }

    /**
     * Phương thức được khởi chạy sau khi contructor được khởi tạo
     * Tác dụng: Mở kết nối đến database nếu đã tồn tại và tạo mới nếu chưa có
     *
     * @param db - Database đang thao tác
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "onOpen: ");
        // Mở chế độ foreign key cho cơ sở dữ liệu
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * Đây là nơi để chúng ta viết những câu lệnh tạo bảng. Nó được gọi khi database đã được tạo.
     *
     * @param db - Database đang thao tác
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate: ");
        createTable(db);
    }



    /**
     * Tạo các bảng cho cơ sở dữ liệu
     *
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    private void createTable(SQLiteDatabase db) {
        try {
            db.execSQL(UnitDAO.CREATE_TABLE_SQL);
            db.execSQL(ProductDAO.CREATE_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nó được gọi khi database được nâng cấp, ví dụ như chỉnh sửa cấu trúc các bảng, thêm những thay đổi cho database,..
     *
     * @param db         - Database đang thao tác
     * @param oldVersion - Phiên bản cũ của database
     * @param newVersion - Phiên bản mới của database
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Phương thức dùng để thêm 1 bản ghi vào cơ sở dữ liệu
     *
     * @param tableName     - Têm bảng
     * @param contentValues - Giá trị muốn thêm mới
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public long addNewRecord(String tableName, ContentValues contentValues) {
        long result = -1;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            result = sqLiteDatabase.insert(tableName, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Phương thức dùng để lấy bản ghi từ cơ sở dữ liệu
     *
     * @param table         - tên bảng
     * @param columns       - danh sách cột giá trị muốn lấy
     * @param selection     - điều kiện
     * @param selectionArgs - giá trị điều kiện
     * @param groupBy       - câu lệnh groupby
     * @param having        - câu lệnh having
     * @param orderBy       - câu lệnh orderBy
     * @return Giá trị trả về là 1 cursor
     * có thể trả về null
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            return sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Phương thức dùng để lấy bản ghi từ cơ sở dữ liệu
     *
     * @param sql           - Câu lệnh select (giá trị điều kiện được tạo bằng dấu ?)
     * @param selectionArgs - điều kiện where
     * @return Giá trị trả về là 1 cursor
     * có thể trả về null
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            return sqLiteDatabase.rawQuery(sql, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Phương thức dùng để update 1 bản ghi vào cơ sở dữ liệu
     *
     * @param tableName     - tên bảng
     * @param contentValues - đối tượng muốn chỉnh sửa(giá trị mới)
     * @param whereClause   - tập các điều kiện lọc (dùng dấu ? để tạo điều kiện lọc)
     * @param whereArgs     - tập các giá trị của điều kiện lọc (lấy theo đúng thứ tự)
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public int updateRecord(String tableName, ContentValues contentValues, String whereClause, String[] whereArgs) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            return sqLiteDatabase.update(tableName, contentValues, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Phương thức dùng để xóa 1 bản ghi khỏi cơ sở dữ liệu
     *
     * @param table       - tên bảng
     * @param whereClause -  tập điều kiện lọc (dùng dấu ? để tạo điều kiện lọc)
     * @param whereArgs   - tập các giá trị của điều kiện lọc (lấy theo đúng thứ tự)
     * @created_by lxphuoc on 3/16/2019
     * @modified_by lxphuoc on 3/16/2019 ‐ Cập nhật, bổ sung
     */
    @Override
    public int deleteRecord(String table, String whereClause, String[] whereArgs) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


}
