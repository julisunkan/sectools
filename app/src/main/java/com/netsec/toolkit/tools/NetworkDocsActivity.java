package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class NetworkDocsActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Network Documentation Tool"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getToolId()       { return "network_docs"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etTitle = et("Document title");   l.addView(etTitle);
        EditText etType  = et("Type (Diagram/IP Scheme/Policy/SOP)"); l.addView(etType);
        EditText etDesc  = et("Description / Notes"); l.addView(etDesc);
        new AlertDialog.Builder(this).setTitle("Add Document")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String title = etTitle.getText().toString().trim(); if(title.isEmpty()) return;
                addItem(title, etType.getText().toString(), etDesc.getText().toString(),
                    new java.util.Date().toString(), "#546E7A");
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
