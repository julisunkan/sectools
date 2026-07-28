package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class PacketCaptureActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Packet Capture Log Viewer"; }
    @Override protected String getCategoryColor(){ return "#0091EA"; }
    @Override protected String getToolId()       { return "packet_capture"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#0091EA"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etName = et("Capture filename (.pcap)"); l.addView(etName);
        EditText etDesc = et("Description / filter used"); l.addView(etDesc);
        EditText etSize = et("File size / packet count"); l.addView(etSize);
        new AlertDialog.Builder(this).setTitle("Log Capture")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String name = etName.getText().toString().trim(); if(name.isEmpty()) return;
                addItem(name, etDesc.getText().toString(), etSize.getText().toString(),
                    new java.util.Date().toString(), "#0091EA");
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
