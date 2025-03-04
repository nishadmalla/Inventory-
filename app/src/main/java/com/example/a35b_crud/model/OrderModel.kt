package com.example.a35b_crud.model

import android.os.Parcel
import android.os.Parcelable

data class OrderModel(
    var orderId: String = "",
    var userId: String = "",
    var orderDate: String = "",
    var totalAmount: Double = 0.0,
    var orderStatus: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(userId)
        parcel.writeString(orderDate)
        parcel.writeDouble(totalAmount)
        parcel.writeString(orderStatus)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel = OrderModel(parcel)
        override fun newArray(size: Int): Array<OrderModel?> = arrayOfNulls(size)
    }
}
