package pl.kondi.bazatelefonow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.kondi.bazatelefonow.data.PhonesContract.PhonesEntry;

public class DBHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "phonesDatabase.db";
    public final static int DATABASE_VERSION = 1;

    public final static String TABLE_PHONES_CREATE = "CREATE TABLE " + PhonesContract.PhonesEntry.TABLE_NAME + "("
            + PhonesContract.PhonesEntry._ID + " integer primary key autoincrement, "
            + PhonesContract.PhonesEntry.COLUMN_MANUFACTURER + " text not null,"
            + PhonesContract.PhonesEntry.COLUMN_MODEL + " text not null,"
            + PhonesEntry.COLUMN_ANDROIDVERSION + " text, "
            + PhonesContract.PhonesEntry.COLUMN_WWW + " text);";

    private static final String TABLE_TELEFONY_DROP = "DROP TABLE IF EXISTS " + PhonesContract.PhonesEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tworzenie bazy
        db.execSQL(TABLE_PHONES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //aktualizacja bazy do nowej wersji
        db.execSQL("DROP TABLE IF EXISTS " + PhonesContract.PhonesEntry.TABLE_NAME);
        onCreate(db);
    }
}
