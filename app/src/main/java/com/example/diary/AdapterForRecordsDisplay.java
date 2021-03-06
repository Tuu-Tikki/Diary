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

    //the constructor
    public AdapterForRecordsDisplay(List<BloodPressureData> records) {
        memberRecords = records;
    }

    //View holder is in charge of displaying a single item with a view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView pressureTextView;
        public TextView pulseTextView;

        public ViewHolder(View view) {
            super(view);

            dateTextView = (TextView) view.findViewById(R.id.record_date);
            timeTextView = (TextView) view.findViewById(R.id.record_time);
            pressureTextView = (TextView) view.findViewById(R.id.record_pressure);
            pulseTextView = (TextView) view.findViewById(R.id.record_pulse);
        }
    }

    //onCreateViewHolder needs to construct a RecyclerView.ViewHolder and set the view it uses to display its contents.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_pressure_records, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //onBindViewHolder needs to fetch the appropriate data, and use it to fill in the view holder's layout
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        BloodPressureData record = memberRecords.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;

        TextView textViewDate = holder.dateTextView;
        textViewDate.setText(dateToDayMonthYear(record.dateOfRecord));
        TextView textViewTime = holder.timeTextView;
        textViewTime.setText(record.timeOfRecord);
        TextView textViewPressure = holder.pressureTextView;
        String stringPressure = record.systolicPressure + "/" + record.diastolicPressure;
        textViewPressure.setText(stringPressure);
        TextView textViewPulse = holder.pulseTextView;
        String stringPulse = String.valueOf(record.pulse);
        textViewPulse.setText(stringPulse);
    }

    //return the size of the dataset
    @Override
    public int getItemCount() {
        return memberRecords.size();
    }

    //change date from year/month/day to day/month/year
    public String dateToDayMonthYear(String date) {
        String reverseDate = "";
        reverseDate = date.substring(date.length()-2, date.length()) +
                date.substring(date.length()-6, date.length()-2) +
                date.substring(0, 4);
        return reverseDate;
    }
}
