package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;
import com.netsec.toolkit.utils.CryptoUtils;

public class SecureNotesActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Secure Notes Vault"; }
    @Override protected String getCategoryColor(){ return "#D50000"; }
    @Override protected String getToolId()       { return "secure_notes"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#D50000"; }

    @Override
    protected void showAddDialog() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL); l.setPadding(48,32,48,0);
        EditText etTitle = et("Note title"); l.addView(etTitle);
        EditText etNote  = et("Note content"); l.addView(etNote);
        EditText etPass  = et("Encryption password"); l.addView(etPass);
        new AlertDialog.Builder(this).setTitle("Add Encrypted Note")
            .setView(l).setPositiveButton("Encrypt & Save", (d,w) -> {
                String title = etTitle.getText().toString().trim(); if(title.isEmpty()) return;
                String note  = etNote.getText().toString();
                String pass  = etPass.getText().toString();
                try {
                    String encrypted = pass.isEmpty() ? note : CryptoUtils.aesEncrypt(note, pass);
                    addItem(title, pass.isEmpty()?"(not encrypted)":"(AES-256 encrypted)", encrypted,
                        new java.util.Date().toString(), "#D50000");
                } catch (Exception e) { /* show error */ }
            }).setNegativeButton("Cancel",null).show();
    }
    private EditText et(String h) { EditText e=new EditText(this); e.setHint(h); return e; }
}
