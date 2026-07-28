package com.netsec.toolkit;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.netsec.toolkit.api.ApiConfig;

public class SettingsActivity extends AppCompatActivity {

    private ApiConfig config;

    // API setting view references — each is an include of item_setting_api.xml
    private TextInputEditText etIpApi, etMacVendors, etVirustotal, etAbuseipdb,
            etHibp, etIpinfo, etHackertarget, etNvd, etOtx, etScreenshotone,
            etWappalyzer, etBuiltwith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        config = ApiConfig.get(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("API Settings");
        }

        // Configure each setting card
        setup(R.id.setting_ip_api,       "ip-api.com Base URL",
              "Free IP geolocation API. Default: http://ip-api.com/json/ — no key needed.\nUsed by: IP Lookup, GeoIP Lookup",
              ApiConfig.KEY_IP_API, ApiConfig.DEFAULT_IP_API);

        setup(R.id.setting_macvendors,   "macvendors.com Base URL",
              "Free MAC vendor lookup. Default: https://api.macvendors.com/ — no key needed.\nUsed by: MAC Address Vendor Lookup",
              ApiConfig.KEY_MACVENDORS, ApiConfig.DEFAULT_MACVENDORS);

        setup(R.id.setting_virustotal,   "VirusTotal API Key",
              "Free tier: 500 requests/day. Get key at virustotal.com/gui/join-us\nUsed by: Domain Reputation, File Integrity",
              ApiConfig.KEY_VIRUSTOTAL, "");

        setup(R.id.setting_abuseipdb,    "AbuseIPDB API Key",
              "Free tier: 1000 checks/day. Get key at abuseipdb.com/api\nUsed by: IP Reputation Checker",
              ApiConfig.KEY_ABUSEIPDB, "");

        setup(R.id.setting_hibp,         "Have I Been Pwned API Key",
              "Paid key required. Get at haveibeenpwned.com/API/Key\nUsed by: Email Breach Checker",
              ApiConfig.KEY_HIBP, "");

        setup(R.id.setting_ipinfo,       "ipinfo.io Token",
              "Free tier: 50k requests/month. Get at ipinfo.io/signup\nUsed by: ASN Lookup",
              ApiConfig.KEY_IPINFO, "");

        setup(R.id.setting_hackertarget, "HackerTarget API Key (optional)",
              "Free tier works without a key (rate-limited). Get key at hackertarget.com\nUsed by: Reverse IP Lookup",
              ApiConfig.KEY_HACKERTARGET, "");

        setup(R.id.setting_nvd,          "NVD API Key (optional)",
              "Increases NVD rate limit. Get free key at nvd.nist.gov/developers/request-an-api-key\nUsed by: CVE Search, NVD Browser",
              ApiConfig.KEY_NVD, "");

        setup(R.id.setting_otx,          "AlienVault OTX API Key",
              "Free. Register at otx.alienvault.com, find key in Settings.\nUsed by: IOC Manager, Threat Feed",
              ApiConfig.KEY_OTX, "");

        setup(R.id.setting_screenshotone,"ScreenshotOne API Key",
              "Free tier available. Get key at screenshotone.com\nUsed by: Website Screenshot Organizer",
              ApiConfig.KEY_SCREENSHOTONE, "");

        setup(R.id.setting_wappalyzer,   "Wappalyzer API Key",
              "Paid API. Get key at wappalyzer.com/api\nUsed by: Website Technology Detector",
              ApiConfig.KEY_WAPPALYZER, "");

        setup(R.id.setting_builtwith,    "BuiltWith API Key",
              "Paid API. Get key at builtwith.com/catalog/api\nUsed by: Web Fingerprinting Tool",
              ApiConfig.KEY_BUILTWITH, "");

        findViewById(R.id.btn_save_settings).setOnClickListener(v -> saveAll());
    }

    private void setup(int viewId, String label, String desc, String prefKey, String defaultVal) {
        View card = findViewById(viewId);
        if (card == null) return;
        ((TextView) card.findViewById(R.id.tv_api_label)).setText(label);
        ((TextView) card.findViewById(R.id.tv_api_desc)).setText(desc);
        TextInputEditText et = card.findViewById(R.id.et_api_value);
        String stored = config.get(prefKey, defaultVal);
        et.setText(stored);
        // Store reference by tag
        et.setTag(prefKey);
    }

    private void saveAll() {
        int[] cardIds = {
            R.id.setting_ip_api, R.id.setting_macvendors, R.id.setting_virustotal,
            R.id.setting_abuseipdb, R.id.setting_hibp, R.id.setting_ipinfo,
            R.id.setting_hackertarget, R.id.setting_nvd, R.id.setting_otx,
            R.id.setting_screenshotone, R.id.setting_wappalyzer, R.id.setting_builtwith
        };
        for (int id : cardIds) {
            View card = findViewById(id);
            if (card == null) continue;
            TextInputEditText et = card.findViewById(R.id.et_api_value);
            String key = (String) et.getTag();
            String val = et.getText() != null ? et.getText().toString().trim() : "";
            if (key != null) config.save(key, val);
        }
        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
