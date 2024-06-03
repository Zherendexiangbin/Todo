package net.onest.time.navigation.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import net.onest.time.adapter.ranking.RankingAdapter
import net.onest.time.api.StatisticApi
import net.onest.time.api.vo.UserVo
import net.onest.time.databinding.ActivityRankingListBinding

class RankingListActivity : AppCompatActivity() {
    private val TAG = javaClass.name

    private lateinit var binding: ActivityRankingListBinding
    private var rankingList = ArrayList<UserVo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingListBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        val rankingAdapter = RankingAdapter(this, rankingList)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rankingList.run {
            layoutManager = linearLayoutManager
            adapter = rankingAdapter
        }

        StatisticApi.rankingList({
            rankingList = it as ArrayList<UserVo>
            Log.i(TAG, "onCreate: 数据加载成功")
        }, {
            Log.e(TAG, "onCreate: ${it.code} ${it.message}")
        })
    }
}