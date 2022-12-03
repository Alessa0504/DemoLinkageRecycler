package adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import base.BaseRecyclerHolder
import base.BaseRecyclerViewAdapter
import bean.Item
import com.example.demolinkagerecycler.R

class RAdapter(context: Context, resLayout: Int, data: List<Item>) :
    BaseRecyclerViewAdapter<Item>(context, resLayout, data) {

    override fun convert(holder: BaseRecyclerHolder?, position: Int) {
        holder?.getView<TextView>(R.id.tvName)?.text =
            String.format(getmData()[position].title + getmData()[position].name)
        holder?.getView<TextView>(R.id.tvPrice)?.text = getmData()[position].price
        //悬停的标题头-RAdapter传入的resLayout是item_goods.xml
        val headerLayout = holder?.getView<FrameLayout>(R.id.stick_header)
        val tvHeader = holder?.getView<TextView>(R.id.tv_header)
        if (position == 0) {  // 防止position=-1越界
            headerLayout?.visibility = View.VISIBLE
            tvHeader?.text = getmData()[position].title
        } else {
            // 上下滑动时，当上下两项属于同一个分类时，同一个分类的不同名称item中间，不展示悬停的标题头(分类x)
            if (TextUtils.equals(getmData()[position].title, getmData()[position - 1].title)) {
                headerLayout?.visibility = View.GONE
            } else {
                headerLayout?.visibility = View.VISIBLE
                tvHeader?.text = getmData()[position].title
            }
        }
    }
}