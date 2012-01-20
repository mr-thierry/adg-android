/*
 * Copyright (C) 2012 Thierry-Dimitri Roy <thierryd@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.analysedesgeeks.android.service.impl;

import roboguice.util.Ln;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.analysedesgeeks.android.service.DatabaseService;
import com.google.inject.Inject;

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
