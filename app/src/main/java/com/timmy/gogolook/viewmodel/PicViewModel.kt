package com.timmy.gogolook.viewmodel

import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PicViewModel @ViewModelInject constructor(private val picRepository: PicRepository) : ViewModel() {

    val observeIsLoading: ObservableField<Boolean> by lazy { ObservableField<Boolean>() }

    val haveContent: ObservableField<Boolean> by lazy { ObservableField<Boolean>() }

    val observeContent: ObservableField<String> by lazy { ObservableField<String>() }

    val liveHideKeyBoard: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val liveShowToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun getData() {
        showGetAPIScreen()
        picRepository.getDataFromAPI()
    }


    fun getLiveDataByAPI() = picRepository.getLiveDataByAPI()

    /**搜尋方法*/
    fun search() {
        liveHideKeyBoard.postValue(true) // 先隱藏鍵盤再執行搜尋。
        showGetAPIScreen()

        // 開始執行搜尋動作
        val searchString = observeContent.get()
        if(searchString.isNullOrBlank()){
            liveShowToast.postValue("Empty input, use the default value to display.")
        }
        picRepository.searchFromAPI(searchString)
    }

    /**初始化搜尋中畫面*/
    private fun showGetAPIScreen() {
        observeIsLoading.set(true)
    }

    /**Enter可以直接搜尋*/
    val editorActionListener: TextView.OnEditorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == KeyEvent.KEYCODE_UNKNOWN || actionId == KeyEvent.KEYCODE_CALL) {
            search()
            true
        } else false
    }

    companion object {

        @JvmStatic
        @BindingAdapter("app:onEditorActionListener")
        fun bindOnEditorActionListener(editText: EditText, editorActionListener: TextView.OnEditorActionListener) {
            editText.setOnEditorActionListener(editorActionListener)
        }
    }

}