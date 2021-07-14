package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
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
import com.mkvk.smartschool.adapters.StudentSyllabusLessonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentSyllabuslesson extends BaseActivity {

    ListView lessonList;
    StudentSyllabusLessonAdapter adapter;
    ArrayList<String> NameList = new ArrayList<String>();
    ArrayList<String> totalList = new ArrayList<String>();
    ArrayList<String> total_completeList = new ArrayList<String>();
    ArrayList<String> topicArray = new ArrayList<String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    String subjectid,sectionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_syllabus_lesson, null, false);
        mDrawerLayout.addView(contentView, 0);
         subjectid = getIntent().getExtras().getString("SubjectList");
         sectionid = getIntent().getExtras().getString("SectionIdlist");

        titleTV.setText(getApplicationContext().getString(R.string.Lesson));

        lessonList = (ListView) findViewById(R.id.studentLesson_listView);
        adapter = new StudentSyllabusLessonAdapter(StudentSyllabuslesson.this,
                NameList,topicArray,total_completeList,totalList);
        lessonList.setAdapter(adapter);


        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("subject_group_subject_id",subjectid);
            params.put("subject_group_class_sections_id", sectionid);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getSubjectsLessonsUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try{
                        Log.e("Result", result);
                        JSONArray dataArray = new JSONArray(result);

                        NameList.clear();
                        totalList.clear();
                        total_completeList.clear();
                        topicArray.clear();
                        if (dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                NameList.add(dataArray.getJSONObject(i).getString("name"));
                                total_completeList.add(dataArray.getJSONObject(i).getString("total_complete"));
                                totalList.add(dataArray.getJSONObject(i).getString("total"));
                                topicArray.add(dataArray.getJSONObject(i).getJSONArray("topics").toString());

                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e) {
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
                Toast.makeText(StudentSyllabuslesson.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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

            public String getBodyContentType()
            {
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSyllabuslesson.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
