package base

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mViews: SparseArrayCompat<View>? = null

    init {
        mViews = SparseArrayCompat()
    }

    fun <V : View?> getView(@IdRes ids: Int): V {
        var v = mViews!![ids]
        if (v == null) {
            v = itemView.findViewById(ids)
            mViews!!.put(ids, v)
        }
        return v as V
    }
}