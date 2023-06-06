package com.example.gsontest.util

import android.content.Context
import java.io.IOException

import java.io.InputStream
import java.nio.charset.Charset


object LocalResourceUtil {
    fun loadJSONFromAsset(context: Context, file: String): String? {
        var json: String? = try {
            val `is`: InputStream = context.assets.open(file)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}