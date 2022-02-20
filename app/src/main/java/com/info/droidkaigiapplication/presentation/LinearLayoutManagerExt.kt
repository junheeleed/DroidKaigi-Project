package com.info.droidkaigiapplication.presentation

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager

fun LinearLayoutManager.getScrollState(): ScrollState {
    val savedState = onSaveInstanceState()
    val parcel = Parcel.obtain()
    savedState!!.writeToParcel(parcel, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    parcel.setDataPosition(0)

    val firstInt = parcel.readInt()
    val secondInt = parcel.readInt()

    val scrollState = ScrollState(
            anchorPosition = firstInt,
            anchorOffset = secondInt)
    parcel.recycle()

    return scrollState
}

fun LinearLayoutManager.restoreScrollState(scrollState: ScrollState) {
    if (scrollState.anchorPosition < 0) return

    val parcel = Parcel.obtain()
    parcel.writeInt(scrollState.anchorPosition)
    parcel.writeInt(scrollState.anchorOffset)
    parcel.writeInt(0)
    parcel.setDataPosition(0)

    val savedState = LinearLayoutManager.SavedState.CREATOR.createFromParcel(parcel)

    parcel.recycle()

    onRestoreInstanceState(savedState)
}

class ScrollState(val anchorPosition: Int, val anchorOffset: Int)
