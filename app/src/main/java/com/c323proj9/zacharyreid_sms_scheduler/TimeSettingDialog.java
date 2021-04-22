package com.c323proj9.zacharyreid_sms_scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;

public class TimeSettingDialog extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener, TimePickerDialog.OnClickListener {
    private static final String HOURS_KEY = "hours";
    private static final String MINUTES_KEY = "minutes";
    private static final String TARGET_KEY = "target";

    public static TimeSettingDialog newInstance(int hours, int minutes, int targetResId) {
        TimeSettingDialog dialog = new TimeSettingDialog();
        Bundle args = new Bundle();
        args.putInt(HOURS_KEY, hours);
        args.putInt(MINUTES_KEY, minutes);
        args.putInt(TARGET_KEY, targetResId);
        dialog.setArguments(args);
        return dialog;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        Calendar now = Calendar.getInstance();
        int hour = getArguments().getInt(HOURS_KEY, now.get(Calendar.HOUR_OF_DAY));
        int minute = getArguments().getInt(MINUTES_KEY, now.get(Calendar.MINUTE));
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
