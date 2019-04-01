package com.example.lxphuoc.cukcuklite.menulist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.BaseActivity;
import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.addproduct.AddProductActivity;
import com.example.lxphuoc.cukcuklite.data.dbhelper.SqlConstant;
import com.example.lxphuoc.cukcuklite.data.model.Products;
import com.example.lxphuoc.cukcuklite.editproduct.EditProductActivity;

import java.util.ArrayList;

/**
 * ‐ Màn hình danh sách sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/25/2019
 */

public class MenuListActivity extends BaseActivity implements IMenuListContract.IView, NavigationView.OnNavigationItemSelectedListener {


    RecyclerView rcvProductList;

    MenuAdapter mMenuAdapter;

    IMenuListContract.IPresenter mPresenter;

    LinearLayout llEmptyState;

    Toolbar toolbar;

    BroadcastReceiver mAddNewProductBroadCast, mDeleteProductBroadcast, mUpdateProductBroadcast;

    DrawerLayout dlMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        initView();
        // Khởi tạo adapter
        mMenuAdapter = new MenuAdapter(this);

        mMenuAdapter.setOnItemClickListener(new MenuAdapter.IItemSelected() {
            @Override
            public void onItemClick(int productId, View v) {
                try {
                    openAddEditProductActivity(productId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // Khởi tạo presenter và load danh sách sản phẩm
        mPresenter = new MenuListPresenter(this);
        mPresenter.getListProduct();

        // Khởi tạo recyleview
        rcvProductList = findViewById(R.id.rcvProductList);

        // set layout cho recyle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvProductList.setLayoutManager(layoutManager);
        rcvProductList.setAdapter(mMenuAdapter);


    }

    /**
     * Phuowng thức khởi tạo giá trị mặc định cho các phần từ trên giao diện
     *
     * @created_by lxphuoc on 3/27/2019
     */
    private void initView() {
        llEmptyState = findViewById(R.id.llEmptyState);

        // Khởi tạo toolbar
        initToolbar();

        // Khởi tạo BroadCast
        initBroadCast();
    }


    /**
     * Đăng ký broadCast thêm mới sản phẩm
     *
     * @created_by lxphuoc on 3/25/2019
     */
    private void initBroadCast() {
        mAddNewProductBroadCast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getListProduct();
            }
        };
        mDeleteProductBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getListProduct();
            }
        };
        mUpdateProductBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int productId = intent.getIntExtra(SqlConstant.PRODUCT_ID, 0);
                if (productId > 0) {
                    mMenuAdapter.updateData(productId);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mAddNewProductBroadCast, new IntentFilter(AppConstant.ADD_NEW_PRODUCT_BROADCAST));
        LocalBroadcastManager.getInstance(this).registerReceiver(mDeleteProductBroadcast, new IntentFilter(AppConstant.DELETE_PRODUCT_BROADCAST));
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateProductBroadcast, new IntentFilter(AppConstant.UPDATE_PRODUCT_INFO));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mAddNewProductBroadCast);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDeleteProductBroadcast);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateProductBroadcast);
        super.onDestroy();
    }


    /**
     * Khởi tạo toolbar
     *
     * @created_by lxphuoc on 3/25/2019
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.tbMenuList);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        dlMenu = findViewById(R.id.dlMenu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dlMenu, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlMenu.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nvMenu);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navMenu);
    }


    @Override
    public void onBackPressed() {
        if (dlMenu.isDrawerOpen(GravityCompat.START)) {
            dlMenu.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            if (id == R.id.menu_add) {
                navigateToActivity(this, AddProductActivity.class, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Phương thức gán danh sách sản phẩm vào recyle view
     *
     * @param products - danh sách sản phẩm
     */
    @Override
    public void setProducts(ArrayList<Products> products) {
        mMenuAdapter.setData(products);
        if (products.size() == 0) {
            showEmptySate();
        } else {
            hideEmptyState();
        }
    }

    /**
     * Phương thức ản trang thái chưa có danh sách sản phẩm
     *
     * @created_by lxphuoc
     */
    private void hideEmptyState() {
        llEmptyState.setVisibility(View.GONE);
    }

    /**
     * Phương thức chuyển sang màn hình activity thêm mới và chỉnh sửa sản phẩm
     *
     * @param productId - mã sản phẩm - truyền vào bằng 0 nếu thêm mới
     */
    private void openAddEditProductActivity(int productId) {
        Bundle bundle = new Bundle();
        bundle.putInt(SqlConstant.PRODUCT_ID, productId);
        navigateToActivity(this, EditProductActivity.class, bundle);
    }

    /**
     * Phương thức hiển thị trang thái chưa có danh sách sản phẩm
     *
     * @created_by lxphuoc
     */
    @Override
    public void showEmptySate() {
        llEmptyState.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();

        dlMenu.closeDrawer(GravityCompat.START);
        return true;
    }
}
