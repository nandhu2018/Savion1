package savion.tns.com.savion;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Otpverification extends AppCompatActivity {
    private Button btnVerifyOtp;
    private EditText inputOtp;
    SmsVerifyCatcher smsVerifyCatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                inputOtp.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });
        smsVerifyCatcher.setPhoneNumberFilter("56161174");
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVerifyOtp.setBackgroundColor(getResources().getColor(R.color.transparent_button_focused1));
                if (inputOtp.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("otp"))){
                    btnVerifyOtp.setEnabled(false);
                    btnVerifyOtp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    SharedPreferences.Editor editor = getSharedPreferences("mobile", MODE_PRIVATE).edit();
                    editor.putString("mobile",getIntent().getStringExtra("mobile"));
                    editor.apply();
                    Toast.makeText(Otpverification.this, "Service Booked", Toast.LENGTH_SHORT).show();

                    finish();
                }else{
                    btnVerifyOtp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    btnVerifyOtp.setEnabled(true);
                    Toast.makeText(Otpverification.this, "OTP does not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
}
