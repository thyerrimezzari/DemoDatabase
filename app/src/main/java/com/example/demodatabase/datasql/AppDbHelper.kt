package com.example.demodatabase.datasql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE IF NOT EXISTS usuarios (" +
            "id INTEGER PRIMARY KEY autoincrement," +
            "name varchar(255) NOT NULL," +
            "email varchar(255) NOT NULL);" +
    "CREATE TABLE IF NOT EXISTS produtos (" +
            "id INTEGER PRIMARY KEY autoincrement," +
            "name varchar(255) NOT NULL);"

class AppDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        // esse código irá rodar na criação do banco
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // implementaremos isso caso necessário
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // implementaremos isso caso necessário
    }
    companion object {
        // nome e versão do banco
        const val DATABASE_NAME = "SatcDemoTest.db"
        const val DATABASE_VERSION = 1
    }

}