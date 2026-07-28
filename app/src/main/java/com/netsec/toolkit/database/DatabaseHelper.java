package com.netsec.toolkit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "netsec_toolkit.db";
    private static final int DB_VERSION = 1;

    // Generic history table (shared by all lookup tools)
    public static final String TABLE_HISTORY = "tool_history";
    // Generic list table (shared by all list tools)
    public static final String TABLE_ITEMS = "tool_items";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null) instance = new DatabaseHelper(ctx.getApplicationContext());
        return instance;
    }

    private DatabaseHelper(Context ctx) { super(ctx, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tool history: tool_id, query, result, timestamp
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tool_id TEXT NOT NULL," +
                "query TEXT," +
                "result TEXT," +
                "timestamp INTEGER DEFAULT (strftime('%s','now'))" +
                ")");

        // Generic items table: tool_id, title, subtitle, body, meta, color_tag
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tool_id TEXT NOT NULL," +
                "title TEXT," +
                "subtitle TEXT," +
                "body TEXT," +
                "meta TEXT," +
                "color_tag TEXT," +
                "timestamp INTEGER DEFAULT (strftime('%s','now'))" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // ── History ──────────────────────────────────────────────────────────────

    public void saveHistory(String toolId, String query, String result) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tool_id", toolId);
        cv.put("query", query);
        cv.put("result", result);
        db.insert(TABLE_HISTORY, null, cv);
        // Keep only last 100 per tool
        db.execSQL("DELETE FROM " + TABLE_HISTORY +
                " WHERE tool_id=? AND id NOT IN " +
                "(SELECT id FROM " + TABLE_HISTORY + " WHERE tool_id=? ORDER BY id DESC LIMIT 100)",
                new String[]{toolId, toolId});
    }

    public List<HistoryItem> getHistory(String toolId) {
        List<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id,query,result,timestamp FROM " + TABLE_HISTORY +
                " WHERE tool_id=? ORDER BY id DESC LIMIT 50", new String[]{toolId});
        while (c.moveToNext()) {
            list.add(new HistoryItem(c.getLong(0), c.getString(1), c.getString(2), c.getLong(3)));
        }
        c.close();
        return list;
    }

    public void clearHistory(String toolId) {
        getWritableDatabase().delete(TABLE_HISTORY, "tool_id=?", new String[]{toolId});
    }

    // ── Items ─────────────────────────────────────────────────────────────────

    public long addItem(String toolId, String title, String subtitle, String body, String meta, String colorTag) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tool_id", toolId);
        cv.put("title", title);
        cv.put("subtitle", subtitle);
        cv.put("body", body);
        cv.put("meta", meta);
        cv.put("color_tag", colorTag);
        return db.insert(TABLE_ITEMS, null, cv);
    }

    public List<ListItem> getItems(String toolId) {
        List<ListItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id,title,subtitle,body,meta,color_tag,timestamp FROM " + TABLE_ITEMS +
                " WHERE tool_id=? ORDER BY id DESC", new String[]{toolId});
        while (c.moveToNext()) {
            list.add(new ListItem(c.getLong(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4), c.getString(5), c.getLong(6)));
        }
        c.close();
        return list;
    }

    public List<ListItem> searchItems(String toolId, String query) {
        List<ListItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String q = "%" + query + "%";
        Cursor c = db.rawQuery("SELECT id,title,subtitle,body,meta,color_tag,timestamp FROM " + TABLE_ITEMS +
                " WHERE tool_id=? AND (title LIKE ? OR subtitle LIKE ? OR body LIKE ?)" +
                " ORDER BY id DESC", new String[]{toolId, q, q, q});
        while (c.moveToNext()) {
            list.add(new ListItem(c.getLong(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4), c.getString(5), c.getLong(6)));
        }
        c.close();
        return list;
    }

    public void deleteItem(long id) {
        getWritableDatabase().delete(TABLE_ITEMS, "id=?", new String[]{String.valueOf(id)});
    }

    // ── Models ────────────────────────────────────────────────────────────────

    public static class HistoryItem {
        public final long id;
        public final String query, result;
        public final long timestamp;
        public HistoryItem(long id, String q, String r, long ts) {
            this.id = id; this.query = q; this.result = r; this.timestamp = ts;
        }
    }

    public static class ListItem {
        public final long id;
        public final String title, subtitle, body, meta, colorTag;
        public final long timestamp;
        public ListItem(long id, String title, String subtitle, String body, String meta, String colorTag, long ts) {
            this.id = id; this.title = title; this.subtitle = subtitle;
            this.body = body; this.meta = meta; this.colorTag = colorTag; this.timestamp = ts;
        }
    }
}
