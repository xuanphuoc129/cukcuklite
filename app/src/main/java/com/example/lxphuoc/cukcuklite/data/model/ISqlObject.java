package com.example.lxphuoc.cukcuklite.data.model;

import android.content.ContentValues;
import android.database.Cursor;

public interface ISqlObject {

    ContentValues toContentValues();

    void fromCursor(Cursor cursor);

}
