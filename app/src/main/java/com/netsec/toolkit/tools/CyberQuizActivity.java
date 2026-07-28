package com.netsec.toolkit.tools;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.material.button.MaterialButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.netsec.toolkit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CyberQuizActivity extends AppCompatActivity {

    private TextView tvScore, tvProgress, tvCategory, tvQuestion;
    private ProgressBar progressQuiz;
    private MaterialButton[] answerButtons;
    private int currentQ = 0, score = 0;
    private List<Q> questions;

    static class Q {
        String category, question, correct;
        String[] options;
        Q(String cat, String q, String correct, String... opts) {
            this.category = cat; this.question = q; this.correct = correct;
            this.options = opts;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cybersecurity Quiz");
        }

        tvScore       = findViewById(R.id.tv_score);
        tvProgress    = findViewById(R.id.tv_progress);
        tvCategory    = findViewById(R.id.tv_category_badge);
        tvQuestion    = findViewById(R.id.tv_question);
        progressQuiz  = findViewById(R.id.progress_quiz);
        answerButtons = new MaterialButton[]{
            findViewById(R.id.btn_a), findViewById(R.id.btn_b),
            findViewById(R.id.btn_c), findViewById(R.id.btn_d)
        };

        questions = buildQuestions();
        Collections.shuffle(questions);
        progressQuiz.setMax(questions.size());
        showQuestion();
    }

    private void showQuestion() {
        if (currentQ >= questions.size()) { showResult(); return; }
        Q q = questions.get(currentQ);
        tvScore.setText("Score: " + score + "/" + currentQ);
        tvProgress.setText((currentQ + 1) + "/" + questions.size());
        tvCategory.setText(q.category);
        tvQuestion.setText(q.question);
        progressQuiz.setProgress(currentQ);

        List<String> opts = new ArrayList<>(Arrays.asList(q.options));
        if (!opts.contains(q.correct)) opts.add(q.correct);
        Collections.shuffle(opts);
        while (opts.size() < 4) opts.add("N/A");

        for (int i = 0; i < answerButtons.length; i++) {
            String opt = opts.get(i);
            answerButtons[i].setText(opt);
            answerButtons[i].setEnabled(true);
            answerButtons[i].setStrokeColor(android.content.res.ColorStateList.valueOf(
                    getColor(R.color.divider)));
            answerButtons[i].setTextColor(getColor(R.color.on_surface));
            final String answer = opt;
            final boolean isCorrect = answer.equals(q.correct);
            final int idx = i;
            answerButtons[i].setOnClickListener(v -> handleAnswer(idx, isCorrect, opts, q.correct));
        }
    }

    private void handleAnswer(int chosen, boolean isCorrect, List<String> opts, String correct) {
        for (MaterialButton btn : answerButtons) btn.setEnabled(false);
        if (isCorrect) {
            score++;
            answerButtons[chosen].setStrokeColor(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#00E676")));
        } else {
            answerButtons[chosen].setStrokeColor(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#FF5252")));
            // Highlight correct
            for (int i = 0; i < answerButtons.length; i++) {
                if (answerButtons[i].getText().toString().equals(correct)) {
                    answerButtons[i].setStrokeColor(
                            android.content.res.ColorStateList.valueOf(Color.parseColor("#00E676")));
                }
            }
        }
        currentQ++;
        new Handler().postDelayed(this::showQuestion, 1500);
    }

    private void showResult() {
        int pct = (score * 100) / questions.size();
        String grade = pct >= 90 ? "Expert" : pct >= 70 ? "Advanced" : pct >= 50 ? "Intermediate" : "Beginner";
        new AlertDialog.Builder(this)
            .setTitle("Quiz Complete!")
            .setMessage("Score: " + score + "/" + questions.size() + " (" + pct + "%)\nLevel: " + grade)
            .setPositiveButton("Restart", (d, w) -> { currentQ = 0; score = 0; Collections.shuffle(questions); showQuestion(); })
            .setNegativeButton("Exit", (d, w) -> finish())
            .setCancelable(false).show();
    }

    private List<Q> buildQuestions() {
        List<Q> qs = new ArrayList<>();
        qs.add(new Q("Cryptography", "Which algorithm is considered broken for password hashing?", "MD5", "Argon2", "bcrypt", "PBKDF2"));
        qs.add(new Q("Cryptography", "What does AES stand for?", "Advanced Encryption Standard", "Advanced Encryption System", "Asymmetric Encryption Standard", "Applied Encryption Suite"));
        qs.add(new Q("Cryptography", "What is the recommended minimum RSA key size today?", "2048 bits", "512 bits", "1024 bits", "128 bits"));
        qs.add(new Q("Cryptography", "Which TLS version should you use in 2024?", "TLS 1.3", "TLS 1.0", "SSL 3.0", "TLS 1.1"));
        qs.add(new Q("Cryptography", "What property ensures a hash cannot be reversed?", "Preimage resistance", "Collision resistance", "Avalanche effect", "Key expansion"));
        qs.add(new Q("Network", "What port does HTTPS use by default?", "443", "80", "8080", "8443"));
        qs.add(new Q("Network", "What does ARP stand for?", "Address Resolution Protocol", "Advanced Routing Protocol", "Automatic Reply Protocol", "Application Request Protocol"));
        qs.add(new Q("Network", "Which protocol resolves domain names to IP addresses?", "DNS", "DHCP", "ARP", "ICMP"));
        qs.add(new Q("Network", "What does CIDR stand for?", "Classless Inter-Domain Routing", "Class-based IP Domain Routing", "Common Internet Data Registry", "Centralized IP Domain Resolution"));
        qs.add(new Q("Network", "Which command tests network reachability?", "ping", "traceroute", "netstat", "curl"));
        qs.add(new Q("Web Security", "What does XSS stand for?", "Cross-Site Scripting", "Cross-Server Security", "XML Script Syntax", "External Script Scanning"));
        qs.add(new Q("Web Security", "What header prevents clickjacking?", "X-Frame-Options", "Content-Type", "X-XSS-Protection", "CORS"));
        qs.add(new Q("Web Security", "What does CORS stand for?", "Cross-Origin Resource Sharing", "Content Origin Resource System", "Cross-Origin Request Security", "Client-Only Request Sharing"));
        qs.add(new Q("Web Security", "SQL injection attacks target what?", "Databases", "Web servers", "Firewalls", "DNS servers"));
        qs.add(new Q("Web Security", "What is CSRF?", "Cross-Site Request Forgery", "Common Server Response Format", "Cross-Script Resource Fetch", "Content Security Response Field"));
        qs.add(new Q("OSINT", "What does WHOIS provide?", "Domain registration information", "Website server logs", "User login records", "SSL certificate details"));
        qs.add(new Q("OSINT", "Certificate Transparency logs are used to?", "Detect unauthorized certificates", "Speed up TLS handshake", "Store private keys", "Block malicious IPs"));
        qs.add(new Q("OSINT", "What is passive reconnaissance?", "Gathering info without direct contact", "Scanning ports actively", "Exploiting vulnerabilities", "Sending phishing emails"));
        qs.add(new Q("Vulnerability", "What does CVE stand for?", "Common Vulnerabilities and Exposures", "Critical Vulnerability Enumeration", "Common Vulnerability Exploits", "Certified Vulnerability Entry"));
        qs.add(new Q("Vulnerability", "CVSS scores range from?", "0.0 to 10.0", "1 to 100", "0 to 5", "0 to 1000"));
        qs.add(new Q("Vulnerability", "A CVSS score of 9.8 indicates?", "Critical severity", "High severity", "Medium severity", "Low severity"));
        qs.add(new Q("Vulnerability", "CISA KEV tracks what?", "Known Exploited Vulnerabilities", "Key Encryption Vectors", "Kernel Exploit Variants", "Known Error Values"));
        qs.add(new Q("Incident Response", "What is the first phase of incident response?", "Preparation", "Detection", "Containment", "Eradication"));
        qs.add(new Q("Incident Response", "Digital forensics preserves evidence through?", "Chain of custody", "Patch management", "Penetration testing", "Vulnerability scanning"));
        qs.add(new Q("Authentication", "MFA stands for?", "Multi-Factor Authentication", "Multi-Form Authorization", "Managed Firewall Access", "Multiple Function Authentication"));
        qs.add(new Q("Authentication", "Which is the most secure MFA method?", "Hardware security key (FIDO2)", "SMS OTP", "Email code", "Security questions"));
        qs.add(new Q("Protocols", "What does DNSSEC provide?", "DNS response authentication", "DNS encryption", "DNS caching", "DNS load balancing"));
        qs.add(new Q("Protocols", "SPF, DKIM, and DMARC protect against?", "Email spoofing", "SQL injection", "DDoS attacks", "SSL stripping"));
        qs.add(new Q("Encryption", "Which mode of AES provides authentication?", "GCM", "ECB", "CBC", "CTR"));
        qs.add(new Q("Encryption", "What is Perfect Forward Secrecy?", "Session keys can't decrypt past traffic", "Keys never expire", "Same key for all sessions", "Keys are stored in hardware"));
        return qs;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
