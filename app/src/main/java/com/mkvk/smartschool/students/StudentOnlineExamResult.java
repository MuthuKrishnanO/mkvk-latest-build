package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.mkvk.smartschool.adapters.StudentOnlineExamResultAdapter;
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

public class StudentOnlineExamResult extends AppCompatActivity {
    public ImageView backBtn;
    public String defaultDatetimeFormat, currency;
    RecyclerView recyclerView;
    LinearLayout nodata_layout;
    StudentOnlineExamResultAdapter adapter;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    SwipeRefreshLayout pullToRefresh;
    public TextView titleTV;
    TextView exam,fromdate,attempt,todate,duration,description,percent,total_quest,correct,wrong,notattempt,score,exam_marks,rank,scored_marks,neg_marks,total_descriptive;
    LinearLayout neg_marks_layout;
    protected FrameLayout mDrawerLayout, actionBar;
    ArrayList<String> select_optionlist = new ArrayList<String>();
    ArrayList<String> correctlist = new ArrayList<String>();
    ArrayList<String> option_a = new ArrayList<String>();
    ArrayList<String> option_b = new ArrayList<String>();
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> questionlist = new ArrayList<String>();
    ArrayList<String> subject_namelist = new ArrayList<String>();
    ArrayList<String> is_rank_generated = new ArrayList<String>();
    String is_neg_marking_marks;
    ArrayList<String> option_c = new ArrayList<String>();
    ArrayList<String> option_d = new ArrayList<String>();
    ArrayList<String> option_e = new ArrayList<String>();
    ArrayList<String> neg_marks_list = new ArrayList<String>();
    ArrayList<String> remark_list = new ArrayList<String>();
    ArrayList<String> question_typelist = new ArrayList<String>();
    ArrayList<String> marklist = new ArrayList<String>();
    ArrayList<String> getmarklist = new ArrayList<String>();
    String OnlineExam_student_Id,exam_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_online_exam_result);
        backBtn = findViewById(R.id.actionBar_backBtn);
        mDrawerLayout = findViewById(R.id.container);
        actionBar = findViewById(R.id.actionBarSecondary);
        titleTV = findViewById(R.id.actionBar_title);
        OnlineExam_student_Id = getIntent().getExtras().getString("OnlineExam_students_Id");
        exam_id = getIntent().getExtras().getString("exams_id");
        exam = findViewById(R.id.exam);
        fromdate = findViewById(R.id.fromdate);
        attempt = findViewById(R.id.attempt);
        todate = findViewById(R.id.todate);
        exam_marks = findViewById(R.id.exam_marks);
        scored_marks = findViewById(R.id.scored_marks);
        neg_marks = findViewById(R.id.neg_marks);
        neg_marks_layout = findViewById(R.id.neg_marks_layout);
        duration = findViewById(R.id.duration);
        description = findViewById(R.id.description);
        total_descriptive = findViewById(R.id.total_descriptive);
        percent = findViewById(R.id.percent);
        total_quest = findViewById(R.id.total_quest);
        correct = findViewById(R.id.correct);
        wrong = findViewById(R.id.wrong);
        rank = findViewById(R.id.rank);
        notattempt = findViewById(R.id.notattempt);
        score = findViewById(R.id.score);
        defaultDatetimeFormat = Utility.getSharedPreferences(getApplicationContext(), "datetimeFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);

        decorate();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        titleTV.setText(getApplicationContext().getString(R.string.onlineexamresult));
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
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("onlineexam_student_id",OnlineExam_student_Id);
            params.put("exam_id",exam_id);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        } else {
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

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getOnlineExamResultUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONObject dataArray = obj.getJSONObject("result");
                        JSONObject examArray = dataArray.getJSONObject("exam");
                        questionlist.clear();
                        question_typelist.clear();
                        idlist.clear();
                        subject_namelist.clear();
                        select_optionlist.clear();
                        correctlist.clear();
                        marklist.clear();

                        if(dataArray.length() != 0) {

                            try {
                                exam.setText(examArray.getString("exam"));
                                duration.setText(examArray.getString("duration"));
                                attempt .setText(examArray.getString("attempt"));
                                fromdate.setText(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDatetimeFormat, examArray.getString("exam_from")));
                                todate.setText(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDatetimeFormat, examArray.getString("exam_to")));
                                percent.setText(examArray.getString("passing_percentage"));
                                total_quest.setText(examArray.getString("total_question"));
                                correct.setText(examArray.getString("correct_ans"));
                                total_descriptive.setText(examArray.getString("total_descriptive"));
                                description.setText(Html.fromHtml(examArray.getString("description"), Html.FROM_HTML_MODE_COMPACT));
                                wrong.setText(examArray.getString("wrong_ans"));
                                notattempt.setText(examArray.getString("not_attempted"));
                                exam_marks.setText(examArray.getString("exam_total_marks"));
                                scored_marks.setText(examArray.getString("exam_total_scored"));

                                if(examArray.getString("rank").equals("0")){
                                    rank.setText("Awaited");
                                }else{
                                    rank.setText(examArray.getString("rank"));
                                }
                                is_neg_marking_marks=examArray.getString("is_neg_marking");
                                if(is_neg_marking_marks.equals("1")){
                                    neg_marks_layout.setVisibility(View.VISIBLE);
                                    neg_marks.setText(examArray.getString("exam_total_neg_marks"));
                                }else{
                                    neg_marks_layout.setVisibility(View.GONE);
                                }

                                String scores = String.format("%.2f", Float.valueOf(examArray.getString("score")));

                                score.setText(String.valueOf(scores));
                                JSONArray resultArray = dataArray.getJSONArray("question_result");
                                if(resultArray.length() != 0) {
                                    for(int i = 0; i < resultArray.length(); i++) {
                                        idlist.add(resultArray.getJSONObject(i).getString("id"));
                                        questionlist.add(resultArray.getJSONObject(i).getString("question"));
                                        subject_namelist.add(resultArray.getJSONObject(i).getString("subject_name"));
                                        select_optionlist.add(resultArray.getJSONObject(i).getString("select_option"));
                                        question_typelist.add(resultArray.getJSONObject(i).getString("question_type"));
                                        getmarklist.add(resultArray.getJSONObject(i).getString("score_marks")+"/"+resultArray.getJSONObject(i).getString("marks"));
                                        marklist.add(resultArray.getJSONObject(i).getString("marks"));
                                        correctlist.add(resultArray.getJSONObject(i).getString("correct"));
                                        option_a.add(resultArray.getJSONObject(i).getString("opt_a"));
                                        option_b.add(resultArray.getJSONObject(i).getString("opt_b"));
                                        option_c.add(resultArray.getJSONObject(i).getString("opt_c"));
                                        option_d.add(resultArray.getJSONObject(i).getString("opt_d"));
                                        option_e.add(resultArray.getJSONObject(i).getString("opt_e"));
                                        neg_marks_list.add(resultArray.getJSONObject(i).getString("neg_marks"));
                                        remark_list.add(resultArray.getJSONObject(i).getString("remark"));

                                    }
                                }
                                adapter = new StudentOnlineExamResultAdapter(StudentOnlineExamResult.this,questionlist,subject_namelist,
                                        select_optionlist,correctlist,idlist,option_a,option_b,option_c,option_d,option_e,question_typelist,marklist,getmarklist,is_neg_marking_marks,neg_marks_list,remark_list);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            nodata_layout.setVisibility(View.VISIBLE);
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
                Toast.makeText(StudentOnlineExamResult.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineExamResult.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
