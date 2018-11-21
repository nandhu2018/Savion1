package savion.tns.com.savion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.julianraj.validatedtextinputlayout.ValidatedTextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.unstoppable.submitbuttonview.SubmitButton;

import net.igenius.customcheckbox.CustomCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.out;


public class Book extends Fragment {
    private static final int REQUEST_WELCOME_SCREEN_RESULT = 13;

    private PrefManager pref;
    ValidatedTextInputLayout name, mobile, place, vehicleno,email;
    private Button sBtnLoading, sBtnProgress;
    CustomCheckBox washing, mech, elect, tyre, access, other;
    Spinner time, vehicletype;
    EditText remarks;
    List<String> timeperiod, type;
    ArrayAdapter timeadapter, typeadapter;
    TextInputLayout date;
    int day, month, year = 2018;
    String dateval, servicetypeval="", timeval, vehicletypeval;
    DatabaseHandler db;
    KProgressHUD hud;
    Calendar cal;
    int k,l;
    public Book() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main1, container, false);

        /*// The welcome screen for this app (only one that automatically shows)
        sampleWelcomeScreen = new WelcomeHelper(getActivity(), SampleWelcomeActivity.class);
        sampleWelcomeScreen.show(savedInstanceState);*/


        sBtnLoading = (Button) view.findViewById(R.id.submitbutton);
        name = ((ValidatedTextInputLayout) view.findViewById(R.id.username));
        mobile = ((ValidatedTextInputLayout) view.findViewById(R.id.mobile));
        place = ((ValidatedTextInputLayout) view.findViewById(R.id.place));
        vehicleno = ((ValidatedTextInputLayout) view.findViewById(R.id.vehicleno));
        email = ((ValidatedTextInputLayout) view.findViewById(R.id.email));


        washing = (CustomCheckBox) view.findViewById(R.id.washing);
        mech = (CustomCheckBox) view.findViewById(R.id.mechanical);
        elect = (CustomCheckBox) view.findViewById(R.id.electrical);
        tyre = (CustomCheckBox) view.findViewById(R.id.tyre);
        access = (CustomCheckBox) view.findViewById(R.id.accessories);
        other = (CustomCheckBox) view.findViewById(R.id.others);

        date = (TextInputLayout) view.findViewById(R.id.date);
        time = (Spinner) view.findViewById(R.id.preftime);
        vehicletype = (Spinner) view.findViewById(R.id.vehicletype);

        remarks = (EditText) view.findViewById(R.id.remarks);
        cal = Calendar.getInstance();
        db=new DatabaseHandler(getActivity());
        pref = new PrefManager(getActivity());
        timeperiod = new ArrayList<>();
        loaddefault();


        type = new ArrayList<>();



        /*for (int i = 10; i <= 19; i++) {
            for (int j = 1; j <= 2; j++) {
                if (i == 11) {
                    if (j == 1) {
                        timeperiod.add(i + ":00am - " + i + ":30am");
                    } else {
                        timeperiod.add(i + ":30am - " + (i + 1) + ":00pm");
                    }
                } else if (i > 11) {
                    if (i == 12) {
                        if (j == 1) {
                            timeperiod.add(i + ":00pm - " + i + ":30pm");
                        } else {
                            timeperiod.add(i + ":30pm - " + "01:00pm");
                        }
                    } else {
                        if (j == 1) {
                            timeperiod.add((i - 12) + ":00pm - " + (i - 12) + ":30pm");
                        } else {
                            timeperiod.add((i - 12) + ":30pm - " + (i - 11) + ":00pm");
                        }
                    }

                } else {
                    if (j == 1) {
                        timeperiod.add(i + ":00am - " + i + ":30am");
                    } else {
                        timeperiod.add(i + ":30am - " + (i + 1) + ":00am");
                    }
                }

            }
        }*/

        timeadapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, timeperiod);
        timeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(timeadapter);


        type.add("Car");
        type.add("Truck");

        typeadapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, type);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicletype.setAdapter(typeadapter);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String cdate = sdf.format(c.getTime());


        sBtnLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    sBtnLoading.setBackgroundColor(getResources().getColor(R.color.transparent_button_focused1));
                    sBtnLoading.setEnabled(false);
                    if (validateFields()) {

                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("details", MODE_PRIVATE).edit();
                        editor.putString("name", name.getEditText().getText().toString());
                        editor.putString("email", email.getEditText().getText().toString());
                        editor.putString("mobile", mobile.getEditText().getText().toString());
                        editor.putString("place", place.getEditText().getText().toString());
                        editor.putString("vehicle", vehicleno.getEditText().getText().toString());
                        editor.apply();
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("mobile", Context.MODE_PRIVATE);

                        if (servicetypeval.length() > 0) {

                            if (mobile.getEditText().getText().toString().equalsIgnoreCase(sharedpreferences.getString("mobile", ""))) {
                                hud = KProgressHUD.create(getActivity())
                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                                hud.show();
                                makeJsonRequest(name.getEditText().getText().toString(), email.getEditText().getText().toString(), mobile.getEditText().getText().toString(), vehicleno.getEditText().getText().toString(), place.getEditText().getText().toString(), date.getEditText().getText().toString(), timeval, vehicletypeval, servicetypeval, remarks.getText().toString());
                            } else {

                                sBtnLoading.setBackgroundColor(getResources().getColor(R.color.transparent_button_focused1));
                                mobileverification(mobile.getEditText().getText().toString());


                            }

                        } else {
                            sBtnLoading.setEnabled(true);
                            sBtnLoading.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            Toast.makeText(getActivity(), "Select a service type to continue.", Toast.LENGTH_SHORT).show();
                        }


                    /*db.adddetail(name.getEditText().getText().toString(),email.getEditText().getText().toString(),mobile.getEditText().getText().toString(),place.getEditText().getText().toString(),vehicleno.getEditText().getText().toString(),servicetypeval,date.getEditText().getText().toString(),timeval,vehicletypeval,remarks.getText().toString());
                    History.myListener.updateView(true,"");*/
                    } else {

                        sBtnLoading.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                }else {
                    Toast.makeText(getActivity(), "Check Internet Connectivity.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        date.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDialog(0);

                // Get Current Date
               /* final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
*/

                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.getEditText().setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dateval = "";
                                dateval = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                timeperiod.clear();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar c = Calendar.getInstance();
                                String cdate = sdf.format(c.getTime());
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                                Date d1 = null;
                                Date d2 = null;
                                Date current=Calendar.getInstance().getTime();
                                try {
                                    d1 = format.parse(dateval);
                                    d2 = format.parse(cdate);
                                    long diff = d1.getTime() - d2.getTime();
                                    long diffDays = diff / (24 * 60 * 60 * 1000);
                                    long diffSeconds = diff / 1000 % 60;
                                    long diffMinutes = diff / (60 * 1000) % 60;
                                    long diffHours = diff / (60 * 60 * 1000) % 24;
                                    if (diffDays==0){

                                        loadtime();

                                    }else {
                                        for (int i=1;i<=19;i++){
                                            for (int j=1;j<=2;j++){
                                                if (j==1){
                                                    timeperiod.add(i+":00");
                                                }else {
                                                    timeperiod.add(i+":30");
                                                }
                                            }
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, day);



                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();




            }
        });

        washing.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                servicetypeval=servicetypeval+"washing"+"|";
            }
        });
        mech.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                servicetypeval=servicetypeval+"mechanical"+"|";
            }
        });
        elect.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                servicetypeval=servicetypeval+"electrical"+"|";
            }
        });
        tyre.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                servicetypeval=servicetypeval+"tyre"+"|";
            }
        });
        access.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                servicetypeval=servicetypeval+"accessories"+"|";
            }
        });
        other.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                servicetypeval=servicetypeval+"others"+"|";
            }
        });



        vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                vehicletypeval= String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                timeval= String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }

    private void loadtime(){
        Date date1 = new Date();
        date1.setHours(date1.getHours() );
        System.out.println(date1);
        SimpleDateFormat simpDate,simpDate1;
        simpDate = new SimpleDateFormat("HH");
        simpDate1 = new SimpleDateFormat("mm");
        System.out.println(simpDate.format(date1));


        if (Integer.parseInt(simpDate.format(date1))<=10){
            k=10;

        }else if (Integer.parseInt(simpDate.format(date1))>10&&Integer.parseInt(simpDate.format(date1))<20){
            k=Integer.parseInt(simpDate.format(date1));
        }

        if (Integer.parseInt(simpDate1.format(date1))>=0&&Integer.parseInt(simpDate1.format(date1))<30){
            l=2;
        }else if (Integer.parseInt(simpDate1.format(date1))>=30){
            l=1;
            k=k+1;
        }

        if (Integer.parseInt(simpDate.format(date1))>=20) {
            k=10;
            l=1;
            cal.add(Calendar.DAY_OF_YEAR, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String cdate = sdf.format(cal.getTime());
            date.getEditText().setText(cdate);
        }
        for (int i=k;i<=19;i++){
            for (int j=l;j<=2;j++){
                if (j==1){
                    timeperiod.add(i+":00");
                }else {
                    timeperiod.add(i+":30");
                }
            }
        }
    }
    public void loaddefault(){
        timeperiod.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 0);
        String cdate = sdf.format(cal.getTime());
        date.getEditText().setText(cdate);
        loadtime();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("name")) {

            name.getEditText().setText(sharedpreferences.getString("name",""));
        }
        if (sharedpreferences.contains("email")) {

            email.getEditText().setText(sharedpreferences.getString("email",""));
        }
        if (sharedpreferences.contains("mobile")) {

            mobile.getEditText().setText(sharedpreferences.getString("mobile",""));
        }
        if (sharedpreferences.contains("place")) {

            place.getEditText().setText(sharedpreferences.getString("place",""));
        }
        if (sharedpreferences.contains("vehicle")) {

            vehicleno.getEditText().setText(sharedpreferences.getString("vehicle",""));
        }

    }
    private boolean validateFields() {
        boolean flag = true;
        if (!name.validate()) {
            flag = false;
        }
        if (!mobile.validate())
            flag = false;
        if (!place.validate())
            flag = false;
        if (!vehicleno.validate())
            flag = false;

        return flag;
    }

    public void makeJsonRequest(final String name, final String email, final String mobile, final String vehicleno, final String place, final String date, final String time, final String vehicletype, final String servicetype, final String remarks) {

        String urlJsonObj = "http://www.savion.in/booking/service_book.php";
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("email", email);
            obj.put("mob", "+91"+mobile);
            obj.put("place", place);
            if (servicetypeval.length()>0){
                obj.put("ser_type",servicetypeval.substring(0,servicetypeval.length()-1));
            }else {
                obj.put("ser_type","washing");
            }

            obj.put("date", date);
            obj.put("time", time);
            obj.put("veh_no", vehicleno);
            obj.put("veh_type", vehicletype);
            obj.put("remarks", remarks);


        } catch (JSONException e) {
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object


                    if (response.has("status")) {

                        String status = response.getString("status");
                        if (status.equalsIgnoreCase("sucess")) {
                            hud.dismiss();
                            sBtnLoading.setEnabled(true);
                            sBtnLoading.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            db.adddetail(name,email,mobile,place,vehicleno,servicetype,date,time,vehicletype,remarks);
                            History.myListener.updateView(true,"");
                            Toast.makeText(getActivity(), "Service Booked", Toast.LENGTH_SHORT).show();


                            final AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Service Booked");

                            alertDialog.setMessage("Your service has been registered.You just need to come to service station at the registered time. ");
                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.tick);

                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog closed
                                    alertDialog.dismiss();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                            //db.adddetail(name,email,mobile,place,vehicleno,servicetype,date,time,vehicletype,remarks);
                           /* new CDialog(getActivity()).createAlert("Booked",
                                    CDConstants.SUCCESS,
                                    // Type of dialog
                                    CDConstants.MEDIUM)    //  size of dialog
                                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                    .setDuration(2000)   // in milliseconds
                                    .setTextSize(CDConstants.MEDIUM)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                    .setBackgroundColor(getResources().getColor(R.color.blue_background))
                                    .show();*/
                        } else {
                            sBtnLoading.setEnabled(true);
                            hud.dismiss();
                        }

                    }

                    //txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    sBtnLoading.setEnabled(true);
                    hud.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                hud.dismiss();
                sBtnLoading.setEnabled(true);
            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }
    public void makeJsonRequest1(final String name, final String email, final String mobile, final String vehicleno, final String place, final String date, final String time, final String vehicletype, final String servicetype, final String remarks) {

        String urlJsonObj = "http://www.savion.in/booking/service_book.php";
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("email", email);
            obj.put("mob", "+91"+mobile);
            obj.put("place", place);
            if (servicetypeval.length()>0){
                obj.put("ser_type",servicetypeval.substring(0,servicetypeval.length()-1));
            }else {
                obj.put("ser_type","washing");
            }

            obj.put("date", date);
            obj.put("time", time);
            obj.put("veh_no", vehicleno);
            obj.put("veh_type", vehicletype);
            obj.put("remarks", remarks);


        } catch (JSONException e) {
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object


                    if (response.has("status")) {

                        String status = response.getString("status");
                        if (status.equalsIgnoreCase("sucess")) {
                            sBtnLoading.setEnabled(true);
                            sBtnLoading.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            db.adddetail(name,email,mobile,place,vehicleno,servicetype,date,time,vehicletype,remarks);
                            History.myListener.updateView(true,"");

                            //db.adddetail(name,email,mobile,place,vehicleno,servicetype,date,time,vehicletype,remarks);
                           /* new CDialog(getActivity()).createAlert("Booked",
                                    CDConstants.SUCCESS,
                                    // Type of dialog
                                    CDConstants.MEDIUM)    //  size of dialog
                                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                    .setDuration(2000)   // in milliseconds
                                    .setTextSize(CDConstants.MEDIUM)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                    .setBackgroundColor(getResources().getColor(R.color.blue_background))
                                    .show();*/
                        } else {
                            sBtnLoading.setEnabled(true);
                        }

                    }

                    //txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    sBtnLoading.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                sBtnLoading.setEnabled(true);
            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }
    public void mobileverification(final String mobile) {

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

                    sBtnLoading.setEnabled(true);
                    pref.setIsWaitingForSms(true);
                    sBtnLoading.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Intent otp=new Intent(getActivity(),Otpverification.class);
                    otp.putExtra("mobile",mobile);
                    otp.putExtra("otp",response.getString("otp"));
                    startActivity(otp);
                    makeJsonRequest1(name.getEditText().getText().toString(),email.getEditText().getText().toString(),mobile,vehicleno.getEditText().getText().toString(),place.getEditText().getText().toString(),date.getEditText().getText().toString(),timeval,vehicletypeval,servicetypeval,remarks.getText().toString());
                    //txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    sBtnLoading.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                sBtnLoading.setEnabled(true);

            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }






    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }



}
