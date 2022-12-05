package imp

import android.view.View

interface ShopCart {
    fun add(view: View, position: Int)
    fun remove(view: View, position: Int)
}