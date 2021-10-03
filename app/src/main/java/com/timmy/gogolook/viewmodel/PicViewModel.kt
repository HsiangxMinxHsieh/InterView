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

    /**是否正在載入中，用於顯示加載框 true=>Loading中，false=>載入完畢 */
    val observeIsLoading: ObservableField<Boolean> by lazy { ObservableField<Boolean>() }

    /**是否有搜尋到內容，用於顯示搜索結果是否有內容 true=>有內容，不顯示，false=>沒有內容，要顯示 */
    val haveContent: ObservableField<Boolean> by lazy { ObservableField<Boolean>() }

    /**輸入框文字，用於雙向綁定EditText物件 */
    val observeContent: ObservableField<String> by lazy { ObservableField<String>() }

    /**搜尋紀錄列表，用於儲存與更新搜尋紀錄 */
    val liveSearchRecord: MutableLiveData<TreeSet<String>> by lazy { MutableLiveData<TreeSet<String>>() }

    /**關閉軟鍵盤通知，用於通知Activity關閉軟鍵盤*/
    val liveHideKeyBoard: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    /**訊息通知，用於通知Activity顯示Toast訊息 */
    val liveShowToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        getData() // 初始化時取得資料
        liveSearchRecord.postValue(TreeSet()) //初始化塞值(不然裡面是null)
    }

    private fun getData() {
        showGetAPIScreen()
        picRepository.getDefaultDataFromAPI()
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

    fun getIsListLayout() = picRepository.getIsListLayout()

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