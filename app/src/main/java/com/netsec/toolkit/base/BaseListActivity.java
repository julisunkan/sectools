package com.netsec.toolkit.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.netsec.toolkit.R;
import com.netsec.toolkit.adapters.ListItemAdapter;
import com.netsec.toolkit.database.DatabaseHelper;

import java.util.List;

public abstract class BaseListActivity extends AppCompatActivity {

    protected RecyclerView rvItems;
    protected TextView tvEmpty;
    protected FloatingActionButton fabAdd;
    protected SearchView searchView;
    protected ListItemAdapter adapter;
    protected DatabaseHelper db;

    protected abstract String getToolTitle();
    protected abstract String getCategoryColor();
    protected abstract String getToolId();
    protected abstract void showAddDialog();
    protected abstract String getColorTagForItem(DatabaseHelper.ListItem item);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tool);

        db = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getToolTitle());
        }
        try { toolbar.setBackgroundColor(Color.parseColor(getCategoryColor())); }
        catch (Exception ignored) {}

        rvItems    = findViewById(R.id.rv_items);
        tvEmpty    = findViewById(R.id.tv_empty);
        fabAdd     = findViewById(R.id.fab_add);
        searchView = findViewById(R.id.search_view);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListItemAdapter(this, item -> {
            db.deleteItem(item.id);
            loadItems();
        });
        rvItems.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddDialog());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { searchItems(q); return true; }
            @Override public boolean onQueryTextChange(String q) { searchItems(q); return true; }
        });

        loadItems();
    }

    protected void loadItems() {
        List<DatabaseHelper.ListItem> items = db.getItems(getToolId());
        adapter.setItems(items);
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void searchItems(String q) {
        List<DatabaseHelper.ListItem> items = q.isEmpty()
                ? db.getItems(getToolId())
                : db.searchItems(getToolId(), q);
        adapter.setItems(items);
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    protected void addItem(String title, String subtitle, String body, String meta, String color) {
        db.addItem(getToolId(), title, subtitle, body, meta, color);
        loadItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
