package com.hiit.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<DataModel> dataModelArrayList;


    public RecyclerViewAdapter(Context ctx, ArrayList<DataModel> dataModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recyclerview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.name.setText(dataModelArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {

        return dataModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public MyViewHolder(final View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataModel dataModel = dataModelArrayList.get(getAdapterPosition());

                    showPopup(dataModel);
/*                    Toast.makeText(itemView.getContext(),"Element " + getAdapterPosition() + " clicked", Toast.LENGTH_SHORT).show();
                    Log.d("hello", "Element " + getAdapterPosition() + " clicked.");*/
                }
            });
        }

    }


    public void showPopup(DataModel dataModel) {
        final View popupView = inflater.inflate(R.layout.popup, null);

        PopUp popup = new PopUp();
        popup.showPopupWindow(popupView, dataModel);

    }

}