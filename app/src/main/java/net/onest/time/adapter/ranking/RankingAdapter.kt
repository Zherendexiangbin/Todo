package net.onest.time.adapter.ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.onest.time.api.vo.UserVo
import net.onest.time.databinding.RankingItemBinding

class RankingAdapter(
    var context: Context,
    var rankingList: MutableList<UserVo>,
) : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {
    private lateinit var binding: RankingItemBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ranking = binding.ranking
        val username = binding.username
        val signature = binding.signature
        val tomatoDuration = binding.tomatoDuration
        val avatar = binding.avatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RankingItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = rankingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userVo = rankingList[position]

        holder.ranking.text = (position + 1).toString()
        holder.username.text = userVo.userName
        holder.signature.text = userVo.signature
        holder.tomatoDuration.text = "${userVo.tomatoDuration}分钟"
        Glide.with(context)
            .load(userVo.avatar)
            .into(holder.avatar)
    }

}