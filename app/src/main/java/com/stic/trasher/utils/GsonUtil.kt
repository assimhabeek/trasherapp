package com.stic.trasher.utils

import com.google.gson.GsonBuilder
import java.sql.Date

object GsonUtil {

    val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, GsonDateAdapter())
        .registerTypeHierarchyAdapter(ByteArray::class.java, GsonByteArrayAdapter())
        .serializeNulls()
        .create()

}