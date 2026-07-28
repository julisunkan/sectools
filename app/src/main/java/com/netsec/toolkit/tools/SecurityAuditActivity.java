package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class SecurityAuditActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Security Audit Checklist"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getToolId()       { return "security_audit"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void onCreate(android.os.Bundle s) {
        super.onCreate(s);
        if (db.getItems(getToolId()).isEmpty()) {
            String[] checks = {
                "Patch management up-to-date","MFA enabled on all accounts","Firewall rules reviewed",
                "Least privilege enforced","Logs collected and monitored","Backups tested and encrypted",
                "Network segmentation in place","Vulnerability scans performed","Security awareness training done",
                "Incident response plan tested","Password policy enforced","Data encryption at rest/in transit",
                "Third-party vendor risk assessed","Physical security controls checked","DNS/email security (SPF,DKIM,DMARC)"
            };
            for (String c : checks) db.addItem(getToolId(), c, "", "", "#546E7A", "#546E7A");
            loadItems();
        }
    }

    @Override
    protected void showAddDialog() {
        EditText et = new EditText(this); et.setHint("Audit item");
        new AlertDialog.Builder(this).setTitle("Add Audit Item").setView(et)
            .setPositiveButton("Add", (d,w) -> {
                String item = et.getText().toString().trim(); if(item.isEmpty()) return;
                addItem(item, "", "", "", "#546E7A");
            }).setNegativeButton("Cancel",null).show();
    }
}
