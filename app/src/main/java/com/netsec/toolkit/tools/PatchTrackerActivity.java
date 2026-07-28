package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class PatchTrackerActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Patch Tracking Tool"; }
    @Override protected String getCategoryColor(){ return "#DD2C00"; }
    @Override protected String getToolId()       { return "patch_tracker"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#DD2C00"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etSystem = et("System / Software");  l.addView(etSystem);
        EditText etPatch  = et("Patch / Version");    l.addView(etPatch);
        EditText etDate   = et("Patch Date");         l.addView(etDate);
        EditText etStatus = et("Status (Applied/Pending/Skipped)"); l.addView(etStatus);
        new AlertDialog.Builder(this).setTitle("Add Patch Record")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String sys = etSystem.getText().toString().trim(); if(sys.isEmpty()) return;
                String st = etStatus.getText().toString().toUpperCase();
                String color = st.startsWith("A")?"#00E676":st.startsWith("P")?"#FFD740":"#FF5252";
                addItem(sys, etPatch.getText().toString(), etStatus.getText().toString(),
                    etDate.getText().toString(), color);
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
