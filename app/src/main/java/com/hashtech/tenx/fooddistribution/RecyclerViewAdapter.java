package com.hashtech.tenx.fooddistribution;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<CustomDataType> listitem;
    private Context context;

    public RecyclerViewAdapter(List<CustomDataType> listitem, Context context) {
        this.listitem = listitem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row,parent,false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomDataType customDataType = listitem.get(position);
        holder.nameTextView.setText(customDataType.getNameOfSupplier());
        holder.phoneTextView.setText(Integer.toString(customDataType.getPhone()));
        holder.addressTextView.setText(customDataType.getAddress());
        holder.dayTextView.setText(customDataType.getDay());
        holder.timeTextView.setText(Integer.toString(customDataType.getTime()));
        holder.surplusTextView.setText(Integer.toString(customDataType.getSurplus()));

        holder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView,phoneTextView,addressTextView,dayTextView,timeTextView,surplusTextView;
        public Button requestButton;



        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            surplusTextView = itemView.findViewById(R.id.surplusTextView);
            requestButton = itemView.findViewById(R.id.requestButton);

        }
    }




}
