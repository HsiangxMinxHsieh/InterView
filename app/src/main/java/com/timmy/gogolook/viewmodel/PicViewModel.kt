package com.timmy.gogolook.viewmodel

import android.view.KeyEvent
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.util.*

class PicViewModel @ViewModelInject constructor(private val picRepository: PicRepository) : ViewModel() {

    val observeIsLoading: ObservableField<Boolean> by lazy { ObservableField<Boolean>() }

    val haveContent: ObservableField<Boolean> by lazy { ObservableField<Boolean>() }

    val observeContent: ObservableField<String> by lazy { ObservableField<String>() }

    val liveSearchRecord: MutableLiveData<TreeSet<String>> by lazy { MutableLiveData<TreeSet<String>>() }

    val liveHideKeyBoard: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val liveShowToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        getData()
        liveSearchRecord.postValue(TreeSet())
    }


    private fun getData() {
        showGetAPIScreen()
        picRepository.getDataFromAPI()
    }

    fun getLiveDataByAPI() = picRepository.getLiveDataByAPI()

    /**搜尋方法*/
    fun search() {
        liveHideKeyBoard.postValue(true) // 先隱藏鍵盤再執行搜尋。
        showGetAPIScreen()

        // 開始執行搜尋動作
        val searchString = observeContent.get() ?: return
        observeContent.set("") //清空搜尋文字(讓下次案下去會顯示搜尋紀錄)
        if (searchString.isBlank()) {
            liveShowToast.postValue("Empty input, use the default value to display.")
        }
        val list = liveSearchRecord.value
        list?.add(searchString)
        liveSearchRecord.postValue(list)

        picRepository.searchFromAPI(searchString)
    }

    /**初始化搜尋中畫面*/
    private fun showGetAPIScreen() {
        observeIsLoading.set(true)
    }

    /**Enter可以直接搜尋 為了要兼容模擬器時電腦鍵盤可以用，花費不少功夫在嘗試...Orz*/
    val editorActionListener: TextView.OnEditorActionListener = TextView.OnEditorActionListener { _, actionId, event ->
        Timber.e("執行到搜尋")
        if ((actionId == KeyEvent.KEYCODE_UNKNOWN || actionId == KeyEvent.KEYCODE_CALL) && event?.action != KeyEvent.ACTION_DOWN) {
            search()
        }
        true
    }

    companion object {

        @JvmStatic
        @BindingAdapter("onEditorActionListener")
        fun bindOnEditorActionListener(editText: EditText, editorActionListener: TextView.OnEditorActionListener) {
            editText.setOnEditorActionListener(editorActionListener)
        }

    }

}