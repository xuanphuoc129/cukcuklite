package com.example.lxphuoc.cukcuklite.menulist;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.lxphuoc.cukcuklite.AppConstant;
import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.data.ProductRespository;
import com.example.lxphuoc.cukcuklite.data.model.Products;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * ‐ Adapter cho danh sách sản phẩm trên RecyclerView
 * <p>
 * ‐ @created_by lxphuoc on 3/25/2019.
 * ‐ @modified_by lxphuoc on 3/25/2019.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.RecyclerViewHolder> {

    private static IItemSelected listener;

    private ArrayList<Products> mProducts;

    private Context mContext;

    MenuAdapter(Context context) {
        mProducts = new ArrayList<>();
        mContext = context;
    }

    /**
     * Phương thức cập nhật thông tin sản phẩm trong chuỗi
     *
     * @param productId - Mã sản phẩm cập nhật
     * @created_by lxphuoc on 3/25/2019
     */
    void updateData(int productId){
        for (int i = 0; i < mProducts.size(); i++) {
            if(productId == mProducts.get(i).getProductId()){
                Products product = ProductRespository.getInstance().getProductInfo(productId);
                if(product != null){
                    mProducts.get(i).fromProducts(product);
                }
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
    * Phương thức gán giá trị sản phẩm vào cho RecyclerView binding
    *
    * @param products - Danh sách sản phẩm
    * @created_by lxphuoc on 3/25/2019
    */
    public void setData(ArrayList<Products> products){
        mProducts = products;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.updateView(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlIconContainer;

        ImageView ivProductIcon;

        TextView tvProductName, tvProductPrice, tvSaleOffStatus;

        int productId;

        RecyclerViewHolder(final View itemView) {
            super(itemView);

            rlIconContainer = itemView.findViewById(R.id.rlIconContainer);
            ivProductIcon = itemView.findViewById(R.id.ivProductIcon);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvSaleOffStatus = itemView.findViewById(R.id.tvSaleOffStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(productId,itemView);
                }
            });

        }

        /**
        * Phương thức cập nhật thông tin sản phẩm cho từng đối tượng trên list
        *
        * @param product Thông tin sản phẩm
        * @created_by lxphuoc on 3/25/2019
        */

        void updateView(Products product) {

            productId = product.getProductId();

            // Đổ màu lên background
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_circle);
            drawable.setColorFilter(Color.parseColor(product.getProductColor()), PorterDuff.Mode.SRC);
            rlIconContainer.setBackground(drawable);

            // Cập nhật ảnh icon
            ivProductIcon.setImageBitmap(getBitmapFromAssets(product.getProductThumbnail()));

            // Cập nhật tên và giá tiền
            tvProductName.setText(product.getProductName());
            String priceString = mContext.getString(R.string.price_text) +" "+ NumberFormat.getNumberInstance(Locale.US).format(product.getProductPrice());
            tvProductPrice.setText(priceString);

            // Cập nhật trạng thái sản phẩm
            if(product.getProductStatus() == 2){ // Sản phẩm ở trạng thái ngừng bán
                tvSaleOffStatus.setVisibility(View.VISIBLE);
            }else{
                tvSaleOffStatus.setVisibility(View.GONE);
            }
        }

        /**
        * Phương thức chuyển ảnh thành bitmap từ đường dẫn file trong assets
        *
        * @param fileName Tên file ảnh
        * @return giải thích hàm này trả về
        *
        * @created_by lxphuoc on 3/25/2019
        */
        Bitmap getBitmapFromAssets(String fileName) {
            AssetManager assetManager = mContext.getAssets();
            InputStream istr = null;
            try {
                istr = assetManager.open(AppConstant.THUMBNAIL_ASSETS + "/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return BitmapFactory.decodeStream(istr);
        }
    }

    /**
     * Phương thức gán sự kiện callback cho sự kiện chọn sản phẩm
     * @param listener Sự kiện call back
     * @created_by lxphuoc on 3/28/2019
     */
    void setOnItemClickListener(IItemSelected listener) {
        MenuAdapter.listener = listener;
    }

    /**
     * Interface cho sự kiện khi click vào sản phẩm
     * @created_by lxphuoc on 3/28/2019
     */
    public interface IItemSelected {
        void onItemClick(int position, View v);
    }

}
