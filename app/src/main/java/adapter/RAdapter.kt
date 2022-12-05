package adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import base.BaseRecyclerHolder
import base.BaseRecyclerViewAdapter
import bean.Item
import com.example.demolinkagerecycler.R
import imp.ShopCart

class RAdapter(context: Context, resLayout: Int, data: List<Item>, shopCartImp: ShopCart) :
    BaseRecyclerViewAdapter<Item>(context, resLayout, data) {

    private val TAG = "RAdapter"
    private lateinit var dishItem: Item   //某一项position位置的item
    private var shopCartImp: ShopCart

    init {
        this.shopCartImp = shopCartImp
    }

    override fun convert(holder: BaseRecyclerHolder?, position: Int) {
        //某项菜品
        dishItem = getmData()[position]
        //菜品名称
        holder?.getView<TextView>(R.id.tvName)?.text =
            String.format(dishItem.title + dishItem.name)
        //菜品单价
        holder?.getView<TextView>(R.id.tvPrice)?.text = dishItem.price.toString()
        //悬停的标题头-RAdapter传入的resLayout是item_goods.xml
        val headerLayout = holder?.getView<FrameLayout>(R.id.stick_header)
        val tvHeader = holder?.getView<TextView>(R.id.tv_header)
        if (position == 0) {  // 防止position=-1越界
            headerLayout?.visibility = View.VISIBLE
            tvHeader?.text = dishItem.title
        } else {
            // 上下滑动时，当上下两项属于同一个分类时，同一个分类的不同名称item中间，不展示悬停的标题头(分类x)
            if (TextUtils.equals(dishItem.title, getmData()[position - 1].title)) {
                headerLayout?.visibility = View.GONE
            } else {
                headerLayout?.visibility = View.VISIBLE
                tvHeader?.text = dishItem.title
            }
        }
        //菜品数量
        holder?.getView<TextView>(R.id.right_dish_account)?.text = dishItem.amountOrder.toString()
        //添加菜品监听
        holder?.getView<ImageView>(R.id.right_dish_add)?.setOnClickListener {
            //调用点击后的接口方法，弹出购物车popWindow
            this.shopCartImp.add(it, position)
            //局部item刷新
            notifyItemChanged(position)
        }
        //删除菜品监听
        holder?.getView<ImageView>(R.id.right_dish_remove)?.setOnClickListener {
            //调用点击后的接口方法，弹出购物车popWindow
            this.shopCartImp.remove(it, position)
            notifyItemChanged(position)
        }
    }
}