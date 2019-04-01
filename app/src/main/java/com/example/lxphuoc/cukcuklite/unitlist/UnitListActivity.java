package com.example.lxphuoc.cukcuklite.unitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.BaseActivity;
import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;
import com.example.lxphuoc.cukcuklite.data.model.Units;
import com.example.lxphuoc.cukcuklite.editproduct.ConfirmDeleteProductFragment;

import java.util.ArrayList;

/**
 * ‐ Màn hình danh sách đơn vị tính
 * <p>
 * <p>
 * ‐ @created_by lxphuoc on 3/26/2019
 * ‐ @modified_by lxphuoc on 3/26/2019 ‐ Diễn giải thay đổi
 */

public class UnitListActivity extends BaseActivity implements IUnitListContract.IView, View.OnClickListener {

    IUnitListContract.IPresenter mPresenter;

    UnitListAdapter mUnitListAdapter;

    ArrayList<Units> mUnits;

    RecyclerView rcvUnitList;

    ImageView ivAddButtonUnit, ivBackButton;

    Button btnDoneUnit;

    int unitIdSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);

        initView();

        mPresenter = new UnitListPresenter(this);
        mPresenter.onLoadUnits();

        Bundle bundle = getIntent().getExtras();

        int unitId = 0;
        if(bundle != null){
            unitId = bundle.getInt(SqlConstant.UNIT_ID);
        }

        mUnitListAdapter = new UnitListAdapter(mUnits, unitId);
        initEvent();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rcvUnitList.setLayoutManager(linearLayoutManager);
        rcvUnitList.setAdapter(mUnitListAdapter);

    }

    /**
     * Khởi tạo sự kiện cho click item
     *
     * @created_by lxphuoc on 3/26/2019
     */
    private void initEvent() {
        mUnitListAdapter.setOnClickListener(new UnitListAdapter.OnClickItem() {
            @Override
            public void onClickEdit(int unitId) {
                showEditDialog(unitId);
            }

            @Override
            public void onClickSelect(int unitId) {
                unitIdSelected = unitId;
            }

            @Override
            public void onLongClickItem(int unitId, View view) {
                unitIdSelected = unitId;
                showPopupDelete(view);
            }
        });
    }

    /**
     * Phương thức hiển thị popup xóa sản phẩm
     * @param view - item đã gửi sự kiện
     *             @created_by lxphuoc on 3/28/2019
     */
    private void showPopupDelete(View view) {
        try {
            PopupMenu popup = new PopupMenu(this,view);
            popup.getMenuInflater()
                    .inflate(R.menu.menu_remove_product, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if(id == R.id.iDeleteProduct){
                        showConfirmDialogRemoveUnit();
                    }
                    return true;
                }
            });

            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức hiển thị hộp thoại xác nhận xóa đơn vị tính
     * @created_by lxphuoc on 3/27/2019
     */
    private void showConfirmDialogRemoveUnit() {
        try {
            Units unit = UnitRespository.getInstance().getUnitInfo(unitIdSelected);
            ConfirmDeleteProductFragment fragment = ConfirmDeleteProductFragment.createInstance(unit == null ? "" : unit.getUnitName(),R.string.question_ask_delete_unit, new ConfirmDeleteProductFragment.IOnClickConfirm() {
                @Override
                public void onClickAccept() {
                    mPresenter.deleteUnits(unitIdSelected);
                }
            });
            fragment.show(getSupportFragmentManager(), "fragment_confirm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Khởi tạo các giá trị từ id trên view
     *
     * @created_by lxphuoc on 3/26/2019
     */
    private void initView() {
        rcvUnitList = findViewById(R.id.rcvUnitList);
        ivAddButtonUnit = findViewById(R.id.ivAddButtonUnit);
        ivBackButton = findViewById(R.id.ivBackButton);
        btnDoneUnit = findViewById(R.id.btnDoneUnit);

        btnDoneUnit.setOnClickListener(this);
        ivBackButton.setOnClickListener(this);
        ivAddButtonUnit.setOnClickListener(this);
    }

    /**
     * Phương thức gán danh sách đơn vị
     * @param units - danh sách đơn vị tính
     *              @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void setUnits(ArrayList<Units> units) {
        mUnits = units;
    }

    /**
     * Phương thức hiển thị thông báo thêm đơn vị tính thành công
     * @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void addNewSuccess(int unitId) {
        showToast(getResources().getString(R.string.add_new_unit_success_message));
        unitIdSelected = unitId;
        onDone();
    }

    /**
     * Phương thức hiển thị thông báo thêm đơn vị tính thất bại
     * @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void addNewError() {
        showToast(getResources().getString(R.string.add_new_unit_error_message));
    }

    /**
     * Phương thức hiển thị thông báo xóa đơn vị tính thành công
     * @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void deleteSuccess() {
        showToast(getResources().getString(R.string.delete_success_message));
        for (int i = 0; i < mUnits.size(); i++) {
            if(mUnits.get(i).getUnitId() == unitIdSelected){
                mUnits.remove(i);
                mUnitListAdapter.setUnits(mUnits);
                mUnitListAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    /**
     * Phương thức hiển thị lỗi xóa đơn vị tính
     * @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void deleteError() {
        showToast(getResources().getString(R.string.delete_error_message));
    }

    /**
     * Phương thức cập nhật tên đơn vị tính thành công
     * @param unitIdUpdate - mã đơn vị tính đã cập nhật
     *                     @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void updateSuccess(int unitIdUpdate) {
        showToast(getResources().getString(R.string.update_success_message));
        unitIdSelected = unitIdUpdate;
        onDone();
    }

    /**
     * Phương thức hiển thị lỗi cập nhật tên đơn vị tính
     * @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void updateError() {
        showToast(getResources().getString(R.string.update_error_message));
    }
    /**
     * Phương thức hiển thị màn hình thêm mới tên đơn vị tính
     *               @created_by lxphuoc on 3/28/2019
     */

    @Override
    public void showAddDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddUpdateUnitFragment fragment = AddUpdateUnitFragment.createInstance(R.string.add_unit_title, 0, new AddUpdateUnitFragment.IOnClickDone() {
            @Override
            public void onClickDone(String name) {
                addUnit(name);
            }

            @Override
            public void onClickDone(String name, int unitId) {

            }
        });
        fragment.show(fm, "dialog_fragment_add_unit");
    }

    /**
     * Phương thức hiển thị màn hình chỉnh sửa tên đơn vị tính
     * @param unitId - Mã đơn vị tính
     *               @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void showEditDialog(int unitId) {
        FragmentManager fm = getSupportFragmentManager();
        AddUpdateUnitFragment fragment = AddUpdateUnitFragment.createInstance(R.string.edit_unit_title, unitId, new AddUpdateUnitFragment.IOnClickDone() {
            @Override
            public void onClickDone(String name) {

            }

            @Override
            public void onClickDone(String name, int unitId) {
                updateUnit(name,unitId);
            }
        });
        fragment.show(fm, "dialog_fragment_add_unit");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        try {
            switch (id) {
                case R.id.ivBackButton:
                    onBackPressed();
                    break;
                case R.id.ivAddButtonUnit:
                    showAddDialog();
                    break;
                case R.id.btnDoneUnit:
                    onDone();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Phương thức kết thúc luồng chọn đơn vị tính cho sản phẩm
    *
    * @created_by lxphuoc on 3/26/2019
    */
    private void onDone() {
        Intent intent = new Intent(AppConstant.UPDATE_PRODUCT_UNIT);
        intent.putExtra(SqlConstant.UNIT_ID, unitIdSelected);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        onBackPressed();
    }

    /**
     * Phương thức thêm mới đơn vị tính
     * @param name - Tên đơn vị tính
     *             @created_by lxphuoc on 3/28/2019
     */
    public void addUnit(String name){
        mPresenter.addNewUnits(name);
    }
    /**
     * Phương thức cập nhật tên đơn vị tính
     * @param name - Tên đơn vị tính
     *             @created_by lxphuoc on 3/28/2019
     */
    public void updateUnit(String name, int unitIdSelected) {
        mPresenter.updateUnits(name,unitIdSelected);
    }
    /**
     * Phương thức hiển thị thông báo tên đơn vị tính đã tồn tại khi thêm mới hoặc cập nhật tên đơn vị tính
     *             @created_by lxphuoc on 3/28/2019
     */
    @Override
    public void showNameIsExistError() {
        showToast(getResources().getString(R.string.error_message_unit_name_exist));
    }
}
