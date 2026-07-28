package com.netsec.toolkit.api;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiConfig {
    private static final String PREFS = "api_config";

    public static final String KEY_IP_API       = "ip_api_base";
    public static final String KEY_MACVENDORS   = "macvendors_base";
    public static final String KEY_VIRUSTOTAL   = "virustotal_key";
    public static final String KEY_ABUSEIPDB    = "abuseipdb_key";
    public static final String KEY_HIBP         = "hibp_key";
    public static final String KEY_IPINFO       = "ipinfo_token";
    public static final String KEY_HACKERTARGET = "hackertarget_key";
    public static final String KEY_NVD          = "nvd_key";
    public static final String KEY_OTX          = "otx_key";
    public static final String KEY_SCREENSHOTONE= "screenshotone_key";
    public static final String KEY_WAPPALYZER   = "wappalyzer_key";
    public static final String KEY_BUILTWITH    = "builtwith_key";

    // Defaults (free, no-key endpoints)
    public static final String DEFAULT_IP_API      = "http://ip-api.com/json/";
    public static final String DEFAULT_MACVENDORS  = "https://api.macvendors.com/";

    private final SharedPreferences prefs;

    private static ApiConfig instance;
    public static ApiConfig get(Context ctx) {
        if (instance == null) instance = new ApiConfig(ctx.getApplicationContext());
        return instance;
    }

    private ApiConfig(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public String ipApi()       { return prefs.getString(KEY_IP_API, DEFAULT_IP_API); }
    public String macVendors()  { return prefs.getString(KEY_MACVENDORS, DEFAULT_MACVENDORS); }
    public String virusTotal()  { return prefs.getString(KEY_VIRUSTOTAL, ""); }
    public String abuseIpDb()   { return prefs.getString(KEY_ABUSEIPDB, ""); }
    public String hibp()        { return prefs.getString(KEY_HIBP, ""); }
    public String ipInfo()      { return prefs.getString(KEY_IPINFO, ""); }
    public String hackerTarget(){ return prefs.getString(KEY_HACKERTARGET, ""); }
    public String nvd()         { return prefs.getString(KEY_NVD, ""); }
    public String otx()         { return prefs.getString(KEY_OTX, ""); }
    public String screenshotOne(){ return prefs.getString(KEY_SCREENSHOTONE, ""); }
    public String wappalyzer()  { return prefs.getString(KEY_WAPPALYZER, ""); }
    public String builtWith()   { return prefs.getString(KEY_BUILTWITH, ""); }

    public void save(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String get(String key, String def) {
        return prefs.getString(key, def);
    }
}
