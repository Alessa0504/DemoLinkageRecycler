package view

import adapter.PopupDishAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
        // 去除默认白色背景：FrameLayout是我们的自定义view的父控件
        val parent = viewBinding.root.parent as ViewGroup
        parent.setBackgroundResource(android.R.color.transparent)
        // 弹出时不展示蒙层
//        window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        // 添加弹出动画为没有动画
        window!!.setWindowAnimations(R.style.NullAnimationDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.rvCart.layoutManager = LinearLayoutManager(context)
        viewBinding.shoppingCartLayout.setOnClickListener {
            if (this.isShowing) {
                this.dismiss()
            }
        }
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
        viewBinding.tvCartTotalPrice.text = this.getPriceTotal().toString()
        viewBinding.tvCartTotalAmount.text = this.getTotalDishItemNum().toString()
    }

    fun removeFromCart(view: View, position: Int) {
        if (data[position].amountOrder <= 0) return
        data[position].amountOrder--
        priceTotal -= data[position].price
        if (data[position].amountOrder <= 0) {
            listCartItem.remove(data[position])
        }
        viewBinding.rvCart.adapter =
            PopupDishAdapter(context, R.layout.item_pop_dish, listCartItem)
        viewBinding.tvCartTotalPrice.text = this.getPriceTotal().toString()
        viewBinding.tvCartTotalAmount.text = this.getTotalDishItemNum().toString()
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

    override fun show() {
        super.show()
        animationShow(1000)
    }

    private fun animationShow(mDuration: Int) {
        val animatorSet = AnimatorSet()
        animatorSet.play(
            ObjectAnimator.ofFloat(viewBinding.llAbove, "translationY", 1000f, 0f)
                .setDuration(mDuration.toLong())
        )
        animatorSet.start()
    }
}