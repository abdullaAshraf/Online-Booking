package com.example.aabdu.booking;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private FragmentActivity myContext;
    private RecyclerView recyclerView;
    static BookingAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    static List<Booking> bookingsList = new ArrayList<>();

    // url to fetch menu json
    private static final String URL = "https://api.androidhive.info/json/menu.json";

    public CheckFragment() {
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
        View view = inflater.inflate(R.layout.check_booking_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        fillUserBookingList();
        mAdapter = new BookingAdapter(bookingsList, myContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 10));
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        Button btnHome = (Button) view.findViewById(R.id.btn_home);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();

                FragmentManager fm = myContext.getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.content_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }


    void fillUserBookingList(){
        DataHandler dh = new DataHandler(myContext);
        dh.getReservations(MainActivity.userEmail);

        /*
        //demo data
        bookingsList.add(new Booking("12/05/2017",3,15,5,45));
        bookingsList.add(new Booking("25/01/2018",7,45,5,30));
        bookingsList.add(new Booking("05/11/2019",12,30,18,0));
        */
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BookingAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String date = bookingsList.get(viewHolder.getAdapterPosition()).getDate();

            // backup of removed item for undo purpose
            final Booking deletedItem = bookingsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, date + " removed from schedule!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
