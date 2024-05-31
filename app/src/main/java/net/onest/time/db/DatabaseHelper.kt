package net.onest.time.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import net.onest.time.utils.applicationContext

class DatabaseHelper : SQLiteOpenHelper(
    applicationContext(),
    "todo",
    null,
    1
) {
    companion object {
        @JvmStatic
        private val TAG: String = "DatabaseHelper"
    }

    private val createTaskTableSql =
        """
        create table task (
            task_id            bigint       primary key, 
            user_id            bigint,
            task_name          varchar(255),
            type               int,
            clock_duration     int,
            task_status        tinyint,
            remark             varchar(100),
            estimate           varchar(255),
            rest_time          int,
            again              tinyint,
            category           varchar(100),
            today_total_times  int,
            tomato_clock_times int,
            stop_times         int,
            inner_interrupt    int,
            outer_interrupt    int,
            background         varchar(255),
            started_at         timestamp,
            completed_at       timestamp,
            created_at         timestamp,
            updated_at         timestamp,
            deleted            int
        );
        """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTaskTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}