package com.netsec.toolkit.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.netsec.toolkit.R;
import com.netsec.toolkit.adapters.HistoryAdapter;
import com.netsec.toolkit.database.DatabaseHelper;

import java.util.List;

public abstract class BaseToolActivity extends AppCompatActivity {

    protected TextInputEditText etInput1, etInput2, etInput3;
    protected TextInputLayout tilInput1, tilInput2, tilInput3;
    protected MaterialButton btnExecute, btnCopy, btnShare, btnSave, btnClearHistory;
    protected TextView tvResult, tvNoHistory;
    protected View cardResult, progressBar, cardHistory;
    protected RecyclerView rvHistory;
    protected HistoryAdapter historyAdapter;
    protected DatabaseHelper db;

    /** Subclasses provide their display title. */
    protected abstract String getToolTitle();

    /** The hex color string for this tool's category toolbar. */
    protected abstract String getCategoryColor();

    /** Hint texts for input fields. Return 1–3 strings; extras are shown. */
    protected abstract String[] getInputHints();

    /** Label for the execute button. */
    protected String getExecuteLabel() { return "Execute"; }

    /** Is input 3 a multiline text area? */
    protected boolean isInput3MultiLine() { return false; }

    /**
     * Perform the tool's action. Called on a background thread.
     * Call cb.onResult() or cb.onError() — they post to UI thread automatically.
     */
    protected abstract void performAction(String[] inputs, ResultCallback cb);

    public interface ResultCallback {
        void onResult(String result);
        void onError(String error);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_base);

        db = DatabaseHelper.getInstance(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getToolTitle());
        }
        try {
            toolbar.setBackgroundColor(Color.parseColor(getCategoryColor()));
        } catch (Exception ignored) {}

        // Input fields
        tilInput1 = findViewById(R.id.til_input1);
        tilInput2 = findViewById(R.id.til_input2);
        tilInput3 = findViewById(R.id.til_input3);
        etInput1  = findViewById(R.id.et_input1);
        etInput2  = findViewById(R.id.et_input2);
        etInput3  = findViewById(R.id.et_input3);

        String[] hints = getInputHints();
        if (hints != null && hints.length > 0) {
            tilInput1.setHint(hints[0]);
            tilInput1.setVisibility(View.VISIBLE);
        } else {
            tilInput1.setVisibility(View.GONE);
        }
        if (hints != null && hints.length > 1) {
            tilInput2.setHint(hints[1]);
            tilInput2.setVisibility(View.VISIBLE);
        }
        if (hints != null && hints.length > 2) {
            tilInput3.setHint(hints[2]);
            tilInput3.setVisibility(View.VISIBLE);
        }

        // Buttons
        btnExecute = findViewById(R.id.btn_execute);
        btnExecute.setText(getExecuteLabel());
        btnExecute.setOnClickListener(v -> onExecuteClicked());

        cardResult  = findViewById(R.id.card_result);
        progressBar = findViewById(R.id.progress_bar);
        tvResult    = findViewById(R.id.tv_result);

        btnCopy  = findViewById(R.id.btn_copy);
        btnShare = findViewById(R.id.btn_share);
        btnSave  = findViewById(R.id.btn_save_history);

        btnCopy.setOnClickListener(v -> copyResult());
        btnShare.setOnClickListener(v -> shareResult());
        btnSave.setOnClickListener(v -> saveToHistory());

        // History
        cardHistory   = findViewById(R.id.card_history);
        rvHistory     = findViewById(R.id.rv_history);
        tvNoHistory   = findViewById(R.id.tv_no_history);
        btnClearHistory = findViewById(R.id.btn_clear_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(this, item -> showResult(item.query, item.result));
        rvHistory.setAdapter(historyAdapter);
        btnClearHistory.setOnClickListener(v -> {
            db.clearHistory(getToolId());
            loadHistory();
        });

        loadHistory();
    }

    protected String getToolId() { return getClass().getSimpleName(); }

    private void onExecuteClicked() {
        String input1 = etInput1 != null ? text(etInput1) : "";
        if (getInputHints() != null && getInputHints().length > 0 && input1.isEmpty()) {
            tilInput1.setError("Required");
            return;
        }
        tilInput1.setError(null);

        String input2 = etInput2 != null ? text(etInput2) : "";
        String input3 = etInput3 != null ? text(etInput3) : "";
        String[] inputs = {input1, input2, input3};

        showLoading(true);
        cardResult.setVisibility(View.VISIBLE);
        tvResult.setText("Running...");

        ResultCallback cb = new ResultCallback() {
            @Override public void onResult(String result) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showResult(input1, result);
                });
            }
            @Override public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showResult(input1, "ERROR: " + error);
                    tvResult.setTextColor(Color.parseColor("#FF5252"));
                });
            }
        };

        new Thread(() -> performAction(inputs, cb)).start();
    }

    protected void showResult(String query, String result) {
        cardResult.setVisibility(View.VISIBLE);
        tvResult.setText(result);
        tvResult.setTextColor(getColor(R.color.on_surface));
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnExecute.setEnabled(!loading);
    }

    private void copyResult() {
        String text = tvResult.getText().toString();
        if (text.isEmpty()) return;
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("result", text));
        Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
    }

    private void shareResult() {
        String text = tvResult.getText().toString();
        if (text.isEmpty()) return;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(i, "Share result"));
    }

    private void saveToHistory() {
        String query  = text(etInput1);
        String result = tvResult.getText().toString();
        if (result.isEmpty() || result.equals("Running...")) return;
        db.saveHistory(getToolId(), query, result);
        loadHistory();
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    protected void loadHistory() {
        List<DatabaseHelper.HistoryItem> items = db.getHistory(getToolId());
        historyAdapter.setItems(items);
        tvNoHistory.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    protected String text(TextInputEditText et) {
        if (et == null || et.getText() == null) return "";
        return et.getText().toString().trim();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
