package savion.tns.com.savion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileVerification1 extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;

    private String verificationCode,otpval;
    private Button btnRequestSms, btnVerifyOtp,btnVerifypin;
    private EditText inputName, inputEmail, inputMobile, inputOtp,enterpin,reenterpin;
    private ProgressBar progressBar;
    private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private MobileVerification1.ViewPagerAdapter adapter;
    private LinearLayout layoutEditMobile;
    SmsVerifyCatcher smsVerifyCatcher;
    String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        viewPager = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);

        //StartFirebaseLogin();

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                inputOtp.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });
        smsVerifyCatcher.setPhoneNumberFilter("56161174");
        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        pref = new PrefManager(this);

        pref.setMobileNumber(getIntent().getStringExtra("mobile"));
        viewPager.setCurrentItem(1);
// hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);
// Checking for user session
        // if user is already logged in, take him to main activity
        if (pref.isLoggedIn()) {
            Toast.makeText(this, "already logged in", Toast.LENGTH_SHORT).show();
        }

        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(1);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }

        adapter = new MobileVerification1.ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }



    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;

            }
            return findViewById(resId);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                btnRequestSms.setEnabled(false);
                validateForm();
                break;

            case R.id.btn_verify_otp:
                btnVerifyOtp.setEnabled(false);
               /* PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, inputOtp.getText().toString());
                SigninWithPhone(credential);*/
                if (inputOtp.getText().toString().equalsIgnoreCase(getIntent().getStringExtra("otp"))){
                    SharedPreferences.Editor editor = getSharedPreferences("mobile", MODE_PRIVATE).edit();
                    editor.putString("mobile",inputMobile.getText().toString());
                    editor.apply();
                    btnVerifyOtp.setEnabled(true);
                    finish();
                }else{
                    btnVerifyOtp.setEnabled(true);
                    Toast.makeText(this, "OTP does not match.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;

        }
    }

    /**
     * Validating user details form
     */
    private void validateForm() {
        /*String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();*/
        String mobile = inputMobile.getText().toString().trim();

        /*// validating empty name and email
        if (name.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your details", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // validating mobile number
        // it should be of 10 digits length
        if (isValidPhoneNumber(mobile)) {

            // request for sms
            progressBar.setVisibility(View.VISIBLE);


            // saving the mobile number in shared preferences
            pref.setMobileNumber(mobile);
            /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91"+inputMobile.getText().toString(),                     // Phone number to verify
                    60,                           // Timeout duration
                    TimeUnit.SECONDS,                // Unit of timeout
                    MobileVerification.this,        // Activity (for callback binding)
                    mCallback);*/

            makeJsonRequest(mobile);
            txtEditMobile.setText(pref.getMobileNumber());
            layoutEditMobile.setVisibility(View.VISIBLE);
        } else {
            btnRequestSms.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }
    public void makeJsonRequest(String mobile) {

        String urlJsonObj = "http://www.savion.in/booking/sentotp.php";
        JSONObject obj = new JSONObject();
        try {
            obj.put("mob", "+91"+mobile);


        } catch (JSONException e) {
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object


                    btnRequestSms.setEnabled(true);
                    pref.setIsWaitingForSms(true);
                    viewPager.setCurrentItem(1);
                    otp=response.getString("otp");
                    progressBar.setVisibility(View.GONE);



                    //txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    btnRequestSms.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                btnRequestSms.setEnabled(true);
            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }



}
