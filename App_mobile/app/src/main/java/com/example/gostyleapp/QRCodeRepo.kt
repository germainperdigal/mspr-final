package com.example.gostyleapp

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QRCodeRepo {

    /** Gson */
    val gson = Gson();

    /**
     * Get stored QR Code list
     *
     * @param sharedPreferences The shared preferences
     */
    fun getStoredQRCodeList(sharedPreferences: SharedPreferences): MutableList<QRCodeModel> {
        val storedList = sharedPreferences.getString("qrListTemp", "empty");

        return if (storedList !== "empty") {
            val listType = object : TypeToken<List<QRCodeModel>>() {}.type;

            gson.fromJson(storedList, listType);
        } else {
            mutableListOf();
        }
    }

    /**
     * Save new QR in device
     *
     * @param sharedPreferences  The shared preferences
     * @param newList            The new list of QRs
     */
    fun saveNewQr(sharedPreferences: SharedPreferences, newList: MutableList<QRCodeModel>) {
        val editor = sharedPreferences.edit();
        val stringList = gson.toJson(newList).toString();

        editor.putString("qrListTemp", stringList);
        editor.apply();
    }

    /**
     * Disable item
     *
     * @param sharedPreferences  The shared preferences
     * @param item               The item do disable
     */
    fun disableItem(sharedPreferences: SharedPreferences, item: QRCodeModel) {
        val storedList = sharedPreferences.getString("qrListTemp", "empty");

            val listType = object : TypeToken<List<QRCodeModel>>() {}.type;
            val list: MutableList<QRCodeModel> = gson.fromJson(storedList, listType);

            list.forEach {
                if (it.id == item.id) {
                    it.status = false;
                }
            }

            saveNewQr(sharedPreferences, list);
    }
}
