package cubesystem.kurimabank.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    static final String DB = "kurimabank.db";
    static final int DB_VERSION = 1;

    public MySQLiteOpenHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        StringBuilder createTable = new StringBuilder();
        //明細テーブル
        createTable.append("create table KURT_DETAIL (");
        createTable.append("  DETAILNO text primary key ");
        createTable.append(", DETAILYMD text not null");
        createTable.append(", RECEIVEPAYKBN text not null");
        createTable.append(", ITMCD text not null");
        createTable.append(", ABSTRACT text not null");
        createTable.append(", RECEIVEPAYMETHODCD text not null");
        createTable.append(", MONEY integer not null");
        createTable.append(");");

        db.execSQL(createTable.toString());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder dropTable = new StringBuilder();
        //明細テーブル
        dropTable.append("drop table KURT_DETAIL;");

        db.execSQL(dropTable.toString());
        onCreate(db);
    }
}
