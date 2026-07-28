package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;
import com.netsec.toolkit.utils.CryptoUtils;

public class OtpBackupActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "OTP Secret Backup"; }
    @Override protected String getCategoryColor(){ return "#FF6D00"; }
    @Override protected String getToolId()       { return "otp_backup"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#FF6D00"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etLabel  = et("Label (e.g. Google, GitHub)"); l.addView(etLabel);
        EditText etSecret = et("TOTP Secret key");              l.addView(etSecret);
        EditText etPass   = et("Encryption password");          l.addView(etPass);
        new AlertDialog.Builder(this).setTitle("Backup OTP Secret")
            .setView(l).setPositiveButton("Encrypt & Save", (d,w) -> {
                String label  = etLabel.getText().toString().trim(); if(label.isEmpty()) return;
                String secret = etSecret.getText().toString();
                String pass   = etPass.getText().toString();
                try {
                    String body = pass.isEmpty() ? secret : CryptoUtils.aesEncrypt(secret, pass);
                    addItem(label, pass.isEmpty()?"not encrypted":"AES-256 encrypted", body,
                        "TOTP", "#FF6D00");
                } catch(Exception e) { /* error */ }
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
