# NetSec Toolkit

A comprehensive Android cybersecurity app with **100 working security tools** organized into 10 categories.

## Project Overview

**NetSec Toolkit** is a native Android app (Java, API 24–34) featuring 100 security tools across networking, web analysis, cryptography, OSINT, vulnerability intelligence, and security management. All data is stored locally in SQLite. No root required. Many tools work fully offline.

## Tech Stack

- **Language:** Java
- **Min SDK:** 24 / Target SDK: 34
- **Build:** Gradle with View Binding
- **UI:** Material Design 3 (dark cybersecurity theme)
- **Charts:** MPAndroidChart
- **QR:** ZXing
- **Architecture:** Single-module, activity-based
- **Storage:** SQLite via DatabaseHelper (shared history + items tables)

## Package Structure

```
com.netsec.toolkit/
├── App.java                   # Application class
├── SplashActivity.java
├── MainActivity.java          # Navigation drawer + 100-tool grid
├── SettingsActivity.java      # API key configuration
├── api/ApiConfig.java         # API key storage (SharedPreferences)
├── base/
│   ├── BaseToolActivity.java  # Shared lookup/analysis tool base
│   └── BaseListActivity.java  # Shared list/database tool base
├── database/DatabaseHelper.java  # SQLite: history + items tables
├── utils/
│   ├── HttpUtil.java          # HTTP GET, headers, redirects
│   ├── CryptoUtils.java       # AES, RSA, SHA, MD5, bcrypt helpers
│   ├── NetworkUtils.java      # Ping, port scan, ARP, interfaces
│   └── FormatUtils.java       # JSON pretty-print, timestamps
├── adapters/                  # RecyclerView adapters
├── model/ToolEntry.java       # Tool metadata model
└── tools/                    # 100 tool activities
```

## Tool Categories

| # | Category | Tools |
|---|----------|-------|
| 1 | Network Tools | 1–10: Scanner, WiFi, IP Lookup, Ping, Traceroute, TCP/UDP Port Scanner, LAN Inventory, MAC Vendor, ARP |
| 2 | DNS & Network | 11–20: DNS, Reverse DNS, IPv4/IPv6/CIDR Calc, Packet Capture, Bandwidth, Latency, Network Interface, Gateway |
| 3 | Web Inspector | 21–30: HTTP Headers, HTTPS Check, SSL Cert, Cert Expiry, Security Headers, Cookies, JWT Decoder, JWT Sig, Base64, URL Encode |
| 4 | Web Tools | 31–40: URL Parser, robots.txt, sitemap, CSP, CORS, HTTP Status, Redirect, Tech Detect, HTML Viewer, Web Fingerprint |
| 5 | Password & Hashing | 41–50: Strength, Generator, Entropy, Hash Gen, Hash ID, Checksum, AES Encrypt/Decrypt, Secure Notes, Password Vault |
| 6 | Encryption Suite | 51–60: OTP Backup, RSA Key Gen, RSA Encrypt, Digital Sig, File Integrity, Text Encrypt, QR Secret, Crypto Learning, Secure Random, Hash Compare |
| 7 | OSINT & Recon | 61–70: WHOIS, RDAP, Domain Info, DNS Records, Cert Transparency, Subdomain Enum, ASN, GeoIP, Reverse IP, IP Reputation |
| 8 | Reputation & Breach | 71–80: Domain Reputation, Email Breach, Username Search, DNS Compare, Domain Expiry, Screenshot Organizer, Passive Recon, DNS Propagation, Email Header, Metadata |
| 9 | Vulnerability Intel | 81–90: CVE Search, CVSS Calc, NVD Browser, CISA KEV, Vuln Notes, Patch Tracker, Security Bulletin, Exploit Ref, Software Inventory, Asset Risk |
| 10 | Security Management | 91–100: IR Checklist, Security Audit, Firewall Rules, Device Inventory, Network Docs, Log Viewer, IOC Manager, Threat Feed, Cyber Quiz, All-in-One Dashboard |

## APIs (configure in Settings)

| API | Free | Key Required | Used By |
|-----|------|-------------|---------|
| ip-api.com | Yes | No | IP Lookup, GeoIP |
| macvendors.com | Yes | No | MAC Vendor |
| Google DNS over HTTPS | Yes | No | DNS Lookup, DNS Records |
| RDAP (rdap.org) | Yes | No | WHOIS, Domain Info |
| crt.sh | Yes | No | Cert Transparency, Subdomain Enum |
| AbuseIPDB | Partial | Yes | IP Reputation |
| VirusTotal | Partial | Yes | Domain Reputation, File Integrity |
| Have I Been Pwned | Paid | Yes | Email Breach |
| ipinfo.io | Partial | Yes | ASN Lookup |
| HackerTarget | Partial | Optional | Reverse IP |
| NVD | Yes | Optional | CVE Search, NVD Browser |
| CISA KEV | Yes | No | CISA KEV Browser |
| AlienVault OTX | Yes | Yes | IOC Manager, Threat Feed |
| Wappalyzer | Paid | Yes | Tech Detector |
| BuiltWith | Paid | Yes | Web Fingerprint |

## Building / Running

This is a native Android project — it cannot run in the browser.

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

## User Preferences

- Keep existing Java codebase structure
- Do not migrate to Kotlin without explicit request
- Dark cybersecurity color theme (background: #12121F)
- All 100 tools accessible from main navigation drawer
