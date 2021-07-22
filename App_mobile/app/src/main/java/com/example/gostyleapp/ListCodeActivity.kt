package com.example.gostyleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_code.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_row.*

class ListCodeActivity : AppCompatActivity(), (QRCode) -> Unit {
    private val qrCodeRepo: QRCodeRepo = QRCodeRepo()
    private var dataList: ArrayList<QRCode> = ArrayList()
    private val qrCodeAdapter: DataListAdapter = DataListAdapter(dataList, this)

    /**
     * On create
     *
     * @param savedInstanceState  The saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_code);
        val storedList = this.qrCodeRepo.getStoredQRCodeList(getSharedPreferences("qrCodesList", MODE_PRIVATE));

        if (storedList.count() > 0) {
            val sortedList = storedList.reversed();
            sortedList.forEach {
                dataList.add(it);
                qrCodeAdapter.dataList = dataList;
                qrCodeAdapter.notifyDataSetChanged();
            }
        } else {
            emptyListText.visibility = View.VISIBLE;
            emptyListText.text = "Aucun code promo scanné... À vous de jouer ! ${String(Character.toChars(0x270C))}";
        }


        listViewCodePromo.layoutManager = LinearLayoutManager(this);
        listViewCodePromo.adapter = qrCodeAdapter;

        btn_return_details.setOnClickListener {
            startActivity(Intent(this@ListCodeActivity, MainActivity::class.java));
            finish();
        }
    }

    override fun invoke(model: QRCode) {

    }
}
