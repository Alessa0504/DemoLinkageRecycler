package view

import android.content.Context
import android.graphics.PointF
import androidx.appcompat.widget.AppCompatImageView


/**
 * @Description:自定义添加购物车时曲线弹出的ImageView
 * @author zouji
 * @date 2022/12/6
 */
class MoveImageView(context: Context) : AppCompatImageView(context) {

    fun setMPointF(pointF: PointF) {
        x = pointF.x
        y = pointF.y
    }
}