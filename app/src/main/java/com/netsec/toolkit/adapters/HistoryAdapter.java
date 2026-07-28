package com.netsec.toolkit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;
import com.netsec.toolkit.database.DatabaseHelper;
import com.netsec.toolkit.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {
    private List<DatabaseHelper.HistoryItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DatabaseHelper.HistoryItem item);
    }

    public HistoryAdapter(Context ctx, OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<DatabaseHelper.HistoryItem> newItems) {
        items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        DatabaseHelper.HistoryItem item = items.get(position);
        holder.tvQuery.setText(item.query != null ? item.query : "(empty)");
        String summary = item.result != null ? item.result.replace("\n", " ") : "";
        holder.tvSummary.setText(summary.length() > 120 ? summary.substring(0, 120) + "…" : summary);
        holder.tvTime.setText(FormatUtils.timeAgo(item.timestamp));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvQuery, tvSummary, tvTime;
        VH(View v) {
            super(v);
            tvQuery   = v.findViewById(R.id.tv_query);
            tvSummary = v.findViewById(R.id.tv_summary);
            tvTime    = v.findViewById(R.id.tv_time);
        }
    }
}
