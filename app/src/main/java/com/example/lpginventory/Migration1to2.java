package com.example.lpginventory;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration1to2 extends Migration {
    public Migration1to2() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        // Perform the necessary SQL operations to migrate the data
        database.execSQL("ALTER TABLE persons ADD COLUMN lpgWeight REAL NOT NULL DEFAULT 0");
        database.execSQL("ALTER TABLE persons ADD COLUMN amount REAL NOT NULL DEFAULT 0");
        database.execSQL("ALTER TABLE persons ADD COLUMN mop TEXT");
    }
}
