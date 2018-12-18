package com.example.arnesh07.help;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFrag extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    timePickerListener caller;

    public interface timePickerListener{
       void startCountdown(int secondsRemaining);
       void goToHelp();
       void getLocation();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (timePickerListener) context;
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

       // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // LayoutInflater inflater = getActivity().getLayoutInflater();


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //Log.v("hour abhi",Integer.toString(minute));
        /*final View v=inflater.inflate(R.layout.time_picker, null);
        builder.setView(v)
        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // sign in the user ...
               EditText hoursText = (EditText)v.findViewById(R.id.hours);
               EditText minutesText=(EditText)v.findViewById(R.id.mins);
               String hoursString=hoursText.getText().toString();
               String minutesString=minutesText.getText().toString();
               int hours=Integer.parseInt(hoursString);
               int minutes=Integer.parseInt(minutesString);

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TimePickerFrag.this.getDialog().cancel();
                    }
                });
        return builder.create();
          */
        // Create a new instance of TimePickerDialog and return it

        //return new TimePickerDialog(getActivity(), this, hour, minute,
              //  DateFormat.is24HourFormat(getActivity()));

        TimePickerDialog tmp =new TimePickerDialog(getActivity(), this, hour, minute,false);;
         return tmp;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Time Obtained
        //Log.v("hour",Integer.toString(hourOfDay));
        //Log.v("minute",Integer.toString(minute));
        final Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        int secondsRemaining;
        if(hourOfDay>currentHour){
            secondsRemaining=((hourOfDay-currentHour)*60 + (minute-currentMinute))*60;
        }
        else if(hourOfDay<currentHour){
            secondsRemaining=((24-(currentHour-hourOfDay))*60+ (minute-currentMinute))*60;
        }
        else{
            if(currentMinute<minute){
                secondsRemaining=(minute-currentMinute)*60;
            }
            else if(currentMinute>minute){
                 secondsRemaining=24*3600+ (minute-currentMinute)*60;
            }
            else{
                secondsRemaining=24*3600;
            }
        }
        caller.startCountdown(secondsRemaining);
        //Now location services will start and the user will be tracked.
        caller.getLocation();
        caller.goToHelp();
        Log.v("Seconds",Integer.toString(secondsRemaining));
    }
}
