package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class AssetRiskActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Asset Risk Tracker"; }
    @Override protected String getCategoryColor(){ return "#DD2C00"; }
    @Override protected String getToolId()       { return "asset_risk"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#DD2C00"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etAsset = et("Asset name / IP");     l.addView(etAsset);
        EditText etRisk  = et("Risk level: Critical/High/Medium/Low"); l.addView(etRisk);
        EditText etDesc  = et("Risk description");    l.addView(etDesc);
        EditText etOwner = et("Owner / Contact");     l.addView(etOwner);
        new AlertDialog.Builder(this).setTitle("Add Asset")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String asset = etAsset.getText().toString().trim(); if(asset.isEmpty()) return;
                String risk = etRisk.getText().toString().toUpperCase();
                String color = risk.startsWith("C")?"#FF1744":risk.startsWith("H")?"#FF6D00":risk.startsWith("M")?"#FFD600":"#69F0AE";
                addItem(asset, etRisk.getText().toString(), etDesc.getText().toString(), etOwner.getText().toString(), color);
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
