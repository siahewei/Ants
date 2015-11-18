package mine.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jacky on 15/10/6.
 */
public abstract class AbstractMigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);
}
