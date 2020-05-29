package com.example.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForRecordsDisplay extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BloodPressureData> memberRecords;

    public AdapterForRecordsDisplay(List<BloodPressureData> records) {
        memberRecords = records;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView pressureTextView;
        public TextView pulseTextView;

        public ViewHolder(View view) {
            super(view);

            dateTextView = (TextView) view.findViewById(R.id.recordDate);
            timeTextView = (TextView) view.findViewById(R.id.recordTime);
            pressureTextView = (TextView) view.findViewById(R.id.recordPressure);
            pulseTextView = (TextView) view.findViewById(R.id.recordPulse);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_pressure_records, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        BloodPressureData record = memberRecords.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;

        TextView textViewDate = holder.dateTextView;
        textViewDate.setText(record.dateOfRecord);
        TextView textViewTime = holder.timeTextView;
        textViewTime.setText(record.timeOfRecord);
        TextView textViewPressure = holder.pressureTextView;
        String stringPressure = record.systolicPressure + "/" + record.diastolicPressure;
        textViewPressure.setText(stringPressure);
        TextView textViewPulse = holder.pulseTextView;
        String stringPulse = String.valueOf(record.pulse);
        textViewPulse.setText(stringPulse);
    }

    @Override
    public int getItemCount() {
        return memberRecords.size();
    }
}