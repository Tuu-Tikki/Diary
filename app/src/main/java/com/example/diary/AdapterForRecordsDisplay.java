package com.example.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForRecordsDisplay extends
        RecyclerView.Adapter<AdapterForRecordsDisplay.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView dateTextView;
            public TextView timeTextView;
            public TextView pressureTextView;
            public TextView pulseTextView;

            public ViewHolder(View itemView) {
                super(itemView);

                dateTextView = (TextView) itemView.findViewById(R.id.recordDate);
                timeTextView = (TextView) itemView.findViewById(R.id.recordTime);
                pressureTextView = (TextView) itemView.findViewById(R.id.recordPressure);
                pulseTextView = (TextView) itemView.findViewById(R.id.recordPulse);
            }
        }

        private List<BloodPressureData> memberRecords;

        public AdapterForRecordsDisplay(List<BloodPressureData> records){
            memberRecords = records;
        }

        @Override
        public AdapterForRecordsDisplay.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View recordView = inflater.inflate(R.layout.item_pressure_records, parent, false);

            ViewHolder viewHolder = new ViewHolder(recordView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(AdapterForRecordsDisplay.ViewHolder viewHolder, int position) {
            BloodPressureData record = memberRecords.get(position);

            TextView textViewDate = viewHolder.dateTextView;
            textViewDate.setText(record.dateOfRecord);
            TextView textViewTime = viewHolder.timeTextView;
            textViewTime.setText(record.timeOfRecord);
            TextView textViewPressure = viewHolder.pressureTextView;
            String stringPressure = record.systolicPressure + "/" + record.diastolicPressure;
            textViewPressure.setText(stringPressure);
            TextView textViewPulse = viewHolder.pulseTextView;
            String stringPulse = String.valueOf(record.pulse);
            textViewPulse.setText(stringPulse);
        }

        @Override
        public int getItemCount() {
            return memberRecords.size();
        }
}