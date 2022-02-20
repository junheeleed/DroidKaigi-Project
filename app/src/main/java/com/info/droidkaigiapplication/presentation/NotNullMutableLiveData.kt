package com.info.droidkaigiapplication.presentation

import androidx.lifecycle.MutableLiveData

class NotNullMutableLiveData<T>(value: T): MutableLiveData<T>(value) {
}