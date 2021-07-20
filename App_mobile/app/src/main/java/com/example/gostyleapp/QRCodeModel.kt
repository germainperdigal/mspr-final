package com.example.gostyleapp

import com.google.gson.annotations.SerializedName

/** QR Code model */
data class QRCodeModel(
    /** QR Code ID */
    @SerializedName("qr-code-id") val id: String,

    /** QR Code discount value */
    @SerializedName("valeur-code-promo") var value: Float,

    /** QR Code discount start date */
    @SerializedName("date-debut-promotion") val start: String,

    /** QR Code discount end date */
    @SerializedName("date-fin-promotion") val end: String,

    /** QR Code category */
    @SerializedName("categorie") val category: String,

    /** QR Code description */
    @SerializedName("description") val description: String,

    /** QR Code status */
    @SerializedName("statut") var status: Boolean
)