package com.netsec.toolkit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;
import com.netsec.toolkit.model.ToolEntry;

import java.util.ArrayList;
import java.util.List;

public class ToolCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TOOL   = 1;

    private final List<Object> displayList = new ArrayList<>();
    private final OnToolClickListener listener;

    public interface OnToolClickListener {
        void onToolClick(ToolEntry tool);
    }

    public ToolCardAdapter(List<Object> displayList, OnToolClickListener listener) {
        this.displayList.addAll(displayList);
        this.listener = listener;
    }

    @Override public int getItemViewType(int pos) {
        return displayList.get(pos) instanceof String ? TYPE_HEADER : TYPE_TOOL;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            return new HeaderVH(inf.inflate(R.layout.item_category_header, parent, false));
        }
        return new ToolVH(inf.inflate(R.layout.item_tool_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderVH) {
            ((HeaderVH) holder).tvName.setText((String) displayList.get(position));
        } else {
            ToolEntry tool = (ToolEntry) displayList.get(position);
            ToolVH h = (ToolVH) holder;
            h.tvNum.setText(String.valueOf(tool.number));
            h.tvName.setText(tool.name);
            h.tvCategory.setText(tool.category);
            try {
                h.tvNum.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor(tool.color)));
                h.colorDot.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor(tool.color)));
            } catch (Exception ignored) {}
            holder.itemView.setOnClickListener(v -> listener.onToolClick(tool));
        }
    }

    @Override public int getItemCount() { return displayList.size(); }

    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView tvName;
        HeaderVH(View v) { super(v); tvName = v.findViewById(R.id.tv_category_name); }
    }

    static class ToolVH extends RecyclerView.ViewHolder {
        TextView tvNum, tvName, tvCategory;
        View colorDot;
        ToolVH(View v) {
            super(v);
            tvNum      = v.findViewById(R.id.tv_num);
            tvName     = v.findViewById(R.id.tv_name);
            tvCategory = v.findViewById(R.id.tv_category);
            colorDot   = v.findViewById(R.id.color_dot);
        }
    }
}
