package com.example.news.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.news.interfaces.DAO
import com.example.news.model.ArticlesTable

@Database(entities = [ArticlesTable::class], version = 1)
abstract class DbRoomHelper : RoomDatabase(){
    abstract fun dao():DAO
    companion object{
        var db:DbRoomHelper? = null
        fun initDb(context: Context) : DbRoomHelper{
            if (db==null){
                db=Room.databaseBuilder(context,
                    DbRoomHelper::class.java,
                    "db_name")
                    .allowMainThreadQueries()
                    .build()
            }
            return db!!
        }
    }
}