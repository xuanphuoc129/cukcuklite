package com.example.lxphuoc.cukcuklite.data.dbhelper;


import android.content.ContentValues;
import android.database.Cursor;


/**
‐ Interface trừu tượng khác phương thức tương tác với cơ sở dữ liệu SQLite

‐ @created_by lxphuoc on 3/16/2019
‐ @modified_by lxphuoc on 3/16/2019 ‐ Diễn giải thay đổi
*/

public interface ISqliteHelper {

    long addNewRecord(String tableName, ContentValues contentValues);

    Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    Cursor rawQuery(String sql, String[] selectionArgs);

    int updateRecord(String table, ContentValues values, String whereClause, String[] whereArgs);

    int deleteRecord(String table, String whereClause, String[] whereArgs);

}
