package com.example.gostyleapp

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_code.view.*
import kotlinx.android.synthetic.main.item_row.view.*

/** Data list adapter */
class DataListAdapter(var dataList: List<QRCodeModel>, val clickListener: (QRCodeModel) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    /** List view holder */
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        /** QR Code repo */
        val qrCodeRepo: QRCodeRepo = QRCodeRepo();

        /**
         * Disable row
         *
         * @param itemView The item view
         */
        fun disableRow(itemView: View) {
            itemView.category.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
            itemView.dateFinValidite.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
            itemView.description.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
            itemView.valeur.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
            itemView.btn_close_qr.visibility = View.INVISIBLE;
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        fun bind(model: QRCodeModel, clickListener: (QRCodeModel) -> Unit){
            itemView.category.text = model.category;
            itemView.dateFinValidite.text = model.end;
            itemView.description.text = model.description;
            itemView.valeur.text = "${model.value} €";

            if(!model.status) {
                disableRow(itemView);
            }

            itemView.setOnClickListener{
                clickListener(model)
            }

            itemView.btn_close_qr.setOnClickListener() {
                qrCodeRepo.disableItem(itemView.context.getSharedPreferences("qrCodesList", MODE_PRIVATE), model);
                disableRow(itemView);
            }
        }
    }

    /**
     * On create view holder
     *
     * @param parent   The view group
     * @param viewType The view type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ListViewHolder(view)
    }

    /**
     * On bind view holder
     *
     * @param holder   The holder
     * @param position The position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListViewHolder).bind(dataList[position], clickListener)
    }

    /** Get item count */
    override fun getItemCount(): Int {
        return dataList.size
    }

}
/*class ListViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_list_code, parent, false)) {
    private var mCategoryView: TextView? = null
    private var mDateFinValiditeView: TextView? = null
    private var mDescriptionView: TextView? = null
    private var mValeurView: TextView? = null

    init{
        mCategoryView = itemView.findViewById(R.id.category)
        mDateFinValiditeView = itemView.findViewById(R.id.dateFinValidite)
        mDescriptionView = itemView.findViewById(R.id.description)
        mValeurView = itemView.findViewById(R.id.valeur)
    }

    fun bind(model: QRCodeModel) {
        println(model)
        mCategoryView?.text = model.Category
        mDateFinValiditeView?.text = model.FinishDatePromo
        mDescriptionView?.text = model.Description
        mValeurView?.text = model.ValPromo.toString()
        /*println("pute")
        println(itemView.category)
        println("oui")
        itemView.category.text = model.Category
        itemView.dateFinValidite.text = model.FinishDatePromo
        itemView.description.text = model.Description
        itemView.valeur.text = model.ValPromo.toString()
        println(itemView.category.text)*/
    }
}

class DataListAdapter(private val dataList: List<QRCodeModel>) : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val model: QRCodeModel = dataList[position]
        holder.bind(model)
    }
}*/
