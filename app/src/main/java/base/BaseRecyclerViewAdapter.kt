package base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T>(context: Context, resLayout: Int, data: List<T>) :
    RecyclerView.Adapter<BaseRecyclerHolder>() {
    private var context: Context? = null
    private var resLayout: Int? = null
    private var listResLayout: ArrayList<Int>? = null
    private var mData: List<T>? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var recyclerView: RecyclerView? = null

    init {
        this.context = context
        this.resLayout = resLayout
        this.listResLayout = ArrayList()
        this.listResLayout?.add(resLayout)
        this.mData = data
    }

    fun getmData(): List<T> {
        return mData!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder {
        val view = LayoutInflater.from(context).inflate(listResLayout!![viewType], parent, false)
        val holder = BaseRecyclerHolder(view)
        if (null != mOnItemClickListener) {
            // 每一项item的点击事件
            holder.itemView.setOnClickListener { v ->
                // holder.adapterPosition：获取item位置，如果RecyclerView正在刷新，则获取到的是-1
                mOnItemClickListener!!.onItemClick(v, holder.adapterPosition)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseRecyclerHolder, position: Int) {
        convert(holder, position)
    }

    /**
     * 需要重写的方法
     * @param holder
     * @param position
     */
    abstract fun convert(holder: BaseRecyclerHolder?, position: Int)

    override fun getItemCount(): Int {
        return mData!!.size
    }


    fun setOnItemClickListener(listener: OnItemClickListener?): BaseRecyclerViewAdapter<T> {
        mOnItemClickListener = listener
        return this
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun bindToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
    }

    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }
}