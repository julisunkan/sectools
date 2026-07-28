package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class SoftwareInventoryActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Software Version Inventory"; }
    @Override protected String getCategoryColor(){ return "#DD2C00"; }
    @Override protected String getToolId()       { return "software_inventory"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etName    = et("Software name");           l.addView(etName);
        EditText etVersion = et("Current version");         l.addView(etVersion);
        EditText etLatest  = et("Latest version (optional)"); l.addView(etLatest);
        EditText etHost    = et("Host / System");           l.addView(etHost);
        new AlertDialog.Builder(this).setTitle("Add Software")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String name = etName.getText().toString().trim(); if(name.isEmpty()) return;
                String ver = etVersion.getText().toString(), latest = etLatest.getText().toString();
                String color = (!latest.isEmpty() && !ver.equals(latest)) ? "#FFD740" : "#00E676";
                addItem(name, "v" + ver + (latest.isEmpty()?"":"  (latest: v"+latest+")"),
                    etHost.getText().toString(), "", color);
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
