package ch.heigvd.pdg_grocerypal.SQLite_localDB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GroceryPalDBHelper(context: Context) : SQLiteOpenHelper(context, "GroceryPalLocalDB",null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE Unit ( " +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT );"
        )
        db.execSQL(
                "CREATE TABLE INGERDIENT ( " +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT," +
                    "Fiber FLOAT," +
                    "Protein FLOAT," +
                    "Energy INTEGER," +
                    "Carbs FLOAT," +
                    "Fat FLOAT );"
        )
        db.execSQL(
                "CREATE TABLE In_Shopping_List (" +
                    "Ingredient_id INTEGER NOT NULL," +
                    "Unit_id INTEGER NOT NULL," +
                    "Quantity FLOAT NOT NULL," +
                    "Buy BOOLEAN DEFAULT 0 NOT NULL );"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}