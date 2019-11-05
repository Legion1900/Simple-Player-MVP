package com.legion1900.mvpplayer.views.listeners

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.legion1900.mvpplayer.contracts.PlayerContract

class ItemSelectedListener(
    private val presenter: PlayerContract.ChooserPresenter,
    private val onEvent: PlayerContract.ChooserPresenter.(String) -> Unit
) : AdapterView.OnItemSelectedListener {

    private var falseTrigger = true

    override fun onNothingSelected(parent: AdapterView<*>?) {
        /*
        * Nothing to do here.
        * */
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (falseTrigger)
            falseTrigger = false
        else {
            val data = (view as TextView).text
            presenter.onEvent(data.toString())
        }
    }
}