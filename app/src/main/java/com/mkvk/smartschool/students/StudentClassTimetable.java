package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mkvk.smartschool.BaseActivity;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentClassTimetableAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentClassTimetable extends BaseActivity {

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    RecyclerView mondayList, tuesdayList, wednesdayList, thursdayList, fridayList, saturdayList, sundayList;
    TextView Header1, Header2, Header3, Header4, Header5, Header6, Header7;
    LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7;
    StudentClassTimetableAdapter  Adapter1,Adapter2,Adapter3,Adapter4,Adapter5,Adapter6,Adapter7;

    ArrayList<String> mondaySubject = new ArrayList<>();
    ArrayList<String> mondayTime = new ArrayList<>();
    ArrayList<String> mondayRoomNo = new ArrayList<>();

    ArrayList<String> tuesdaySubject = new ArrayList<>();
    ArrayList<String> tuesdayTime = new ArrayList<>();
    ArrayList<String> tuesdayRoomNo = new ArrayList<>();

    ArrayList<String> wednesdaySubject = new ArrayList<>();
    ArrayList<String> wednesdayTime = new ArrayList<>();
    ArrayList<String> wednesdayRoomNo = new ArrayList<>();

    ArrayList<String> thursdaySubject = new ArrayList<>();
    ArrayList<String> thursdayTime = new ArrayList<>();
    ArrayList<String> thursdayRoomNo = new ArrayList<>();

    ArrayList<String> fridaySubject = new ArrayList<>();
    ArrayList<String> fridayTime = new ArrayList<>();
    ArrayList<String> fridayRoomNo = new ArrayList<>();

    ArrayList<String> saturdaySubject = new ArrayList<>();
    ArrayList<String> saturdayTime = new ArrayList<>();
    ArrayList<String> saturdayRoomNo = new ArrayList<>();

    ArrayList<String> sundaySubject = new ArrayList<>();
    ArrayList<String> sundayTime = new ArrayList<>();
    ArrayList<String> sundayRoomNo = new ArrayList<>();

    public String startweek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_class_timetable_activity, null, false);
        mDrawerLayout.addView(contentView, 0);
        startweek = Utility.getSharedPreferences(getApplicationContext(), "startWeek");
        titleTV.setText(getApplicationContext().getString(R.string.timeTable));

        mondayList = findViewById(R.id.classTimetable_mondayList);
        tuesdayList = findViewById(R.id.classTimetable_tuesdayList);
        wednesdayList = findViewById(R.id.classTimetable_wednesdayList);
        thursdayList = findViewById(R.id.classTimetable_thursdayList);
        fridayList = findViewById(R.id.classTimetable_fridayList);
        saturdayList = findViewById(R.id.classTimetable_saturdayList);
        sundayList = findViewById(R.id.classTimetable_sundayList);

        Header1 = findViewById(R.id.classTimetable_Header1);
        Header2= findViewById(R.id.classTimetable_Header2);
        Header3 = findViewById(R.id.classTimetable_Header3);
        Header4 = findViewById(R.id.classTimetable_Header4);
        Header5 = findViewById(R.id.classTimetable_Header5);
        Header6 = findViewById(R.id.classTimetable_Header6);
        Header7 = findViewById(R.id.classTimetable_Header7);

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);
        layout7 = findViewById(R.id.layout7);

        decorate();
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());

        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        if(startweek.equals("Sunday")){
            Header1.setText(getApplicationContext().getString(R.string.sunday));
            Header2.setText(getApplicationContext().getString(R.string.monday));
            Header3.setText(getApplicationContext().getString(R.string.tuesday));
            Header4.setText(getApplicationContext().getString(R.string.wednesday));
            Header5.setText(getApplicationContext().getString(R.string.thursday));
            Header6.setText(getApplicationContext().getString(R.string.friday));
            Header7.setText(getApplicationContext().getString(R.string.saturday));

           Adapter1 = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);
           Adapter2 = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
           Adapter3 = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
           Adapter4 = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
           Adapter5 = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
           Adapter6 = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
           Adapter7 = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);

        }else if(startweek.equals("Monday")){
            Header1.setText(getApplicationContext().getString(R.string.monday));
            Header2.setText(getApplicationContext().getString(R.string.tuesday));
            Header3.setText(getApplicationContext().getString(R.string.wednesday));
            Header4.setText(getApplicationContext().getString(R.string.thursday));
            Header5.setText(getApplicationContext().getString(R.string.friday));
            Header6.setText(getApplicationContext().getString(R.string.saturday));
            Header7.setText(getApplicationContext().getString(R.string.sunday));

            Adapter1 = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
            Adapter2 = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
            Adapter3 = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
            Adapter4 = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
            Adapter5 = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
            Adapter6 = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
            Adapter7 = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);

        }else if(startweek.equals("Tuesday")){
            Header1.setText(getApplicationContext().getString(R.string.tuesday));
            Header2.setText(getApplicationContext().getString(R.string.wednesday));
            Header3.setText(getApplicationContext().getString(R.string.thursday));
            Header4.setText(getApplicationContext().getString(R.string.friday));
            Header5.setText(getApplicationContext().getString(R.string.saturday));
            Header6.setText(getApplicationContext().getString(R.string.sunday));
            Header7.setText(getApplicationContext().getString(R.string.monday));

            Adapter1 = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
            Adapter2 = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
            Adapter3 = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
            Adapter4 = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
            Adapter5 = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
            Adapter6 = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);
            Adapter7 = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);

        }else if(startweek.equals("Wednesday")){
            Header1.setText(getApplicationContext().getString(R.string.wednesday));
            Header2.setText(getApplicationContext().getString(R.string.thursday));
            Header3.setText(getApplicationContext().getString(R.string.friday));
            Header4.setText(getApplicationContext().getString(R.string.saturday));
            Header5.setText(getApplicationContext().getString(R.string.sunday));
            Header6.setText(getApplicationContext().getString(R.string.monday));
            Header7.setText(getApplicationContext().getString(R.string.tuesday));

            Adapter1 = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
            Adapter2 = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
            Adapter3 = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
            Adapter4 = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
            Adapter5 = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);
            Adapter6 = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
            Adapter7 = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);

        }else if(startweek.equals("Thursday")){
            Header1.setText(getApplicationContext().getString(R.string.thursday));
            Header2.setText(getApplicationContext().getString(R.string.friday));
            Header3.setText(getApplicationContext().getString(R.string.saturday));
            Header4.setText(getApplicationContext().getString(R.string.sunday));
            Header5.setText(getApplicationContext().getString(R.string.monday));
            Header6.setText(getApplicationContext().getString(R.string.tuesday));
            Header7.setText(getApplicationContext().getString(R.string.wednesday));

            Adapter1= new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
            Adapter2= new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
            Adapter3= new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
            Adapter4= new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);
            Adapter5= new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
            Adapter6= new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
            Adapter7= new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);

        }else if(startweek.equals("Friday")){
            Header1.setText(getApplicationContext().getString(R.string.friday));
            Header2.setText(getApplicationContext().getString(R.string.saturday));
            Header3.setText(getApplicationContext().getString(R.string.sunday));
            Header4.setText(getApplicationContext().getString(R.string.monday));
            Header5.setText(getApplicationContext().getString(R.string.tuesday));
            Header6.setText(getApplicationContext().getString(R.string.wednesday));
            Header7.setText(getApplicationContext().getString(R.string.thursday));

            Adapter1 = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
            Adapter2 = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
            Adapter3 = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);
            Adapter4 = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
            Adapter5 = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
            Adapter6 = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
            Adapter7 = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);

        }else if(startweek.equals("Saturday")){
            Header1.setText(getApplicationContext().getString(R.string.saturday));
            Header2.setText(getApplicationContext().getString(R.string.sunday));
            Header3.setText(getApplicationContext().getString(R.string.monday));
            Header4.setText(getApplicationContext().getString(R.string.tuesday));
            Header5.setText(getApplicationContext().getString(R.string.wednesday));
            Header6.setText(getApplicationContext().getString(R.string.thursday));
            Header7.setText(getApplicationContext().getString(R.string.friday));

            Adapter1 = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
            Adapter2 = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);
            Adapter3 = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
            Adapter4 = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
            Adapter5 = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
            Adapter6 = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
            Adapter7 = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
        }

        RecyclerView.LayoutManager LayoutManager1 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager2 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager3 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager4 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager5 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager6 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager7 = new LinearLayoutManager(getApplicationContext());

        mondayList.setLayoutManager(LayoutManager1);
        mondayList.setItemAnimator(new DefaultItemAnimator());
        mondayList.setAdapter(Adapter1);


        tuesdayList.setLayoutManager(LayoutManager2);
        tuesdayList.setItemAnimator(new DefaultItemAnimator());
        tuesdayList.setAdapter(Adapter2);


        wednesdayList.setLayoutManager(LayoutManager3);
        wednesdayList.setItemAnimator(new DefaultItemAnimator());
        wednesdayList.setAdapter(Adapter3);

        thursdayList.setLayoutManager(LayoutManager4);
        thursdayList.setItemAnimator(new DefaultItemAnimator());
        thursdayList.setAdapter(Adapter4);

        fridayList.setLayoutManager(LayoutManager5);
        fridayList.setItemAnimator(new DefaultItemAnimator());
        fridayList.setAdapter(Adapter5);


        saturdayList.setLayoutManager(LayoutManager6);
        saturdayList.setItemAnimator(new DefaultItemAnimator());
        saturdayList.setAdapter(Adapter6);


        sundayList.setLayoutManager(LayoutManager7);
        sundayList.setItemAnimator(new DefaultItemAnimator());
        sundayList.setAdapter(Adapter7);

    }

    private void decorate() {
        Header1.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        Header2.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        Header3.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        Header4.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        Header5.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        Header6.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        Header7.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getClassScheduleUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        if (success.equals("200")) {

                            JSONObject dataObject = object.getJSONObject("timetable");

                            JSONArray mondayArray = dataObject.getJSONArray("Monday");
                            JSONArray tuesdayArray = dataObject.getJSONArray("Tuesday");
                            JSONArray wednesdayArray = dataObject.getJSONArray("Wednesday");
                            JSONArray thursdayArray = dataObject.getJSONArray("Thursday");
                            JSONArray fridayArray = dataObject.getJSONArray("Friday");
                            JSONArray saturdayArray = dataObject.getJSONArray("Saturday");
                            JSONArray sundayArray = dataObject.getJSONArray("Sunday");

                            if (mondayArray.length() > 0) {
                                for (int i = 0; i < mondayArray.length(); i++) {
                                    if(mondayArray.getJSONObject(i).getString("code").equals("")){
                                        mondaySubject.add(mondayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        mondaySubject.add(mondayArray.getJSONObject(i).getString("subject_name")+" ("+mondayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!mondayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        mondayTime.add(mondayArray.getJSONObject(i).getString("time_from") + " - " + mondayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        mondayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    mondayRoomNo.add(mondayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            if (tuesdayArray.length() > 0) {
                                for (int i = 0; i < tuesdayArray.length(); i++) {
                                    if(tuesdayArray.getJSONObject(i).getString("code").equals("")){
                                        tuesdaySubject.add(tuesdayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        tuesdaySubject.add(tuesdayArray.getJSONObject(i).getString("subject_name")+" ("+tuesdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!tuesdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        tuesdayTime.add(tuesdayArray.getJSONObject(i).getString("time_from") + " - " + tuesdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        tuesdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    tuesdayRoomNo.add(tuesdayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            if (wednesdayArray.length() > 0) {
                                for (int i = 0; i < wednesdayArray.length(); i++) {
                                    if(wednesdayArray.getJSONObject(i).getString("code").equals("")){
                                        wednesdaySubject.add(wednesdayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        wednesdaySubject.add(wednesdayArray.getJSONObject(i).getString("subject_name")+" ("+wednesdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!wednesdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        wednesdayTime.add(wednesdayArray.getJSONObject(i).getString("time_from") + " - " + wednesdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        wednesdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    wednesdayRoomNo.add(wednesdayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            if (thursdayArray.length() > 0) {
                                for (int i = 0; i < thursdayArray.length(); i++) {
                                    if(thursdayArray.getJSONObject(i).getString("code").equals("")){
                                        thursdaySubject.add(thursdayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        thursdaySubject.add(thursdayArray.getJSONObject(i).getString("subject_name")+" ("+thursdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!thursdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        thursdayTime.add(thursdayArray.getJSONObject(i).getString("time_from") + " - " + thursdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        thursdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    thursdayRoomNo.add(thursdayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            if (fridayArray.length() > 0) {
                                for (int i = 0; i < fridayArray.length(); i++) {
                                    if(fridayArray.getJSONObject(i).getString("code").equals("")){
                                        fridaySubject.add(fridayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        fridaySubject.add(fridayArray.getJSONObject(i).getString("subject_name")+" ("+fridayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!fridayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        fridayTime.add(fridayArray.getJSONObject(i).getString("time_from") + " - " + fridayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        fridayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    fridayRoomNo.add(fridayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            if (saturdayArray.length() > 0) {
                                for (int i = 0; i < saturdayArray.length(); i++) {
                                    if(saturdayArray.getJSONObject(i).getString("code").equals("")){
                                        saturdaySubject.add(saturdayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        saturdaySubject.add(saturdayArray.getJSONObject(i).getString("subject_name")+" ("+saturdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!saturdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        saturdayTime.add(saturdayArray.getJSONObject(i).getString("time_from") + " - " + saturdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        saturdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    saturdayRoomNo.add(saturdayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            if (sundayArray.length() > 0) {
                                for (int i = 0; i < sundayArray.length(); i++) {
                                    if(sundayArray.getJSONObject(i).getString("code").equals("")){
                                        sundaySubject.add(sundayArray.getJSONObject(i).getString("subject_name"));
                                    }else{
                                        sundaySubject.add(sundayArray.getJSONObject(i).getString("subject_name")+" ("+sundayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!sundayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        sundayTime.add(sundayArray.getJSONObject(i).getString("time_from") + " - " + sundayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        sundayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    sundayRoomNo.add(sundayArray.getJSONObject(i).getString("room_no"));
                                }
                            }

                            Adapter1.notifyDataSetChanged();
                            Adapter2.notifyDataSetChanged();
                            Adapter3.notifyDataSetChanged();
                            Adapter4.notifyDataSetChanged();
                            Adapter5.notifyDataSetChanged();
                            Adapter6.notifyDataSetChanged();
                            Adapter7.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentClassTimetable.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentClassTimetable.this);//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

}
