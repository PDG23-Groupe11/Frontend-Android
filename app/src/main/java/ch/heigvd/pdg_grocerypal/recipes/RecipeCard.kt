package ch.heigvd.pdg_grocerypal.recipes

import android.os.Parcel
import android.os.Parcelable
data class RecipeCard(val recipeImage:Int, val recipeName:String, val recipeDuration: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(recipeImage)
        parcel.writeString(recipeName)
        parcel.writeString(recipeDuration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeCard> {
        override fun createFromParcel(parcel: Parcel): RecipeCard {
            return RecipeCard(parcel)
        }

        override fun newArray(size: Int): Array<RecipeCard?> {
            return arrayOfNulls(size)
        }
    }
}
