// composeApp/src/iosMain/kotlin/com/gundogar/shoplist/data/local/DatabaseDriverFactory.ios.kt
package com.gundogar.shoplist.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.gundogar.shoplist.database.ShopListDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = ShopListDatabase.Schema,
            name = "shoplist_v4.db",
            onConfiguration = { config ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(
                        foreignKeyConstraints = true
                    )
                )
            },
            // iOS uses SQLDelight's automatic migration via .sqm files
            // The migrations at database/1.sqm and database/2.sqm are applied automatically
        )
    }
}