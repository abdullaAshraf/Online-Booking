package com.example.aabdu.booking;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BlocksAdapter extends RecyclerView.Adapter<BlocksAdapter.MyViewHolder>{
    private List<Block> blocksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public View color;

        public MyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.textTime);
            color = view.findViewById(R.id.colorIndicator);
        }
    }


    public BlocksAdapter(List<Block> blocksList) {
        this.blocksList = blocksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Block block = blocksList.get(position);
        if(position == 0 || block.isReserved() != blocksList.get(position-1).isReserved())
            holder.time.setText(block.getTime());
        else
            holder.time.setText("");
        holder.color.setBackgroundColor(block.getColor());
    }

    @Override
    public int getItemCount() {
        return blocksList.size();
    }
}
