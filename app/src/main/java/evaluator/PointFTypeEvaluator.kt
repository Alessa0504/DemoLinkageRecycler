package evaluator

import android.animation.TypeEvaluator
import android.graphics.PointF

/**
 * @Description: 自定义估值器
 * @author zouji
 * @date 2022/12/7
 */
class PointFTypeEvaluator(pointF: PointF) : TypeEvaluator<PointF> {

    //每一个估值器相应一个属性动画。每一个属性动画仅相应唯一一个控制点
    private var control: PointF

    init {
        control = pointF  //接收控制点
    }

    //返回值是TypeEvaluator<T>中定义的T
    override fun evaluate(fraction: Float, startValue: PointF?, endValue: PointF?): PointF {
        return getBezierPoint(startValue!!, endValue!!, control, fraction)
    }

    /**
     * 二次贝塞尔曲线公式
     *
     * @param start   开始的数据点
     * @param end     结束的数据点
     * @param control 控制点
     * @param t       float 0-1
     * @return 不同t对应的PointF
     */
    private fun getBezierPoint(start: PointF, end: PointF, control: PointF, t: Float): PointF {
        val mPointF = PointF()
        mPointF.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x
        mPointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y
        return mPointF
    }
}