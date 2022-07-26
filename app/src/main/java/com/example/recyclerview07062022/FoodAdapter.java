package com.example.recyclerview07062022;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    List<Food> foodList;
    Context context;
    OnListenerItemClick onListenerItemClick;

    public FoodAdapter(List<Food> foodList, Context context) {

        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_item_food, parent, false);

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return (foodList == null || foodList.size() == 0) ? 0 : foodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvCloseTime, tvAddress, tvServiceKind, tvDistance, tvDiscount;
        ImageView img;
        List<ServiceKind> serviceKindList;
        int hourCurrent, minutesCurrent;
        Calendar calendar;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.text_view_address);
            tvName = itemView.findViewById(R.id.text_view_name);
            tvCloseTime = itemView.findViewById(R.id.text_view_title_close_time);
            tvServiceKind = itemView.findViewById(R.id.text_view_service_kind);
            tvDistance = itemView.findViewById(R.id.text_view_distance);
            tvDiscount = itemView.findViewById(R.id.text_view_discount);
            img = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListenerItemClick.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(Food food){
            img.setImageResource(food.getImage());
            tvName.setText(food.getName());
            tvAddress.setText(food.getAddress());
            tvDistance.setText(String.format(">%.1f km",food.getDistance()));

            // List Service Kind
            serviceKindList = food.getServiceKindList();
            if(serviceKindList==null || serviceKindList.size()==0){
                tvServiceKind.setVisibility(View.GONE);
            }else{
                if(serviceKindList.size() == 1){
                    tvServiceKind.setText(food.getServiceKindList().get(0).toString());
                }else{
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    for (int i = 0; i < food.getServiceKindList().size(); i++){
                        if(i==food.getServiceKindList().size()-1){
                            builder.append(food.getServiceKindList().get(i).toString());
                        }else{
                            builder.append(food.getServiceKindList().get(i).toString());
                            builder.append(" - ");
                        }
                    }
                    tvServiceKind.setVisibility(View.VISIBLE);
                    tvServiceKind.setText(builder);
                }
            }

            // Discount
            if(!food.getDiscount().isEmpty()){
                tvDiscount.setVisibility(View.VISIBLE);
                tvDiscount.setText(food.getDiscount());
            }else{
                tvDiscount.setVisibility(View.GONE);
            }

            // Close time
            calendar = Calendar.getInstance();
            hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
            minutesCurrent = calendar.get(Calendar.MINUTE);

            // 7:00 -> 23:00
            // 8: 03
            if (!checkCurrentTimeOver(food.getHourOpenTime(), hourCurrent, food.getHourCloseTime())) {

                if(hourCurrent == food.getHourOpenTime()){
                    if (minutesCurrent < food.getMinutesOpenTime()) {
                        tvCloseTime.setVisibility(View.VISIBLE);
                    }else{
                        tvCloseTime.setVisibility(View.GONE);
                    }
                }else if(hourCurrent == food.getHourCloseTime()){
                    if (minutesCurrent >= food.getMinutesCloseTime()) {
                        tvCloseTime.setVisibility(View.VISIBLE);
                    }else {
                        tvCloseTime.setVisibility(View.GONE);
                    }
                }else{
                    tvCloseTime.setVisibility(View.GONE);
                }

            }else{
                tvCloseTime.setVisibility(View.VISIBLE);

            }

        }
    }

    private boolean checkCurrentTimeOver(int timeOpen, int timeCurrent, int timeClose) {
        return ((timeCurrent < timeOpen) || (timeClose < timeCurrent));
    }

    public void setOnClickListener(OnListenerItemClick onListenerItemClick){
        this.onListenerItemClick = onListenerItemClick;
    }

    interface OnListenerItemClick{
        void onClick(int position);
    }
}
