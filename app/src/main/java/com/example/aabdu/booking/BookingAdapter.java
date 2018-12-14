package com.example.aabdu.booking;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder>{
    private List<Booking> bookingsList;
    private DataHandler dh;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView stime;
        public TextView etime;
        public TextView date;
        public View viewForeground;


        public MyViewHolder(View view) {
            super(view);
            stime = view.findViewById(R.id.start_time);
            etime = view.findViewById(R.id.end_time);
            date = view.findViewById(R.id.date);
            viewForeground = view.findViewById(R.id.foreground);
        }
    }


    public BookingAdapter(List<Booking> bookingsList , Context mcontext) {
        dh = new DataHandler(mcontext);
        this.bookingsList = bookingsList;
    }

    public void setItems(List<Booking> bookingsList) {
        this.bookingsList = bookingsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Booking booking = bookingsList.get(position);
        holder.stime.setText(booking.getTime(true));
        holder.etime.setText(booking.getTime(false));
        holder.date.setText(booking.getDate());
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public void removeItem(int position) {
        bookingsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        notifyItemRemoved(position);
        dh.deleteReservation(bookingsList.get(position).getDateTime());
    }

    public void restoreItem(Booking item, int position) {
        bookingsList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
        dh.addReservation(MainActivity.userEmail,item.getDateTime(),item.getDur());
    }
}
