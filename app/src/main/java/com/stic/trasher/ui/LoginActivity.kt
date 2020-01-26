package com.stic.trasher.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.stic.trasher.R
import com.stic.trasher.ui.SignUpActivity.Companion.SIGN_UP_SUCCESS
import com.stic.trasher.utils.*
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import retrofit2.Call
import retrofit2.Response


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

        PermissionManager.redirectIfPermissionsMessing(this)
        SessionManager.redirectIfLoggedIn(this)


        bindViewElements()

        styleViewElements()

        bindViewEvents()


/*
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
*/
    }


    private fun bindViewElements() {

        googleSignInButton = findViewById(R.id.google_sign_in_button)

        signInButton = findViewById(R.id.sign_in_button)

        username = findViewById(R.id.username)

        password = findViewById(R.id.password)

        signUpButton = findViewById(R.id.sign_up_button)
    }


    private fun styleViewElements() {

        googleSignInButton.setSize(SignInButton.SIZE_STANDARD)

    }


    private fun bindViewEvents() {

        signInButton.setOnClickListener {


            //            if (isInputsValid()) {
            HttpClient.authService.login(
                JWtRequest(
                    username.text.toString(),
                    password.text.toString()
                )
            ).enqueue(object : retrofit2.Callback<JwtResponse> {

                override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                    t.printStackTrace()
                    showToast(t.message)
                }

                override fun onResponse(
                    call: Call<JwtResponse>,
                    response: Response<JwtResponse>
                ) {
                    if (response.code() == 200 && response.body()!=null) {
                        val body = response.body()!!
                        println(body.token)
                        storeToken(body.token)
                        showMainActivity()
                    } else {
                        showToast("Wrong username or password")
                    }
                }

            })

        }
        //      }

/*
        googleSignInButton.setOnClickListener {

            googleSignIn()

        }
*/

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, SIGN_UP_SUCCESS)
        }
    }


    private fun isInputsValid(): Boolean {
        return username.validator()
            .nonEmpty()
            .minLength(3)
            .addErrorCallback {
                username.setError(it, null)
            }.check()
                &&
                password.validator()
                    .nonEmpty()
                    .minLength(3)
                    .addErrorCallback {
                        password.setError(it, null)
                    }.check()
    }


    private fun storeToken(token: String) {
        SessionManager.registerUserToken(this, token)
    }

/*
    private fun googleSignIn() {

        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }
*/

/*

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

*/
/*
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {

            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)


        } catch (e: ApiException) {

            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)

        }

    }
*/

/*
    private fun updateUI(account: GoogleSignInAccount?) {

        if (account != null) {

            showMainActivity(account)

        }

    }
*/

    private fun showMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun showToast(message: String?) {
        Toast.makeText(applicationContext, message.orEmpty(), Toast.LENGTH_LONG).show()
    }

}
