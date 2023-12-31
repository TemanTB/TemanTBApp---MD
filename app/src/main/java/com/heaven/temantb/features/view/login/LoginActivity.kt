package com.heaven.temantb.features.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.heaven.temantb.R
import com.heaven.temantb.databinding.ActivityLoginBinding
import com.heaven.temantb.features.data.di.AlertIndicator
import com.heaven.temantb.features.view.ViewModelFactory
import com.heaven.temantb.features.view.main.MainActivity
import com.heaven.temantb.features.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginUser(email, password)
        }

        binding.haveAccountButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        loginViewModel.login(email, password).observe(this) { result ->
            if (result!= null){
                when (result) {
                    AlertIndicator.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is AlertIndicator.Success -> {
                        binding.progressBar.isVisible = false
                        showSuccessDialog(getString(R.string.login_successful_message))
                    }

                    is AlertIndicator.Error -> {
                        binding.progressBar.isVisible = false
                        showErrorDialog(getString(R.string.login_failed_message))
                    }
                }
            }
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.yay))
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog(message: String){
        AlertDialog.Builder(this).apply {
            setTitle("Oops!")
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.emailEditText.requestFocus()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val alreadyRegister = ObjectAnimator.ofFloat(binding.haveAccountButton, View.ALPHA, 1f).setDuration(100)
        val msgLogin = ObjectAnimator.ofFloat(binding.tvSubMessageLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                alreadyRegister,
                msgLogin
            )
            startDelay = 100
        }.start()
    }

}
