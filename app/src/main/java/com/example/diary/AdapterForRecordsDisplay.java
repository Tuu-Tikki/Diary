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

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_RECORD = 1;

        private List<BloodPressureData> memberRecords;

        public AdapterForRecordsDisplay(List<BloodPressureData> records){
            memberRecords = records;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            if (viewType == TYPE_RECORD) {
                View recordView = inflater.inflate(R.layout.item_pressure_records, parent, false);
                RecordViewHolder viewHolder = new RecordViewHolder(recordView);
                return viewHolder;
            } else if (viewType == TYPE_HEADER) {
                View headerView = inflater.inflate(R.layout.header, parent, false);
                HeaderViewHolder viewHolder = new HeaderViewHolder(headerView);
                return viewHolder;
            }
            else return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof HeaderViewHolder) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

                TextView textViewDate = headerViewHolder.dateTextView;
                textViewDate.setText(R.string.column_date);
                TextView textViewTime = headerViewHolder.timeTextView;
                textViewTime.setText(R.string.column_time);
                TextView textViewPressure = headerViewHolder.pressureTextView;
                textViewPressure.setText(R.string.column_pressure);
                TextView textViewPulse = headerViewHolder.pulseTextView;
                textViewPulse.setText(R.string.column_pulse);
            }
            else if (viewHolder instanceof RecordViewHolder) {
                BloodPressureData record = memberRecords.get(position-1);
                RecordViewHolder recordViewHolder = (RecordViewHolder) viewHolder;

                TextView textViewDate = recordViewHolder.dateTextView;
                textViewDate.setText(record.dateOfRecord);
                TextView textViewTime = recordViewHolder.timeTextView;
                textViewTime.setText(record.timeOfRecord);
                TextView textViewPressure = recordViewHolder.pressureTextView;
                String stringPressure = record.systolicPressure + "/" + record.diastolicPressure;
                textViewPressure.setText(stringPressure);
                TextView textViewPulse = recordViewHolder.pulseTextView;
                String stringPulse = String.valueOf(record.pulse);
                textViewPulse.setText(stringPulse);
            }
        }

        @Override
        public int getItemCount() {
            return memberRecords.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (0 == position) {
            return TYPE_HEADER;
            }
            return TYPE_RECORD;
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            public TextView dateTextView;
            public TextView timeTextView;
            public TextView pressureTextView;
            public TextView pulseTextView;

            public HeaderViewHolder(View headerView) {
                super(headerView);

                dateTextView = (TextView) headerView.findViewById(R.id.headerDate);
                timeTextView = (TextView) headerView.findViewById(R.id.headerTime);
                pressureTextView = (TextView) headerView.findViewById(R.id.headerPressure);
                pulseTextView = (TextView) headerView.findViewById(R.id.headerPulse);
            }
        }

        public class RecordViewHolder extends RecyclerView.ViewHolder {
            public TextView dateTextView;
            public TextView timeTextView;
            public TextView pressureTextView;
            public TextView pulseTextView;

            public RecordViewHolder(View recordView) {
                super(recordView);

                dateTextView = (TextView) recordView.findViewById(R.id.recordDate);
                timeTextView = (TextView) recordView.findViewById(R.id.recordTime);
                pressureTextView = (TextView) recordView.findViewById(R.id.recordPressure);
                pulseTextView = (TextView) recordView.findViewById(R.id.recordPulse);
        }
    }
}