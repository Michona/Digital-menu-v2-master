package com.example.terz99.digitalmenuv2.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terz99.digitalmenuv2.BillActivity;
import com.example.terz99.digitalmenuv2.Item;
import com.example.terz99.digitalmenuv2.MyOrderActivity;
import com.example.terz99.digitalmenuv2.OrderItem;
import com.example.terz99.digitalmenuv2.R;
import com.example.terz99.digitalmenuv2.ThreeTwoImageView;
import com.example.terz99.digitalmenuv2.data.OrderContract;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder>{

    private Context mContext;

    private ArrayList<OrderItem> mData;

    private boolean timesclicked = false;

    private int mIdentifier;

    public OrderAdapter(Context context, ArrayList<OrderItem> data, int identifier){
        mContext = context;
        mData = data;
        mIdentifier = identifier;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false));
    }


    private int getPixelsFromDPs(int dps) {

        Resources r = mContext.getResources();
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));

    }

    @Override
    public void onBindViewHolder(final OrderItemViewHolder holder, final int position) {

        final OrderItem currOrderItem = mData.get(position);
        final OrderAdapter.OrderItemViewHolder OVHolder = holder;

        holder.imageView.setImageResource(currOrderItem.getmImageId());
        holder.imageView.setVerticalFadingEdgeEnabled(true);
        holder.imageView.setAdjustViewBounds(true);
        holder.imageView.setScaleType(ThreeTwoImageView.ScaleType.CENTER_CROP);

        holder.nameTextView.setText(currOrderItem.getmName());

        holder.quantityTextView.setText(String.valueOf(currOrderItem.getmQuantity()));

        final double fullPrice = currOrderItem.getmPrice()*(double)currOrderItem.getmQuantity();


        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        holder.priceTextView.setText(String.valueOf(decimalFormat.format(fullPrice)) + "$");

        if(mIdentifier == BillActivity.BILL_ID){
            holder.clearButton.setVisibility(View.INVISIBLE);
        } else {

            holder.clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String selection = OrderContract.OrderEntry.COLUMN_NAME + "=?";
                    String[] selectionArgs = {
                            currOrderItem.getmName()
                    };
                    mContext.getContentResolver().delete(OrderContract.OrderEntry.CONTENT_URI, selection, selectionArgs);
                    mData.remove(position);
                    Toast.makeText(mContext, currOrderItem.getmName() + " " +  mContext.getString(R.string.deletion_successful), Toast.LENGTH_SHORT).show();
                    MyOrderActivity.deletedItem = currOrderItem;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ((mData != null) ? mData.size() : 0);
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder{

        TextView quantityTextView;
        TextView nameTextView;
        ThreeTwoImageView imageView;
        TextView priceTextView;
        LinearLayout mLinearLayout;
        ImageButton clearButton;

        public OrderItemViewHolder(View itemView) {
            super(itemView);

            quantityTextView = (TextView) itemView.findViewById(R.id.o_quantity);
            nameTextView = (TextView) itemView.findViewById(R.id.o_text);
            priceTextView = (TextView) itemView.findViewById(R.id.o_price);
            imageView = (ThreeTwoImageView) itemView.findViewById(R.id.o_image);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.o_LL);
            clearButton = (ImageButton) itemView.findViewById(R.id.clear_button);

        }
    }
}
