package com.netsec.toolkit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;

import java.util.ArrayList;
import java.util.List;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.VH> {
    private final List<String[]> hosts = new ArrayList<>();

    public void addHost(String ip, String hostname) {
        hosts.add(new String[]{ip, hostname});
        notifyItemInserted(hosts.size() - 1);
    }

    public void clear() { hosts.clear(); notifyDataSetChanged(); }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        h.tvIp.setText(hosts.get(pos)[0]);
        h.tvHostname.setText(hosts.get(pos)[1]);
        h.tvPing.setText("UP");
    }

    @Override public int getItemCount() { return hosts.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvIp, tvHostname, tvPing;
        VH(View v) { super(v); tvIp=v.findViewById(R.id.tv_ip); tvHostname=v.findViewById(R.id.tv_hostname); tvPing=v.findViewById(R.id.tv_ping); }
    }
}
