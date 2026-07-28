package com.netsec.toolkit.adapters;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;

import java.util.ArrayList;
import java.util.List;

public class WifiNetworkAdapter extends RecyclerView.Adapter<WifiNetworkAdapter.VH> {
    private List<ScanResult> networks = new ArrayList<>();
    private String connectedSsid = "";

    public void setNetworks(List<ScanResult> results, String connected) {
        networks = results != null ? results : new ArrayList<>();
        connectedSsid = connected != null ? connected : "";
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ScanResult r = networks.get(pos);
        String ssid = r.SSID.isEmpty() ? "(hidden)" : r.SSID;
        h.tvTitle.setText(ssid);
        boolean isConnected = ("\"" + ssid + "\"").equals(connectedSsid) || ssid.equals(connectedSsid);
        String security = getSecurity(r.capabilities);
        int bars = signalBars(r.level);
        h.tvSubtitle.setText(security + "   Channel: " + getChannel(r.frequency) + "   " + r.frequency + " MHz");
        h.tvMeta.setText("Signal: " + r.level + " dBm  [" + "▉".repeat(bars) + "░".repeat(4-bars) + "]" + (isConnected ? "  ★ CONNECTED" : ""));
        String color = security.contains("WPA3") ? "#00E676" : security.contains("WPA2") ? "#40C4FF" :
                       security.contains("WPA")  ? "#FFD740" : security.contains("WEP") ? "#FF6D00" : "#FF5252";
        h.colorBar.setBackgroundColor(Color.parseColor(color));
        h.btnDelete.setVisibility(View.GONE);
    }

    @Override public int getItemCount() { return networks.size(); }

    private String getSecurity(String cap) {
        if (cap.contains("WPA3")) return "WPA3";
        if (cap.contains("WPA2")) return "WPA2";
        if (cap.contains("WPA"))  return "WPA";
        if (cap.contains("WEP"))  return "WEP";
        return "OPEN";
    }

    private int signalBars(int level) {
        if (level >= -55) return 4;
        if (level >= -66) return 3;
        if (level >= -77) return 2;
        return 1;
    }

    private int getChannel(int freq) {
        if (freq >= 2412 && freq <= 2484) return (freq - 2407) / 5;
        if (freq >= 5170 && freq <= 5825) return (freq - 5000) / 5;
        return 0;
    }

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
