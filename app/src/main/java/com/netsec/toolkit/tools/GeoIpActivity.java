package com.netsec.toolkit.tools;

import com.netsec.toolkit.api.ApiConfig;
import com.netsec.toolkit.base.BaseToolActivity;
import com.netsec.toolkit.utils.FormatUtils;
import com.netsec.toolkit.utils.HttpUtil;

public class GeoIpActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "GeoIP Lookup"; }
    @Override protected String getCategoryColor() { return "#00BFA5"; }
    @Override protected String getExecuteLabel() { return "GeoIP Lookup"; }
    @Override protected String[] getInputHints() { return new String[]{"IP address (blank = your IP)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String ip = inputs[0].trim();
        try {
            String url = ApiConfig.get(this).ipApi() + ip + "?fields=status,message,continent,continentCode,country,countryCode,region,regionName,city,district,zip,lat,lon,timezone,offset,currency,isp,org,as,asname,reverse,mobile,proxy,hosting,query";
            String raw = HttpUtil.get(url);
            cb.onResult("GeoIP Lookup: " + (ip.isEmpty() ? "(your IP)" : ip) + "\n\n" + FormatUtils.prettyJson(raw));
        } catch (Exception e) { cb.onError(e.getMessage()); }
    }
}
