package net.onest.time.navigation.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import net.onest.time.databinding.ActivityRankingListBinding

class RankingListActivity : AppCompatActivity() {
    lateinit var binding: ActivityRankingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingListBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

    }
}