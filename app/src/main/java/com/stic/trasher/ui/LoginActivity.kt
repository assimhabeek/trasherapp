package com.stic.trasher.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.stic.trasher.R
import com.stic.trasher.ui.SignUpActivity.Companion.SIGN_UP_SUCCESS
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator


class LoginActivity : Activity() {


    companion object {
        private const val RC_SIGN_IN = 1
        private const val TAG: String = "LoginActivity"
    }

    private lateinit var googleSignInButton: SignInButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var signInButton: MaterialButton
    private lateinit var signUpButton: MaterialButton
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        bindViewElements()

        styleViewElements()

        bindViewEvents()


        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }


    private fun bindViewElements() {

        googleSignInButton = bind(R.id.google_sign_in_button)

        signInButton = bind(R.id.sign_in_button)

        username = bind(R.id.username)

        password = bind(R.id.password)

        signUpButton = bind(R.id.sign_up_button)
    }


    private fun styleViewElements() {

        googleSignInButton.setSize(SignInButton.SIZE_STANDARD)

    }


    private fun bindViewEvents() {

        signInButton.setOnClickListener {
            if (isInputsValid()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        googleSignInButton.setOnClickListener {

            googleSignIn()

        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, SIGN_UP_SUCCESS)
        }
    }

    private fun isInputsValid(): Boolean {
        return username.validator()
            .nonEmpty()
            .minLength(5)
            .addErrorCallback {
                username.setError(it, null)
            }.check()
                &&
                password.validator().nonEmpty().atleastOneNumber()
                    .atleastOneSpecialCharacters()
                    .atleastOneUpperCase()
                    .minLength(6)
                    .addErrorCallback {
                        password.setError(it, null)
                    }.check()
    }

    private fun <T : View> Activity.bind(@IdRes res: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(res)
    }


    private fun googleSignIn() {

        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {

            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)


        } catch (e: ApiException) {

            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)

        }

    }

    private fun updateUI(account: GoogleSignInAccount?) {

        if (account != null) {

            showMainActivity(account)

        }

    }

    private fun showMainActivity(account: GoogleSignInAccount) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", account)
        startActivity(intent)
        finish()
    }



}
