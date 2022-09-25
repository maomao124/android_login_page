package mao.android_login_page;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

    }


}