package com.netsec.toolkit.tools;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netsec.toolkit.R;
import com.netsec.toolkit.adapters.HostAdapter;
import com.netsec.toolkit.utils.NetworkUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkScannerActivity extends AppCompatActivity {
    private ProgressBar progressScan;
    private TextView tvSubnet, tvScanStatus, tvFound;
    private Button btnScan, btnStop;
    private RecyclerView rvHosts;
    private HostAdapter adapter;
    private ExecutorService executor;
    private final AtomicBoolean scanning = new AtomicBoolean(false);
    private final AtomicInteger scanned = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_scanner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Local Network Scanner");
        }
        progressScan  = findViewById(R.id.progress_scan);
        tvSubnet      = findViewById(R.id.tv_subnet);
        tvScanStatus  = findViewById(R.id.tv_scan_status);
        tvFound       = findViewById(R.id.tv_found);
        btnScan       = findViewById(R.id.btn_scan);
        btnStop       = findViewById(R.id.btn_stop);
        rvHosts       = findViewById(R.id.rv_hosts);
        rvHosts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HostAdapter();
        rvHosts.setAdapter(adapter);

        String subnet = NetworkUtils.getSubnet(this);
        tvSubnet.setText(subnet != null ? "Network: " + subnet + ".x/24" : "No WiFi connection — connect to WiFi first");

        btnScan.setOnClickListener(v -> startScan(subnet));
        btnStop.setOnClickListener(v -> { scanning.set(false); btnStop.setEnabled(false); });
    }

    private void startScan(String subnet) {
        if (subnet == null) { tvScanStatus.setText("Connect to WiFi first"); return; }
        adapter.clear();
        scanned.set(0);
        scanning.set(true);
        btnScan.setEnabled(false);
        btnStop.setEnabled(true);
        progressScan.setVisibility(View.VISIBLE);
        progressScan.setProgress(0);
        tvFound.setText("Scanning...");

        executor = Executors.newFixedThreadPool(30);
        for (int i = 1; i <= 254; i++) {
            final String ip = subnet + "." + i;
            executor.submit(() -> {
                if (!scanning.get()) return;
                try {
                    InetAddress addr = InetAddress.getByName(ip);
                    boolean reached  = addr.isReachable(1500);
                    int done = scanned.incrementAndGet();
                    runOnUiThread(() -> {
                        progressScan.setProgress(done);
                        tvScanStatus.setText("Scanning " + ip + "... (" + done + "/254)");
                    });
                    if (reached) {
                        String hostname = addr.getHostName();
                        runOnUiThread(() -> {
                            adapter.addHost(ip, hostname.equals(ip) ? "" : hostname);
                            tvFound.setText("Found: " + adapter.getItemCount() + " host(s)");
                        });
                    }
                } catch (Exception ignored) {}
                if (scanned.get() >= 254) runOnUiThread(this::scanComplete);
            });
        }
    }

    private void scanComplete() {
        scanning.set(false);
        btnScan.setEnabled(true);
        btnStop.setEnabled(false);
        progressScan.setVisibility(View.GONE);
        tvScanStatus.setText("Scan complete.");
        tvFound.setText("Found: " + adapter.getItemCount() + " host(s)");
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
    @Override protected void onDestroy() { super.onDestroy(); if (executor != null) executor.shutdownNow(); }
}
