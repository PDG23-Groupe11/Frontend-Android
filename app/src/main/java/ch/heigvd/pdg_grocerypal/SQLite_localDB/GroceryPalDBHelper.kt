package ch.heigvd.pdg_grocerypal.SQLite_localDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ch.heigvd.pdg_grocerypal.backEndConnections.ConnectionRecipeUtils
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem
import ch.heigvd.pdg_grocerypal.data.model.In_Shopping_List
import ch.heigvd.pdg_grocerypal.data.model.Ingredient
import ch.heigvd.pdg_grocerypal.data.model.Ingredient_Quantity
import ch.heigvd.pdg_grocerypal.data.model.Unit


class GroceryPalDBHelper(context: Context) : SQLiteOpenHelper(context, "GroceryPalLocalDB",null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE Unit ( " +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT );"
        )
        db.execSQL(
            "CREATE TABLE Ingredient( " +
                    "ID INTEGER PRIMARY KEY," +
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
        // insertDefaultIngredients(db)
        //insertDefaultShoppingList(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not useful in our case")
    }

    private fun insertDefaultUnits(db: SQLiteDatabase) {
        val unitValues = arrayOf(
            "('g')",
            "('ml')",
            "('pcs')",
            "('c.à.c')",
            "('c.à.s')"
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
                "In_Shopping_List.Unit_id AS UnitId, " +
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
                val ingredientIdIndex = cursor.getColumnIndex("IngredientId")
                val unitIdIndex = cursor.getColumnIndex("UnitId")
                val nameIndex = cursor.getColumnIndex("Name")
                val unitIndex = cursor.getColumnIndex("Unit")
                val quantityIndex = cursor.getColumnIndex("Quantity")
                val isPurchasedIndex = cursor.getColumnIndex("IsPurchased")

                val ingredientId = if (ingredientIdIndex != -1) cursor.getInt(ingredientIdIndex) else 0
                val unitId = if (unitIdIndex != -1) cursor.getInt(unitIdIndex) else 0
                val name = if (nameIndex != -1) cursor.getString(nameIndex) else ""
                val unit = if (unitIndex != -1) cursor.getString(unitIndex) else ""
                val quantity = if (quantityIndex != -1) cursor.getInt(quantityIndex) else 0
                val isPurchased = if (isPurchasedIndex != -1) cursor.getInt(isPurchasedIndex) == 1 else false

                val groceryItem = GroceryItem(
                    ingredientId,
                    unitId,
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

    fun getAllInShoppingListItems(): List<In_Shopping_List> {
        val inShoppingList = mutableListOf<In_Shopping_List>()
        val db = readableDatabase

        val query = "SELECT * FROM In_Shopping_List"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex("Ingredient_id")
                val unitIdIndex = cursor.getColumnIndex("Unit_id")
                val quantityIndex = cursor.getColumnIndex("Quantity")
                val buyIndex = cursor.getColumnIndex("Buy")

                val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
                val unitId = if (unitIdIndex != -1) cursor.getInt(unitIdIndex) else 0
                val quantity = if (quantityIndex != -1) cursor.getInt(quantityIndex) else 0
                val buy = if (buyIndex != -1) cursor.getInt(buyIndex) == 1 else false

                val inShoppingListItem = In_Shopping_List(id, unitId, quantity, buy)
                inShoppingList.add(inShoppingListItem)
            } while (cursor.moveToNext())
        }
        val returnShopping_List: List<In_Shopping_List>
        returnShopping_List = inShoppingList

        cursor.close()
        db.close()

        return returnShopping_List
    }

    fun areShoppingListsDifferent(remoteShoppingList: MutableList<In_Shopping_List>): Boolean {
        val localShoppingList = getAllInShoppingListItems()

        return localShoppingList != remoteShoppingList
    }

//    fun logShoppingList(shoppingList: List<In_Shopping_List>) {
//        for (item in shoppingList) {
//            Log.d("ShoppingList", "ID: ${item.id}, UnitID: ${item.unitId}, Quantity: ${item.quantity}, Buy: ${item.buy}")
//        }
//    }

    fun clearShoppingList() {
        val db = writableDatabase

        // Delete all records from the In_Shopping_List table
        db.delete("In_Shopping_List", null, null)

        db.close()
    }


    fun deletePurchasedItem(ingredientId: Int, unitId: Int) {
        val db = writableDatabase
        val whereClause = "Ingredient_id = ? AND Unit_id = ?"
        val whereArgs = arrayOf(ingredientId.toString(), unitId.toString())

        db.delete("In_Shopping_List", whereClause, whereArgs)
        db.close()
    }

    fun deleteAllPurchasedItems(context: Context) {
        val db = writableDatabase
        val whereClause = "Buy = 1"
        db.delete("In_Shopping_List", whereClause, null)

        db.close()

        ConnectionRecipeUtils.postShoppingListWithAuthToken(context)
    }

    fun updateItemPurchasedStatus(ingredientId: Int, unitId: Int, isPurchased: Boolean) {
        val db = writableDatabase
        val whereClause = "Ingredient_id = ? AND Unit_id = ?"
        val whereArgs = arrayOf(ingredientId.toString(), unitId.toString())
        val values = ContentValues()
        values.put("Buy", if (isPurchased) 1 else 0)
        db.update("In_Shopping_List", values, whereClause, whereArgs)
        db.close()
    }

    fun updateQuantityInShoppingList(ingredientId: Int, unitId: Int, newQuantity: Int) {
        val db = writableDatabase
        val whereClause = "Ingredient_id = ? AND Unit_id = ?"
        val whereArgs = arrayOf(ingredientId.toString(), unitId.toString())
        val values = ContentValues()
        values.put("Quantity", newQuantity)
        db.update("In_Shopping_List", values, whereClause, whereArgs)
        db.close()
    }


    fun getAllIngredients(): MutableList<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        val db = readableDatabase
        val query = "SELECT * FROM Ingredient"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex("ID")
                val nameIndex = cursor.getColumnIndex("Name")
                val fiberIndex = cursor.getColumnIndex("Fiber")
                val proteinIndex = cursor.getColumnIndex("Protein")
                val energyIndex = cursor.getColumnIndex("Energy")
                val carbsIndex = cursor.getColumnIndex("Carbs")
                val fatIndex = cursor.getColumnIndex("Fat")

                val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
                val name = if (nameIndex != -1) cursor.getString(nameIndex) else ""
                val fiber = if (fiberIndex != -1) cursor.getFloat(fiberIndex) else 0.0f
                val protein = if (proteinIndex != -1) cursor.getFloat(proteinIndex) else 0.0f
                val energy = if (energyIndex != -1) cursor.getInt(energyIndex) else 0
                val carb = if (carbsIndex != -1) cursor.getFloat(carbsIndex) else 0.0f
                val fat = if (fatIndex != -1) cursor.getFloat(fatIndex) else 0.0f

                val ingredient = Ingredient(id, name, fiber, protein, energy, carb, fat)
                ingredients.add(ingredient)
            }
        }

        db.close()

        return ingredients
    }

    fun getAllUnits(): MutableList<Unit> {
        val unitList = mutableListOf<Unit>()
        val db = readableDatabase

        val query = "SELECT ID, Name FROM Unit"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex("ID")
                val nameIndex = cursor.getColumnIndex("Name")

                val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
                val name = if (nameIndex != -1) cursor.getString(nameIndex) else ""
                val unit = Unit(id, name)
                unitList.add(unit)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return unitList
    }

    /**
     * add the current shoppingList item to In_Shopping_List table and send it to online DB
     */
    fun addOrUpdateShoppingListItem(context: Context, ingredientId: Int, unitId: Int, quantity: Int) {

        _addOrUpdateShoppingListItem(context, ingredientId, unitId, quantity)
        ConnectionRecipeUtils.postShoppingListWithAuthToken(context)

    }

    /**
     * add the current shoppingList item to In_Shopping_List table
     */
    private fun _addOrUpdateShoppingListItem(context: Context, ingredientId: Int, unitId: Int, quantity: Int) {
        val db = writableDatabase

        val whereClause = "Ingredient_id = ? AND Unit_id = ?"
        val whereArgs = arrayOf(ingredientId.toString(), unitId.toString())

        val cursor = db.query("In_Shopping_List", arrayOf("Quantity"), whereClause, whereArgs, null, null, null)
        val columnIndex = cursor.getColumnIndex("Quantity")
        val existingQuantity = if (columnIndex != -1 && cursor.moveToFirst()) {
            cursor.getInt(columnIndex)
        } else {
            0
        }
        val newQuantity = existingQuantity + quantity

        if (cursor.moveToFirst()) {
            val contentValues = ContentValues()
            contentValues.put("Quantity", newQuantity)
            db.update("In_Shopping_List", contentValues, whereClause, whereArgs)
        } else {
            val contentValues = ContentValues()
            contentValues.put("Ingredient_id", ingredientId)
            contentValues.put("Unit_id", unitId)
            contentValues.put("Quantity", newQuantity)

            db.insert("In_Shopping_List", null, contentValues)
        }
        cursor.close()
        db.close()
    }

    /**
     * add a full shopping list and send the local database to online DB
     */
    fun addOrUpdateShoppingListItems(context: Context,ingredients: List<Ingredient_Quantity>) {
        for (ingredient in ingredients) {
            _addOrUpdateShoppingListItem(context, ingredient.id, ingredient.unitId, ingredient.quantity)
        }
        ConnectionRecipeUtils.postShoppingListWithAuthToken(context)
    }


    fun getUnitName(unitId: Int): String {
        val db = readableDatabase
        val query = "SELECT Name FROM Unit WHERE ID = ?"
        val cursor = db.rawQuery(query, arrayOf(unitId.toString()))

        var unitName = ""
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex("Name")
            unitName = cursor.getString(nameIndex) ?: ""
        }

        cursor.close()
        db.close()

        return unitName
    }

    fun insertIngredients(ingredientsList: List<Ingredient>) {
        val db = writableDatabase

        db.beginTransaction()
        try {
            for (ingredient in ingredientsList) {
                val values = ContentValues()
                values.put("ID", ingredient.id)
                values.put("Name", ingredient.name)
                values.put("Fiber", ingredient.fiber)
                values.put("Protein", ingredient.protein)
                values.put("Energy", ingredient.energy)
                values.put("Carbs", ingredient.carb)
                values.put("Fat", ingredient.fat)

                // Insert with conflict resolution strategy
                db.insertWithOnConflict("Ingredient", null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun insertShoppingListItems(shoppingList: List<In_Shopping_List>) {
        val db = writableDatabase

        for (item in shoppingList) {
            val values = ContentValues()
            values.put("Ingredient_id", item.id)
            values.put("Unit_id", item.unitId)
            values.put("Quantity", item.quantity)
            values.put("Buy", item.buy)

            db.insert("In_Shopping_List", null, values)
        }

        db.close()
    }

}