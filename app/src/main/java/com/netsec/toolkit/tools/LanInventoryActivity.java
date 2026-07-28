package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class LanInventoryActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "LAN Device Inventory"; }
    @Override protected String getCategoryColor(){ return "#7C4DFF"; }
    @Override protected String getToolId()       { return "lan_inventory"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#7C4DFF"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etIp   = et("IP Address");  l.addView(etIp);
        EditText etMac  = et("MAC Address"); l.addView(etMac);
        EditText etName = et("Device Name"); l.addView(etName);
        EditText etType = et("Device Type (e.g. Router, PC, Phone)"); l.addView(etType);
        new AlertDialog.Builder(this).setTitle("Add Device")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String ip = etIp.getText().toString().trim(); if(ip.isEmpty()) return;
                addItem(ip, etName.getText().toString(), etType.getText().toString(), etMac.getText().toString(), "#7C4DFF");
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
