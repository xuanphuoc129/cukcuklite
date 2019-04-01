package com.example.lxphuoc.cukcuklite.editproduct;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.data.model.Products;

/**
 * ‐ Màn hình hỏi người dùng có muốn xóa sản phẩm
 * <p>
 * ‐ @created_by lxphuoc on 3/27/2019.
 * ‐ @modified_by lxphuoc on 3/27/2019.
 */

public class ConfirmDeleteProductFragment extends DialogFragment {

    TextView tvProductNameConfirm;

    ImageView ivCloseDeleteProduct;

    Button btnNo, btnYes;

    Products mProduct;

    private static IOnClickConfirm mIOnClickConfirm;

    public ConfirmDeleteProductFragment() {
        mProduct = new Products();
    }

    public static ConfirmDeleteProductFragment createInstance(String name, int resId, IOnClickConfirm mIOnClickConfirm) {
        ConfirmDeleteProductFragment fragment = new ConfirmDeleteProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("resId", resId);
        fragment.setOnClickConfirm(mIOnClickConfirm);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_confirm_delete_product, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        tvProductNameConfirm = view.findViewById(R.id.tvProductNameConfirm);
        ivCloseDeleteProduct = view.findViewById(R.id.ivCloseDeleteProduct);
        btnNo = view.findViewById(R.id.btnNoDeleteProduct);
        btnYes = view.findViewById(R.id.btnYesDeleteProduct);

        ivCloseDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mIOnClickConfirm.onClickAccept();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = "";
            int resId = R.string.question_ask_delete_product;
            if (bundle.containsKey("name")) {
                name = bundle.getString("name");
            }
            if (bundle.containsKey("resId")) {
                resId = bundle.getInt("resId");
            }
            String srcString = getResources().getString(resId, name);
            tvProductNameConfirm.setText(Html.fromHtml(srcString));

        }
    }

    /**
     * Phương thức thiết lập sự kiện callback khi người dùng chọn Có
     *
     * @param mIOnClickConfirm - Interface callback từ activity
     * @created_by lxphuoc
     */
    public void setOnClickConfirm(IOnClickConfirm mIOnClickConfirm) {
        ConfirmDeleteProductFragment.mIOnClickConfirm = mIOnClickConfirm;
    }

    /**
     * Interface call back cho sự kiện chọn "Có"
     *
     * @created_by lxphuoc
     */
    public interface IOnClickConfirm {
        void onClickAccept();
    }
}
