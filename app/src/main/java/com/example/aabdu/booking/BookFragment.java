package com.example.aabdu.booking;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookFragment extends Fragment{

    final SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yyyy");
    private FragmentActivity myContext;
    private RecyclerView recyclerView;
    private BlocksAdapter mAdapter;

    private static final int INTERVAL = 15;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    private TimePicker picker;
    private NumberPicker minutePicker;

    private TimePicker picker2;
    private NumberPicker minutePicker2;

    public BookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_booking_fragment, container, false);

        picker = (TimePicker) view.findViewById(R.id.picker_start_time);
        picker2 = (TimePicker) view.findViewById(R.id.picker_end_time);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        setMinutePicker();
        setMinutePicker2();

        mAdapter = new BlocksAdapter(MainActivity.blockList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Block block = MainActivity.blockList.get(position);
                Toast.makeText(getContext(),"This time is " + (!block.isReserved() ? "free" : "reserved"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mAdapter.notifyDataSetChanged();

        final Button  dateButton = (Button) view.findViewById(R.id.btn_date);



        final CalendarDatePickerDialogFragment.OnDateSetListener dateSetListener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                // Set date from user input.
                Calendar date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, 9);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dateButton.setText(sdf.format(date.getTime()));
                //TODO change reserved bar
            }
        };

        dateButton.setText(sdf.format(Calendar.getInstance().getTime()));

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                MonthAdapter.CalendarDay startDay = new MonthAdapter.CalendarDay(calendar);
                calendar.add(Calendar.DATE, 14);
                MonthAdapter.CalendarDay endDay = new MonthAdapter.CalendarDay(calendar);
                calendar.add(Calendar.DATE, -14);

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(dateSetListener)
                        .setFirstDayOfWeek(Calendar.SATURDAY)
                        .setPreselectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .setDateRange(startDay,endDay)
                        .setDoneText("Done")
                        .setCancelText("Cancel");
                cdp.show(myContext.getSupportFragmentManager(), "Date Picker");
            }
        });

        final TextView durtation = (TextView) view.findViewById(R.id.textDuration);

        final TimePicker.OnTimeChangedListener pickerListener = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar date1 = Calendar.getInstance();
                date1.set(Calendar.HOUR_OF_DAY,picker.getCurrentHour());
                date1.set(Calendar.MINUTE,getMinute());

                Calendar date2 = Calendar.getInstance();
                date2.set(Calendar.HOUR_OF_DAY,picker2.getCurrentHour());
                date2.set(Calendar.MINUTE,getMinute2());

                long seconds = (date2.getTimeInMillis() - date1.getTimeInMillis()) / 1000;
                int hours = (int) seconds/3600;
                int mins = (int) (seconds/60) % 60;

                //TODO handle negative

                String diff = hours + ":" + (mins < 10 ? "0"+mins : mins) + " Hours";
                durtation.setText(diff);
            }
        };

        picker.setOnTimeChangedListener(pickerListener);
        picker2.setOnTimeChangedListener(pickerListener);

        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        return view;
    }


    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = picker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {
            return picker.getCurrentMinute();
        }
    }

    public void setMinutePicker2() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = picker2.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker2 = (NumberPicker) minute;
            minutePicker2.setMinValue(0);
            minutePicker2.setMaxValue(numValues - 1);
            minutePicker2.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute2() {
        if (minutePicker2 != null) {
            return (minutePicker2.getValue() * INTERVAL);
        } else {
            return picker2.getCurrentMinute();
        }
    }


}
