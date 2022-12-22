package com.example.demolinkagerecycler

import adapter.LAdapter
import adapter.RAdapter
import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.PointF
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import base.BaseRecyclerViewAdapter
import bean.Item
import bean.ItemL
import bean.LinkBean
import com.example.demolinkagerecycler.databinding.ActivityMainBinding
import evaluator.PointFTypeEvaluator
import imp.ShopCart
import view.MoveImageView
import view.ShopCartDialog


class MainActivity : AppCompatActivity(), ShopCart {
    private var linkBean: LinkBean? = null
    private var lAdapter: LAdapter? = null  //左边分类list adapter
    private var rAdapter: RAdapter? = null  //右边餐品名称list adapter
    private lateinit var shopCartDialog: ShopCartDialog
    private var index = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        val listItemLS = ArrayList<ItemL>()
        val listItemS = ArrayList<Item>()
        for (i in 0 until 15) {
            val itemL = ItemL("分类$i")
            listItemLS.add(itemL)
            for (j in 0 until 10) {
                val item = Item("分类$i", "名称$j", (2 + i + j) * 3)
                listItemS.add(item)
            }
        }
        linkBean = LinkBean(listItemLS, listItemS)
    }

    private fun initView() {
        binding.layoutRightHeader.tvHeader.text = linkBean!!.itemLS[0].title
        binding.rvLeft.layoutManager = LinearLayoutManager(this)
        binding.rvRight.layoutManager = LinearLayoutManager(this)
        lAdapter = LAdapter(this, R.layout.item, linkBean!!.itemLS)
        lAdapter?.bindToRecyclerView(binding.rvLeft)
        binding.rvLeft.adapter = lAdapter
        shopCartDialog = ShopCartDialog(this, linkBean!!.itemS)
        rAdapter = RAdapter(this, R.layout.item_goods, linkBean!!.itemS, this@MainActivity)
        binding.rvRight.adapter = rAdapter
        //关闭动画效果防止局部刷新notifyItemChanged时item闪烁
        val sa = binding.rvRight.itemAnimator as SimpleItemAnimator
        sa.supportsChangeAnimations = false
    }

    private fun initListener() {
        lAdapter?.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                if (binding.rvRight.scrollState != RecyclerView.SCROLL_STATE_IDLE) return
                lAdapter?.setChecked(position)
                val title: String = lAdapter!!.getmData()[position].title
                for (i in 0 until rAdapter?.getmData()!!.size) {
                    //根据左边选中的条目获取到右面此条目Title相同的位置索引
                    if (TextUtils.equals(title, rAdapter?.getmData()!![i].title)) {
                        index = i
                        moveToPosition_R(index)
                        return
                    }
                }
            }
        })

        binding.rvRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = binding.rvRight.layoutManager as LinearLayoutManager
                val index = linearLayoutManager.findFirstVisibleItemPosition()
                //上滑时，不同分类的悬停标题头stick_header，上滑的分类标题stick_header只是被遮挡了，并没有GONE掉
                binding.layoutRightHeader.tvHeader.text =
                    rAdapter?.getmData()?.get(index)?.title   //悬停标题头设置为"分类x"标题
                lAdapter?.setToPosition(rAdapter?.getmData()?.get(index)?.title!!)
            }
        })

        binding.shoppingCartLayout.setOnClickListener {
            if (shopCartDialog.isShowing) {
                return@setOnClickListener
            }
            shopCartDialog.show()
        }
    }

    private fun moveToPosition_R(index: Int) {
        val linearLayoutManager = binding.rvRight.layoutManager as LinearLayoutManager
        linearLayoutManager.scrollToPositionWithOffset(index, 0)
    }

    /**
     * 菜单list添加单个菜品
     * @param view
     * @param position
     */
    override fun add(view: View, position: Int) {
        addAnimation(view)
        shopCartDialog.addToCart(position)
        showBottomView()
    }

    /**
     * 菜单list删除单个菜品
     * @param view
     * @param position
     */
    override fun remove(view: View, position: Int) {
        shopCartDialog.removeFromCart(position)
        showBottomView()
    }

    /**
     * 展示底部控件
     */
    private fun showBottomView() {
        if (shopCartDialog.getPriceTotal() > 0) {
            binding.tvCartTotalPrice.visibility = View.VISIBLE
            binding.tvCartTotalPrice.text = shopCartDialog.getPriceTotal().toString()
        } else {
            binding.tvCartTotalPrice.visibility = View.GONE
        }
        if (shopCartDialog.getTotalDishItemNum() > 0) {
            binding.tvCartTotalAmount.visibility = View.VISIBLE
            binding.tvCartTotalAmount.text = shopCartDialog.getTotalDishItemNum().toString()
        } else {
            binding.tvCartTotalAmount.visibility = View.GONE
        }
    }

    /**
     * 点击添加菜品二次曲线动效
     */
    private fun addAnimation(view: View) {
        val childLocation = IntArray(2)
        val parentLocation = IntArray(2)
        val cartLocation = IntArray(2)
        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy
        view.getLocationInWindow(childLocation)
        binding.rvRight.getLocationInWindow(parentLocation)
        binding.shoppingCart.getLocationInWindow(cartLocation)

        //2.利用二次贝塞尔曲线，需首先计算出MoveImageView的2个数据点和一个控制点
        val startP = PointF()
        val endP = PointF()
        val controlP = PointF()
        //开始的数据点坐标就是 addView的坐标？
        startP.x = (childLocation[0]).toFloat()
        startP.y = (childLocation[1] - parentLocation[1]).toFloat()
        //结束的数据点坐标就是 购物车的坐标
        endP.x = (cartLocation[0]).toFloat()
        endP.y = (cartLocation[1] - parentLocation[1]).toFloat()
        //控制点坐标 x等于 购物车x；y等于 addView的y
        controlP.x = endP.x
        controlP.y = startP.y

        //3.自定义曲线移动的imageView
        val moveImageView = MoveImageView(this)
        moveImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp)
        //设置img在父布局中的坐标位置
        moveImageView.setMPointF(startP)
        //父布局加入该imageView -不能用binding.rvRight作为root，否则会报错RecyclerView$ViewHolder.shouldIgnore()
        binding.root.addView(moveImageView)
        //启动属性动画
        val objectAnimator = ObjectAnimator.ofObject(
            moveImageView,
            "mPointF",
            PointFTypeEvaluator(controlP),
            startP,
            endP
        )
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                moveImageView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                //动画结束后，父布局移除moveImageView
                moveImageView.visibility = View.GONE
                binding.root.removeView(moveImageView)
                //shoppingCart 开始一个放大动画
                val scaleAnim: Animation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.cart_scale)
                binding.shoppingCart.startAnimation(scaleAnim)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        //也可以在代码中创建Animator
//        val scaleAnimatorX: ObjectAnimator =
//            ObjectAnimator.ofFloat(binding.shoppingCart, "scaleX", 0.6f, 1.0f)
//        val scaleAnimatorY: ObjectAnimator =
//            ObjectAnimator.ofFloat(binding.shoppingCart, "scaleY", 0.6f, 1.0f)
//        scaleAnimatorX.interpolator = AccelerateInterpolator()
//        scaleAnimatorY.interpolator = AccelerateInterpolator()
//        val animatorSet = AnimatorSet()
//        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(objectAnimator)
//        animatorSet.start()
        objectAnimator.duration = 800
        objectAnimator.start()
    }
}