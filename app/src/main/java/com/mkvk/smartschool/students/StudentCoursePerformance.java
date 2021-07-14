package com.mkvk.smartschool.students;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
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
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentCoursePerAdapter;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentCoursePerformance extends BaseActivity {
    String course_id;
    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    ArrayList<String> quiz_titleList = new ArrayList<String>();
    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> correct_answerList = new ArrayList<String>();
    ArrayList<String> wrong_answerList = new ArrayList<String>();
    ArrayList<String> not_answerList = new ArrayList<String>();
    ArrayList<String> percentageList = new ArrayList<String>();
    ArrayList<String> total_questionList = new ArrayList<String>();
    StudentCoursePerAdapter adapter;
    RecyclerView recyclerview;
    ProgressBar progressBar;
    TextView completedquiz_count,quiz_count,completedlesson_count,lesson_count,course_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_course_performance, null, false);
        mDrawerLayout.addView(contentView, 0);


        titleTV.setText(getApplicationContext().getString(R.string.courseperformance));
        course_id = getIntent().getStringExtra("CourseId");
        lesson_count=findViewById(R.id.lesson_count);
        completedlesson_count=findViewById(R.id.completedlesson_count);
        quiz_count=findViewById(R.id.quiz_count);
        progressBar=findViewById(R.id.progressBar);
        course_progress=findViewById(R.id.course_progress);
        completedquiz_count=findViewById(R.id.completedquiz_count);
        recyclerview=findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        adapter = new StudentCoursePerAdapter(StudentCoursePerformance.this,
                quiz_titleList,idList,correct_answerList,wrong_answerList,not_answerList,percentageList,total_questionList);
        recyclerview.setAdapter(adapter);

        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("course_id", course_id);
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject objt=new JSONObject(params);
            Log.e("params ", objt.toString());
            getDataFromApi(objt.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }
    private void getDataFromApi (String bodyParams) {
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.courseperformanceUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try{
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("result");

                        lesson_count.setText("Total Lesson: "+obj.getString("lessoncount"));
                        completedlesson_count.setText("Completed Lesson: "+ obj.getString("lessoncompleted"));
                        quiz_count.setText("Total Quiz: "+obj.getString("quizcount"));
                        completedquiz_count.setText("Completed Quiz: "+obj.getString("quizcompleted"));
                        Integer progressvalueint=(int)(Double.parseDouble(obj.getString("percentage")));
                        progressBar.setProgress(progressvalueint);
                        if(progressvalueint==100){
                            progressBar.getProgressDrawable().setColorFilter(
                                    getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                        }else if(progressvalueint>0 && progressvalueint<100){
                            progressBar.getProgressDrawable().setColorFilter(
                                    getResources().getColor(R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                        course_progress.setText(progressvalueint+"%");


                        quiz_titleList.clear();
                        total_questionList.clear();
                        idList.clear();
                        correct_answerList.clear();
                        wrong_answerList.clear();
                        not_answerList.clear();
                        percentageList.clear();

                        if (dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                quiz_titleList.add(dataArray.getJSONObject(i).getString("quiz_title"));
                                idList.add(dataArray.getJSONObject(i).getString("id"));
                                total_questionList.add(dataArray.getJSONObject(i).getString("total_question"));
                                correct_answerList.add(dataArray.getJSONObject(i).getString("correct_answer"));
                                wrong_answerList.add(dataArray.getJSONObject(i).getString("wrong_answer"));
                                not_answerList.add(dataArray.getJSONObject(i).getString("not_answer"));
                                percentageList.add(dataArray.getJSONObject(i).getString("percentage"));
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentCoursePerformance.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", Constants.contentType);
                    headers.put("User-ID", Utility.getSharedPreferences(StudentCoursePerformance.this, "userId"));
                    headers.put("Authorization", Utility.getSharedPreferences(StudentCoursePerformance.this, "accessToken"));

                Log.e("Headers new added", headers.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentCoursePerformance.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
