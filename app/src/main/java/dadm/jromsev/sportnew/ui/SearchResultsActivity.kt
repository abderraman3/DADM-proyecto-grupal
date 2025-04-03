package dadm.jromsev.sportnew.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.databinding.SearchResultsBinding

class SearchResultsActivity : AppCompatActivity() {
    private lateinit var binding: SearchResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}