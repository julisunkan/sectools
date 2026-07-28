package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class DeviceInventoryActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Device Inventory Manager"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getToolId()       { return "device_inventory"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etName = et("Device name / hostname"); l.addView(etName);
        EditText etIp   = et("IP Address");             l.addView(etIp);
        EditText etType = et("Type (Server/PC/Mobile/IoT)"); l.addView(etType);
        EditText etOs   = et("Operating System");       l.addView(etOs);
        EditText etOwner= et("Owner / Location");       l.addView(etOwner);
        new AlertDialog.Builder(this).setTitle("Add Device")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String name = etName.getText().toString().trim(); if(name.isEmpty()) return;
                addItem(name + "  [" + etIp.getText().toString() + "]",
                    etType.getText().toString() + "  " + etOs.getText().toString(),
                    etOwner.getText().toString(), "", "#546E7A");
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
