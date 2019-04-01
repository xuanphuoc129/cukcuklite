package com.example.lxphuoc.cukcuklite.pickcolor;

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


import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
‐ Màn hình chọn màu cho sản phẩm
*
‐ @created_by lxphuoc on 3/28/2019
‐ @modified_by lxphuoc on 3/28/2019 ‐ Diễn giải thay đổi
*/
public class PickColorFrament extends DialogFragment {

    private static IOnSelectItem mIOnSelectItem;

    RecyclerView rcvItemsList;

    Button btnCancel;

    ArrayList<String> items;

    PickColorAdapter mPickColorAdapter;

    public PickColorFrament() {
        items = new ArrayList<>();
    }

    public static PickColorFrament createInstance(IOnSelectItem mIOnSelectItem) {

        PickColorFrament fragment = new PickColorFrament();

        Bundle bundle = new Bundle();
        bundle.putInt("numberColumn", 4);

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

        int numberColumn = 2;

        createItems();

        Bundle bundle = getArguments();


        if (bundle != null && bundle.containsKey("numberColumn")) {
            numberColumn = getArguments().getInt("numberColumn");
        }

        mPickColorAdapter = new PickColorAdapter(items);
        addClickItemEvent();

        // Create layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), numberColumn);
        rcvItemsList.setLayoutManager(gridLayoutManager);
        rcvItemsList.setAdapter(mPickColorAdapter);

    }
    /**
     * Phương thức thêm sự kiện click vào item biểu tượng
     * @created_by lxphuoc on 3/28/2019
     */
    private void addClickItemEvent() {
        mPickColorAdapter.setOnClickListener(new PickColorAdapter.OnClickColorItem() {
            @Override
            public void onClickItem(String color) {
                mIOnSelectItem.onSelectItem(color);
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
     * Phương thức lấy danh sách màu sắc rồi truyền vào cho adapter
     * @created_by lxphuoc on 3/28/2019
     */
    private void createItems() {
        items = new ArrayList<>();
        items.addAll(Arrays.asList(AppConstant.COLOR_ARRAY));
    }

    /**
     * Phương thức gán sự kiện callback
     * @param mIOnSelectItem - Đối tượng callback
     *                       @created_by lxphuoc on 3/28/2019
     */
    public void setOnSelectItem(PickColorFrament.IOnSelectItem mIOnSelectItem){
        PickColorFrament.mIOnSelectItem = mIOnSelectItem;
    }

    /**
     * Interface cho sự kiện callback
     */
    public interface IOnSelectItem{
        void onSelectItem(String item);
    }
}
