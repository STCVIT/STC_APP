package `in`.stcvit.stcapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import `in`.stcvit.stcapp.model.explore.BoardMember
import `in`.stcvit.stcapp.model.explore.Event
import `in`.stcvit.stcapp.model.explore.Project
import `in`.stcvit.stcapp.model.resource.Detail
import `in`.stcvit.stcapp.model.resource.Resource
import `in`.stcvit.stcapp.model.resource.Roadmap

@Database(
    entities = [
        Detail::class,
        Roadmap::class,
        Resource::class,
        BoardMember::class,
        Event::class,
        Project::class
    ], version = 1, exportSchema = false
)
abstract class STCDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: STCDatabase? = null

        fun getInstance(context: Context): STCDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                STCDatabase::class.java, "STCDatabase.db"
            ).build()
    }
}