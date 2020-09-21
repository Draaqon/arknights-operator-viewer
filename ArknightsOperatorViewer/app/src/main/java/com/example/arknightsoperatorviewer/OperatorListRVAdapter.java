package com.example.arknightsoperatorviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OperatorListRVAdapter extends RecyclerView.Adapter<OperatorListRVAdapter.ViewHolder> {

    private static final String TAG = "OperatorListRVAdapter";

    private ArrayList<OperatorInfo> opDetailList;
    private OnOperatorListener opList;
    private Context context;

    public OperatorListRVAdapter(Context context, ArrayList<OperatorInfo> opDetailList, OnOperatorListener opList)
    {
        this.opDetailList = opDetailList;
        this.context = context;
        this.opList = opList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_operatorlistitem, parent, false);
        ViewHolder holder = new ViewHolder(view, opList);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = "";
        if (opDetailList.get(position).getName().toLowerCase().equals("ch\'en".toLowerCase()))
        {
            name = "chen";
        }
        else
        {
            name = opDetailList.get(position).getName();
        }
        int id = context.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + name.toLowerCase(), null, null);
        holder.image.setImageResource(id);
        holder.operatorName.setText(opDetailList.get(position).getName().toUpperCase());
        String stars = "";
        for(int i = 0; i < opDetailList.get(position).getStars(); i++)
        {
            stars += "★";
        }

        holder.stars.setText(stars);
    }

    @Override
    public int getItemCount()
    {
        return opDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView image;
        TextView operatorName, stars;
        RelativeLayout parentLayout;
        OnOperatorListener onOpListener;

        public ViewHolder(@NonNull View itemView, OnOperatorListener onOpListener) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            operatorName = itemView.findViewById(R.id.operator_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            stars = itemView.findViewById(R.id.txtStars);
            this.onOpListener = onOpListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            onOpListener.onOperatorClick(getAdapterPosition());
        }
    }

    public interface OnOperatorListener
    {
        void onOperatorClick(int position);
    }

}
