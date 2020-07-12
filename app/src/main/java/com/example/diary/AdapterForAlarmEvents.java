package com.example.diary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class AdapterForAlarmEvents extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AlarmEvents> displayedEvents;

    //the constructor
    public AdapterForAlarmEvents(List<AlarmEvents> events) {
        displayedEvents = events;
    }

    //View holder is in charge of displaying a single item with a view
    public  class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox measurement;
        public CheckBox pills;
        public TextView time;
        public ImageButton delete;

        public ViewHolder (View view) {
            super(view);

            measurement = (CheckBox) view.findViewById(R.id.is_measurement_true);
            pills = (CheckBox) view.findViewById(R.id.is_pills_true);
            time = (TextView) view.findViewById(R.id.time_of_alarm);
            delete = (ImageButton) view.findViewById(R.id.delete_alarm_button);
        }
    }

    //onCreateViewHolder needs to construct a RecyclerView.ViewHolder and set the view it uses to display its contents.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_alarm_events, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    //onBindViewHolder needs to fetch the appropriate data, and use it to fill in the view holder's layout
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final AlarmEvents event = displayedEvents.get(position);
        final ViewHolder holder = (ViewHolder) viewHolder;

        CheckBox checkBoxMeasurement = holder.measurement;
        checkBoxMeasurement.setChecked(event.measurement);
        CheckBox checkBoxPills = holder.pills;
        checkBoxPills.setChecked(event.pills);
        TextView alarmTime = holder.time;
        alarmTime.setText(event.timeOfAlarm);

        //delete the record from db and from the list when the button delete pressed
        final ImageButton deleteButton = holder.delete;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlarmDatabase db = Room.databaseBuilder(holder.itemView.getContext(),
                        AlarmDatabase.class, "AlarmEvents")
                        .build();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.alarmDao().delete(event);
                    }
                });

                //cancel the corresponding alarm
                Context context = deleteButton.getContext();
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                int requestCode = event.alarmEventId;
                final Intent intentForAlarmReceiver = new Intent(context, AlarmReceiver.class);
                intentForAlarmReceiver.putExtra("RequestCode", requestCode);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                        intentForAlarmReceiver, 0);
                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);

                //update the list of events for RecyclerView
                displayedEvents.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, displayedEvents.size());
            }
        });
    }

    //return the size of the dataset
    @Override
    public int getItemCount() {
        return displayedEvents.size();
    }
}
