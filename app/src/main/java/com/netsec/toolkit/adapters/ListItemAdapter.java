package com.netsec.toolkit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;
import com.netsec.toolkit.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.VH> {
    private List<DatabaseHelper.ListItem> items = new ArrayList<>();
    private final OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(DatabaseHelper.ListItem item);
    }

    public ListItemAdapter(Context ctx, OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setItems(List<DatabaseHelper.ListItem> newItems) {
        items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_entry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        DatabaseHelper.ListItem item = items.get(position);
        h.tvTitle.setText(item.title != null ? item.title : "");
        h.tvSubtitle.setText(item.subtitle != null ? item.subtitle : "");
        h.tvMeta.setText(item.meta != null ? item.meta : "");
        try {
            if (item.colorTag != null && !item.colorTag.isEmpty()) {
                h.colorBar.setBackgroundColor(Color.parseColor(item.colorTag));
            }
        } catch (Exception ignored) {}
        h.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDelete(item);
        });
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle, tvMeta;
        LinearLayout colorBar;
        ImageButton btnDelete;
        VH(View v) {
            super(v);
            tvTitle    = v.findViewById(R.id.tv_title);
            tvSubtitle = v.findViewById(R.id.tv_subtitle);
            tvMeta     = v.findViewById(R.id.tv_meta);
            colorBar   = v.findViewById(R.id.color_bar);
            btnDelete  = v.findViewById(R.id.btn_delete);
        }
    }
}
