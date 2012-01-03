package com.analysedesgeeks.android.service.impl;

import roboguice.util.Ln;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.analysedesgeeks.android.service.DatabaseService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DatabaseServiceImpl extends SQLiteOpenHelper implements DatabaseService {

	private static final int TABLE_VERSION = 38;

	@Inject
	public DatabaseServiceImpl(final Context context) {
		super(context, "adg.data", null, TABLE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {

		final String query = "CREATE TABLE DATA (KEY TEXT PRIMARY KEY, VALUE TEXT)";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Ln.w("Upgrading database from version %s to %s, which will destroy all old data", oldVersion, newVersion);

		db.execSQL("DROP TABLE IF EXISTS DATA");

		onCreate(db);
	}

	@Override
	public void save(final String dataAsString) {
		// TODO Auto-generated method stub

	}

}
