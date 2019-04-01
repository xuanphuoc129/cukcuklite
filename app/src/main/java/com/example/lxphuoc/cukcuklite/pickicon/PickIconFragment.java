package com.example.lxphuoc.cukcuklite.pickicon;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.pickcolor.PickColorAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Màn hình chọn biểu tượng cho sản phẩm
 * @created_by lxphuoc on 3/28/2019
 */

public class PickIconFragment extends DialogFragment {
    private static IOnSelectItem mIOnSelectItem;

    RecyclerView rcvItemsList;

    Button btnCancel;

    ArrayList<String> items;

    PickIconAdapter mPickIconAdapter;

    public PickIconFragment() {
        items = new ArrayList<>();
    }

    public static PickIconFragment createInstance(IOnSelectItem mIOnSelectItem) {

        PickIconFragment fragment = new PickIconFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("numberColumn", 5);
        fragment.setOnSelectItem(mIOnSelectItem);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_pick_color, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvItemsList = view.findViewById(R.id.rcvItemsList);
        btnCancel = view.findViewById(R.id.btnCancel);

        int numberColumn = 4;

        Bundle bundle = getArguments();

        createItems();

        if (bundle != null) {
            if(bundle.containsKey("numberColumn")){
                numberColumn = getArguments().getInt("numberColumn");
            }
        }


        mPickIconAdapter = new PickIconAdapter(items);
        addClickItemEvent();

        // Create layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), numberColumn);
        rcvItemsList.setLayoutManager(gridLayoutManager);
        rcvItemsList.setAdapter(mPickIconAdapter);

    }

    /**
     * Phương thức thêm sự kiện click vào item biểu tượng
     * @created_by lxphuoc on 3/28/2019
     */
    private void addClickItemEvent() {
        mPickIconAdapter.setOnClickListener(new PickColorAdapter.OnClickColorItem() {
            @Override
            public void onClickItem(String icon) {
                mIOnSelectItem.onSelectItem(icon);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Phương thức lấy danh sách biểu tượng rồi truyền vào cho adapter
     * @created_by lxphuoc on 3/28/2019
     */
    private void createItems() {
        items = new ArrayList<>();
        try {
            AssetManager assetManager = getContext().getAssets();
            if(assetManager != null){
                String[] lists = assetManager.list("icondefault");

                if (lists != null) {
                    items.addAll(Arrays.asList(lists));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    /**
     * Phương thức gán sự kiện callback 
     * @param mIOnSelectItem - Đối tượng callback
     *                       @created_by lxphuoc on 3/28/2019
     */
    public void setOnSelectItem(PickIconFragment.IOnSelectItem mIOnSelectItem){
        PickIconFragment.mIOnSelectItem = mIOnSelectItem;
    }

    /**
     * Interface cho sự kiện callback
     */
    public interface IOnSelectItem{
        void onSelectItem(String item);
    }

}
