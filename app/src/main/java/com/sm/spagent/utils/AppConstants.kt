package com.sm.spagent.utils

public class AppConstants {
    public class Http {
        companion object {
            private val baseUrl = "https://stagingapp.engine.shurjopayment.com/"
            private val apiBaseUrl = "https://stagingapp.engine.shurjopayment.com/api/"
            private val loginBaseUrl = "http://104.248.147.247:8000/api/v1/"
            //
            val QR_LOGIN = "{$loginBaseUrl}qr-login/"
            val SHOP_OWNER = "{$baseUrl}shop_owner_img/"
            val SHOP_OWNER_IMAGE = "{$baseUrl}shop_owner_img/"
        }
    }
}