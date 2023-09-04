package ch.heigvd.pdg_grocerypal.data.model

import android.os.Parcel
import android.os.Parcelable

data class GroceryItem(
    val name: String,
    val unit: String,
    var quantity: Int,
    var isPurchased: Boolean = false
) : Parcelable {

    // Implement the describeContents function as required by Parcelable
    override fun describeContents(): Int {
        return 0
    }

    // Implement the writeToParcel function to write the object's properties to a Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(unit)
        parcel.writeInt(quantity)
        parcel.writeByte(if (isPurchased) 1 else 0) // 1 if true, 0 if false
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GroceryItem> = object : Parcelable.Creator<GroceryItem> {
            override fun createFromParcel(parcel: Parcel): GroceryItem {
                return GroceryItem(parcel)
            }

            override fun newArray(size: Int): Array<GroceryItem?> {
                return arrayOfNulls(size)
            }
        }

        // Helper function to read a list of GroceryItem from a Parcel
        fun readList(parcel: Parcel): List<GroceryItem> {
            val listSize = parcel.readInt()
            val groceryItemList = ArrayList<GroceryItem>(listSize)
            for (i in 0 until listSize) {
                groceryItemList.add(GroceryItem(parcel))
            }
            return groceryItemList
        }
    }

    // Constructor for reading from a Parcel
    constructor(parcel: Parcel) : this(
        name = parcel.readString() ?: "",
        unit = parcel.readString() ?: "",
        quantity = parcel.readInt(),
        isPurchased = parcel.readByte() != 0.toByte() // Convert 0/1 to Boolean
    )
}
