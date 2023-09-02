package ch.heigvd.pdg_grocerypal.recipes

import android.os.Parcel
import android.os.Parcelable
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem

data class RecipeCard(
    val recipeImage: Int,
    val recipeName: String,
    val recipeDuration: String,
    val groceryList: List<GroceryItem> // Include groceryList here
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        // Read groceryList from parcel
        mutableListOf<GroceryItem>().apply {
            parcel.readTypedList(this, GroceryItem.CREATOR)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(recipeImage)
        parcel.writeString(recipeName)
        parcel.writeString(recipeDuration)
        // Write groceryList to parcel
        parcel.writeTypedList(groceryList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecipeCard> = object : Parcelable.Creator<RecipeCard> {
            override fun createFromParcel(parcel: Parcel): RecipeCard {
                return RecipeCard(parcel)
            }

            override fun newArray(size: Int): Array<RecipeCard?> {
                return arrayOfNulls(size)
            }
        }
    }
}
