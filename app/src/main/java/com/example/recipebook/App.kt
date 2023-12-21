package com.example.recipebook

import android.app.Application
import com.example.recipebook.data.MainDb

class App : Application() {
    val database by lazy { MainDb.createDataBase(this) }
}