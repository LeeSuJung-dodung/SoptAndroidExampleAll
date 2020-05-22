package com.example.hw2_sopt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2_sopt.data.RequestLogin
import com.example.hw2_sopt.data.RequestRegister
import com.example.hw2_sopt.data.ResponseLogin
import com.example.hw2_sopt.data.ResponseRegister
import com.example.hw2_sopt.network.RequestToServer
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity: AppCompatActivity() {
    val requestToServer: RequestToServer = RequestToServer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        login_ok.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            if (r_et_pw.text.isNullOrBlank() || r_et_id.text.isNullOrBlank() || r_et_email.text.isNullOrBlank()
                || r_et_name.text.isNullOrBlank() || r_et_pw_correct.text.isNullOrBlank() || r_et_phone.text.isNullOrBlank()) {
                showToast("빈칸을 확인하세요!")
            }
            else if(r_et_pw.getText().toString() != r_et_pw_correct.getText().toString()) {
                showToast("비밀번호와 비밀번호 확인의 값이 다릅니다")
            }
            else {
                requestToServer.service.requestRegister(
                    RequestRegister(
                        id = r_et_id.text.toString(),
                        password = r_et_pw.text.toString(),
                        name = r_et_name.text.toString(),
                        email = r_et_email.text.toString(),
                        phone = r_et_phone.text.toString()
                    )//회원가입 정보를 전달
                ).enqueue(object : Callback<ResponseRegister>{ //콜백 등록, Retrofit의 콜백을 import 해줘야함!
                    override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                        //통신 실패
                    }

                    override fun onResponse(
                        call: Call<ResponseRegister>,
                        response: Response<ResponseRegister>
                    ) {
                        //통신 성공
                        if (response.isSuccessful) //status code가 200~300 사이일 때 응답 body 이용 가능
                        {
                            if (response.body()!!.success) //ResponseRegister의 success가 true인 경우 -> 로그인
                            {
                                Toast.makeText(this@RegisterActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                intent.putExtra("id", r_et_id.text.toString())
                                intent.putExtra("pw", r_et_pw.text.toString())
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                            else
                            {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "정보를 확인하세요",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })

            }
        }
    }
}


