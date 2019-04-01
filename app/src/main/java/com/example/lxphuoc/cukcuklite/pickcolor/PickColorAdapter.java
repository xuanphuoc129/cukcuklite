package com.example.lxphuoc.cukcuklite.pickcolor;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lxphuoc.cukcuklite.R;

import java.util.ArrayList;

/**
 * Adapter cho danh sách màu
 * @created_by lxphuoc on 3/28/2019
 */

public class PickColorAdapter extends RecyclerView.Adapter<PickColorAdapter.ViewHolder> {

    private ArrayList<String> mDatasets;

    private Drawable drawable;

    private static OnClickColorItem mOnClickColorItem;

    PickColorAdapter(ArrayList<String> mDatasets) {
        this.mDatasets = mDatasets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        drawable = viewGroup.getContext().getResources().getDrawable(R.drawable.bg_circle);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        drawable.setColorFilter(Color.parseColor(mDatasets.get(i)), PorterDuff.Mode.SRC);
        viewHolder.ivItemColor.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItemColor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemColor = itemView.findViewById(R.id.ivItemColor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickColorItem.onClickItem(mDatasets.get(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Phương thức gán sự kiện callback cho sự kiện chọn màu
     * @param onClickListener Sự kiện call back
     * @created_by lxphuoc on 3/28/2019
     */
    public void setOnClickListener(OnClickColorItem onClickListener){
        PickColorAdapter.mOnClickColorItem = onClickListener;
    }

    /**
     * Interface cho sự kiện callback về context gọi đến dialog
     * @created_by lxphuoc on 3/28/2019
     */
    public interface OnClickColorItem{
        void onClickItem(String color);
    }
}
