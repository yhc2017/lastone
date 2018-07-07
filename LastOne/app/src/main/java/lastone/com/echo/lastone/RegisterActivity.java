package lastone.com.echo.lastone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import lastone.com.echo.utils.User;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by HUAHUA on 2017/8/24.
 * 手机号注册页面
 */

public class RegisterActivity extends AppCompatActivity{
    private ImageView iv_register_back;

//    Intent intent;

    private EditText ed_tel, ed_pwd, ed_sure_pwd, ed_name;
    private Button btn_regidter;
    private String address = "http://193.112.12.207/EMUSIC/userRegister.php";  // 请求验证的地址


    //执行事件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initViews();
        setEvents();
    }




    //所有按钮实例成对象
    public void initViews() {
        iv_register_back = (ImageView)findViewById(R.id.register_back);

        ed_tel = (EditText) findViewById(R.id.register_iphone);
        ed_pwd = (EditText) findViewById(R.id.register_password);
        ed_sure_pwd = (EditText) findViewById(R.id.register_repassword);
        ed_name = (EditText) findViewById(R.id.register_uname);
        btn_regidter = (Button) findViewById(R.id.register_next_btn);

    }


    //所有的对按钮的事件进行监听
    public void setEvents() {
        //匿名方法
        MyListener listener = new MyListener();
        iv_register_back.setOnClickListener(listener);

        btn_regidter.setOnClickListener(listener);  // 注册按钮

        // 判断前后密码是否正确
        ed_sure_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pwd = ed_pwd.getText().toString();
                    String surePwd = ed_sure_pwd.getText().toString();
                    if (!pwd.equals(surePwd)) {
                        Toast.makeText(RegisterActivity.this, R.string.surePwdErro, Toast.LENGTH_SHORT).show();
                        ed_pwd.setText("");
                        ed_sure_pwd.setText("");
                    }

                }

            }
        });

    }

    //选择触发的事件
    public class MyListener implements  View.OnClickListener { /*用接口的方式*/
        public void onClick(View v) {
            Intent intent = null;
            int id = v.getId();   /*得到v的id付给id*/
            switch (id) {
                case R.id.register_back:{
                    intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.d("注册页面", "跳转首页 ");
                    finish();
                    break;
                }
                case R.id.register_next_btn: {

                    String tel = ed_tel.getText().toString();
                    String pwd = ed_pwd.getText().toString();
                    String surePwd = ed_sure_pwd.getText().toString();
                    String name = ed_name.getText().toString();

                    if (tel.equals("") || pwd.equals("") || surePwd.equals("") || name.equals("")) {
                        Toast.makeText(RegisterActivity.this, R.string.RegInfoNotFull, Toast.LENGTH_LONG).show();
                    } else {
                        // 发起网络请求进行注册
                        sendRequestWithOkHttp(address, new okhttp3.Callback(){
                            /**
                             * Called when the request could not be executed due to cancellation, a connectivity problem or
                             * timeout. Because networks can fail during an exchange, it is possible that the remote server
                             * accepted the request before the failure.
                             *
                             * @param call
                             * @param e
                             */
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("MainActivity","出现异常");
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "获取信息失败,请检测网络连接！", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseData = response.body().string(); //得到返回具体内容

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            parseJSONWithGson(responseData); // 解析数据
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                            }
                        });
                    }

                    break;
                }
                default:
                    break;
            }
        }
    }

    private void sendRequestWithOkHttp(String address, okhttp3.Callback callback) {
        String pwd = ed_pwd.getText().toString();
        String tel = ed_tel.getText().toString();
        String name = ed_name.getText().toString();

        //*******
        OkHttpClient client = new OkHttpClient(); //创建 OkHttpClient实例
        RequestBody requestBody = new FormBody.Builder()
                .add("tel", tel)
                .add("pwd", pwd)
                .add("name", name)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
        //*******
    }

    private void parseJSONWithGson(String jsonData) throws JSONException {

//********
        String responName = "";
        String responPwd = "";
        String responImgUrl = "";
        String responTel = "";
        Gson gson = new Gson();
        List<User> appList = gson.fromJson(jsonData, new TypeToken<List<User>>()
        {}.getType());

        for (User userBean : appList) {
            responName = userBean.getNickname();
            responPwd = userBean.getPwd();
//            responImgUrl = userBean.getImg();
//            responTel = userBean.getTel();
        }
        if (responName.equals("0") && responPwd.equals("0"))
            Toast.makeText(RegisterActivity.this, R.string.register_faile, Toast.LENGTH_SHORT).show();
        else {
            String regResTel = ed_tel.getText().toString();
            String regResPwd = ed_pwd.getText().toString();

//            Toast.makeText(RegisterActivity.this, R.string.register_success + regResPwd + regResTel, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getSharedPreferences("registerData", MODE_PRIVATE).edit();
            editor.putBoolean("registerState", true);
            editor.putString("registerTel", regResTel);
            editor.putString("registerPwd", regResPwd);
            editor.apply();

//            app.setRegisterPwd(regResPwd);
//            app.setRegisterTel(regResTel);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//            intent.putExtra("regResTel", regResTel);
//            intent.putExtra("regResPwd", regResPwd);
//            intent.putExtra("name", responName);
//            intent.putExtra("imgUrl", responImgUrl);
//            intent.putExtra("pwd", responPwd);
//            intent.putExtra("tel", responTel);
            startActivity(intent);
            finish();
        }



//********
    }

}
