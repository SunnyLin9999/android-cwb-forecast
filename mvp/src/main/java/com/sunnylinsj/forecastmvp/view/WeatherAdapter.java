package com.sunnylinsj.forecastmvp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnylinsj.forecastmvp.R;
import com.sunnylinsj.forecastmvp.model.WeatherInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    private final String TAG = WeatherAdapter.class.getName();

    private List<WeatherInfo> mData;

    public WeatherAdapter(List<WeatherInfo> data){
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item,parent, false);

        ViewHolder holder = new ViewHolder(view);
        holder.mCradView = view.findViewById(R.id.card_view);
        holder.mTvTime = view.findViewById(R.id.time);
        holder.mIvIcon = view.findViewById(R.id.icon);
        holder.mTvWx = view.findViewById(R.id.wx);
        holder.mTvPoP = view.findViewById(R.id.pop);
        holder.mTvCI = view.findViewById(R.id.ci);
        holder.mTvTempRange = view.findViewById(R.id.mint_maxt);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherInfo info = mData.get(position);

        //set forecast start time
        String inputStartTime = info.getStartTime();
        String inPattern = "yyyy-MM-dd HH:mm:ss";
        String outPattern = "MM/dd HH:mm";
        SimpleDateFormat inputSdf = new SimpleDateFormat(inPattern);
        SimpleDateFormat outputSdf = new SimpleDateFormat(outPattern);
        String reformattedStr = null;
        try {
            Date inputDate = inputSdf.parse(inputStartTime);
            reformattedStr = outputSdf.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mTvTime.setText(reformattedStr);

        //Set image view
        int pop = Integer.valueOf(info.getPop());
        if(pop < 20) {
            holder.mIvIcon.setImageResource(R.drawable.ic_wb_sunny);
        } else if(pop > 70) {
            holder.mIvIcon.setImageResource(R.drawable.ic_wb_raining);
        } else {
            holder.mIvIcon.setImageResource(R.drawable.ic_wb_cloudy);
        }

        holder.mTvWx.setText(info.getWx());
        holder.mTvPoP.setText(info.getPop() + "%");
        holder.mTvCI.setText(info.getCi());
        holder.mTvTempRange.setText(info.getMint() + "° ~ " +info.getMaxt() + "°");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mCradView;
        public TextView mTvTime;
        public ImageView mIvIcon;
        public TextView mTvWx;
        public TextView mTvPoP;
        public TextView mTvCI;
        public TextView mTvTempRange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
