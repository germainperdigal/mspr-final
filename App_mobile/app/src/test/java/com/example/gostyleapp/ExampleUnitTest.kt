package com.example.gostyleapp

import android.content.SharedPreferences
import com.google.gson.Gson
import org.junit.*
import org.junit.Assert
import org.mockito.*
import org.mockito.Mockito.`when`
import kotlin.reflect.typeOf


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
internal class ExampleUnitTest {
    /** Shared preferences */
    @Mock
    var sp: SharedPreferences? = null

    /** Editor */
    @Mock
    var editor: SharedPreferences.Editor? = null

    /** List */
    val list = mutableListOf<QRCode>();

    /** Gson */
    val gson = Gson();

    /** Qr code repo */
    val qrCoderepo = QRCodeRepo();

    /** Test QR */
    val testQr = QRCode("id", 12.toFloat(), "start", "end", "cat", "desc", true);

    @Before
    fun setup() {
        sp = Mockito.mock(SharedPreferences::class.java)
        editor = Mockito.mock(SharedPreferences.Editor::class.java)
        list.add(testQr);
    }

    @Test
    fun testListOnLoading() {
        `when`(sp!!.getString("qrListTemp", "empty")).thenReturn(gson.toJson(list).toString())
        val list = qrCoderepo.getStoredQRCodeList(sp!!);

        assert(list.size == 1);
        assert(list.contains(testQr));
    }
}