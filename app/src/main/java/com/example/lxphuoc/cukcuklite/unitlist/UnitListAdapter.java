package com.example.lxphuoc.cukcuklite.unitlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.data.model.Units;

import java.util.ArrayList;

/**
 * ‐ Adapter cho recycle view danh sách đơn vị tính
 * <p>
 * ‐ @created_by lxphuoc on 3/26/2019.
 * ‐ @modified_by lxphuoc on 3/26/2019.
 */

public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.ViewHolder> {

    private static OnClickItem mOnClickItem;

    private ArrayList<Units> mUnits;

    private int unitIdSelected;

    UnitListAdapter(ArrayList<Units> mUnits, int unitId) {
        unitIdSelected = unitId;
        this.mUnits = mUnits;
    }

    public void setUnits(ArrayList<Units> units) {
        mUnits = units;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(mUnits.get(position));
    }

    @Override
    public int getItemCount() {
        return mUnits == null ? 0 : mUnits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitName;

        ImageView ivUnitItemEdit, ivSelected;

        ViewHolder(final View itemView) {
            super(itemView);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            ivUnitItemEdit = itemView.findViewById(R.id.ivUnitItemEdit);
            ivSelected = itemView.findViewById(R.id.ivSelected);

            ivUnitItemEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickItem.onClickEdit(mUnits.get(getAdapterPosition()).getUnitId());
                }
            });

            tvUnitName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int temp = unitIdSelected;
                    unitIdSelected = mUnits.get(getAdapterPosition()).getUnitId();

                    notifyItemChanged(getAdapterPosition());

                    for (int i = 0; i < mUnits.size(); i++) {
                        if(mUnits.get(i).getUnitId() == temp){
                            notifyItemChanged(i);
                            break;
                        }
                    }
                    mOnClickItem.onClickSelect(unitIdSelected);
                }
            });

            tvUnitName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnClickItem.onLongClickItem(mUnits.get(getAdapterPosition()).getUnitId(),itemView);
                    return true;
                }
            });

        }

        /**
         * Phương thức thiết lập giá trị cho item trong recycle view
         * @param unit - Đơn vị tính
         *             @created_by lxphuoc on 3/28/2019
         */
        void onBindView(Units unit) {
            tvUnitName.setText(unit.getUnitName());
            if (unitIdSelected > 0 && unit.getUnitId() == unitIdSelected) {
                ivSelected.setVisibility(View.VISIBLE);
            }else{
                ivSelected.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Phương thức cài đặt sự kiện callback khi người dùng click vào 1 item
     * @param onClickListener - Interface truyền về activity
     *                        @created_by lxphuoc on 3/28/2019
     */
    public void setOnClickListener(UnitListAdapter.OnClickItem onClickListener) {
        UnitListAdapter.mOnClickItem = onClickListener;
    }

    /**
     * Interface sự kiện callback khi người dùng click vào 1 item
     * @created_by lxphuoc on 3/28/2019
     */
    public interface OnClickItem {

        void onClickEdit(int unitId);

        void onClickSelect(int unitId);

        void onLongClickItem(int unitId, View view);
    }
}
