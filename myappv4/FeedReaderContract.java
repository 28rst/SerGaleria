package com.example.rafae.myappv4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by rafae on 13/03/2018.
 */

public final class FeedReaderContract extends SQLiteOpenHelper {
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    public FeedReaderContract(Context context) {
        super(context, FeedEntry.DATABASE_NAME, null, FeedEntry.DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedEntry.SQL_CREATE_ENTRIES);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static class FeedEntry implements BaseColumns {

        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "prolserhum.db";
        public static final String TABLE_NAME = "animal";
        public static final String COLUMN_NOME_COMUM = "comum";
        public static final String COLUMN_NOME_ESPECIE = "especie";
        public static final String COLUMN_SEXO = "SEXO";
        public static final String COLUMN_ORDEM= "ORDEM";
        public static final String COLUMN_FAMILIA = "FAM";
        public static final String COLUMN_CLASSE = "CLASSE";
        public static final String COLUMN_GENERO = "GENERO";
        public static final String COLUMN_FILO = "FILO";
        public static final String COLUMN_REINO = "REINO";
        public static final String COLUMN_DESCRICAO = "DESCRICAO";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FeedEntry.COLUMN_NOME_ESPECIE+ " TEXT NOT NULL, " +
                        FeedEntry.COLUMN_NOME_COMUM+ " TEXT, " +
                        FeedEntry.COLUMN_SEXO+" TEXT, "+
                        FeedEntry.COLUMN_REINO+" TEXT DEFAULT 'Animalia', "+
                        FeedEntry.COLUMN_FILO+" TEXT, "+
                        FeedEntry.COLUMN_CLASSE+" TEXT, "+
                        FeedEntry.COLUMN_ORDEM+" TEXT, "+
                        FeedEntry.COLUMN_FAMILIA+" TEXT, "+
                        FeedEntry.COLUMN_GENERO+" TEXT, "+
                        FeedEntry.COLUMN_DESCRICAO+" TEXT);";
    }

}




