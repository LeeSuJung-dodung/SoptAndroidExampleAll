package com.example.hw2_sopt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2_sopt.data.RequestLogin
import com.example.hw2_sopt.data.ResponseLogin
import com.example.hw2_sopt.network.RequestToServer
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val requestToServer:RequestToServer=RequestToServer //싱글톤 그대로 가져옴
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_id.textChangedListner {s->
            if(s.isNullOrBlank()){ //it.isNullOrBlank도 가능 , 이름 명시해도 가능
                showToast("아이디가 빈칸이네요")
            }
        }
        /*
        et_id.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrBlank()){
                    showToast("아이디가 빈칸이네요")
                }
            }

        })
        */
        btn_login.setOnClickListener {
            if(et_id.text.isNullOrBlank()||et_pw.text.isNullOrBlank()) {
                /*Toast.makeText(this, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()*/
                showToast("아이디/비밀번호를 확인하세요!")
            }
            else
            {
                //로그인 요청
                requestToServer.service.requestLogin(
                    RequestLogin(
                        id=et_id.text.toString(),
                        password = et_pw.text.toString()
                    )//로긘 정보를 전달
                ).enqueue(object :Callback<ResponseLogin>{ //콜백 등록, Retrofit의 콜백을 import 해줘야함!
                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                       //통신 실패
                    }

                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: Response<ResponseLogin>
                    ) {
                        //통신 성공
                        if(response.isSuccessful) //status code가 200~300 사이일 때 응답 body 이용 가능
                        {
                            if(response.body()!!.success) //ResponseLogin의 success가 true인 경우 -> 로그인
                            {
                                Toast.makeText(this@LoginActivity,"로그인 성공",Toast.LENGTH_SHORT).show()
                                val intent= Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@LoginActivity,"아이디/비밀번호를 확인하세요",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                })

            }
        }

        tv_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivityForResult(intent,1000)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1000 -> {
                    val text1= data!!.getStringExtra("id")
                    val text2 = data!!.getStringExtra("pw")
                    et_id.setText(text1)
                    et_pw.setText(text2)
                }
            }
        }
    }
}