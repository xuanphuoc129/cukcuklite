package com.example.lxphuoc.cukcuklite.keyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.keyboard.model.InputKeys;

import java.util.ArrayList;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.ViewHoder> {

    private ArrayList<InputKeys> mDatasets;

    private static OnclickInputKey mOnclickInputKey;

    private Context mContext;

    public KeyboardAdapter(ArrayList<InputKeys> mDatasets) {
        this.mDatasets = mDatasets;
    }



    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_input_key, viewGroup, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, int i) {
        viewHoder.initView(mDatasets.get(i));
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    void onChangelabel() {
        mDatasets.get(19).setName("Xong");
        notifyItemChanged(19);
    }

    class ViewHoder extends RecyclerView.ViewHolder {

        TextView tvInputKey;

        ImageView ivIconInput;

        RelativeLayout rlItemContainer;

        ViewHoder(@NonNull View itemView) {
            super(itemView);

            tvInputKey = itemView.findViewById(R.id.tvInputKey);
            ivIconInput = itemView.findViewById(R.id.ivIconInput);
            rlItemContainer = itemView.findViewById(R.id.rlItemContainer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputKeys input = mDatasets.get(getAdapterPosition());
                    if(input.getId() == 8 || input.getId() == 12){
                        mDatasets.get(19).setName("=");
                        notifyItemChanged(19);
                    }
                    mOnclickInputKey.onClickItem(mDatasets.get(getAdapterPosition()).getId());
                }
            });
        }

        /**
        *  Khởi tạo giá trị cho view
        *
        * @param inputKey Đối tượng bingding
        *
        * @created_by lxphuoc on 3/26/2019
        */
        void initView(InputKeys inputKey){

            tvInputKey.setText(inputKey.getName());

            if(inputKey.getId() == 20) {  // Button "Xong"
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_border_radius_gray);
                drawable.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC);
                rlItemContainer.setBackground(drawable);

                tvInputKey.setAllCaps(true);
                tvInputKey.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            } else if(inputKey.getId() == 4){  // Button "Xoa"
                ivIconInput.setVisibility(View.VISIBLE);
                tvInputKey.setVisibility(View.GONE);
            }
        }
    }

    /**
    * Phương thức set sự kiện click vào item
    *
    * @param onClickListener Sự kiện call back khi click item
    *
    * @created_by lxphuoc on 3/26/2019
    */
    public void setOnClickListener(KeyboardAdapter.OnclickInputKey onClickListener) {
        KeyboardAdapter.mOnclickInputKey = onClickListener;
    }

    public interface OnclickInputKey {
        void onClickItem(int id);
    }
}
