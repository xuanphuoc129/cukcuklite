package com.example.lxphuoc.cukcuklite.unitlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;
import com.example.lxphuoc.cukcuklite.data.model.Units;

/**
 * ‐ Class được tạo ra dể người dùng đăng nhập vào hệ thống
 * <p>
 * ‐ @created_by lxphuoc on 3/26/2019.
 * ‐ @modified_by lxphuoc on 3/26/2019.
 */

public class AddUpdateUnitFragment extends DialogFragment {

    private static IOnClickDone mIOnClickDone;
    Button btnSaveAddUnit , btnCancelAddUnit;

    EditText etInputUnit;

    AppCompatImageView ivCloseAddUnit;

    TextView tvUnitTitleName;

    int unitIdSelected;

    public AddUpdateUnitFragment() {

    }

    public static AddUpdateUnitFragment createInstance(int unitTitle , int unitId, IOnClickDone mIOnClickDone){
        AddUpdateUnitFragment fragment = new AddUpdateUnitFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("unitTitle", unitTitle);
        bundle.putInt(SqlConstant.UNIT_ID, unitId);
        setOnclickDoneListener(mIOnClickDone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_unit,container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        btnSaveAddUnit = view.findViewById(R.id.btnSaveAddUnit);
        etInputUnit = view.findViewById(R.id.etInputUnit);
        btnCancelAddUnit = view.findViewById(R.id.btnCancelAddUnit);
        ivCloseAddUnit = view.findViewById(R.id.ivCloseAddUnit);
        tvUnitTitleName = view.findViewById(R.id.tvUnitTitleName);

        btnSaveAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unitIdSelected > 0){ // Chỉnh sửa
                    mIOnClickDone.onClickDone(etInputUnit.getText().toString(), unitIdSelected);
                }else{ // Thêm mới
                    mIOnClickDone.onClickDone(etInputUnit.getText().toString());
                }
                dismiss();
            }
        });
        btnCancelAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ivCloseAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle bundle = getArguments();
        if(bundle != null){
            if(bundle.containsKey("unitTitle")){
                tvUnitTitleName.setText(bundle.getInt("unitTitle"));
            }
            if(bundle.containsKey(SqlConstant.UNIT_ID)){
                unitIdSelected = bundle.getInt(SqlConstant.UNIT_ID);
                if(unitIdSelected > 0){
                    Units unit = UnitRespository.getInstance().getUnitInfo(unitIdSelected);
                    if(unit != null){
                        etInputUnit.setText(unit.getUnitName());
                        etInputUnit.setSelection(etInputUnit.getText().length());
                    }
                }
            }
        }

    }

    /**
     * Phương thức gán sự kiện callback trả lại cho activity
     * @param mIOnClickDone - Sự kiện callback
     *                      @created_by lxphuoc on 3/28/2019
     */
    private static void setOnclickDoneListener(IOnClickDone mIOnClickDone){
        AddUpdateUnitFragment.mIOnClickDone = mIOnClickDone;
    }

    /**
     * Interface cho sự kiện callback click done
     * @created_by lxphuoc on 3/28/2019
     */
    public interface IOnClickDone{
        void onClickDone(String name);
        void onClickDone(String name, int unitId);
    }


}
