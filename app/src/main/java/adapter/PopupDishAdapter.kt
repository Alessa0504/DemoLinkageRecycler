package adapter

import android.content.Context
import android.widget.TextView
import base.BaseRecyclerHolder
import base.BaseRecyclerViewAdapter
import bean.Item
import com.example.demolinkagerecycler.R

class PopupDishAdapter(context: Context, resLayout: Int, dataCart: List<Item>) :
    BaseRecyclerViewAdapter<Item>(context, resLayout, dataCart) {
    override fun convert(holder: BaseRecyclerHolder?, position: Int) {
        holder?.getView<TextView>(R.id.right_dish_name)?.text =
            String.format(getmData()[position].title + getmData()[position].name)
        holder?.getView<TextView>(R.id.right_dish_price)?.text =
            getmData()[position].price.toString()
        holder?.getView<TextView>(R.id.right_dish_account)?.text =
            getmData()[position].amountOrder.toString()
    }
}