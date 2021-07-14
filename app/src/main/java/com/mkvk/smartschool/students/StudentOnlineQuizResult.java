package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.mkvk.smartschool.adapters.StudentOnlineQuizResultAdapter;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentOnlineQuizResult extends BaseActivity {
    public String defaultDatetimeFormat, currency;
    RecyclerView recyclerView;
    LinearLayout nodata_layout;
    StudentOnlineQuizResultAdapter adapter;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    ArrayList<String> select_optionlist = new ArrayList<String>();
    ArrayList<String> correctlist = new ArrayList<String>();
    ArrayList<String> option_a = new ArrayList<String>();
    ArrayList<String> option_b = new ArrayList<String>();
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> questionlist = new ArrayList<String>();
    ArrayList<String> subject_namelist = new ArrayList<String>();
    String is_neg_marking_marks;
    ArrayList<String> option_c = new ArrayList<String>();
    ArrayList<String> option_d = new ArrayList<String>();
    ArrayList<String> option_e = new ArrayList<String>();
    ArrayList<String> neg_marks_list = new ArrayList<String>();
    ArrayList<String> remark_list = new ArrayList<String>();
    ArrayList<String> question_typelist = new ArrayList<String>();
    ArrayList<String> marklist = new ArrayList<String>();
    ArrayList<String> getmarklist = new ArrayList<String>();
    String OnlineExam_student_Id,exam_id,quiz_id,quiz_name;
    PieChart pieChart;
    Button reset_btn;
    TextView correct,wrong,notattempt,total_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_online_quiz_result, null, false);
        mDrawerLayout.addView(contentView, 0);
        // searchView.setVisibility(View.GONE);
        titleTV.setText(getApplicationContext().getString(R.string.quizresult));
        reset_quiz.setVisibility(View.VISIBLE);
        quiz_id = getIntent().getExtras().getString("quiz_id");
        quiz_name = getIntent().getExtras().getString("quiz_name");
        OnlineExam_student_Id = getIntent().getExtras().getString("OnlineExam_students_Id");
        exam_id = getIntent().getExtras().getString("exams_id");

        defaultDatetimeFormat = Utility.getSharedPreferences(getApplicationContext(), "datetimeFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        pieChart = findViewById(R.id.piechart);
        total_question = findViewById(R.id.total_question);
        correct = findViewById(R.id.correct);
        wrong = findViewById(R.id.wrong);
        notattempt = findViewById(R.id.notattempt);
        reset_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isConnectingToInternet(getApplicationContext())){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("quiz_id",quiz_id);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    resetQuiz(obj.toString());
                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }

            }
        });
        decorate();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));


        titleTV.setText(getApplicationContext().getString(R.string.quizresult));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        nodata_layout = (LinearLayout) findViewById(R.id.nodata_layout);
        loaddata();


    }

    private void decorate() {
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            params.put("quiz_id",quiz_id);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();
    }
    private void resetQuiz (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.resetquizUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        String success = object.getString("status");
                        String message = object.getString("msg");
                        if(success.equals("1")){
                            Toast.makeText(StudentOnlineQuizResult.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(StudentOnlineQuizResult.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentOnlineQuizResult.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineQuizResult.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.quizresultUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("result");
                        // Set the data and color to the pie chart
                        pieChart.addPieSlice(
                                new PieModel(
                                        "Correct Answer",
                                        Integer.parseInt(dataArray.getJSONObject(0).getString("correct_answer")),
                                        Color.parseColor("#66AA18")));
                        pieChart.addPieSlice(
                                new PieModel(
                                        "Wrong Answer",
                                        Integer.parseInt(dataArray.getJSONObject(0).getString("wrong_answer")),
                                        Color.parseColor("#EF5350")));
                        pieChart.addPieSlice(
                                new PieModel(
                                        "Not Attempted",
                                        Integer.parseInt(dataArray.getJSONObject(0).getString("not_answer")),
                                        Color.parseColor("#C8C4C4")));

                        // To animate the pie chart
                        pieChart.startAnimation();
                        correct.setText(dataArray.getJSONObject(0).getString("correct_answer"));
                        wrong.setText(dataArray.getJSONObject(0).getString("wrong_answer"));
                        notattempt.setText(dataArray.getJSONObject(0).getString("not_answer"));
                        total_question.setText(dataArray.getJSONObject(0).getString("total_question"));

                        JSONArray answerlistArray = obj.getJSONArray("answerlist");
                        questionlist.clear();
                        question_typelist.clear();
                        idlist.clear();
                        select_optionlist.clear();
                        correctlist.clear();
                        option_a.clear();
                        option_b.clear();
                        option_c.clear();
                        option_d.clear();
                        option_e.clear();

                            try {
                                if(answerlistArray.length() != 0) {
                                    for(int i = 0; i < answerlistArray.length(); i++) {
                                        idlist.add(answerlistArray.getJSONObject(i).getString("question_id"));
                                        questionlist.add(answerlistArray.getJSONObject(i).getString("question"));
                                        select_optionlist.add(answerlistArray.getJSONObject(i).getString("your_answer"));
                                        correctlist.add(answerlistArray.getJSONObject(i).getString("correct_answer"));
                                        option_a.add(answerlistArray.getJSONObject(i).getString("option_1"));
                                        option_b.add(answerlistArray.getJSONObject(i).getString("option_2"));
                                        option_c.add(answerlistArray.getJSONObject(i).getString("option_3"));
                                        option_d.add(answerlistArray.getJSONObject(i).getString("option_4"));
                                        option_e.add(answerlistArray.getJSONObject(i).getString("option_5"));
                                        answerlistArray.getJSONObject(i).put("","");

                                    }
                                }
                                adapter = new StudentOnlineQuizResultAdapter(StudentOnlineQuizResult.this,questionlist,subject_namelist,
                                        select_optionlist,correctlist,idlist,option_a,option_b,option_c,option_d,option_e,question_typelist,marklist,getmarklist,is_neg_marking_marks,neg_marks_list,remark_list);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                Toast.makeText(StudentOnlineQuizResult.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineQuizResult.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
