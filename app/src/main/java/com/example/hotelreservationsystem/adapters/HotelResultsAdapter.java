package com.example.hotelreservationsystem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.OnClickListener;
import com.example.hotelreservationsystem.R;
import com.example.hotelreservationsystem.models.HotelData;

import java.util.List;

public class HotelResultsAdapter extends RecyclerView.Adapter<HotelResultsAdapter.ViewHolder> {
    private List<HotelData> hotelDataList;
    private LayoutInflater layoutInflater;
    private OnClickListener clickListener;

    public HotelResultsAdapter(Context context, List<HotelData> hotelDataList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.hotelDataList = hotelDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hotel_detail_search_results_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String hotelName = hotelDataList.get(position).getName();
        String hotelPrice = hotelDataList.get(position).getPrice();
        String hotelAvailability = hotelDataList.get(position).getAvailability();

        holder.hotelName.setText(hotelName);
        holder.hotelPrice.setText("$"+hotelPrice);

        ForegroundColorSpan green = new ForegroundColorSpan(Color.GREEN);
        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);

        if(hotelAvailability=="true") {
            SpannableString availability = new SpannableString("\u2022 Available");
            availability.setSpan(green,
                    0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.hotelAvailability.setText(availability);
        }
        else {
            SpannableString availability = new SpannableString("\u2022 Not Available");
            availability.setSpan(red,
                    0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.hotelAvailability.setText(availability);
        }
        if(hotelAvailability == "false") {
            holder.bookButton.setEnabled(false);
            holder.bookButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(hotelDataList != null) {
            return hotelDataList.size();
        }
        return 0;
    }

    public void setItemClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hotelName, hotelPrice, hotelAvailability;
        Button bookButton;

        public ViewHolder(@NonNull View view) {
            super(view);

            hotelName = view.findViewById(R.id.hotel_name_search_results_text_view);
            hotelPrice = view.findViewById(R.id.hotel_price_search_results_text_view);
            hotelAvailability = view.findViewById(R.id.hotel_availability_search_results_text_view);

            bookButton = view.findViewById(R.id.book_hotel_search_results_button);

            bookButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null) {
                clickListener.onClick(view, getAbsoluteAdapterPosition());
            }
        }
    }
}
