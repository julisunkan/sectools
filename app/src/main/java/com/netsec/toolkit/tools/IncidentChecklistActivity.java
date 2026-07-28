package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class IncidentChecklistActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Incident Response Checklist"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getToolId()       { return "incident_checklist"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void onCreate(android.os.Bundle s) {
        super.onCreate(s);
        if (db.getItems(getToolId()).isEmpty()) {
            String[][] items = {
                {"Preparation","Verify IR team contacts",""},{"Identification","Detect and triage the incident",""},
                {"Containment","Isolate affected systems",""},{"Eradication","Remove threat/malware",""},
                {"Recovery","Restore systems and verify",""},{"Lessons Learned","Post-incident review",""},
                {"Documentation","Record all actions taken",""},{"Communication","Notify stakeholders",""},
                {"Evidence Collection","Preserve logs and forensics",""},{"Legal/Compliance","Regulatory notifications",""}
            };
            for (String[] item : items) db.addItem(getToolId(), item[0], item[1], item[2], "PENDING", "#546E7A");
            loadItems();
        }
    }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etStep   = et("Step / Task"); l.addView(etStep);
        EditText etDetail = et("Details");     l.addView(etDetail);
        new AlertDialog.Builder(this).setTitle("Add Checklist Item")
            .setView(l).setPositiveButton("Add", (d,w) -> {
                String step = etStep.getText().toString().trim(); if(step.isEmpty()) return;
                addItem(step, etDetail.getText().toString(), "PENDING", "", "#546E7A");
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
