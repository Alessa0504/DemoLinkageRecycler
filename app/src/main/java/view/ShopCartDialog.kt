package view

import adapter.PopupDishAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import bean.Item
import com.example.demolinkagerecycler.R
import com.example.demolinkagerecycler.databinding.CartPopupviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class ShopCartDialog(context: Context, data: List<Item>) : BottomSheetDialog(context) {

    private var viewBinding: CartPopupviewBinding
    private var listCartItem = ArrayList<Item>()
    private var data: List<Item>
    private var priceTotal = 0

    init {
        this.data = data
        viewBinding = CartPopupviewBinding.inflate(layoutInflater, null, false)
        setContentView(viewBinding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.rvCart.layoutManager = LinearLayoutManager(context)
    }

    fun addToCart(position: Int) {
        data[position].amountOrder++
        priceTotal += data[position].price
        //同一个菜品去重
        if (!listCartItem.contains(data[position])) {
            listCartItem.add(data[position])
        }
        viewBinding.rvCart.adapter =
            PopupDishAdapter(context, R.layout.item_pop_dish, listCartItem)
//        this.show()
    }

    fun removeFromCart(view: View, position: Int) {
        if (data[position].amountOrder <= 0) return
        data[position].amountOrder--
        priceTotal -= data[position].price
        if (data[position].amountOrder <= 0) {
            listCartItem.remove(data[position])
        }
//        viewBinding.tvCartTotalPrice.text = priceTotal.toString()
//        viewBinding.tvCartTotalAmount.text = getTotalDishItemNum().toString()
        viewBinding.rvCart.adapter =
            PopupDishAdapter(context, R.layout.item_pop_dish, listCartItem)
        this.show()
    }

    /**
     * 购物车中菜品总数
     */
    fun getTotalDishItemNum(): Int {
        var sumAmount = 0
        for (item in listCartItem) {
            sumAmount += item.amountOrder
        }
        return sumAmount
    }

    fun getPriceTotal(): Int {
        return priceTotal
    }
}