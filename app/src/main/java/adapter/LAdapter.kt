package adapter

import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseRecyclerHolder
import base.BaseRecyclerViewAdapter
import bean.ItemL
import com.example.demolinkagerecycler.R

class LAdapter(context: Context, resLayout: Int, data: List<ItemL>) :
    BaseRecyclerViewAdapter<ItemL>(context, resLayout, data) {
    //当前选中项
    private var checked = 0

    private var context: Context? = null

    init {
        this.context = context
    }

    override fun convert(holder: BaseRecyclerHolder?, position: Int) {
        val tvTitle = holder?.getView<TextView>(R.id.tv_title)
        tvTitle?.text = getmData()[position].title
        if (checked == position) {
            tvTitle?.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            tvTitle?.setBackgroundResource(R.color.colorfff)
        } else {
            tvTitle?.setTextColor(ContextCompat.getColor(context!!, R.color.color666))
            tvTitle?.setBackgroundResource(R.color.color16333333)
        }
    }

    fun setChecked(checked: Int) {
        this.checked = checked
        notifyDataSetChanged()  //调用后会触发requestLayout, onBindViewHolder会被调用
    }

    /**
     * 让左边的额条目选中
     */
    fun setToPosition(title: String?) {
        if (TextUtils.equals(title, getmData()[checked].title)) return
        if (TextUtils.isEmpty(title)) return
        for (i in 0..getmData().size) {
            if (TextUtils.equals(getmData()[i].title, title)) {
                setChecked(i)
                moveToPosition(i)
                return
            }
        }
    }

    /**
     * 如果选中的条目不在显示范围内，要滑动条目让该条目显示出来
     */
    private fun moveToPosition(index: Int) {
        val linearLayoutManager = getRecyclerView()?.layoutManager as LinearLayoutManager
//        val first = linearLayoutManager.findFirstVisibleItemPosition()
//        val last = linearLayoutManager.findLastVisibleItemPosition()
//        if (index <= first || index >= last) {
//            linearLayoutManager.scrollToPosition(index)
//        }
        linearLayoutManager.scrollToPositionWithOffset(index, 0)
    }
}