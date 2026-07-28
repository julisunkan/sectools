package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class FirewallRulesActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Firewall Rule Organizer"; }
    @Override protected String getCategoryColor(){ return "#546E7A"; }
    @Override protected String getToolId()       { return "firewall_rules"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#546E7A"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etAction = et("Action: ALLOW / DENY / LOG"); l.addView(etAction);
        EditText etProto  = et("Protocol: TCP/UDP/ICMP/ANY"); l.addView(etProto);
        EditText etSrc    = et("Source IP / CIDR");          l.addView(etSrc);
        EditText etDst    = et("Destination IP:Port");       l.addView(etDst);
        EditText etDesc   = et("Description");               l.addView(etDesc);
        new AlertDialog.Builder(this).setTitle("Add Firewall Rule")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String action = etAction.getText().toString().trim().toUpperCase();
                String color = action.startsWith("A")?"#00E676":action.startsWith("D")?"#FF5252":"#FFD740";
                addItem(action + "  " + etProto.getText().toString(),
                    etSrc.getText().toString() + " → " + etDst.getText().toString(),
                    etDesc.getText().toString(), "", color);
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
