package ch.heigvd.pdg_grocerypal.SQLite_localDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem

class GroceryPalDBHelper(context: Context) : SQLiteOpenHelper(context, "GroceryPalLocalDB",null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE Unit ( " +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT );"
        )
        db.execSQL(
                "CREATE TABLE Ingredient( " +
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
                    "Quantity INTEGER NOT NULL," +
                    "Buy BOOLEAN DEFAULT 0 NOT NULL );"
        )

        // A SUPPRIMER quand relié à la DB distante
        insertDefaultUnits(db)
        insertDefaultIngredients(db)
        insertDefaultShoppingList(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not useful in our case")
    }

    private fun insertDefaultUnits(db: SQLiteDatabase) {
        val unitValues = arrayOf(
            "('g')",
            "('l')",
            "('pcs')",
            // Ajoutez d'autres unités de base ici
        )

        unitValues.forEach { value ->
            db.execSQL("INSERT INTO Unit (Name) VALUES $value")
        }
    }
    private fun insertDefaultIngredients(db: SQLiteDatabase) {
        val ingredientValues = arrayOf(
            "('Farine', 2.5, 10.0, 360, 70.0, 1.0)",
            "('Lait', 0.0, 3.0, 42, 4.8, 1.0)",
            "('Oeuf', 0.0, 13.0, 155, 1.1, 11.0)",
            "('Chocolat noir', 7.0, 5.3, 570, 55.0, 35.0)",
            "('Chocolat au lait', 3.0, 5.0, 520, 58.0, 30.0)",
            "('Ovomaltine', 3.0, 5.0, 520, 58.0, 30.0)"
            // Ajoutez d'autres ingrédients de base ici
        )

        ingredientValues.forEach { value ->
            db.execSQL("INSERT INTO Ingredient (Name, Fiber, Protein, Energy, Carbs, Fat) VALUES $value")
        }
    }

    private fun insertDefaultShoppingList(db: SQLiteDatabase) {
        // Insertion d'une liste de courses par défaut ici
        db.execSQL("INSERT INTO In_Shopping_List (Ingredient_id, Unit_id, Quantity, Buy) VALUES (1, 1, '100', 0)")
        db.execSQL("INSERT INTO In_Shopping_List (Ingredient_id, Unit_id, Quantity, Buy) VALUES (2, 2, '2', 0)")
        db.execSQL("INSERT INTO In_Shopping_List (Ingredient_id, Unit_id, Quantity, Buy) VALUES (3, 3, '2', 0)")
        db.execSQL("INSERT INTO In_Shopping_List (Ingredient_id, Unit_id, Quantity, Buy) VALUES (4, 1, '100', 0)")
        db.execSQL("INSERT INTO In_Shopping_List (Ingredient_id, Unit_id, Quantity, Buy) VALUES (5, 1, '100', 0)")
        db.execSQL("INSERT INTO In_Shopping_List (Ingredient_id, Unit_id, Quantity, Buy) VALUES (6, 1, '300', 0)")

    }

    fun getAllShoppingListItems(): MutableList<GroceryItem> {
        val groceryList = mutableListOf<GroceryItem>()

        val db = readableDatabase

        val query = "SELECT " +
                "In_Shopping_List.Ingredient_id AS IngredientId, " +
                "Ingredient.Name AS Name, " +
                "Unit.Name AS Unit, " +
                "In_Shopping_List.Quantity AS Quantity, " +
                "In_Shopping_List.Buy AS IsPurchased " +
                "FROM In_Shopping_List " +
                "INNER JOIN Ingredient ON In_Shopping_List.Ingredient_id = Ingredient.ID " +
                "INNER JOIN Unit ON In_Shopping_List.Unit_id = Unit.ID"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val ingredientId = cursor.getInt(cursor.getColumnIndex("IngredientId"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val unit = cursor.getString(cursor.getColumnIndex("Unit"))
                val quantity = cursor.getInt(cursor.getColumnIndex("Quantity"))
                val isPurchased = cursor.getInt(cursor.getColumnIndex("IsPurchased")) == 1

                val groceryItem = GroceryItem(
                    ingredientId,
                    name,
                    unit,
                    quantity,
                    isPurchased
                )

                groceryList.add(groceryItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return groceryList
    }


    fun deletePurchasedItem(ingredientId: Int) {
        val db = writableDatabase
        val whereClause = "Ingredient_id = ?"
        val whereArgs = arrayOf(ingredientId.toString())

        db.delete("In_Shopping_List", whereClause, whereArgs)
        db.close()
    }

    fun deleteAllPurchasedItems() {
        val db = writableDatabase
        val whereClause = "Buy = 1"
        db.delete("In_Shopping_List", whereClause, null)

        db.close()
    }

    fun updateItemPurchasedStatus(ingredientId: Int, isPurchased: Boolean) {
        val db = writableDatabase
        val whereClause = "Ingredient_id = ?"
        val whereArgs = arrayOf(ingredientId.toString())
        val values = ContentValues()
        values.put("Buy", if (isPurchased) 1 else 0)
        db.update("In_Shopping_List", values, whereClause, whereArgs)
        db.close()
    }
    fun updateQuantityInShoppingList(ingredientId: Int, newQuantity: Int) {
        val db = writableDatabase
        val whereClause = "Ingredient_id = ?"
        val whereArgs = arrayOf(ingredientId.toString())
        val values = ContentValues()
        values.put("Quantity", newQuantity)
        db.update("In_Shopping_List", values, whereClause, whereArgs)
        db.close()
    }



}