package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.findViewById<ImageButton>(R.id.btn_return).setOnClickListener {
            finish()
        }
    }
}