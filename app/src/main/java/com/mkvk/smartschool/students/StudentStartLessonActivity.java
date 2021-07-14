package com.mkvk.smartschool.students;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.mkvk.smartschool.adapters.StartLessonAdapter;
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

public class StudentStartLessonActivity extends BaseActivity {

    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public String coursethumbnail, currency,course_id,course_name;
    ArrayList<String> section_titleList = new ArrayList<String>();
    ArrayList<String> section_idList = new ArrayList<String>();
    ArrayList<String> lesson_countList = new ArrayList<String>();
    ArrayList<String> lessonArray = new ArrayList<String>();
    ArrayList<String> resultArray = new ArrayList<String>();
    ArrayList<String> quizArray = new ArrayList<String>();
    StartLessonAdapter adapter;
    Boolean isLoggegIn;
    SwipeRefreshLayout pullToRefresh;
    TextView courseDetails_courseNameTV;
    ListView lessonList;
    long downloadID;
    RelativeLayout webview_layout;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_start_lesson, null, false);
        mDrawerLayout.addView(contentView, 0);
       // searchView.setVisibility(View.GONE);

        course_performance.setVisibility(View.VISIBLE);
        course_id = getIntent().getStringExtra("CourseId");
        course_name = getIntent().getStringExtra("course_name");
        lessonList = (ListView) findViewById(R.id.Lesson_listView);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new ChromeClient());
        webView.loadUrl("");
        courseDetails_courseNameTV = (TextView) findViewById(R.id.courseDetails_courseNameTV);

        courseDetails_courseNameTV.setText(course_name);

        adapter = new StartLessonAdapter(StudentStartLessonActivity.this,
                section_titleList,section_idList,resultArray,lesson_countList);
        lessonList.setAdapter(adapter);
        loaddata();

        course_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentStartLessonActivity.this,StudentCoursePerformance.class);
                intent.putExtra("CourseId",course_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);

            }
        });
    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("course_id", course_id);
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject objt=new JSONObject(params);
            Log.e("params ", objt.toString());
            getDataFromApi(objt.toString());

            adapter = new StartLessonAdapter(StudentStartLessonActivity.this,
                    section_titleList,section_idList,resultArray,lesson_countList);
            lessonList.setAdapter(adapter);
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

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.coursecurriculumUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();

                    try{
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("sectionList");
                        section_titleList.clear();
                        section_idList.clear();
                        lesson_countList.clear();
                        resultArray.clear();

                        if (dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                section_titleList.add(dataArray.getJSONObject(i).getString("section_title"));
                                section_idList.add(dataArray.getJSONObject(i).getString("id"));
                                lesson_countList.add(dataArray.getJSONObject(i).getString("title"));
                                String resultarray=dataArray.getJSONObject(i).getJSONArray("lesson_quiz").toString();
                                resultArray.add(resultarray);
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
                Toast.makeText(StudentStartLessonActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                try {
                    isLoggegIn = Utility.getSharedPreferencesBoolean(StudentStartLessonActivity.this, Constants.isLoggegIn);
                } catch (NullPointerException NPE) {
                    isLoggegIn = false;
                }
                if(isLoggegIn){
                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", Constants.contentType);
                    headers.put("User-ID", Utility.getSharedPreferences(StudentStartLessonActivity.this, "userId"));
                    headers.put("Authorization", Utility.getSharedPreferences(StudentStartLessonActivity.this, "accessToken"));
                }else {
                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", Constants.contentType);
                }

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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentStartLessonActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.notification_logo)
                                .setContentTitle(context.getApplicationContext().getString(R.string.app_name))
                                .setContentText("All Download completed");

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());

            }
        }
    };
    public class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }
    }
}
