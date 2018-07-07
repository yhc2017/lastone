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
import android.widget.TextView;
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
 * Created by HUAHUA on 2018/3/15.
 */

public class LoginActivity extends AppCompatActivity {
    private TextView iv_register;
    private ImageView login_back;

    private EditText ed_loginID, ed_loginPwd;
    private Button bt_login;

    private String address = "http://193.112.12.207/EMUSIC/userLogin.php";  // 请求验证的地址
    private String loginTel = "";    // 获取到的用户登录电话号码
    private String loginPwd = "";   // 获取到的用户登录密码

    private String responName = "";   // 数据库返回的姓名
    private String responPwd = "";  // 数据库返回密码
    private String responImgUrl = "";  // 数据库返回头像路径
    private int responId = 0;  // 数据库返回用户ID
    private String responTel = "";  // 数据库返回用户电话

//    private RoundImageView IV_img;  // 用户头像
//    private TextView tv_name;   // 用户名

//    private String getRegisterName = "";    // 获得注册成功的用户名
//    private String getRegisterImgUrl = "";  // 获得 注册成功的图片地址
//    private String getRegisterPwd = "";  // 获得 注册成功的密码
//    private String getRegisterTel = "";  // 获得 注册成功的电话

    private String registerTel = "";
    private String registerPwd = "";
    private boolean registerState = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
//        app = (App)getApplication();
//        Intent intent = getIntent();
//        getRegisterName = intent.getStringExtra("name");
//        getRegisterImgUrl = intent.getStringExtra("imgUrl");
//        getRegisterPwd = intent.getStringExtra("pwd");
//        getRegisterTel = intent.getStringExtra("tel");
//
//        Toast.makeText(LoginActivity.this, getRegisterTel, Toast.LENGTH_SHORT).show();
//        Toast.makeText(LoginActivity.this, getRegisterPwd, Toast.LENGTH_SHORT).show();

//        Intent intent = getIntent();
//        registerTel = intent.getStringExtra("registerTel");
//        registerPwd = intent.getStringExtra("registerPwd");
        initViews();
        setEvents();
        initInfo();



    }



    //所有按钮实例成对象
    public void initViews() {
        iv_register = (TextView) findViewById(R.id.iv_register);
        login_back = (ImageView) findViewById(R.id.login_back);
        ed_loginID = (EditText) findViewById(R.id.login_id);
        ed_loginPwd = (EditText) findViewById(R.id.login_pwd);
        bt_login = (Button) findViewById(R.id.logbtn);


    }

    //所有的对按钮的事件进行监听
    public void setEvents() {
        //匿名方法
        MyListener listener = new MyListener();
        iv_register.setOnClickListener(listener);
        login_back.setOnClickListener(listener);
        bt_login.setOnClickListener(listener);
    }

    // 保留，可优化，提高用户体验--20170414
    private void initInfo() {
        SharedPreferences pref = getSharedPreferences("registerData", MODE_PRIVATE);
        registerState = pref.getBoolean("registerState", false);
        registerTel = pref.getString("registerTel", "");
        registerPwd = pref.getString("registerPwd", "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                Intent intent = getIntent();
//                String registerTel = intent.getStringExtra("registerTel");
//                String registerPwd = intent.getStringExtra("registerPwd");
//                if (registerTel.equals("") || registerPwd.equals("")) {
//                    ed_loginID.setText("");
//                    ed_loginPwd.setText("");
//                } else {
//                    ed_loginID.setText(registerTel);
//                    ed_loginPwd.setText(registerPwd);
//                }


//                ed_loginID.setText(app.getRegisterTel());
//                ed_loginPwd.setText(app.getRegisterPwd());
                if (registerState) {
                    ed_loginID.setText(registerTel);
                    ed_loginPwd.setText(registerPwd);
//                    ed_loginID.setText("");
//                    ed_loginPwd.setText("");
                } else {
                    ed_loginID.setText("");
                    ed_loginPwd.setText("");
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
                case R.id.iv_register:{
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    Log.d("登录页面", "跳转注册页面 ");
                    finish();
                    break;
                }
                case R.id.login_back:{
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.d("登录页面", "返回主界面 ");
                    finish();
                    break;
                }

                // 登录按钮事件处理
                case R.id.logbtn:{
                    loginTel = ed_loginID.getText().toString();
                    loginPwd = ed_loginPwd.getText().toString();

                    if(loginTel.equals("") || loginPwd.equals("")) {
//                        Toast.makeText(LoginActivity.this, R.string.infoEmpty, Toast.LENGTH_SHORT).show();
                    } else {
//                    Toast.makeText(LoginActivity.this, loginTel + "," + loginPwd, Toast.LENGTH_SHORT).show();
                        // 发起网络请求进行验证
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
                                        Toast.makeText(LoginActivity.this, "获取登录信息失败", Toast.LENGTH_SHORT).show();

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

        //*******
        OkHttpClient client = new OkHttpClient(); //创建 OkHttpClient实例
        RequestBody requestBody = new FormBody.Builder().add("tel", loginTel).add("pwd", loginPwd).build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
        //*******
    }

    private void parseJSONWithGson(String jsonData) throws JSONException {

//********
        Gson gson = new Gson();
        List<User> appList = gson.fromJson(jsonData, new TypeToken<List<User>>()
        {}.getType());

        for (User userBean : appList) {
            responName = userBean.getNickname();
            responPwd = userBean.getPwd();
            responImgUrl = userBean.getIcon();
            responId = userBean.getId();
            responTel = userBean.getTel();
        }

//        Toast.makeText(LoginActivity.this, "responid:***"+responId, Toast.LENGTH_SHORT).show();

        if (responName.equals("0") && responPwd.equals("0")) {
            Toast.makeText(LoginActivity.this, R.string.login_onUser, Toast.LENGTH_SHORT).show();
            ed_loginID.setText("");
        }
        else if (responName.equals("1") && responPwd.equals("0")) {
            Toast.makeText(LoginActivity.this, R.string.login_pwdError, Toast.LENGTH_SHORT).show();
            ed_loginPwd.setText("");
        } else {
            Toast.makeText(LoginActivity.this, R.string.login_succeed, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            editor.putBoolean("loginState", true);
//            editor.putString("name", responName);
//            editor.putString("imgUrl", responImgUrl);
            editor.putInt("usrId", responId);
            editor.apply();

            SharedPreferences.Editor editor2 = getSharedPreferences("registerData", MODE_PRIVATE).edit();
            editor2.putBoolean("registerState", false);
            editor2.apply();

            User user = new User();
            user.setUser_id(responId);
            user.setIcon(responImgUrl);
            user.setNickname(responName);
            user.setPwd(responPwd);
            user.setTel(responTel);
            user.save();

//            List<User> lists = DataSupport.findAll(User.class);
//            for(User l:lists){
//                aa = l.getUser_id();
//
//            }

//            int aa;
//            String name = "", pwd = "", tel, img = "";
//            List<User> userList = DataSupport.select("user_name", "user_pwd")
//                    .where("user_tel = ?", registerTel)
//                    .find(User.class);
//            for (User l:userList) {
//                name = l.getUser_name();
//                pwd = l.getUser_pwd();
//            }
//            Toast.makeText(LoginActivity.this, "----"+name+pwd, Toast.LENGTH_SHORT).show();




//            Toast.makeText(LoginActivity.this, "----"+modifyInfo.getName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
//********
    }

    protected void onStart() {
        super.onStart();

    }



    protected void onDestroy() {

        super.onDestroy();
    }
}
