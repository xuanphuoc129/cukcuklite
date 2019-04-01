package com.example.lxphuoc.cukcuklite.editproduct;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.BaseActivity;
import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.data.UnitRespository;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;
import com.example.lxphuoc.cukcuklite.data.model.Products;
import com.example.lxphuoc.cukcuklite.data.model.Units;
import com.example.lxphuoc.cukcuklite.keyboard.KeyboarFragment;
import com.example.lxphuoc.cukcuklite.pickcolor.PickColorFrament;
import com.example.lxphuoc.cukcuklite.pickicon.PickIconFragment;
import com.example.lxphuoc.cukcuklite.unitlist.UnitListActivity;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * ‐ Màn hình thêm mới và chỉnh sửa thông tin sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/25/2019
 * ‐ @modified_by lxphuoc on 3/25/2019 ‐ Diễn giải thay đổi
 */

public class EditProductActivity extends BaseActivity implements IEditProductContract.IView, View.OnClickListener {

    private Products mProduct;

    private IEditProductContract.IPresenter mPresenter;

    EditText etProductName;

    TextView tvAddEditTitle, tvPriceProduct, tvUnitProduct, tvSaveButton;

    RelativeLayout rlColorContainer, rlThumbnailContainer;

    LinearLayout llPriceProduct, llUnitProduct;

    ImageView ivProductThumbnail, ivBackButton;

    Button btnDelete, btnSaveBottom;

    BroadcastReceiver mUnitBroadcast;

    CheckBox cbProductStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        // Khởi tạo giá trị cho các đối tượng trên layout
        initView();

        // Khởi tạo giá trị mặc định cho biến
        mPresenter = new EditProductPresenter(this);
        mProduct = new Products();

        // Kiểm tra xem có dữ liệu truyền sang từ activity trước hay không
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.containsKey(SqlConstant.PRODUCT_ID)) {
            // Có productId truyền sang -> Người dùng xem thông tin sản phẩm
            int productId = bundle.getInt(SqlConstant.PRODUCT_ID);
            mProduct.setProductId(productId);
            mPresenter.getProductInfo(productId);
            // Thay đổi title sang chỉnh sửa
            tvAddEditTitle = findViewById(R.id.tvAddEditTitle);
            tvAddEditTitle.setText(getResources().getString(R.string.add_edit_edit_product_text));
        }


        mUnitBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int unitId = intent.getIntExtra(SqlConstant.UNIT_ID, 0);
                if (unitId > 0) {
                    mProduct.setProductUnit(unitId);
                    updateProductUnit();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mUnitBroadcast, new IntentFilter(AppConstant.UPDATE_PRODUCT_UNIT));

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUnitBroadcast);
        super.onDestroy();
    }

    /**
     * Khởi tạo giá trị cho các đối tượng trên layout
     *
     * @created_by lxphuoc on 3/25/2019
     */
    private void initView() {
        etProductName = findViewById(R.id.etProductName);
        tvPriceProduct = findViewById(R.id.tvPriceProduct);
        tvUnitProduct = findViewById(R.id.tvUnitProduct);
        rlColorContainer = findViewById(R.id.rlColorContainer);
        rlThumbnailContainer = findViewById(R.id.rlThumbnailContainer);
        ivProductThumbnail = findViewById(R.id.ivProductThumbnail);
        ivBackButton = findViewById(R.id.ivBackButton);
        tvSaveButton = findViewById(R.id.tvSaveButton);
        llPriceProduct = findViewById(R.id.llPriceProduct);
        llUnitProduct = findViewById(R.id.llUnitProduct);
        cbProductStatus = findViewById(R.id.cbProductStatus);


        btnDelete = findViewById(R.id.btnDelete);
        btnSaveBottom = findViewById(R.id.btnSaveBottom);

        ivBackButton.setOnClickListener(this);
        tvSaveButton.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        rlColorContainer.setOnClickListener(this);
        rlThumbnailContainer.setOnClickListener(this);
        llPriceProduct.setOnClickListener(this);
        llUnitProduct.setOnClickListener(this);
        btnSaveBottom.setOnClickListener(this);
    }

    /**
    * Phương thức thiết lập thông tin sản phẩm
    *
    * @param product Thông tin sản phẩm cần cập nhật lên giao diện
    *
    * @created_by lxphuoc on 3/27/2019
    */
    @Override
    public void setProduct(Products product) {
        mProduct.fromProducts(product);
        updateView();
    }

    /**
    * Phương thức cập nhật thông tin giá sản phẩm lên giao diện
    *
    * @created_by lxphuoc on 3/27/2019
    */
    @Override
    public void updateProductPrice() {
        tvPriceProduct.setText(NumberFormat.getNumberInstance(Locale.US).format(mProduct.getProductPrice()));
    }

    /**
     * Phương thức cập nhật thông tin đơn vị tính sản phẩm lên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void updateProductUnit() {
        Units unit = UnitRespository.getInstance().getUnitInfo(mProduct.getProductUnit());
        if (unit != null) {
            tvUnitProduct.setText(unit.getUnitName());
        } else {
            tvUnitProduct.setText(getResources().getString(R.string.add_new_unit_hint));
        }
    }
    /**
     * Phương thức cập nhật thông tin màu sắc sản phẩm lên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void updateProductColor() {
        Drawable drawable = getResources().getDrawable(R.drawable.bg_circle);
        drawable.setColorFilter(Color.parseColor(mProduct.getProductColor()), PorterDuff.Mode.SRC);
        rlThumbnailContainer.setBackground(drawable);
        rlColorContainer.setBackground(drawable);
    }
    /**
     * Phương thức cập nhật thông tin biểu tượng sản phẩm lên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void updateProductThumbnail() {
        ivProductThumbnail.setImageBitmap(getBitmapFromAssets(mProduct.getProductThumbnail()));
    }

    /**
     * Phương thức nhận sự kiện xóa sản phẩm thành công
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void onDeleteSuccess() {
        showToast(getString(R.string.delete_success_message));
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(AppConstant.DELETE_PRODUCT_BROADCAST));
        onBackPressed();
    }

    /**
     * Phương thức nhận sự kiện xóa sản phẩm thất bại
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void onDeleteError() {
        showToast(getString(R.string.delete_error_message));
    }


    /**
     * Phương thức cập nhật giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    private void updateView() {
        if (mProduct != null) {
            etProductName.setText(mProduct.getProductName());
            updateProductColor();
            updateProductPrice();
            updateProductThumbnail();
            updateProductUnit();
            cbProductStatus.setChecked(mProduct.getProductStatus() == 2);
        }
    }

    /**
     * Phương thức thiết lập các sự kiện cho các thao tác của người dùng trên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();

            switch (id) {
                case R.id.ivBackButton:
                    onBackPressed();
                    break;
                case R.id.tvSaveButton:
                    mProduct.setProductName(etProductName.getText().toString());
                    mProduct.setProductStatus(cbProductStatus.isChecked() ? 2 : 1);
                    mPresenter.updateProduct(mProduct);
                    break;
                case R.id.btnSaveBottom:
                    mProduct.setProductName(etProductName.getText().toString());
                    mProduct.setProductStatus(cbProductStatus.isChecked() ? 2 : 1);
                    mPresenter.updateProduct(mProduct);
                    break;
                case R.id.btnDelete:
                    showConfirmDialog();
                    break;
                case R.id.rlColorContainer:
                    showColorDialog();
                    break;
                case R.id.rlThumbnailContainer:
                    showIconDialog();
                    break;
                case R.id.llPriceProduct:
                    showKeyboardDialog();
                    break;
                case R.id.llUnitProduct:
                    openUnitActivity();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức hiển thị hộp thoại xác nhận xóa sản phẩm
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void showConfirmDialog(){
        FragmentManager fm = getSupportFragmentManager();
        ConfirmDeleteProductFragment fragment = ConfirmDeleteProductFragment.createInstance(mProduct.getProductName(),R.string.question_ask_delete_product, new ConfirmDeleteProductFragment.IOnClickConfirm() {
            @Override
            public void onClickAccept() {
                mPresenter.deleteProduct();
            }
        });
        fragment.show(fm, "fragment_confirm");
    }

    /**
     * Phương thức hiển thị hộp thoại bàn phím nhập giá sản phẩm
     *
     * @created_by lxphuoc on 3/27/2019
     */
    public void showKeyboardDialog() {
        FragmentManager fm = getSupportFragmentManager();
        KeyboarFragment keyboarFragment = KeyboarFragment.createInstance(tvPriceProduct.getText().toString(), new KeyboarFragment.IOnClickDone() {
            @Override
            public void onClickDone(long price, String priceShow) {
                setPrice(price,priceShow);
            }
        });
        keyboarFragment.show(fm, "dialog_fragment_keyboard");
    }

    /**
     * Phương thức hiển thị hộp thoại chọn biểu tượng
     *
     * @created_by lxphuoc on 3/27/2019
     */
    public void showIconDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PickIconFragment pickIconFragment = PickIconFragment.createInstance(new PickIconFragment.IOnSelectItem() {
            @Override
            public void onSelectItem(String icon) {
                setProductThumbnail(icon);
            }
        });
        pickIconFragment.show(fm, "fragment_pick_icon");
    }
    /**
     * Phương thức hiển thị hộp thoại chọn màu sắc sản phẩm
     *
     * @created_by lxphuoc on 3/27/2019
     */
    public void showColorDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PickColorFrament pickColorFrament = PickColorFrament.createInstance(new PickColorFrament.IOnSelectItem() {
            @Override
            public void onSelectItem(String color) {
                setColorChange(color);
            }
        });
        pickColorFrament.show(fm, "dialog_fragment_pick_color");
    }

    /**
     * Phương thức chuyển activity đơn vị tính
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void openUnitActivity() {
        Bundle bundle = new Bundle();
        bundle.putInt(SqlConstant.UNIT_ID, mProduct.getProductUnit());
        navigateToActivity(this, UnitListActivity.class, bundle);
    }

    /**
     * Phương thức set màu sắc cho sản phẩm và cập nhật lên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    public void setColorChange(String color) {
        mProduct.setProductColor(color);
        updateProductColor();
    }
    /**
     * Phương thức set biểu tượng cho sản phẩm và cập nhật lên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    public void setProductThumbnail(String productThumbnail) {
        mProduct.setProductThumbnail(productThumbnail);
        updateProductThumbnail();
    }
    /**
     * Phương thức set giá cho sản phẩm và cập nhật lên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    public void setPrice(long price, String priceShow) {
        mProduct.setProductPrice((float) price);
        tvPriceProduct.setText(priceShow);
    }
    /**
     * Phương thức nhận sự kiện cập nhật thông tin sản phẩm thành công
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void onUpdateSuccess() {
        showToast(getResources().getString(R.string.update_success_message));
        Intent intent = new Intent(AppConstant.UPDATE_PRODUCT_INFO);
        intent.putExtra(SqlConstant.PRODUCT_ID, mProduct.getProductId());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        onBackPressed();
    }
    /**
     * Phương thức nhận sự kiện cập nhật thông tin sản phẩm thất bại
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void onUpdateError() {
        showToast(getResources().getString(R.string.update_error_message));
    }

    /**
     * Phương thức nhận sự kiện người dùng click button "Cất" nhưng thông tin sản phẩm không có sự thay đổi gì
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void destroy() {
        onBackPressed();
    }

    /**
     * Phương thức nhận sự kiện cập nhật giao diện khi không thể load thông tin sản phẩm từ mã sản phẩm truyền sang
     *
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void showCantLoadProduct() {

    }
    /**
     * Phương thức hiển thị lỗi khi tên sản phẩm đã tồn tại
     * @created_by lxphuoc on 3/27/2019
     */
    @Override
    public void showNameIsExistError() {
        showToast(getResources().getString(R.string.error_name_product_is_exist));
    }
}
