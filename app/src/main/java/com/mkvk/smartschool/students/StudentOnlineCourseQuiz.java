package com.mkvk.smartschool.students;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentQuestionsListAdapter;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.widget.Toast.makeText;

public class StudentOnlineCourseQuiz extends AppCompatActivity {
    String Online_exam_id,durationList,onlineexam_student_idlist,is_marks_displayList,is_neg_markingList;
    public ImageView backBtn;
    public String defaultDateFormat, currency;
    RecyclerView recyclerView;
    boolean doubleBackToExitPressedOnce = false;
    StudentQuestionsListAdapter adapter;
    List<String> finallist = new ArrayList<String>();
    TextView name,sno,marks,next_TV,savenext_TV;
    RadioButton option1,option2,option3,option4,option5,false_value,true_value;
    CheckBox moption1,moption2,moption3,moption4,moption5;
    LinearLayout previous,next,submit,option5_layout,moption3_layout,moption4_layout,moption5_layout;
    int position=1,hold;
    LinearLayout linear;
    public Map<String, String> params = new Hashtable<String, String>();
    JSONObject result_param=null;
    TextView questions;
    TextView moption_a_value,moption_b_value,moption_c_value,moption_d_value,moption_e_value;
    public Map<String,String> headers = new HashMap<String, String>();
    SwipeRefreshLayout pullToRefresh;
    public TextView titleTV,timer;
    protected FrameLayout mDrawerLayout;
    ArrayList<String> result_statusList = new ArrayList<String>();
    ArrayList<String> attempt_statusList = new ArrayList<String>();
    ArrayList<String> questionList = new ArrayList<String>();
    ArrayList<String> question_idList = new ArrayList<String>();
    ArrayList<String> question_typeList = new ArrayList<String>();
    ArrayList<String> marksList = new ArrayList<String>();
    ArrayList<String> onlineexam_idList = new ArrayList<String>();
    ArrayList<String> opt_aList = new ArrayList<String>();
    ArrayList<String> opt_bList = new ArrayList<String>();
    ArrayList<String> opt_cList = new ArrayList<String>();
    ArrayList<String> opt_dList = new ArrayList<String>();
    ArrayList<String> opt_eList = new ArrayList<String>();
    ArrayList<String> correctlist = new ArrayList<String>();
    ArrayList<String> answerlist = new ArrayList<String>();
  //  JSONArray answerlist = new JSONArray();
    RadioGroup radiogroup;
    int TimeCounter=0;
    boolean isChecked=true;
    JSONArray dataArray=new JSONArray();
    JSONArray dataArray1=new JSONArray();
    JSONObject dataObject=new JSONObject();
    int I=60;
    ImageView profileImageview;
    private long mTimeLeftInMillis;
    JSONArray dlist=new JSONArray();
    JSONArray ARRAYLIST = new JSONArray();
    //ArrayList<String> ARRAYLIST = new ArrayList<String>();
    String selected_answer;
    String selected_answer1="",selected_answer2="",selected_answer3="",selected_answer4="",selected_answer5="";
    JSONObject jsonObject = null;
    JSONObject datanew = null;
    JSONObject newlist=null;
    String question_id,quiz_id,quiz_name;
    EditText descriptive;
    LinearLayout question_layout,nodata_layout,multiplechoice_layout;
    CoordinatorLayout coordinatorLayout;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        quiz_id = getIntent().getExtras().getString("quiz_id");
        quiz_name = getIntent().getExtras().getString("quiz_name");
        Online_exam_id = getIntent().getExtras().getString("Online_Exam_Id");
        durationList = getIntent().getExtras().getString("durationList");
        onlineexam_student_idlist = getIntent().getExtras().getString("onlineexam_student_idlist");
        profileImageview = findViewById(R.id.patientProfile_profileImageview);
        name = findViewById(R.id.name);
        name.setText(quiz_name);
       // submit = findViewById(R.id.submit);

        multiplechoice_layout = findViewById(R.id.multiplechoice_layout);
        radiogroup = findViewById(R.id.radiogroup);
        sno = findViewById(R.id.sno);
        marks = findViewById(R.id.marks);
        moption3_layout = findViewById(R.id.moption3_layout);
        moption4_layout = findViewById(R.id.moption4_layout);
        moption5_layout = findViewById(R.id.moption5_layout);

        questions = findViewById(R.id.questions);
        moption_a_value = findViewById(R.id.moption_a_value);
        moption_b_value = findViewById(R.id.moption_b_value);
        moption_c_value = findViewById(R.id.moption_c_value);
        moption_d_value = findViewById(R.id.moption_d_value);
        moption_e_value = findViewById(R.id.moption_e_value);


        moption1 = findViewById(R.id.moptiona);
        moption2 = findViewById(R.id.moptionb);
        moption3 = findViewById(R.id.moptionc);
        moption4 = findViewById(R.id.moptiond);
        moption5 = findViewById(R.id.moptione);

        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        next_TV = findViewById(R.id.next_TV);
        savenext_TV = findViewById(R.id.savenext_TV);

        linear = findViewById(R.id.linear);
        question_layout = findViewById(R.id.question_layout);
        nodata_layout = findViewById(R.id.nodata_layout);

        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        decorate();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));

        loaddata();

        if(sno.getText().toString().equals("1")){
            previous.setEnabled(false);
        }else{
            previous.setEnabled(true);
        }

        moption1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer1 = "option_1";
                }
                else {
                    selected_answer1="";
                }
            }
        });

        moption2.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer2 = "option_2";
                }
                else {
                    selected_answer2="";
                }
            }
        });
        moption3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer3 = "option_3";
                }
                else {
                    selected_answer3="";
                }
            }
        });
        moption4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer4 = "option_4";
                }
                else {
                    selected_answer4="";
                }
            }
        });
        moption5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer5 = "option_5";
                } else {
                    selected_answer5="";
                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("dataArray Size=="+dataArray.length());
                System.out.println("question Position=="+position+"  question hold=="+hold+"  selected answer=="+selected_answer);
                if(dataArray.length() != 0) {
                    if(hold >= 1){
                        sno.setText(""+hold);
                    }else{ }
                    if(hold==1){
                        previous.setEnabled(false);
                        next.setBackgroundColor(Color.parseColor("#B0DD38"));
                        previous.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                        next.setEnabled(true);
                    }else{
                        previous.setBackgroundColor(Color.parseColor("#B0DD38"));
                        previous.setEnabled(true);
                    }
                    if(dataArray.length()<=(hold)){
                        next_TV.setVisibility(View.VISIBLE);
                        savenext_TV.setVisibility(View.GONE);

                    }else{
                        next_TV.setVisibility(View.GONE);
                        savenext_TV.setVisibility(View.VISIBLE);
                    }

                    if(position==dataArray.length()){
                        position--;
                    }else{
                    }
                    if(Utility.isConnectingToInternet(getApplicationContext())) {
                        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                        params.put("quiz_id", quiz_id);
                        JSONObject obj = new JSONObject(params);
                        Log.e("params ", obj.toString());
                        refreshDataFromApi(obj.toString());
                    }else{
                        makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isConnectingToInternet(getApplicationContext())){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("quiz_id",quiz_id);
                    params.put("question_id",question_id);
                    params.put("answer_1",selected_answer1);
                    params.put("answer_2",selected_answer2);
                    params.put("answer_3",selected_answer3);
                    params.put("answer_4",selected_answer4);
                    params.put("answer_5",selected_answer5);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params submit answer ", obj.toString());
                    saveanswers(obj.toString());
                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }

                moption1.setChecked(false);
                moption2.setChecked(false);
                moption3.setChecked(false);
                moption4.setChecked(false);
                moption5.setChecked(false);

                System.out.println("dataArray Size=="+dataArray.length());
                System.out.println("question Position=="+position+"  question hold=="+hold);
                if(dataArray.length() != 0) {
                    if(dataArray.length()>=(position+1)){
                        sno.setText(""+(position+1));
                    }else{
                    }
                    if(position>0){
                        previous.setEnabled(true);
                        previous.setBackgroundColor(Color.parseColor("#B0DD38"));

                    }else{
                        previous.setEnabled(false);
                        previous.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                    }

                    if(dataArray.length()<=(position+1)){
                        next_TV.setVisibility(View.VISIBLE);
                        savenext_TV.setVisibility(View.GONE);
                        next_TV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new AlertDialog.Builder(StudentOnlineCourseQuiz.this)
                                        .setIcon(R.drawable.ic_access_time_black_24dp)
                                        .setTitle("Submit")
                                        .setMessage("Are you sure,you want to submit your exam?")
                                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if(Utility.isConnectingToInternet(getApplicationContext())){
                                                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                                                    params.put("quiz_id",quiz_id);
                                                    params.put("question_id",question_id);
                                                    params.put("answer_1",selected_answer1);
                                                    params.put("answer_2",selected_answer2);
                                                    params.put("answer_3",selected_answer3);
                                                    params.put("answer_4",selected_answer4);
                                                    params.put("answer_5",selected_answer5);
                                                    JSONObject obj=new JSONObject(params);
                                                    Log.e("params submit answer ", obj.toString());
                                                    submitExam(obj.toString());

                                                }else{
                                                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                                }

                                                finish();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        });
                    }else{
                        next_TV.setVisibility(View.GONE);
                        savenext_TV.setVisibility(View.VISIBLE);
                    }

                    try {
                        questions.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("question"), Html.FROM_HTML_MODE_COMPACT));

                            multiplechoice_layout.setVisibility(View.VISIBLE);
                        moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("option_1"), Html.FROM_HTML_MODE_COMPACT));
                        moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("option_2"), Html.FROM_HTML_MODE_COMPACT));

                        if(dataArray.getJSONObject(position).getString("option_3").equals("")){
                            moption3_layout.setVisibility(View.GONE);
                        }else{
                            moption3_layout.setVisibility(View.VISIBLE);
                            moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("option_3"), Html.FROM_HTML_MODE_COMPACT));

                        }

                        if(dataArray.getJSONObject(position).getString("option_4").equals("")){
                            moption4_layout.setVisibility(View.GONE);
                        }else{
                            moption4_layout.setVisibility(View.VISIBLE);
                            moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("option_4"), Html.FROM_HTML_MODE_COMPACT));

                        }

                        if(dataArray.getJSONObject(position).getString("option_5").equals("")){
                            moption5_layout.setVisibility(View.GONE);
                        }else{
                            moption5_layout.setVisibility(View.VISIBLE);
                            moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("option_5"), Html.FROM_HTML_MODE_COMPACT));
                        }

                        correctlist.add(dataArray.getJSONObject(position).getString("correct_answer"));
                        String answerlist=dataArray.getJSONObject(position).getString("studentanswer");
                        question_id=dataArray.getJSONObject(position).getString("id");
                        System.out.println("answerlist=="+answerlist);

                        if(answerlist.contains("option_1")){
                            moption1.setChecked(true);
                        }else{
                            moption1.setChecked(false);
                        }if(answerlist.contains("option_2")){
                            moption2.setChecked(true);
                        }else{
                            moption2.setChecked(false);
                        }if(answerlist.contains("option_3")){
                            moption3.setChecked(true);
                        }else{
                            moption3.setChecked(false);
                        }if(answerlist.contains("option_4")){
                            moption4.setChecked(true);
                        }else{
                            moption4.setChecked(false);
                        }if(answerlist.contains("option_5")){
                            moption5.setChecked(true);
                        }else{
                            moption5.setChecked(false);
                        }

                        hold=position;
                        position++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.no_animation,  R.anim.down_from_top);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    private void decorate() {
        String appLogo = Utility.getSharedPreferences(this, Constants.appLogo)+"?"+new Random().nextInt(11);
        Picasso.with(getApplicationContext()).load(appLogo).fit().centerInside().placeholder(null).into(profileImageview);
        // Picasso.with(getApplicationContext()).load(Utility.getSharedPreferences(this, "userImage")).placeholder(R.drawable.placeholder_user).into(profileImageview);
        linear.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
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

    private void saveanswers (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        if(pd != null && pd.isShowing()){
            pd.show();}

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.saveanswerUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Exam Questions", result);
                        JSONObject obj = new JSONObject(result);
                        /*if (obj.getString("status").equals("1")) {
                            Toast.makeText(StudentOnlineCourseQuiz.this, "Successfully Submittted", Toast.LENGTH_SHORT).show();
                        }*/

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
                Toast.makeText(StudentOnlineCourseQuiz.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineCourseQuiz.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

    private void submitExam (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        if(pd != null && pd.isShowing()){
            pd.show();}

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.submitquizUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Exam Questions", result);
                        JSONObject obj = new JSONObject(result);
                        if (obj.getString("status").equals("1")) {
                            finish();
                            Intent intent=new Intent(getApplicationContext(),StudentOnlineQuizResult.class);
                            intent.putExtra("quiz_id", quiz_id);
                            intent.putExtra("quiz_name", quiz_name);
                            startActivity(intent);
                            Toast.makeText(StudentOnlineCourseQuiz.this, "Successfully Submittted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentOnlineCourseQuiz.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineCourseQuiz.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getquestionbyquizidUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Exam Questions", result);
                        JSONObject obj = new JSONObject(result);

                       // name.setText(dataObject.getString("exam"));
                        dataArray = obj.getJSONArray("questionlist");
                        System.out.println("dataArray.length()=="+dataArray.length());
                        if(dataArray.length()>1) {
                            question_layout.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            sno.setText("1");
                            questions.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("question"), Html.FROM_HTML_MODE_COMPACT));

                            question_idList.add(dataArray.getJSONObject(0).getString("id"));
                                multiplechoice_layout.setVisibility(View.VISIBLE);
                                moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_1"), Html.FROM_HTML_MODE_COMPACT));
                                moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_2"), Html.FROM_HTML_MODE_COMPACT));


                            if(dataArray.getJSONObject(0).getString("option_3").equals("")){
                                moption3_layout.setVisibility(View.GONE);
                            }else{
                                moption3_layout.setVisibility(View.VISIBLE);
                                moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_3"), Html.FROM_HTML_MODE_COMPACT));


                            }

                            if(dataArray.getJSONObject(0).getString("option_4").equals("")){
                                moption4_layout.setVisibility(View.GONE);
                            }else{
                                moption4_layout.setVisibility(View.VISIBLE);
                                moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_4"), Html.FROM_HTML_MODE_COMPACT));

                            }

                            if(dataArray.getJSONObject(0).getString("option_5").equals("")){
                                    moption5_layout.setVisibility(View.GONE);
                                }else{
                                    moption5_layout.setVisibility(View.VISIBLE);
                                    moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_5"), Html.FROM_HTML_MODE_COMPACT));

                                }


                            correctlist.add(dataArray.getJSONObject(0).getString("correct_answer"));
                            question_id=dataArray.getJSONObject(0).getString("id");
                            String answerlist=dataArray.getJSONObject(0).getString("studentanswer");
                            System.out.println("answerlist=="+answerlist);
                            if(answerlist.contains("option_1")){
                                moption1.setChecked(true);
                            }else{
                                moption1.setChecked(false);
                            }if(answerlist.contains("option_2")){
                                moption2.setChecked(true);
                            }else{
                                moption2.setChecked(false);
                            }if(answerlist.contains("option_3")){
                                moption3.setChecked(true);
                            }else{
                                moption3.setChecked(false);
                            }if(answerlist.contains("option_4")){
                                moption4.setChecked(true);
                            }else{
                                moption4.setChecked(false);
                            }if(answerlist.contains("option_5")){
                                moption5.setChecked(true);
                            }else{
                                moption5.setChecked(false);
                            }

                            //   Toast.makeText(StudentOnlineExamQuestionsNew.this, ""+question_id, Toast.LENGTH_SHORT).show();
                        } else if(dataArray.length()==1){
                            question_layout.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            next.setEnabled(true);
                            next_TV.setVisibility(View.VISIBLE);
                            savenext_TV.setVisibility(View.GONE);
                            next_TV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new AlertDialog.Builder(StudentOnlineCourseQuiz.this)
                                            .setIcon(R.drawable.ic_access_time_black_24dp)
                                            .setTitle("Submit")
                                            .setMessage("Are you sure,you want to submit your exam?")
                                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    if(Utility.isConnectingToInternet(getApplicationContext())){
                                                        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                                                        params.put("quiz_id",quiz_id);
                                                        params.put("question_id",question_id);
                                                        params.put("answer_1",selected_answer1);
                                                        params.put("answer_2",selected_answer2);
                                                        params.put("answer_3",selected_answer3);
                                                        params.put("answer_4",selected_answer4);
                                                        params.put("answer_5",selected_answer5);
                                                        JSONObject obj=new JSONObject(params);
                                                        Log.e("params submit answer ", obj.toString());
                                                        submitExam(obj.toString());

                                                    }else{
                                                        makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                                    }

                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            });
                            next.setBackgroundColor(Color.parseColor("#B0DD38"));
                            previous.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                            previous.setEnabled(false);
                            sno.setText("1");
                            questions.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("question"), Html.FROM_HTML_MODE_COMPACT));

                            question_idList.add(dataArray.getJSONObject(0).getString("id"));


                                multiplechoice_layout.setVisibility(View.VISIBLE);
                            moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_1"), Html.FROM_HTML_MODE_COMPACT));
                            moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_2"), Html.FROM_HTML_MODE_COMPACT));

                            if(dataArray.getJSONObject(0).getString("option_3").equals("")){
                                moption3_layout.setVisibility(View.GONE);
                            }else{
                                moption3_layout.setVisibility(View.VISIBLE);
                                moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_3"), Html.FROM_HTML_MODE_COMPACT));

                            }

                            if(dataArray.getJSONObject(0).getString("option_4").equals("")){
                                moption4_layout.setVisibility(View.GONE);
                            }else{
                                moption4_layout.setVisibility(View.VISIBLE);
                                moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_4"), Html.FROM_HTML_MODE_COMPACT));
                            }

                            if(dataArray.getJSONObject(0).getString("option_5").equals("")){
                                    moption5_layout.setVisibility(View.GONE);
                                }else{
                                    moption5_layout.setVisibility(View.VISIBLE);
                                    moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("option_5"), Html.FROM_HTML_MODE_COMPACT));

                                }

                            correctlist.add(dataArray.getJSONObject(0).getString("correct_answer"));
                            question_id=dataArray.getJSONObject(0).getString("id");
                            String answerlist=dataArray.getJSONObject(0).getString("studentanswer");
                            System.out.println("answerlist=="+answerlist);
                            if(answerlist.contains("option_1")){
                                moption1.setChecked(true);
                            }else{
                                moption1.setChecked(false);
                            }if(answerlist.contains("option_2")){
                                moption2.setChecked(true);
                            }else{
                                moption2.setChecked(false);
                            }if(answerlist.contains("option_3")){
                                moption3.setChecked(true);
                            }else{
                                moption3.setChecked(false);
                            }if(answerlist.contains("option_4")){
                                moption4.setChecked(true);
                            }else{
                                moption4.setChecked(false);
                            }if(answerlist.contains("option_5")){
                                moption5.setChecked(true);
                            }else{
                                moption5.setChecked(false);
                            }

                        }else {
                            nodata_layout.setVisibility(View.VISIBLE);
                            question_layout.setVisibility(View.GONE);
                            next.setEnabled(false);
                            next.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                            next_TV.setVisibility(View.GONE);
                            previous.setEnabled(false);
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
                Toast.makeText(StudentOnlineCourseQuiz.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineCourseQuiz.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

    private void refreshDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getquestionbyquizidUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();

                    try {
                        JSONObject obj = new JSONObject(result);
                        dataArray1 = obj.getJSONArray("questionlist");
                        questions.setText(Html.fromHtml(dataArray1.getJSONObject(hold - 1).getString("question"), Html.FROM_HTML_MODE_COMPACT));
                        multiplechoice_layout.setVisibility(View.VISIBLE);
                        moption_a_value.setText(Html.fromHtml(dataArray1.getJSONObject(hold - 1).getString("option_1"), Html.FROM_HTML_MODE_COMPACT));
                        moption_b_value.setText(Html.fromHtml(dataArray1.getJSONObject(hold - 1).getString("option_2"), Html.FROM_HTML_MODE_COMPACT));

                        if (dataArray1.getJSONObject(hold - 1).getString("option_3").equals("")) {
                            moption3_layout.setVisibility(View.GONE);
                        } else {
                            moption3_layout.setVisibility(View.VISIBLE);
                            moption_c_value.setText(Html.fromHtml(dataArray1.getJSONObject(hold - 1).getString("option_3"), Html.FROM_HTML_MODE_COMPACT));
                        }

                        if (dataArray1.getJSONObject(hold - 1).getString("option_4").equals("")) {
                            moption4_layout.setVisibility(View.GONE);
                        } else {
                            moption4_layout.setVisibility(View.VISIBLE);
                            moption_d_value.setText(Html.fromHtml(dataArray1.getJSONObject(hold - 1).getString("option_4"), Html.FROM_HTML_MODE_COMPACT));
                        }


                        if (dataArray1.getJSONObject(hold - 1).getString("option_5").equals("")) {
                            moption5_layout.setVisibility(View.GONE);
                        } else {
                            moption5_layout.setVisibility(View.VISIBLE);
                            moption_e_value.setText(Html.fromHtml(dataArray1.getJSONObject(hold - 1).getString("option_5"), Html.FROM_HTML_MODE_COMPACT));
                        }

                        correctlist.add(dataArray1.getJSONObject(hold - 1).getString("correct_answer"));
                        String answerlistprev = dataArray1.getJSONObject(hold - 1).getString("studentanswer");

                        System.out.println("answerlistprev==" + answerlistprev);
                        if (answerlistprev.contains("option_1")) {
                            moption1.setChecked(true);
                        } else {
                            moption1.setChecked(false);
                        }
                        if (answerlistprev.contains("option_2")) {
                            moption2.setChecked(true);
                        } else {
                            moption2.setChecked(false);
                        }
                        if (answerlistprev.contains("option_3")) {
                            moption3.setChecked(true);
                        } else {
                            moption3.setChecked(false);
                        }
                        if (answerlistprev.contains("option_4")) {
                            moption4.setChecked(true);
                        } else {
                            moption4.setChecked(false);
                        }
                        if (answerlistprev.contains("option_5")) {
                            moption5.setChecked(true);
                        } else {
                            moption5.setChecked(false);
                        }

                        question_id = dataArray1.getJSONObject(hold - 1).getString("id");

                        position = hold;
                        hold--;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineCourseQuiz.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
