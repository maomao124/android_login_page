package mao.android_login_page;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Class(类名): MainActivity
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/25
 * Time(创建时间)： 20:49
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener
{
    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private CheckBox ck_remember;
    private EditText et_phone;
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private ActivityResultLauncher<Intent> register;
    private Button btn_login;
    private String mPassword = "111111";
    private String mVerifyCode;

    private static final String TAG = "loginPage";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup rb_login = findViewById(R.id.rg_login);
        tv_password = findViewById(R.id.tv_password);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        btn_forget = findViewById(R.id.btn_forget);
        ck_remember = findViewById(R.id.ck_remember);
        rb_password = findViewById(R.id.rb_password);
        rb_verifycode = findViewById(R.id.rb_verifycode);
        btn_login = findViewById(R.id.btn_login);
        // 给rg_login设置单选监听器
        rb_login.setOnCheckedChangeListener(this);
        // 给et_phone添加文本变更监听器
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone, 11));
        // 给et_password添加文本变更监听器
        et_password.addTextChangedListener(new HideTextWatcher(et_password, 6));
        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
        {
            @Override
            public void onActivityResult(ActivityResult result)
            {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK)
                {
                    // 用户密码已改为新密码，故更新密码变量
                    mPassword = intent.getStringExtra("new_password");
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_password)
        {
            // 选择了密码登录
            changeRadioGroupPassword();
        }
        else if (checkedId == R.id.rb_verifycode)
        {
            // 选择了验证码登录
            changeRadioGroupVerifycode();
        }
        else
        {
            //异常
            Log.w(TAG, "onCheckedChanged: error");
        }
       /* switch (checkedId)
        {
            case R.id.rb_password:
                changeRadioGroupPassword();
                break;
            case R.id.rb_verifycode:
                changeRadioGroupVerifycode();
                break;
        }*/
    }

    private void changeRadioGroupVerifycode()
    {
        tv_password.setText(getString(R.string.verifycode));
        et_password.setHint(getString(R.string.input_verifycode));
        btn_forget.setText(getString(R.string.get_verifycode));
        ck_remember.setVisibility(View.GONE);
    }

    private void changeRadioGroupPassword()
    {
        tv_password.setText(getString(R.string.login_password));
        et_password.setHint(getString(R.string.input_password));
        btn_forget.setText(getString(R.string.forget_password));
        ck_remember.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v)
    {
        //获取手机号码
        String phone = et_phone.getText().toString();
        if (phone.length() < 11)
        {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (v.getId() == R.id.btn_forget)
        {
            // 选择了密码方式校验，此时要跳到找回密码页面
            clickForget(phone);
        }
        else if (v.getId() == R.id.btn_login)
        {
            // 密码方式校验
            clickLogin();
        }
        else
        {
            Log.w(TAG, "onClick: error");
        }

        /*switch (v.getId())
        {
            case R.id.btn_forget:
                // 选择了密码方式校验，此时要跳到找回密码页面
                clickForget(phone);
                break;
            case R.id.btn_login:
                // 密码方式校验
                clickLogin();
                break;
        }*/
    }

    private void clickLogin()
    {
        if (rb_password.isChecked())
        {
            if (!mPassword.equals(et_password.getText().toString()))
            {
                Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                return;
            }
            // 提示用户登录成功
            loginSuccess();
        }
        else if (rb_verifycode.isChecked())
        {
            if (mVerifyCode==null)
            {
                Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            // 验证码方式校验
            if (!mVerifyCode.equals(et_password.getText().toString()))
            {
                Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            // 提示用户登录成功
            loginSuccess();
        }
    }

    /**
     * 处理点击忘记密码选项
     *
     * @param phone 电话
     */
    @SuppressLint("DefaultLocale")
    private void clickForget(String phone)
    {
        if (rb_password.isChecked())
        {
            // 以下携带手机号码跳转到找回密码页面
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("phone", phone);
            register.launch(intent);
        }
        else if (rb_verifycode.isChecked())
        {
            // 生成六位随机数字的验证码
            mVerifyCode = String.format("%06d", new Random().nextInt(999999));
            // 以下弹出提醒对话框，提示用户记住六位验证码数字
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请记住验证码");
            builder.setMessage("手机号" + phone + ",本次验证码是" + mVerifyCode + ",请输入验证码");
            builder.setPositiveButton("好的", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * 登录成功
     */
    private void loginSuccess()
    {
        String desc = String.format("您的手机号码是%s，恭喜你通过登录验证，点击“确定”按钮返回上个页面",
                et_phone.getText().toString());
        // 以下弹出提醒对话框，提示用户登录成功
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setPositiveButton("确定返回", (dialog, which) ->
        {
            // 结束当前的活动页面
            finish();
        });
        builder.setNegativeButton("我再看看", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class HideTextWatcher implements TextWatcher
    {
        private final EditText editText;
        private final int maxLength;

        public HideTextWatcher(EditText v, int maxLength)
        {
            this.editText = v;
            this.maxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (s.toString().length() == maxLength)
            {
                // 隐藏输入法软键盘
                closeInput(MainActivity.this, editText);
            }
        }
    }

    /**
     * 关闭(隐藏)输入法
     *
     * @param activity 活动
     * @param view     视图
     */
    public void closeInput(Activity activity, View view)
    {
        //从系统服务中获取输入法管理器
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //关闭屏幕上的输入法软键盘
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}