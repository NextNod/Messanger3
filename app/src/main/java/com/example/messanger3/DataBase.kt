package com.example.messanger3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context, factory: SQLiteDatabase.CursorFactory?)
    : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_KEY + " TEXT)")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addKey(key: String) {
        val values = ContentValues()
        values.put(COLUMN_KEY, key)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getKey(): String {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        result.moveToFirst()
        return result.getString(result.getColumnIndex(COLUMN_KEY))
    }

    fun isEmpty() :Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        result.moveToFirst()

        return result.count == 0
    }

    fun deleteKey() {
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "data.db"
        const val TABLE_NAME = "data"
        const val COLUMN_KEY = "key"
    }
}