package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.mkvk.smartschool.adapters.StudentExamScheduleAdapter;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentExamSchedule extends BaseActivity {

    RecyclerView scheduleList;
    ArrayList<String> subjectList = new ArrayList<String>();
    ArrayList <String> dateList = new ArrayList<String>();
    ArrayList <String> timeList = new ArrayList<String>();
    ArrayList <String> credit_hoursList = new ArrayList<String>();
    ArrayList <String> roomList = new ArrayList<String>();
    ArrayList <String> durationList = new ArrayList<String>();
    ArrayList <String> max_marksList = new ArrayList<String>();
    ArrayList <String> min_marksList = new ArrayList<String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    String passedArg;
    StudentExamScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_exam_schedule_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.examSchedule));

        scheduleList = findViewById(R.id.studentExamSchedule_scheduleListview);
        passedArg = getIntent().getExtras().getString("Exam_group_Id");

        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("exam_group_class_batch_exam_id", passedArg);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        adapter = new StudentExamScheduleAdapter(StudentExamSchedule.this,subjectList, dateList, timeList, roomList,durationList,max_marksList,min_marksList,credit_hoursList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        scheduleList.setLayoutManager(mLayoutManager);
        scheduleList.setItemAnimator(new DefaultItemAnimator());
        scheduleList.setAdapter(adapter);
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getExamScheduleDetailsUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        subjectList.clear();
                        dateList.clear();
                        timeList.clear();
                        roomList.clear();
                        durationList.clear();
                        max_marksList.clear();
                        min_marksList.clear();
                            JSONArray dataArray = object.getJSONArray("exam_subjects");

                            for(int i=0; i<dataArray.length(); i++) {

                                JSONObject dataObject = dataArray.getJSONObject(i);
                                if(dataObject.getString("subject_code").equals("")){
                                    subjectList.add(dataObject.getString("subject_name"));
                                }else{
                                    subjectList.add(dataObject.getString("subject_name")+" ("+dataObject.getString("subject_code")+")");
                                }

                                dateList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataObject.getString("date_from")));
                                timeList.add(dataObject.getString("time_from"));
                                roomList.add(dataObject.getString("room_no"));
                                credit_hoursList.add(dataObject.getString("credit_hours"));
                                durationList.add(dataObject.getString("duration"));
                                max_marksList.add(dataObject.getString("max_marks"));
                                min_marksList.add(dataObject.getString("min_marks"));

                            }
                            adapter.notifyDataSetChanged();


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
                Toast.makeText(StudentExamSchedule.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
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
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StudentExamSchedule.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
