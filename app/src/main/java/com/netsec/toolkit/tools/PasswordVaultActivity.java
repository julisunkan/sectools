package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;
import com.netsec.toolkit.utils.CryptoUtils;

public class PasswordVaultActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Encrypted Password Manager"; }
    @Override protected String getCategoryColor(){ return "#D50000"; }
    @Override protected String getToolId()       { return "password_vault"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#D50000"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etSite = et("Website / Service"); l.addView(etSite);
        EditText etUser = et("Username / Email");  l.addView(etUser);
        EditText etPwd  = et("Password");          l.addView(etPwd);
        EditText etMaster = et("Master password (for encryption)"); l.addView(etMaster);
        new AlertDialog.Builder(this).setTitle("Add Password Entry")
            .setView(l).setPositiveButton("Save", (d,w) -> {
                String site   = etSite.getText().toString().trim(); if(site.isEmpty()) return;
                String user   = etUser.getText().toString();
                String pwd    = etPwd.getText().toString();
                String master = etMaster.getText().toString();
                try {
                    String body = master.isEmpty() ? pwd : CryptoUtils.aesEncrypt(user+":"+pwd, master);
                    addItem(site, user, body, master.isEmpty()?"plain":"encrypted", "#D50000");
                } catch(Exception e) { /* error */ }
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
