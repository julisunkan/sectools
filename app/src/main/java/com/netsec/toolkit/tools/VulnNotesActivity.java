package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class VulnNotesActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Vulnerability Notes Manager"; }
    @Override protected String getCategoryColor(){ return "#DD2C00"; }
    @Override protected String getToolId()       { return "vuln_notes"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#DD2C00"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etCve    = et("CVE-ID or Title");  l.addView(etCve);
        EditText etSev    = et("Severity (Critical/High/Medium/Low)"); l.addView(etSev);
        EditText etNotes  = et("Notes / Remediation"); l.addView(etNotes);
        EditText etStatus = et("Status (Open/In Progress/Patched)"); l.addView(etStatus);
        new AlertDialog.Builder(this).setTitle("Add Vulnerability Note")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String cve = etCve.getText().toString().trim(); if(cve.isEmpty()) return;
                String sev = etSev.getText().toString().toUpperCase();
                String color = sev.startsWith("C")?"#FF1744":sev.startsWith("H")?"#FF6D00":sev.startsWith("M")?"#FFD600":"#69F0AE";
                addItem(cve, etSev.getText().toString(), etNotes.getText().toString(),
                    etStatus.getText().toString(), color);
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
