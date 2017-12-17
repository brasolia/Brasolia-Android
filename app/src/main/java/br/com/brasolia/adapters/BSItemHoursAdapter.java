package br.com.brasolia.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.brasolia.R;
import br.com.brasolia.models.BSOperatingHour;
import br.com.brasolia.models.BSScheduleHour;

/**
 * Created by cayke on 17/12/2017.
 */

public class BSItemHoursAdapter extends RecyclerView.Adapter {
    private List<BSScheduleHour> scheduleHours;
    private List<BSOperatingHour> operatingHours;

    public BSItemHoursAdapter(List<BSScheduleHour> scheduleHours, List<BSOperatingHour> operatingHours) {
        this.scheduleHours = scheduleHours;
        this.operatingHours = operatingHours;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour, parent, false);
        return new BSItemHourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (scheduleHours != null)
            ((BSItemHourViewHolder) holder).bindHour(scheduleHours.get(position), null, position);
        else
            ((BSItemHourViewHolder) holder).bindHour(null, operatingHours.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (scheduleHours != null)
            return scheduleHours.size();
        else
            return operatingHours.size();
    }

    public class BSItemHourViewHolder extends RecyclerView.ViewHolder {
        private TextView day, hour;
        private LinearLayout view;

        public BSItemHourViewHolder(View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.item_hour_tv_day);
            hour = itemView.findViewById(R.id.item_hour_tv_hour);
            view = itemView.findViewById(R.id.item_hour);
        }

        public void bindHour(BSScheduleHour scheduleHour, BSOperatingHour operatingHour, int position) {
            if (position % 2 == 0)
                view.setBackgroundResource(R.color.white);
            else
                view.setBackgroundResource(R.color.bs_lightGrayBackground);

            if (scheduleHour != null) {
                day.setText(scheduleHour.getDayString());
                hour.setText(scheduleHour.getHourString());
            }
            else {
                day.setText(operatingHour.getDay_week());

                if (operatingHour.isOpen())
                    hour.setText(operatingHour.getOpen_hour() + " - " + operatingHour.getClose_hour());
                else
                    hour.setText("Fechado");

            }
        }
    }
}
