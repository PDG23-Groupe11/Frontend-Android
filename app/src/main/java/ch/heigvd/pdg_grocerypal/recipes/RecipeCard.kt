package ch.heigvd.pdg_grocerypal.recipes

import android.os.Parcel
import android.os.Parcelable
import ch.heigvd.pdg_grocerypal.data.model.GroceryItem

data class RecipeCard(
        val id: Int,
        val recipeName: String,
        val nb_per: Int,
        val recipeDuration: String,
        val instruction: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(recipeName)
        parcel.writeInt(nb_per)
        parcel.writeString(recipeDuration)
        parcel.writeString(instruction)
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