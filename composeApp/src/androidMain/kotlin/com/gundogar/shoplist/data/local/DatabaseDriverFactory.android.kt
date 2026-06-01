package com.gundogar.shoplist.data.local

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.gundogar.shoplist.database.ShopListDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = ShopListDatabase.Schema,
            context = context,
            name = "shoplist_v4.db",
            callback = object : AndroidSqliteDriver.Callback(ShopListDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    // Enable foreign key constraints
                    db.execSQL("PRAGMA foreign_keys=ON")
                }

                override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
                    // Handle migration from version 1 to 2
                    if (oldVersion < 2) {
                        // Check if the amount column exists (old schema)
                        val cursor = db.query("PRAGMA table_info(ShoppingListItem)")
                        var hasAmountColumn = false
                        var hasQuantityColumn = false

                        while (cursor.moveToNext()) {
                            val columnName = cursor.getString(cursor.getColumnIndex("name"))
                            if (columnName == "amount") hasAmountColumn = true
                            if (columnName == "quantity") hasQuantityColumn = true
                        }
                        cursor.close()

                        // Only migrate if we have the old schema
                        if (hasAmountColumn && !hasQuantityColumn) {
                            // Add new columns
                            db.execSQL("ALTER TABLE ShoppingListItem ADD COLUMN quantity TEXT NOT NULL DEFAULT ''")
                            db.execSQL("ALTER TABLE ShoppingListItem ADD COLUMN unit TEXT NOT NULL DEFAULT ''")

                            // Migrate data from amount to quantity and unit
                            db.execSQL("""
                                UPDATE ShoppingListItem
                                SET quantity = CASE
                                    WHEN amount GLOB '[0-9]*.[0-9]*' OR amount GLOB '[0-9]*' THEN
                                        CASE
                                            WHEN INSTR(amount, ' ') > 0 THEN RTRIM(SUBSTR(amount, 1, INSTR(amount, ' ') - 1))
                                            ELSE amount
                                        END
                                    ELSE ''
                                END
                            """)

                            db.execSQL("""
                                UPDATE ShoppingListItem
                                SET unit = CASE
                                    WHEN (amount GLOB '[0-9]* *' OR amount GLOB '[0-9]*.[0-9]* *')
                                        AND INSTR(amount, ' ') > 0 THEN LTRIM(SUBSTR(amount, INSTR(amount, ' ') + 1))
                                    WHEN amount NOT GLOB '[0-9]*' AND amount NOT GLOB '[0-9]*.[0-9]*'
                                        AND amount != '' THEN amount
                                    ELSE ''
                                END
                            """)
                        }
                    }

                    // Handle migration from version 2 to 3
                    if (oldVersion < 3) {
                        // Add the per-item completion column (only if it doesn't already exist)
                        val cursor = db.query("PRAGMA table_info(ShoppingListItem)")
                        var hasIsCompletedColumn = false

                        while (cursor.moveToNext()) {
                            val columnName = cursor.getString(cursor.getColumnIndex("name"))
                            if (columnName == "isCompleted") hasIsCompletedColumn = true
                        }
                        cursor.close()

                        if (!hasIsCompletedColumn) {
                            db.execSQL("ALTER TABLE ShoppingListItem ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
                        }
                    }
                }
            }
        )
    }
}
