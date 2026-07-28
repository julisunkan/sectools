package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class IocManagerActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "IOC Manager"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getToolId()       { return "ioc_manager"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etIoc    = et("IOC value (IP, domain, hash, URL)"); l.addView(etIoc);
        EditText etType   = et("Type: IP/Domain/Hash/URL/Email");    l.addView(etType);
        EditText etSev    = et("Severity: Critical/High/Medium/Low");l.addView(etSev);
        EditText etSource = et("Source / Campaign");                  l.addView(etSource);
        new AlertDialog.Builder(this).setTitle("Add IOC")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String ioc = etIoc.getText().toString().trim(); if(ioc.isEmpty()) return;
                String sev = etSev.getText().toString().toUpperCase();
                String color = sev.startsWith("C")?"#FF1744":sev.startsWith("H")?"#FF6D00":sev.startsWith("M")?"#FFD600":"#69F0AE";
                addItem(ioc, etType.getText().toString() + " — " + etSev.getText().toString(),
                    etSource.getText().toString(), new java.util.Date().toString(), color);
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
