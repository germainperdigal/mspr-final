package com.example.gostyleapp

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


private const val CAMERA_REQUEST_CODE = 101

/** Main Activity class */
class MainActivity : AppCompatActivity(){
    /** Code scanner */
    private lateinit var codeScanner: CodeScanner;

    /** Client */
    private val client = OkHttpClient();

    /** QR Code repo */
    private val qrCodeRepo: QRCodeRepo = QRCodeRepo();

    /** Data list */
    private var dataList: ArrayList<QRCodeModel> = ArrayList();

    /**
     * On create
     *
     * @param savedInstanceState  The saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codeScanner();
        setupPermissions();

        btn_list_view.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListCodeActivity::class.java));
            finish();
        }
    }

    /** Code scanner */
    private fun codeScanner() {
        val sharedPreferences = getSharedPreferences("qrCodesList", MODE_PRIVATE);
        val qrList = qrCodeRepo.getStoredQRCodeList(sharedPreferences);

        codeScanner = CodeScanner(this, scanner_view);

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK;
            formats = CodeScanner.ALL_FORMATS;

            autoFocusMode = AutoFocusMode.SAFE;
            scanMode = ScanMode.CONTINUOUS;
            isAutoFocusEnabled = true;
            isFlashEnabled = false;
            text_view.text = "Scannez le QR Code ici...";

            val intent = Intent(this@MainActivity, ListCodeActivity::class.java);

            /** When QR successfly scanned */
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val request: Request = Request.Builder()
                        .url("https://msprdesesmorts.free.beeceptor.com/qr")
                        //.url("api/${it.text}")
                        .build();

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("Main", "Erreur lors de l'appel API: ${e.message}");
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val gson = Gson();

                            val body = response.body?.string();
                            val items = body?.substringAfter("\"items\":[")?.substringBefore("],")

                            val qrcode: QRCodeModel = gson.fromJson(items, QRCodeModel::class.java);

                            if (qrList.indexOf(qrcode) != -1) {
                                qrList.add(qrcode);
                                qrCodeRepo.saveNewQr(sharedPreferences, qrList);
                            }
                        }
                    });
                }
            };

            startActivity(intent);
            finish();

            /** When QR scan throws error */
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Erreur d'initialisation de camera: ${it.message}")
                }
            };
        };

        scanner_view.setOnClickListener {
            codeScanner.startPreview();
        };
    }

    /** On resume */
    override fun onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    /** On pause */
    override fun onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    /** Setup permissions */
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    /** Make request */
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE);
    }

    /** On request permissions result */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE ->
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Vous devez autoriser l'accès à votre caméra pour profiter de l'application!", Toast.LENGTH_SHORT);
            }
        }
    }
}