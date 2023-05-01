package com.example.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.viewmodel.RegisterViewModel
import com.example.storyapp.viewmodel.ViewModelFactory
import java.lang.ref.WeakReference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//var weakReference: WeakReference<ActivityRegisterBinding>? = null

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Glide.with(this)
            .load("https://th.bing.com/th/id/OIP.1MZ3IQ50dBysLAdeCnt5wwHaHa?pid=ImgDet&rs=1")
            .into(binding.ivLogo)

        val pref = UserSession.getInstance(dataStore)
        registerViewModel = ViewModelProvider(
            this, ViewModelFactory(pref))[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        registerViewModel.isError.observe(this) {
            isError = it
        }
        registerViewModel.msg.observe(this){
            AlertDialog.Builder(this@RegisterActivity).apply {
                setTitle(if (isError) "Error" else "Welcome!")
                setMessage(it)
                setPositiveButton("OK"){_,_ ->
                    if (!isError) finish()
                }
                create()
                show()
            }
        }

        binding.buttonRegis.setOnClickListener(this)
        binding.tvAkun.setOnClickListener(this)

        playAnimation()

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbRegis.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.button_regis -> {
                val name = binding.etNama.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                when {
                    name.isEmpty() -> {
                        binding.layoutNama.error = "Isi nama"
                    }
                    email.isEmpty() -> {
                        binding.layoutEmail.error = "Isi email"
                    }
                    password.isEmpty() -> {
                        binding.layoutPassword.error = "Isi pass"
                    }
                    else -> {
                        registerViewModel.register(name, email, password)
                        registerViewModel.isError.observe(this) {
                            if (!it) {
                                AlertDialog.Builder(this@RegisterActivity).apply {
                                    setTitle("Welcome")
                                    setMessage("Berhasil daftar")
                                    setPositiveButton("OK") {_,_ ->
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }
                }
            }
            R.id.tv_akun -> {
                startActivity(Intent(this,LoginActivity::class.java))
            }
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, ALPHA).setDuration(DURATION)
        val tv_halo = ObjectAnimator.ofFloat(binding.tvHalo, View.ALPHA, ALPHA).setDuration(DURATION)
        val tv_login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, ALPHA).setDuration(DURATION)
    //    val tv_nama = ObjectAnimator.ofFloat(binding.tvNama, View.ALPHA, ALPHA).setDuration(DURATION)
        val layout_nama = ObjectAnimator.ofFloat(binding.layoutNama, View.ALPHA, ALPHA).setDuration(DURATION)
      //  val tv_email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, ALPHA).setDuration(DURATION)
        val layout_email = ObjectAnimator.ofFloat(binding.layoutEmail, View.ALPHA, ALPHA).setDuration(DURATION)
     //   val tv_password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, ALPHA).setDuration(DURATION)
        val layout_password = ObjectAnimator.ofFloat(binding.layoutPassword, View.ALPHA, ALPHA).setDuration(DURATION)
        val tv_regis = ObjectAnimator.ofFloat(binding.tvAkun, View.ALPHA, ALPHA).setDuration(DURATION)
        val button_regis = ObjectAnimator.ofFloat(binding.buttonRegis, View.ALPHA, ALPHA).setDuration(DURATION)
        AnimatorSet().apply {
        //    startDelay = 500
            playSequentially(
                logo,
                tv_halo,
                tv_login,
         //       tv_nama,
                layout_nama,
           //     tv_email,
                layout_email,
          //      tv_password,
                layout_password,
                tv_regis,
                button_regis
            )
            start()
        }
    }

    companion object {
        var isError = false
        private const val DURATION = 200L
        private const val ALPHA = 1f

        fun isPasswordError(isError: Boolean){
         //   val binding = weakReference?.get()
        }
    }
}