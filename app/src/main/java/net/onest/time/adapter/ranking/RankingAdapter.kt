package net.onest.time.adapter.ranking

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.onest.time.R
import net.onest.time.api.vo.UserVo
import net.onest.time.databinding.ItemRankingBinding
import net.onest.time.utils.withOnClickInfoDialog
import java.util.Comparator

class RankingAdapter(
    var context: Context,
    var rankingList: MutableList<UserVo>,
) : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {
    private lateinit var binding: ItemRankingBinding

    init {
        rankingList.sortWith(Comparator.comparing(UserVo::getTomatoDuration).reversed())
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ranking = binding.ranking
        val username = binding.username
        val signature = binding.signature
        val tomatoDuration = binding.tomatoDuration
        val avatar = binding.avatar
        val card = binding.card
        val rankingLayout = binding.rankingLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemRankingBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = rankingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userVo = rankingList[position]
        val imageId: Int? = when (position) {
            0 -> R.drawable.ranking_first
            1 -> R.drawable.ranking_second
            2 -> R.drawable.ranking_third
            else -> null
        }
        imageId?.let {
            holder.rankingLayout.background = ContextCompat.getDrawable(context, imageId)
        }

        holder.ranking.text = (position + 1).toString()
        holder.username.text = userVo.userName
        holder.signature.text = userVo.signature
        holder.tomatoDuration.text = "${userVo.tomatoDuration}分钟"
        Glide.with(context)
            .load(userVo.avatar)
            .into(holder.avatar)

        holder.itemView.withOnClickInfoDialog(message = userVo.toString())
    }

}