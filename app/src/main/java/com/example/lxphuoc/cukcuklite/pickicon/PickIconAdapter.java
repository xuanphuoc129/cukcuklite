package com.example.lxphuoc.cukcuklite.pickicon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.pickcolor.PickColorAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PickIconAdapter extends RecyclerView.Adapter<PickIconAdapter.ViewHolder> {

    private static final String SUBFORDER = "icondefault";

    private ArrayList<String> mDatasets;

    private static PickColorAdapter.OnClickColorItem mOnClickColorItem;

    private Context mContext;

    public PickIconAdapter(ArrayList<String> mDatasets) {
        this.mDatasets = mDatasets;
    }

    @NonNull
    @Override
    public PickIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_icon, viewGroup, false);
        return new PickIconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickIconAdapter.ViewHolder viewHolder, int i) {
        viewHolder.initView(mDatasets.get(i));
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickColorItem.onClickItem(mDatasets.get(getAdapterPosition()));
                }
            });
        }

        void initView(String fileName) {
            try {
                // get input stream
                InputStream ims = mContext.getAssets().open(SUBFORDER + "/" + fileName);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                ivIcon.setImageDrawable(d);
                ims.close();

            } catch (IOException ex) {
                ex.fillInStackTrace();
            }
        }
    }

    public void setOnClickListener(PickColorAdapter.OnClickColorItem onClickListener) {
        PickIconAdapter.mOnClickColorItem = onClickListener;
    }

    public interface OnClickColorItem {
        void onClickItem(String color);
    }


}

