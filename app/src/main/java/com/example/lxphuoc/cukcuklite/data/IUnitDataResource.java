package com.example.lxphuoc.cukcuklite.data;


import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

public interface IUnitDataResource {

    long addNewUnit(Units unit);

    Units getUnitInfo(int unitId);

    Units getUnitInfoFromUnitName(String unitName);

    boolean updateUnitInfo(int unitId, Units unit);

    boolean deleteUnit(int unitId);

    ArrayList<Units> getListUnits();

}
