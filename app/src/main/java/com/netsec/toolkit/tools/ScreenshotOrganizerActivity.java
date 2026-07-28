package com.netsec.toolkit.tools;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.netsec.toolkit.base.BaseListActivity;
import com.netsec.toolkit.database.DatabaseHelper;

public class ScreenshotOrganizerActivity extends BaseListActivity {
    @Override protected String getToolTitle()    { return "Website Screenshot Organizer"; }
    @Override protected String getCategoryColor(){ return "#00BFA5"; }
    @Override protected String getToolId()       { return "screenshot_organizer"; }
    @Override protected String getColorTagForItem(DatabaseHelper.ListItem i) { return "#00BFA5"; }

    @Override
    protected void showAddDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 0);
        EditText etUrl   = makeInput("Website URL"); layout.addView(etUrl);
        EditText etLabel = makeInput("Label/Description"); layout.addView(etLabel);
        EditText etNotes = makeInput("Notes"); layout.addView(etNotes);

        new AlertDialog.Builder(this)
            .setTitle("Add Screenshot Record")
            .setView(layout)
            .setPositiveButton("Save", (d, w) -> {
                String url   = etUrl.getText().toString().trim();
                if (url.isEmpty()) return;
                addItem(url, etLabel.getText().toString().trim(), etNotes.getText().toString().trim(),
                        java.text.SimpleDateFormat.getInstance().format(new java.util.Date()), "#00BFA5");
            })
            .setNegativeButton("Cancel", null).show();
    }

    private EditText makeInput(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        return et;
    }
}
