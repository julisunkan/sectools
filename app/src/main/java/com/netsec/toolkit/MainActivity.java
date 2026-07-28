package com.netsec.toolkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.netsec.toolkit.adapters.ToolCardAdapter;
import com.netsec.toolkit.model.ToolEntry;
import com.netsec.toolkit.tools.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("NetSec Toolkit");

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().add(0, 1000, 0, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == 1000) startActivity(new Intent(this, SettingsActivity.class));
            drawerLayout.closeDrawers();
            return true;
        });

        RecyclerView rv = findViewById(R.id.rv_tools);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Object> displayList = buildDisplayList();
        ToolCardAdapter adapter = new ToolCardAdapter(displayList, tool ->
                startActivity(tool.createIntent(this)));
        rv.setAdapter(adapter);
    }

    private List<Object> buildDisplayList() {
        List<Object> list = new ArrayList<>();

        // ── Category 1: Network Tools ─────────────────────────────────────────
        list.add("NETWORK TOOLS");
        list.add(tool(1,  "Local Network Scanner",           "#7C4DFF", NetworkScannerActivity.class));
        list.add(tool(2,  "Wi-Fi Security Analyzer",          "#7C4DFF", WifiAnalyzerActivity.class));
        list.add(tool(3,  "IP Address Lookup",                "#7C4DFF", IpLookupActivity.class));
        list.add(tool(4,  "Ping Utility",                     "#7C4DFF", PingActivity.class));
        list.add(tool(5,  "Traceroute Utility",               "#7C4DFF", TracerouteActivity.class));
        list.add(tool(6,  "TCP Port Scanner",                 "#7C4DFF", TcpPortScannerActivity.class));
        list.add(tool(7,  "UDP Port Scanner",                 "#7C4DFF", UdpPortScannerActivity.class));
        list.add(tool(8,  "LAN Device Inventory",             "#7C4DFF", LanInventoryActivity.class));
        list.add(tool(9,  "MAC Address Vendor Lookup",        "#7C4DFF", MacVendorActivity.class));
        list.add(tool(10, "ARP Table Viewer",                 "#7C4DFF", ArpViewerActivity.class));

        // ── Category 2: DNS & Network Info ────────────────────────────────────
        list.add("DNS & NETWORK INFO");
        list.add(tool(11, "DNS Lookup Tool",                  "#0091EA", DnsLookupActivity.class));
        list.add(tool(12, "Reverse DNS Lookup",               "#0091EA", ReverseDnsActivity.class));
        list.add(tool(13, "IPv4 Subnet Calculator",           "#0091EA", IPv4CalcActivity.class));
        list.add(tool(14, "IPv6 Calculator",                  "#0091EA", IPv6CalcActivity.class));
        list.add(tool(15, "CIDR/Subnet Calculator",           "#0091EA", CidrCalcActivity.class));
        list.add(tool(16, "Packet Capture Log Viewer",        "#0091EA", PacketCaptureActivity.class));
        list.add(tool(17, "Bandwidth Usage Monitor",          "#0091EA", BandwidthMonitorActivity.class));
        list.add(tool(18, "Latency History Tracker",          "#0091EA", LatencyTrackerActivity.class));
        list.add(tool(19, "Network Interface Information",    "#0091EA", NetworkInterfaceActivity.class));
        list.add(tool(20, "Gateway & DNS Inspector",          "#0091EA", GatewayDnsActivity.class));

        // ── Category 3: Web Inspector ─────────────────────────────────────────
        list.add("WEB INSPECTOR");
        list.add(tool(21, "HTTP Header Inspector",            "#00BCD4", HttpHeaderActivity.class));
        list.add(tool(22, "HTTPS Security Checker",           "#00BCD4", HttpsCheckerActivity.class));
        list.add(tool(23, "SSL Certificate Viewer",           "#00BCD4", SslCertActivity.class));
        list.add(tool(24, "Certificate Expiry Monitor",       "#00BCD4", CertExpiryActivity.class));
        list.add(tool(25, "Security Headers Analyzer",        "#00BCD4", SecurityHeadersActivity.class));
        list.add(tool(26, "Cookie Inspector",                 "#00BCD4", CookieInspectorActivity.class));
        list.add(tool(27, "JWT Decoder",                      "#00BCD4", JwtDecoderActivity.class));
        list.add(tool(28, "JWT Signature Checker",            "#00BCD4", JwtSignatureActivity.class));
        list.add(tool(29, "Base64 Encoder/Decoder",           "#00BCD4", Base64Activity.class));
        list.add(tool(30, "URL Encoder/Decoder",              "#00BCD4", UrlEncoderActivity.class));

        // ── Category 4: Web Tools ─────────────────────────────────────────────
        list.add("WEB TOOLS");
        list.add(tool(31, "URL Parser",                       "#00C853", UrlParserActivity.class));
        list.add(tool(32, "robots.txt Viewer",                "#00C853", RobotsTxtActivity.class));
        list.add(tool(33, "sitemap.xml Analyzer",             "#00C853", SitemapActivity.class));
        list.add(tool(34, "CSP Header Checker",               "#00C853", CspCheckerActivity.class));
        list.add(tool(35, "CORS Header Inspector",            "#00C853", CorsInspectorActivity.class));
        list.add(tool(36, "HTTP Status Code Tester",          "#00C853", HttpStatusActivity.class));
        list.add(tool(37, "Website Redirect Checker",         "#00C853", RedirectCheckerActivity.class));
        list.add(tool(38, "Website Technology Detector",      "#00C853", TechDetectorActivity.class));
        list.add(tool(39, "HTML Source Viewer",               "#00C853", HtmlViewerActivity.class));
        list.add(tool(40, "Web Fingerprinting Tool",          "#00C853", WebFingerprintActivity.class));

        // ── Category 5: Password & Hashing ───────────────────────────────────
        list.add("PASSWORD & HASHING");
        list.add(tool(41, "Password Strength Checker",        "#D50000", PasswordStrengthActivity.class));
        list.add(tool(42, "Password Generator",               "#D50000", PasswordGeneratorActivity.class));
        list.add(tool(43, "Password Entropy Calculator",      "#D50000", PasswordEntropyActivity.class));
        list.add(tool(44, "Hash Generator",                   "#D50000", HashGeneratorActivity.class));
        list.add(tool(45, "Hash Identifier",                  "#D50000", HashIdentifierActivity.class));
        list.add(tool(46, "Checksum Verifier",                "#D50000", ChecksumVerifierActivity.class));
        list.add(tool(47, "AES File Encryptor",               "#D50000", AesEncryptorActivity.class));
        list.add(tool(48, "AES File Decryptor",               "#D50000", AesDecryptorActivity.class));
        list.add(tool(49, "Secure Notes Vault",               "#D50000", SecureNotesActivity.class));
        list.add(tool(50, "Encrypted Password Manager",       "#D50000", PasswordVaultActivity.class));

        // ── Category 6: Encryption Suite ─────────────────────────────────────
        list.add("ENCRYPTION SUITE");
        list.add(tool(51, "OTP Secret Backup",                "#FF6D00", OtpBackupActivity.class));
        list.add(tool(52, "RSA Key Generator",                "#FF6D00", RsaKeyGenActivity.class));
        list.add(tool(53, "RSA Encryption Demo",              "#FF6D00", RsaEncryptionActivity.class));
        list.add(tool(54, "Digital Signature Demo",           "#FF6D00", DigitalSignatureActivity.class));
        list.add(tool(55, "File Integrity Checker",           "#FF6D00", FileIntegrityActivity.class));
        list.add(tool(56, "Text Encryption Utility",          "#FF6D00", TextEncryptionActivity.class));
        list.add(tool(57, "QR Secret Storage",                "#FF6D00", QrSecretActivity.class));
        list.add(tool(58, "Cryptography Learning App",        "#FF6D00", CryptoLearningActivity.class));
        list.add(tool(59, "Secure Random Generator",          "#FF6D00", SecureRandomActivity.class));
        list.add(tool(60, "Hash Comparison Tool",             "#FF6D00", HashCompareActivity.class));

        // ── Category 7: OSINT & Recon ─────────────────────────────────────────
        list.add("OSINT & RECON");
        list.add(tool(61, "WHOIS Lookup",                     "#AA00FF", WhoisActivity.class));
        list.add(tool(62, "RDAP Client",                      "#AA00FF", RdapActivity.class));
        list.add(tool(63, "Domain Information Viewer",        "#AA00FF", DomainInfoActivity.class));
        list.add(tool(64, "DNS Record Viewer",                "#AA00FF", DnsRecordActivity.class));
        list.add(tool(65, "Certificate Transparency Search",  "#AA00FF", CertTransparencyActivity.class));
        list.add(tool(66, "Passive Subdomain Enumerator",     "#AA00FF", SubdomainEnumActivity.class));
        list.add(tool(67, "ASN Lookup",                       "#AA00FF", AsnLookupActivity.class));
        list.add(tool(68, "GeoIP Lookup",                     "#AA00FF", GeoIpActivity.class));
        list.add(tool(69, "Reverse IP Lookup",                "#AA00FF", ReverseIpActivity.class));
        list.add(tool(70, "IP Reputation Checker",            "#AA00FF", IpReputationActivity.class));

        // ── Category 8: Reputation & Breach ──────────────────────────────────
        list.add("REPUTATION & BREACH");
        list.add(tool(71, "Domain Reputation Checker",        "#00BFA5", DomainReputationActivity.class));
        list.add(tool(72, "Email Breach Checker",             "#00BFA5", EmailBreachActivity.class));
        list.add(tool(73, "Username Search Manager",          "#00BFA5", UsernameSearchActivity.class));
        list.add(tool(74, "Public DNS Comparison",            "#00BFA5", DnsComparisonActivity.class));
        list.add(tool(75, "Domain Expiration Tracker",        "#00BFA5", DomainExpiryActivity.class));
        list.add(tool(76, "Website Screenshot Organizer",     "#00BFA5", ScreenshotOrganizerActivity.class));
        list.add(tool(77, "Passive Recon Dashboard",          "#00BFA5", PassiveReconActivity.class));
        list.add(tool(78, "DNS Propagation Checker",          "#00BFA5", DnsPropagationActivity.class));
        list.add(tool(79, "Email Header Analyzer",            "#00BFA5", EmailHeaderActivity.class));
        list.add(tool(80, "Metadata Inspector",               "#00BFA5", MetadataInspectorActivity.class));

        // ── Category 9: Vulnerability Intel ──────────────────────────────────
        list.add("VULNERABILITY INTEL");
        list.add(tool(81, "CVE Search App",                   "#DD2C00", CveSearchActivity.class));
        list.add(tool(82, "CVSS Score Calculator",            "#DD2C00", CvssCalcActivity.class));
        list.add(tool(83, "NVD Vulnerability Browser",        "#DD2C00", NvdBrowserActivity.class));
        list.add(tool(84, "CISA KEV Browser",                 "#DD2C00", CisaKevActivity.class));
        list.add(tool(85, "Vulnerability Notes Manager",      "#DD2C00", VulnNotesActivity.class));
        list.add(tool(86, "Patch Tracking Tool",              "#DD2C00", PatchTrackerActivity.class));
        list.add(tool(87, "Security Bulletin Reader",         "#DD2C00", SecurityBulletinActivity.class));
        list.add(tool(88, "Exploit Reference Organizer",      "#DD2C00", ExploitReferenceActivity.class));
        list.add(tool(89, "Software Version Inventory",       "#DD2C00", SoftwareInventoryActivity.class));
        list.add(tool(90, "Asset Risk Tracker",               "#DD2C00", AssetRiskActivity.class));

        // ── Category 10: Security Management ─────────────────────────────────
        list.add("SECURITY MANAGEMENT");
        list.add(tool(91,  "Incident Response Checklist",     "#546E7A", IncidentChecklistActivity.class));
        list.add(tool(92,  "Security Audit Checklist",        "#546E7A", SecurityAuditActivity.class));
        list.add(tool(93,  "Firewall Rule Organizer",         "#546E7A", FirewallRulesActivity.class));
        list.add(tool(94,  "Device Inventory Manager",        "#546E7A", DeviceInventoryActivity.class));
        list.add(tool(95,  "Network Documentation Tool",      "#546E7A", NetworkDocsActivity.class));
        list.add(tool(96,  "Log File Viewer",                 "#546E7A", LogViewerActivity.class));
        list.add(tool(97,  "IOC Manager",                     "#546E7A", IocManagerActivity.class));
        list.add(tool(98,  "Threat Intelligence Feed Reader", "#546E7A", ThreatFeedActivity.class));
        list.add(tool(99,  "Cybersecurity Quiz App",          "#546E7A", CyberQuizActivity.class));
        list.add(tool(100, "All-in-One Cybersecurity Toolkit","#1A237E", CyberToolkitActivity.class));

        return list;
    }

    private ToolEntry tool(int n, String name, String color, Class<?> cls) {
        String cat = getCategoryName(n);
        return new ToolEntry(n, name, cat, color, cls);
    }

    private String getCategoryName(int n) {
        if (n <= 10) return "Network Tools";
        if (n <= 20) return "DNS & Network Info";
        if (n <= 30) return "Web Inspector";
        if (n <= 40) return "Web Tools";
        if (n <= 50) return "Password & Hashing";
        if (n <= 60) return "Encryption Suite";
        if (n <= 70) return "OSINT & Recon";
        if (n <= 80) return "Reputation & Breach";
        if (n <= 90) return "Vulnerability Intel";
        return "Security Management";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
