package com.netsec.toolkit.tools;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;
import com.netsec.toolkit.adapters.WifiNetworkAdapter;
import com.netsec.toolkit.utils.NetworkUtils;

import java.util.List;

public class WifiAnalyzerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tool);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Wi-Fi Security Analyzer");
        }
        toolbar.setBackgroundColor(android.graphics.Color.parseColor("#7C4DFF"));

        RecyclerView rv = findViewById(R.id.rv_items);
        rv.setLayoutManager(new LinearLayoutManager(this));

        WifiNetworkAdapter adapter = new WifiNetworkAdapter();
        rv.setAdapter(adapter);

        // FAB = rescan
        findViewById(R.id.fab_add).setOnClickListener(v -> loadNetworks(adapter));
        loadNetworks(adapter);
    }

    private void loadNetworks(WifiNetworkAdapter adapter) {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) return;
        // Connected network
        WifiInfo connected = wm.getConnectionInfo();
        List<ScanResult> results = wm.getScanResults();
        adapter.setNetworks(results, connected != null ? connected.getSSID() : "");
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
