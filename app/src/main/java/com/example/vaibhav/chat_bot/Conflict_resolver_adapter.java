package com.example.vaibhav.chat_bot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 2/2/18.
 */
public class Conflict_resolver_adapter extends RecyclerView.Adapter<Conflict_resolver_adapter.MyViewHolder> {
    private final Context context;
    private List<Call_Information> items;

    public Conflict_resolver_adapter(List<Call_Information> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conflict_resolver, parent, false);
        return new MyViewHolder(v, context, items);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Call_Information item = items.get(position);
        //TODO Fill in your logic for binding the view.
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public List<Call_Information> arraylist = new ArrayList<>();
        public Context ctx;

        public MyViewHolder(View view, Context ctx, List<Call_Information> arraylist) {
            super(view);
            this.ctx = ctx;
            this.arraylist = arraylist;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

        }
    }

}