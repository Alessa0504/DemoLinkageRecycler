package com.example.demolinkagerecycler

import adapter.LAdapter
import adapter.RAdapter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.BaseRecyclerViewAdapter
import bean.Item
import bean.ItemL
import bean.LinkBean

class MainActivity : AppCompatActivity() {
    private var linkBean: LinkBean? = null
    private var lAdapter: LAdapter? = null  //左边分类list adapter
    private var rAdapter: RAdapter? = null  //右边餐品名称list adapter
    private var rvL: RecyclerView? = null
    private var rvR: RecyclerView? = null
    private var tvHead: TextView? = null
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                val item = Item("分类$i", "名称$j", "￥:" + (2 + i + j) * 3)
                listItemS.add(item)
            }
        }
        linkBean = LinkBean(listItemLS, listItemS)
    }

    private fun initView() {
        tvHead = findViewById(R.id.tv_header)
        tvHead?.text = linkBean!!.itemLS[0].title
        rvL = findViewById(R.id.rv1)
        rvR = findViewById(R.id.rv2)
        rvL?.layoutManager = LinearLayoutManager(this)
        rvR?.layoutManager = LinearLayoutManager(this)
        lAdapter = LAdapter(this, R.layout.item, linkBean!!.itemLS)
        lAdapter?.bindToRecyclerView(rvL)
        rvL?.adapter = lAdapter
        rAdapter = RAdapter(this, R.layout.item_goods, linkBean!!.itemS)
        rvR?.adapter = rAdapter
    }

    private fun initListener() {
        lAdapter?.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                if (rvR?.scrollState != RecyclerView.SCROLL_STATE_IDLE) return
                lAdapter?.setChecked(position)
                val title: String = lAdapter!!.getmData()[position].title
                for (i in 0 until rAdapter?.getmData()!!.size) {
                    //根据左边选中的条目获取到右面此条目Title相同的位置索引；
                    if (TextUtils.equals(title, rAdapter?.getmData()!![i].title)) {
                        index = i
                        moveToPosition_R(index)
                        return
                    }
                }
            }
        })

        rvR?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = rvR?.layoutManager as LinearLayoutManager
                val index = linearLayoutManager.findFirstVisibleItemPosition()
                //上滑时，不同分类的悬停标题头stick_header，上滑的分类标题stick_header只是被遮挡了，并没有GONE掉
                tvHead?.text = rAdapter?.getmData()?.get(index)?.title
                lAdapter?.setToPosition(rAdapter?.getmData()?.get(index)?.title!!)
            }
        })
    }

    private fun moveToPosition_R(index: Int) {
        val linearLayoutManager = rvR?.layoutManager as LinearLayoutManager
//        val f: Int = linearLayoutManager.findFirstVisibleItemPosition()
//        val l: Int = linearLayoutManager.findLastVisibleItemPosition()
        linearLayoutManager.scrollToPositionWithOffset(index, 0)
//        linearLayoutManager.scrollToPosition(index)

    }
}