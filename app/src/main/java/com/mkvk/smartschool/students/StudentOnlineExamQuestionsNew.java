package com.mkvk.smartschool.students;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
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
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentQuestionsListAdapter;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalTime;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import static android.widget.Toast.makeText;

public class StudentOnlineExamQuestionsNew extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String TAG = "StudentOnlineExamQuestionsNew";
    String Online_exam_id,durationList,onlineexam_student_idlist,is_marks_displayList,is_neg_markingList;
    public ImageView backBtn;
    public String defaultDateFormat, currency;
    RecyclerView recyclerView;
    boolean doubleBackToExitPressedOnce = false;
    StudentQuestionsListAdapter adapter;
    List<String> finallist = new ArrayList<String>();
    TextView name,sno,marks,negative_marks,textview,buttonSelectImage;
    private Bitmap bitmap;
    RequestBody file_body;
    ProgressDialog progress;
    RadioButton option1,option2,option3,option4,option5,false_value,true_value;
    CheckBox moption1,moption2,moption3,moption4,moption5;
    LinearLayout previous,next,submit,option5_layout,moption5_layout;
    int position=1,hold;
    public static Boolean camera = false;
    public static Boolean gallery = false;
    private static final int CAMERA_REQUEST = 1888;
    boolean isKitKat = false;
    LinearLayout linear;
    public Map<String, String> params = new Hashtable<String, String>();
    JSONObject result_param=null;
    TextView questions,option_a_value,option_b_value,option_c_value,option_d_value,option_e_value;
    TextView moption_a_value,moption_b_value,moption_c_value,moption_d_value,moption_e_value;
    public Map<String,String> headers = new HashMap<String, String>();
    SwipeRefreshLayout pullToRefresh;
    public TextView titleTV,timer;
    protected FrameLayout mDrawerLayout;
    ArrayList<String> result_statusList = new ArrayList<String>();
    ArrayList<String> attempt_statusList = new ArrayList<String>();
    ArrayList<String> questionList = new ArrayList<String>();
    ArrayList<String> question_idList = new ArrayList<String>();
   String question_typeList;
    String total_descriptiveList;
    ArrayList<String> marksList = new ArrayList<String>();
    ArrayList<String> onlineexam_idList = new ArrayList<String>();
    ArrayList<String> opt_aList = new ArrayList<String>();
    ArrayList<String> opt_bList = new ArrayList<String>();
    ArrayList<String> opt_cList = new ArrayList<String>();
    ArrayList<String> opt_dList = new ArrayList<String>();
    ArrayList<String> opt_eList = new ArrayList<String>();
    ArrayList<String> correctlist = new ArrayList<String>();
    JSONArray answerlist = new JSONArray();
    RadioGroup radiogroup;
    private String filePath="";
    int TimeCounter=0;
    boolean isChecked=true;
    JSONArray dataArray=new JSONArray();
    JSONObject dataObject=new JSONObject();
    int I=60;
    ImageView profileImageview,imageView;
    private long mTimeLeftInMillis;
    JSONObject attachment=null;
    JSONArray dlist=new JSONArray();
    JSONArray filelist=new JSONArray();
    JSONArray ARRAYLIST = new JSONArray();
    String selected_answer;
    JSONObject jsonObject = null;
    JSONObject jsonObjectfile = null;
    JSONObject datanew = null;
    JSONObject newlist=null;
    String question_id;
    EditText descriptive;
    LinearLayout question_layout,nodata_layout,descriptive_layout,multiplechoice_layout,singlechoice_layout,truefalse_layout;
    String[] mimeTypes =
            {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "text/plain", "application/pdf", "application/zip","image/*"};
    private static final int PICK_IMAGE_REQUEST =1 ;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_wise_layout_new);
        Online_exam_id = getIntent().getExtras().getString("Online_Exam_Id");
        durationList = getIntent().getExtras().getString("durationList");
        onlineexam_student_idlist = getIntent().getExtras().getString("onlineexam_student_idlist");
        profileImageview = findViewById(R.id.patientProfile_profileImageview);
        name = findViewById(R.id.name);
        submit = findViewById(R.id.submit);
        descriptive = findViewById(R.id.descriptive);
        descriptive_layout = findViewById(R.id.descriptive_layout);
        multiplechoice_layout = findViewById(R.id.multiplechoice_layout);
        singlechoice_layout = findViewById(R.id.singlechoice_layout);
        truefalse_layout = findViewById(R.id.truefalse_layout);
        radiogroup = findViewById(R.id.radiogroup);
        sno = findViewById(R.id.sno);
        textview = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageView);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        marks = findViewById(R.id.marks);
        negative_marks = findViewById(R.id.negative);
        option5_layout = findViewById(R.id.option5_layout);
        moption5_layout = findViewById(R.id.moption5_layout);
        questions = findViewById(R.id.questions);
        moption_a_value = findViewById(R.id.moption_a_value);
        moption_b_value = findViewById(R.id.moption_b_value);
        moption_c_value = findViewById(R.id.moption_c_value);
        moption_d_value = findViewById(R.id.moption_d_value);
        moption_e_value = findViewById(R.id.moption_e_value);
        option_a_value = findViewById(R.id.option_a_value);
        option_b_value = findViewById(R.id.option_b_value);
        option_c_value = findViewById(R.id.option_c_value);
        option_d_value = findViewById(R.id.option_d_value);
        option_e_value = findViewById(R.id.option_e_value);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        option5 = findViewById(R.id.option5);
        true_value = findViewById(R.id.true_value);
        false_value = findViewById(R.id.false_value);
        moption1 = findViewById(R.id.moptiona);
        moption2 = findViewById(R.id.moptionb);
        moption3 = findViewById(R.id.moptionc);
        moption4 = findViewById(R.id.moptiond);
        moption5 = findViewById(R.id.moptione);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        timer = findViewById(R.id.timer);
        linear = findViewById(R.id.linear);
        question_layout = findViewById(R.id.question_layout);
        nodata_layout = findViewById(R.id.nodata_layout);
        timer.setText(String.valueOf(TimeCounter));
        String time =durationList;
        LocalTime localTime = LocalTime.parse(time);
        int millis = localTime.toSecondOfDay() * 1000;
        mTimeLeftInMillis = millis;

        new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timer.setText(hms);
            }
            public void onFinish() {
                dlist.put(jsonObject);
                System.out.println("dlist== " + dlist);
                Set<String> stationCodes=new HashSet<String>();
                JSONArray tempArray=new JSONArray();
                for(int i=0;i<dlist.length();i++){
                    try {
                        String stationCode = dlist.getJSONObject(i).getString("onlineexam_question_id");
                        System.out.println("stationCode== " + stationCode);
                        if(stationCodes.contains(stationCode)){
                            continue;
                        }
                        else{
                            stationCodes.add(stationCode);
                            tempArray.put(dlist.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dlist= tempArray; //assign temp to original
                System.out.println("tempArray== " + tempArray);
                result_param=new JSONObject();
                try {
                    result_param.put("onlineexam_student_id", onlineexam_student_idlist);
                    result_param.put("rows",dlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // JSONObject obj=new JSONObject(result_param);
                System.out.println("Result==" + dlist);
                if(Utility.isConnectingToInternet(getApplicationContext())){
                    try {
                        submitExam();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }.start();

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

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(StudentOnlineExamQuestionsNew.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(StudentOnlineExamQuestionsNew.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(StudentOnlineExamQuestionsNew.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    showFileChooser();
                }
            }
        });

        moption1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer = "opt_a";
                    ARRAYLIST.put(selected_answer);
                    String stringlist=ARRAYLIST.toString();

                    JSONArray jsonArray = new JSONArray();
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                        jsonObject.put("question_type", question_typeList);
                        jsonObject.put("onlineexam_question_id",question_id);
                        jsonObject.put("select_option", stringlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    System.out.println("JSONArray== " + jsonArray.toString());

                    try {
                        System.out.println("ARRAYLIST== " + ARRAYLIST);
                        newlist=new JSONObject();
                        newlist.put("question",sno.getText().toString());
                        newlist.put("selected_answer",stringlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("newlist==" + newlist.toString());

                }
                else {
                    /*Toast.makeText(StudentOnlineExamQuestionsNew.this, "unchecked", Toast.LENGTH_SHORT).show();
                    String chetimeslot = (String)moption1.getTag();
                    System.out.println("chetimeslot=="+ chetimeslot);*/
                }
            }
        });

        moption2.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer = "opt_b";
                    ARRAYLIST.put(selected_answer);
                    String stringlist=ARRAYLIST.toString();
                    JSONArray jsonArray = new JSONArray();
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                        jsonObject.put("question_type", question_typeList);
                        jsonObject.put("onlineexam_question_id",question_id);
                        jsonObject.put("select_option", stringlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    System.out.println("JSONArray== " + jsonArray.toString());

                    try {
                        System.out.println("ARRAYLIST== " + ARRAYLIST);
                        newlist=new JSONObject();
                        newlist.put("question",sno.getText().toString());
                        newlist.put("selected_answer",stringlist);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("newlist==" + newlist.toString());

                }
                else { }
            }
        });
        moption3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer = "opt_c";
                    ARRAYLIST.put(selected_answer);
                    String stringlist=ARRAYLIST.toString();
                    JSONArray jsonArray = new JSONArray();
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                        jsonObject.put("question_type", question_typeList);
                        jsonObject.put("onlineexam_question_id",question_id);
                        jsonObject.put("select_option", stringlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    System.out.println("JSONArray== " + jsonArray.toString());

                    try {
                        System.out.println("ARRAYLIST== " + ARRAYLIST);
                        newlist=new JSONObject();
                        newlist.put("question",sno.getText().toString());
                        newlist.put("selected_answer",stringlist);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("newlist==" + newlist.toString());

                }
                else { }
            }
        });
        moption4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer = "opt_d";
                    ARRAYLIST.put(selected_answer);
                    String stringlist=ARRAYLIST.toString();
                    JSONArray jsonArray = new JSONArray();
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                        jsonObject.put("question_type", question_typeList);
                        jsonObject.put("onlineexam_question_id",question_id);
                        jsonObject.put("select_option", stringlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    System.out.println("JSONArray== " + jsonArray.toString());

                    try {
                        System.out.println("ARRAYLIST== " + ARRAYLIST);
                        newlist=new JSONObject();
                        newlist.put("question",sno.getText().toString());
                        newlist.put("selected_answer",stringlist);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("newlist==" + newlist.toString());

                }
                else { }
            }
        });
        moption5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() ) {
                    selected_answer = "opt_e";
                    ARRAYLIST.put(selected_answer);
                    String stringlist=ARRAYLIST.toString();
                    JSONArray jsonArray = new JSONArray();
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                        jsonObject.put("question_type", question_typeList);
                        jsonObject.put("onlineexam_question_id",question_id);
                        jsonObject.put("select_option", stringlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    System.out.println("JSONArray== " + jsonArray.toString());

                    try {
                        System.out.println("ARRAYLIST== " + ARRAYLIST);
                        newlist=new JSONObject();
                        newlist.put("question",sno.getText().toString());
                        newlist.put("selected_answer",stringlist);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("newlist==" + newlist.toString());

                } else { }
            }
        });

        true_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "true";
                false_value.setChecked(false);

                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray== " + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);

            }
        });

        false_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "false";
                true_value.setChecked(false);

                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray== " + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "opt_a";
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);
                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray== " + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);

            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "opt_b";
                option1.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);
                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray==" + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "opt_c";
                option2.setChecked(false);
                option1.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);
                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray== " + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "opt_d";
                option2.setChecked(false);
                option3.setChecked(false);
                option1.setChecked(false);
                option5.setChecked(false);
                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray== " + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);
            }
        });

        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_answer = "opt_e";
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option1.setChecked(false);
                JSONArray jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                    jsonObject.put("question_type", question_typeList);
                    jsonObject.put("onlineexam_question_id",question_id);
                    jsonObject.put("select_option", selected_answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                System.out.println("JSONArray== " + jsonArray.toString());

                try {
                    newlist=new JSONObject();
                    newlist.put("question",sno.getText().toString());
                    newlist.put("selected_answer",selected_answer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("newlist==" + newlist.toString());
                answerlist.put(newlist);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    System.out.println("last dataArray Type=="+dataArray.getJSONObject(position-1).getString("question_type"));
                    if(dataArray.getJSONObject(position-1).getString("question_type").equals("descriptive")){
                        selected_answer = descriptive.getText().toString();
                        JSONArray jsonArray = new JSONArray();
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                            jsonObject.put("question_type", question_typeList);
                            jsonObject.put("onlineexam_question_id",question_id);
                            jsonObject.put("select_option", selected_answer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                        System.out.println("JSONArray== " + jsonArray.toString());
                        JSONArray jsonArrayfile = new JSONArray();
                        jsonObjectfile = new JSONObject();
                        try {
                            jsonObjectfile.put("attachment_para", "attachment_"+question_id);
                            jsonObjectfile.put("FilePath", filePath);
                            jsonObjectfile.put("FileBody",file_body);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArrayfile.put(jsonObjectfile);
                        System.out.println("JsonArrayfile== " + jsonArrayfile.toString());
                        if(!filePath.equals("")) {
                            filelist.put(jsonObjectfile);
                            System.out.println("filelist== " + filelist.toString());
                        }
                        attachment=new JSONObject();
                        try {
                            attachment.put("attachment",filelist);
                            System.out.println("attachment==" + attachment.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            newlist=new JSONObject();
                            newlist.put("question",sno.getText().toString());
                            newlist.put("selected_answer",selected_answer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("newlist==" + newlist.toString());

                    }else{}
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new AlertDialog.Builder(StudentOnlineExamQuestionsNew.this)
                        .setIcon(R.drawable.ic_access_time_black_24dp)
                        .setTitle("Submit")
                        .setMessage("Are you sure,you want to submit your exam?")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dlist.put(jsonObject);
                                System.out.println("dlist== " + dlist);

                                Set<String> stationCodes=new HashSet<String>();
                                JSONArray tempArray=new JSONArray();
                                for(int i=0;i<dlist.length();i++){
                                    try {
                                        String stationCode = dlist.getJSONObject(i).getString("onlineexam_question_id");
                                        System.out.println("stationCode== " + stationCode);
                                        if(stationCodes.contains(stationCode)){
                                            continue;
                                        }
                                        else{
                                            stationCodes.add(stationCode);
                                            tempArray.put(dlist.getJSONObject(i));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                dlist= tempArray; //assign temp to original
                                System.out.println("tempArray== " + tempArray);

                                result_param=new JSONObject();
                                try {
                                    result_param.put("onlineexam_student_id", onlineexam_student_idlist);
                                    result_param.put("rows",dlist);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Result==" + result_param.toString());
                                if(Utility.isConnectingToInternet(getApplicationContext())){
                                    try {
                                        submitExam();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                }
                               /* Intent intent=new Intent(StudentOnlineExamQuestionsNew.this,StudentOnlineExam.class);
                                startActivity(intent);
                                finish();*/
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

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radiogroup.getCheckedRadioButtonId() == -1) {
                } else {
                    dlist.put(jsonObject);
                }
                descriptive.setText("");
                System.out.println("dlist== " + dlist.toString());
                JSONObject data=new JSONObject();
                try {
                    data.put("onlineexam_student_id", onlineexam_student_idlist);
                    data.put("rows",dlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                        next.setEnabled(false);
                        next.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                    }else{
                        next.setEnabled(true);
                        next.setBackgroundColor(Color.parseColor("#B0DD38"));
                    }

                    if(position==dataArray.length()){
                        position--;
                    }else{
                    }

                    try {
                        questions.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("question"), Html.FROM_HTML_MODE_COMPACT));
                        question_idList.add(dataArray.getJSONObject(hold-1).getString("question_id"));
                        onlineexam_idList.add(dataArray.getJSONObject(hold-1).getString("onlineexam_id"));
                        question_typeList=dataArray.getJSONObject(hold-1).getString("question_type");
                        if(dataObject.getString("is_marks_display").equals("1")){
                            marks.setVisibility(View.VISIBLE);
                            marks.setText("(Marks:"+dataArray.getJSONObject(hold-1).getString("marks")+")");
                        }else{
                            marks.setVisibility(View.GONE);
                        }

                        if(dataObject.getString("is_neg_marking").equals("1")){
                            negative_marks.setVisibility(View.VISIBLE);
                            negative_marks.setText("(Negative Marks:"+dataArray.getJSONObject(hold-1).getString("neg_marks")+")");
                        }else{
                            negative_marks.setVisibility(View.GONE);
                        }

                        if(dataArray.getJSONObject(hold-1).getString("question_type").equals("singlechoice")){
                            singlechoice_layout.setVisibility(View.VISIBLE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.GONE);

                            option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                            option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                            option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                            option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));


                            if(dataArray.getJSONObject(hold-1).getString("opt_e").equals("")){
                                option5_layout.setVisibility(View.GONE);
                            }else{
                                option5_layout.setVisibility(View.VISIBLE);
                                option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }else if(dataArray.getJSONObject(hold-1).getString("question_type").equals("multichoice")){
                            singlechoice_layout.setVisibility(View.GONE);
                            multiplechoice_layout.setVisibility(View.VISIBLE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.GONE);

                            moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                            moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                            moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                            moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));


                            if(dataArray.getJSONObject(hold-1).getString("opt_e").equals("")){
                                moption5_layout.setVisibility(View.GONE);
                            }else{
                                moption5_layout.setVisibility(View.VISIBLE);
                                moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }else if(dataArray.getJSONObject(hold-1).getString("question_type").equals("descriptive")){
                            singlechoice_layout.setVisibility(View.GONE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.VISIBLE);
                            truefalse_layout.setVisibility(View.GONE);
                        }else if(dataArray.getJSONObject(hold-1).getString("question_type").equals("true_false")){
                            singlechoice_layout.setVisibility(View.GONE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.VISIBLE);

                        }else{
                            singlechoice_layout.setVisibility(View.VISIBLE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.GONE);

                            option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                            option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                            option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                            option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                            if(dataArray.getJSONObject(hold-1).getString("opt_e").equals("")){
                                option5_layout.setVisibility(View.GONE);
                            }else{
                                option5_layout.setVisibility(View.VISIBLE);
                                option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(hold-1).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }
                        correctlist.add(dataArray.getJSONObject(hold-1).getString("correct"));
                        question_id=dataArray.getJSONObject(hold-1).getString("id");

                        position=hold;
                        hold--;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("answerlist=="+answerlist);
                    for (int i = 0; i < answerlist.length(); i++) {
                        try {
                            JSONObject  currObject = answerlist.getJSONObject(i);
                            String question = currObject.getString("question");
                            Integer serial= Integer.valueOf(sno.getText().toString());

                            if(Integer.valueOf(question) == (serial)) {
                                JSONObject prev_answer = currObject;
                                String myoption = prev_answer.getString("selected_answer");
                                System.out.println("Question no== "+question+ " selected_answer== "+myoption+"hold== "+hold);

                                if(dataArray.getJSONObject(hold).getString("question_type").equals("descriptive")){
                                   // Toast.makeText(StudentOnlineExamQuestionsNew.this, "descriptive", Toast.LENGTH_SHORT).show();
                                    descriptive.setText(myoption);
                                }else if(dataArray.getJSONObject(hold).getString("question_type").equals("multichoice")){
                                    //Toast.makeText(StudentOnlineExamQuestionsNew.this, "multichoice", Toast.LENGTH_SHORT).show();
                                    if(myoption.contains("opt_a")){
                                        moption1.setChecked(true);
                                    }else{
                                        moption1.setChecked(false);
                                    }
                                    if(myoption.contains("opt_b")){
                                        moption2.setChecked(true);
                                    }else{
                                        moption2.setChecked(false);
                                    }
                                    if(myoption.contains("opt_c")){
                                        moption3.setChecked(true);
                                    }else{
                                        moption3.setChecked(false);
                                    }if(myoption.contains("opt_d")){
                                        moption4.setChecked(true);
                                    }else{
                                        moption4.setChecked(false);
                                    }if(myoption.contains("opt_e")){
                                        moption5.setChecked(true);
                                    }else{
                                        moption5.setChecked(false);
                                    }
                                }else if(dataArray.getJSONObject(hold).getString("question_type").equals("true_false")){
                                //    Toast.makeText(StudentOnlineExamQuestionsNew.this, "true_false", Toast.LENGTH_SHORT).show();
                                    if(myoption.equals("true")){
                                        true_value.setChecked(true);
                                        false_value.setChecked(false);
                                    }else if(myoption.equals("false")){
                                        false_value.setChecked(true);
                                        true_value.setChecked(false);
                                    }
                                }else if(dataArray.getJSONObject(hold).getString("question_type").equals("singlechoice")){
                                   // Toast.makeText(StudentOnlineExamQuestionsNew.this, "singlechoice", Toast.LENGTH_SHORT).show();
                                    if(myoption.equals("opt_a")){
                                        option1.setChecked(true);
                                        option2.setChecked(false);
                                        option3.setChecked(false);
                                        option4.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_b")){
                                        option2.setChecked(true);
                                        option1.setChecked(false);
                                        option3.setChecked(false);
                                        option4.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_c")){
                                        option3.setChecked(true);
                                        option2.setChecked(false);
                                        option1.setChecked(false);
                                        option4.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_d")){
                                        option4.setChecked(true);
                                        option2.setChecked(false);
                                        option3.setChecked(false);
                                        option1.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_e")){
                                        option5.setChecked(true);
                                        option2.setChecked(false);
                                        option3.setChecked(false);
                                        option4.setChecked(false);
                                        option1.setChecked(false);
                                    }
                                }else{
                                    Toast.makeText(StudentOnlineExamQuestionsNew.this, "not in any", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(dataArray.getJSONObject(position-1).getString("question_type").equals("descriptive")){
                        selected_answer = descriptive.getText().toString();
                        JSONArray jsonArray = new JSONArray();
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("onlineexam_student_id", onlineexam_student_idlist);
                            jsonObject.put("question_type", question_typeList);
                            jsonObject.put("onlineexam_question_id",question_id);
                            jsonObject.put("select_option", selected_answer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                        System.out.println("JSONArray== " + jsonArray.toString());

                        JSONArray jsonArrayfile = new JSONArray();
                        jsonObjectfile = new JSONObject();
                        try {
                            jsonObjectfile.put("attachment_para", "attachment_"+question_id);
                            jsonObjectfile.put("FilePath", filePath);
                            jsonObjectfile.put("FileBody",file_body);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArrayfile.put(jsonObjectfile);
                        System.out.println("JsonArrayfile== " + jsonArrayfile.toString());
                        if(!filePath.equals("")) {
                            filelist.put(jsonObjectfile);
                            System.out.println("filelist== " + filelist.toString());
                        }
                         attachment=new JSONObject();
                        try {
                            attachment.put("attachment",filelist);
                            System.out.println("attachment==" + attachment.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // JSONObject obj=new JSONObject(result_param);

                        try {
                            newlist=new JSONObject();
                            newlist.put("question",sno.getText().toString());
                            newlist.put("selected_answer",selected_answer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("newlist==" + newlist.toString());

                    }else{ }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Toast.makeText(StudentOnlineExamQuestionsNew.this, ""+sno.getText().toString(), Toast.LENGTH_SHORT).show();
                ARRAYLIST = new JSONArray();;
                dlist.put(jsonObject);
                System.out.println("dlist== " + dlist.toString());

                option1.setChecked(false);
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option5.setChecked(false);

                true_value.setChecked(false);
                false_value.setChecked(false);

                moption1.setChecked(false);
                moption2.setChecked(false);
                moption3.setChecked(false);
                moption4.setChecked(false);
                moption5.setChecked(false);

                descriptive.setText("");
                filePath="";
                textview.setText("Select File to upload");
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.uploadfile));



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
                        next.setEnabled(false);
                        next.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                    }else{
                        next.setEnabled(true);
                        next.setBackgroundColor(Color.parseColor("#B0DD38"));
                    }

                    try {
                        questions.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("question"), Html.FROM_HTML_MODE_COMPACT));
                        question_idList.add(dataArray.getJSONObject(position).getString("question_id"));
                        if(dataObject.getString("is_marks_display").equals("1")){
                            marks.setVisibility(View.VISIBLE);
                            marks.setText("(Marks:"+dataArray.getJSONObject(position).getString("marks")+")");
                        }else{
                            marks.setVisibility(View.GONE);
                        }

                        if(dataObject.getString("is_neg_marking").equals("1")){
                            negative_marks.setVisibility(View.VISIBLE);
                            negative_marks.setText("(Negative Marks:"+dataArray.getJSONObject(position).getString("neg_marks")+")");
                        }else{
                            negative_marks.setVisibility(View.GONE);
                        }

                        onlineexam_idList.add(dataArray.getJSONObject(position).getString("onlineexam_id"));
                        question_typeList=dataArray.getJSONObject(position).getString("question_type");
                        if(dataArray.getJSONObject(position).getString("question_type").equals("singlechoice")){
                            singlechoice_layout.setVisibility(View.VISIBLE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.GONE);

                            option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                            option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                            option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                            option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                            if(dataArray.getJSONObject(position).getString("opt_e").equals("")){
                                option5_layout.setVisibility(View.GONE);
                            }else{
                                option5_layout.setVisibility(View.VISIBLE);
                                option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }else if(dataArray.getJSONObject(position).getString("question_type").equals("multichoice")){
                            singlechoice_layout.setVisibility(View.GONE);
                            multiplechoice_layout.setVisibility(View.VISIBLE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.GONE);

                            moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                            moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                            moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                            moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                            if(dataArray.getJSONObject(position).getString("opt_e").equals("")){
                                moption5_layout.setVisibility(View.GONE);
                            }else{
                                moption5_layout.setVisibility(View.VISIBLE);
                                moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }else if(dataArray.getJSONObject(position).getString("question_type").equals("descriptive")){
                            singlechoice_layout.setVisibility(View.GONE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.VISIBLE);
                            truefalse_layout.setVisibility(View.GONE);

                        }else if(dataArray.getJSONObject(position).getString("question_type").equals("true_false")){
                            singlechoice_layout.setVisibility(View.GONE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.VISIBLE);

                        }else{
                            singlechoice_layout.setVisibility(View.VISIBLE);
                            multiplechoice_layout.setVisibility(View.GONE);
                            descriptive_layout.setVisibility(View.GONE);
                            truefalse_layout.setVisibility(View.GONE);

                            option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                            option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                            option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                            option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                            if(dataArray.getJSONObject(position).getString("opt_e").equals("")){
                                option5_layout.setVisibility(View.GONE);
                            }else{
                                option5_layout.setVisibility(View.VISIBLE);
                                option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(position).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                            }
                        }
                        correctlist.add(dataArray.getJSONObject(position).getString("correct"));
                        question_id=dataArray.getJSONObject(position).getString("id");
                        // Toast.makeText(StudentOnlineExamQuestionsNew.this, ""+question_id, Toast.LENGTH_SHORT).show();
                        hold=position;
                        position++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if(dataArray.getJSONObject(position-1).getString("question_type").equals("multichoice")){
                            answerlist.put(newlist);
                            System.out.println("answerlist=="+answerlist);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < answerlist.length(); i++) {
                        try {
                            JSONObject  currObject = answerlist.getJSONObject(i);
                            String question = currObject.getString("question");
                            Integer serial= Integer.valueOf(sno.getText().toString());

                            if(Integer.valueOf(question) == (serial)) {
                                JSONObject prev_answer = currObject;
                                String myoption = prev_answer.getString("selected_answer");

                                if(dataArray.getJSONObject(position-1).getString("question_type").equals("descriptive")){
                                    //Toast.makeText(StudentOnlineExamQuestionsNew.this, "descriptive", Toast.LENGTH_SHORT).show();
                                    descriptive.setText(myoption);
                                }else if(dataArray.getJSONObject(position-1).getString("question_type").equals("multichoice")){
                                  //  Toast.makeText(StudentOnlineExamQuestionsNew.this, "multichoice", Toast.LENGTH_SHORT).show();
                                    if(myoption.contains("opt_a")){
                                        moption1.setChecked(true);
                                    }else{
                                        moption1.setChecked(false);
                                    }if(myoption.contains("opt_b")){
                                        moption2.setChecked(true);
                                    }else{
                                        moption2.setChecked(false);
                                    }if(myoption.contains("opt_c")){
                                        moption3.setChecked(true);
                                    }else{
                                        moption3.setChecked(false);
                                    }if(myoption.contains("opt_d")){
                                        moption4.setChecked(true);
                                    }else{
                                        moption4.setChecked(false);
                                    }if(myoption.contains("opt_e")){
                                        moption5.setChecked(true);
                                    }else{
                                        moption5.setChecked(false);
                                    }
                                }else if(dataArray.getJSONObject(position-1).getString("question_type").equals("true_false")){
                                    if(myoption.equals("true")){
                                        true_value.setChecked(true);
                                        false_value.setChecked(false);
                                    }else if(myoption.equals("false")){
                                        false_value.setChecked(true);
                                        true_value.setChecked(false);
                                    }
                                }else if(dataArray.getJSONObject(position-1).getString("question_type").equals("singlechoice")){
                                    if(myoption.equals("opt_a")){
                                        option1.setChecked(true);
                                        option2.setChecked(false);
                                        option3.setChecked(false);
                                        option4.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_b")){
                                        option2.setChecked(true);
                                        option1.setChecked(false);
                                        option3.setChecked(false);
                                        option4.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_c")){
                                        option3.setChecked(true);
                                        option2.setChecked(false);
                                        option1.setChecked(false);
                                        option4.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_d")){
                                        option4.setChecked(true);
                                        option2.setChecked(false);
                                        option3.setChecked(false);
                                        option1.setChecked(false);
                                        option5.setChecked(false);
                                    }else if(myoption.equals("opt_e")){
                                        option5.setChecked(true);
                                        option2.setChecked(false);
                                        option3.setChecked(false);
                                        option4.setChecked(false);
                                        option1.setChecked(false);
                                    }
                                }else{
                                    Toast.makeText(StudentOnlineExamQuestionsNew.this, "not in any", Toast.LENGTH_SHORT).show();
                                }
                            }else{ }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void showFileChooser() {
        final Dialog dialog = new Dialog(StudentOnlineExamQuestionsNew.this);
        dialog.setContentView(R.layout.choose_file);
        dialog.setCanceledOnTouchOutside(false);
        RelativeLayout headerLay = (RelativeLayout) dialog.findViewById(R.id.addTask_dialog_header);
        final LinearLayout takephoto = (LinearLayout) dialog.findViewById(R.id.takephoto);
        final LinearLayout choosegallery = (LinearLayout) dialog.findViewById(R.id.gallery);
        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.addTask_dialog_crossIcon);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camerapic();
                camera = true;
                gallery = false;
                dialog.dismiss();
            }
        });
        choosegallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengallery();
                gallery = true;
                camera = false;
                dialog.dismiss();
            }
        });

        headerLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        dialog.show();
    }
    void camerapic() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    private void opengallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            }
            isKitKat = true;
            startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_IMAGE_REQUEST);
        } else {
            isKitKat = false;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    @SuppressLint("LongLogTag")
    @TargetApi(19)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            boolean isImageFromGoogleDrive = false;

            Uri uri = data.getData();

            if (isKitKat && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        Pattern DIR_SEPORATOR = Pattern.compile("/");
                        Set<String> rv = new HashSet<>();
                        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                            if (TextUtils.isEmpty(rawExternalStorage)) {
                                rv.add("/storage/sdcard0");
                            } else {
                                rv.add(rawExternalStorage);
                            }
                        } else {
                            String rawUserId;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                rawUserId = "";
                            } else {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                } catch (NumberFormatException ignored) {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                            }
                            if (TextUtils.isEmpty(rawUserId)) {
                                rv.add(rawEmulatedStorageTarget);
                            } else {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                            }
                        }
                        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                            Collections.addAll(rv, rawSecondaryStorages);
                        }
                        String[] temp = rv.toArray(new String[rv.size()]);
                        for (int i = 0; i < temp.length; i++) {
                            File tempf = new File(temp[i] + "/" + split[1]);
                            if (tempf.exists()) {
                                filePath = temp[i] + "/" + split[1];
                            }
                        }
                    }
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {column};
                    try {
                        cursor = getApplicationContext().getContentResolver().query(contentUri, projection, null, null,
                                null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            filePath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {column};

                    try {
                        cursor = getApplicationContext().getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            filePath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                    isImageFromGoogleDrive = true;
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Cursor cursor = null;
                String column = "_data";
                String[] projection = {column};

                try {
                    cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        filePath = cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                filePath = uri.getPath();
            }
            try {
                Log.d(TAG, "Real Path 1 : " + filePath);
                System.out.println("Real Path 1" + filePath);
                textview.setText("File Selected");
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                System.out.println("bitmap image==" + bitmap);
                String file_name=filePath.substring(filePath.lastIndexOf("/")+1);
                String filenameArray[] = file_name.split("\\.");
                String extension = filenameArray[filenameArray.length-1];
                System.out.println("extension"+extension);
                if(extension.equals("jpg")||extension.equals("png")||extension.equals("jpeg")){
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);
                }else{
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selected_file));
                }

                File f = new File(filePath);
                String mimeType = URLConnection.guessContentTypeFromName(f.getName());
                file_body = RequestBody.create(MediaType.parse(mimeType), f);
                System.out.println("file_bodypathasd" + file_body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == CAMERA_REQUEST) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                progress = new ProgressDialog(StudentOnlineExamQuestionsNew.this);
                progress.setTitle("uploading");
                progress.setMessage("Please Wait....");
                progress.show();
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                filePath = getRealPathFromURI(tempUri);
                System.out.println("pathasd" + filePath);
                File f = new File(filePath);
                String mimeType = URLConnection.guessContentTypeFromName(f.getName());
                file_body = RequestBody.create(MediaType.parse(mimeType), f);
                System.out.println("file_bodypathasd" + file_body);
                progress.dismiss();
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

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
        if(Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            params.put("online_exam_id", Online_exam_id);
            JSONObject obj = new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void submitExam() throws IOException {
        final String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.saveOnlineExamUrl;
        OkHttpClient client=new OkHttpClient();

        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("onlineexam_student_id", onlineexam_student_idlist)
                .addFormDataPart("rows",String.valueOf(dlist));
        try {
            JSONObject attach_obj = new JSONObject(attachment.toString());
            JSONArray attachdataArray = attach_obj.getJSONArray("attachment");

            for (int i = 0; i < attachdataArray.length(); i++) {
                String file_name=attachdataArray.getJSONObject(i).getString("FilePath").substring(attachdataArray.getJSONObject(i).getString("FilePath").lastIndexOf("/")+1);
                File f = new File(attachdataArray.getJSONObject(i).getString("FilePath"));
                String mimeType = URLConnection.guessContentTypeFromName(f.getName());
                file_body = RequestBody.create(MediaType.parse(mimeType), f);
                System.out.println("attachment_para==" +attachdataArray.getJSONObject(i).getString("attachment_para"));
                System.out.println("FileName==" +file_name);
                System.out.println("FileBody==" +file_body);
                multipartBody.addFormDataPart(attachdataArray.getJSONObject(i).getString("attachment_para"), file_name,file_body);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = multipartBody.build();

            okhttp3.Request request=new okhttp3.Request.Builder()
                    .url(url)
                    .header("Client-Service", Constants.clientService)
                    .header("Auth-Key", Constants.authKey)
                    .header("User-ID",Utility.getSharedPreferences(getApplicationContext(), "userId"))
                    .header("Authorization",Utility.getSharedPreferences(getApplicationContext(), "accessToken"))
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    makeText(StudentOnlineExamQuestionsNew.this, R.string.apiErrorMsg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    ResponseBody body = response.body();
                    if(body != null) {
                        try {
                            String jsonData = response.body().string();
                            System.out.println("response====="+jsonData);
                            try {
                                final JSONObject Jobject = new JSONObject(jsonData);
                                String Jarray = Jobject.getString("status");

                                if(Jarray.equals("1")){
                                    runOnUiThread(new Runnable(){
                                        public void run() {
                                            Toast.makeText(StudentOnlineExamQuestionsNew.this, getApplicationContext().getString(R.string.submit_success), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable(){
                                        public void run() {
                                            try {
                                                JSONObject error = Jobject.getJSONObject("error");
                                                Toast.makeText(StudentOnlineExamQuestionsNew.this, error.getString("file"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });

    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getOnlineExamQuestionUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Exam Questions", result);
                        JSONObject obj = new JSONObject(result);
                        dataObject = obj.getJSONObject("exam");
                        question_idList.clear();
                        onlineexam_idList.clear();
                        name.setText(dataObject.getString("exam"));
                        total_descriptiveList=dataObject.getString("descriptive");

                        JSONObject statusObject = dataObject.getJSONObject("status");
                        result_statusList.add(statusObject.getString("exam_result_publish_status"));
                        attempt_statusList.add(statusObject.getString("exam_attempt_status"));

                        dataArray = dataObject.getJSONArray("questions");
                        System.out.println("dataArray.length()=="+dataArray.length());
                        if(dataArray.length()>1) {
                            question_layout.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            sno.setText("1");
                            questions.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("question"), Html.FROM_HTML_MODE_COMPACT));
                            //questions.loadDataWithBaseURL(dataArray.getJSONObject(0).getString("question"), "text/html; charset=utf-8", "UTF-8");
                            question_idList.add(dataArray.getJSONObject(0).getString("id"));
                            question_typeList=dataArray.getJSONObject(0).getString("question_type");

                            onlineexam_idList.add(dataArray.getJSONObject(0).getString("onlineexam_id"));

                            if(dataObject.getString("is_marks_display").equals("1")){
                                marks.setVisibility(View.VISIBLE);
                                marks.setText("(Marks:"+dataArray.getJSONObject(0).getString("marks")+")");
                            }else{
                                marks.setVisibility(View.GONE);
                            }

                            if(dataObject.getString("is_neg_marking").equals("1")){
                                negative_marks.setVisibility(View.VISIBLE);
                                negative_marks.setText("(Negative Marks:"+dataArray.getJSONObject(0).getString("neg_marks")+")");
                            }else{
                                negative_marks.setVisibility(View.GONE);
                            }


                            if(dataArray.getJSONObject(0).getString("question_type").equals("singlechoice")){
                                singlechoice_layout.setVisibility(View.VISIBLE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.GONE);
                                truefalse_layout.setVisibility(View.GONE);
                                option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                                option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                                option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                                option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                                if(dataArray.getJSONObject(0).getString("opt_e").equals("")){
                                    option5_layout.setVisibility(View.GONE);
                                }else{
                                    option5_layout.setVisibility(View.VISIBLE);
                                    option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                                }
                            }else if(dataArray.getJSONObject(0).getString("question_type").equals("multichoice")){
                                singlechoice_layout.setVisibility(View.GONE);
                                multiplechoice_layout.setVisibility(View.VISIBLE);
                                descriptive_layout.setVisibility(View.GONE);
                                truefalse_layout.setVisibility(View.GONE);

                                moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                                moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                                moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                                moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                                if(dataArray.getJSONObject(0).getString("opt_e").equals("")){
                                    moption5_layout.setVisibility(View.GONE);
                                }else{
                                    moption5_layout.setVisibility(View.VISIBLE);
                                    moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                                }
                            }else if(dataArray.getJSONObject(0).getString("question_type").equals("descriptive")){
                                singlechoice_layout.setVisibility(View.GONE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.VISIBLE);
                                truefalse_layout.setVisibility(View.GONE);
                            }else if(dataArray.getJSONObject(0).getString("question_type").equals("true_false")){
                                singlechoice_layout.setVisibility(View.GONE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.GONE);
                                truefalse_layout.setVisibility(View.VISIBLE);
                            }else{
                                singlechoice_layout.setVisibility(View.VISIBLE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.GONE);

                                option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                                option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                                option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                                option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                                if(dataArray.getJSONObject(0).getString("opt_e").equals("")){
                                    option5_layout.setVisibility(View.GONE);
                                }else{
                                    option5_layout.setVisibility(View.VISIBLE);
                                    option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                                }
                            }

                            correctlist.add(dataArray.getJSONObject(0).getString("correct"));
                            question_id=dataArray.getJSONObject(0).getString("id");
                            //   Toast.makeText(StudentOnlineExamQuestionsNew.this, ""+question_id, Toast.LENGTH_SHORT).show();
                        } else if(dataArray.length()==1){
                            question_layout.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            next.setEnabled(false);
                            next.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                            previous.setBackgroundColor(Color.parseColor("#D8D6D3D3"));
                            previous.setEnabled(false);
                            sno.setText("1");
                            questions.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("question"), Html.FROM_HTML_MODE_COMPACT));
                            question_idList.add(dataArray.getJSONObject(0).getString("id"));
                            onlineexam_idList.add(dataArray.getJSONObject(0).getString("onlineexam_id"));
                            question_typeList=dataArray.getJSONObject(0).getString("question_type");
                            if(dataObject.getString("is_marks_display").equals("1")){
                                marks.setVisibility(View.VISIBLE);
                                marks.setText("(Marks:"+dataArray.getJSONObject(0).getString("marks")+")");
                            }else{
                                marks.setVisibility(View.GONE);
                            }

                            if(dataObject.getString("is_neg_marking").equals("1")){
                                negative_marks.setVisibility(View.VISIBLE);
                                negative_marks.setText("(Negative Marks:"+dataArray.getJSONObject(0).getString("neg_marks")+")");
                            }else{
                                negative_marks.setVisibility(View.GONE);
                            }


                            if(dataArray.getJSONObject(0).getString("question_type").equals("singlechoice")){
                                singlechoice_layout.setVisibility(View.VISIBLE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.GONE);
                                option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                                option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                                option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                                option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                                if(dataArray.getJSONObject(0).getString("opt_e").equals("")){
                                    option5_layout.setVisibility(View.GONE);
                                }else{
                                    option5_layout.setVisibility(View.VISIBLE);
                                    option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));

                                }
                            }else if(dataArray.getJSONObject(0).getString("question_type").equals("multichoice")){
                                singlechoice_layout.setVisibility(View.GONE);
                                multiplechoice_layout.setVisibility(View.VISIBLE);
                                descriptive_layout.setVisibility(View.GONE);

                                moption_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                                moption_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                                moption_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                                moption_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                                if(dataArray.getJSONObject(0).getString("opt_e").equals("")){
                                    moption5_layout.setVisibility(View.GONE);
                                }else{
                                    moption5_layout.setVisibility(View.VISIBLE);
                                    moption_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                                }
                            }else if(dataArray.getJSONObject(0).getString("question_type").equals("descriptive")){
                                singlechoice_layout.setVisibility(View.GONE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.VISIBLE);
                            }else if(dataArray.getJSONObject(0).getString("question_type").equals("true_false")){
                                singlechoice_layout.setVisibility(View.GONE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.GONE);
                                truefalse_layout.setVisibility(View.VISIBLE);
                            }else{
                                singlechoice_layout.setVisibility(View.VISIBLE);
                                multiplechoice_layout.setVisibility(View.GONE);
                                descriptive_layout.setVisibility(View.GONE);
                                option_a_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_a"), Html.FROM_HTML_MODE_COMPACT));
                                option_b_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_b"), Html.FROM_HTML_MODE_COMPACT));
                                option_c_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_c"), Html.FROM_HTML_MODE_COMPACT));
                                option_d_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_d"), Html.FROM_HTML_MODE_COMPACT));

                                if(dataArray.getJSONObject(0).getString("opt_e").equals("")){
                                    option5_layout.setVisibility(View.GONE);
                                }else{
                                    option5_layout.setVisibility(View.VISIBLE);
                                    option_e_value.setText(Html.fromHtml(dataArray.getJSONObject(0).getString("opt_e"), Html.FROM_HTML_MODE_COMPACT));
                                }
                            }

                            correctlist.add(dataArray.getJSONObject(0).getString("correct"));
                            question_id=dataArray.getJSONObject(0).getString("id");
                            //Toast.makeText(StudentOnlineExamQuestionsNew.this, ""+question_id, Toast.LENGTH_SHORT).show();
                        }else {
                            nodata_layout.setVisibility(View.VISIBLE);
                            question_layout.setVisibility(View.GONE);
                            next.setEnabled(false);
                            submit.setEnabled(false);
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
                Toast.makeText(StudentOnlineExamQuestionsNew.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineExamQuestionsNew.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
