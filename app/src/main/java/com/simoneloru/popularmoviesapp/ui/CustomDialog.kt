package com.simoneloru.popularmoviesapp.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.simoneloru.popularmoviesapp.R


class CustomDialog(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.movie_detail)

    }
}
