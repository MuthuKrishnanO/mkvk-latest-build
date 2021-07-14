package com.mkvk.smartschool.students;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
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
import com.mkvk.smartschool.BaseActivity;
import com.mkvk.smartschool.OpenPdf;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentSyllabusStatusAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentSyllabus extends BaseActivity {
    RecyclerView recyclerView;
    LinearLayout nodata_layout,main_layout;
    SwipeRefreshLayout pullToRefresh;
    ArrayList<String> lacture_videolist = new ArrayList<String>();
    ArrayList<String> lacture_youtube_urllist = new ArrayList<String>();
    String subjectid,sectionid,timefrom,timeto,date,time,subjects;
    public Map<String, String>  headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    TextView classes,subject,dates,lesson,topic,subtopic,generalobjective,teachingMethod,previousknowledge,comprehensive,syllabus_date;
    StudentSyllabusStatusAdapter adapter;
    String formatedDate = "";
    LinearLayout data,nodata,presentation;
    String youtube,attachment,lacture_video,pesentation_link;
    ImageView youtubeurl,download_attachment,download_video;
    long downloadID;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_syllabus, null, false);
        mDrawerLayout.addView(contentView, 0);
        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        classes=findViewById(R.id.classes);
        classes.setText(Utility.getSharedPreferences(this, Constants.classSection));
        subject=findViewById(R.id.subject);
        dates=findViewById(R.id.dates);
        presentation=findViewById(R.id.presentation);
        presentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(StudentSyllabus.this, pesentation_link, Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(StudentSyllabus.this);
                dialog.setContentView(R.layout.presentation_sheet);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
                final ProgressDialog progressDialog = new ProgressDialog(StudentSyllabus.this);
                progressDialog.setMessage("Loading Data...");
                progressDialog.setCancelable(false);
                TextView headerTV = dialog.findViewById(R.id.homework_bottomSheet_headerTV);
                ImageView closeBtn = dialog.findViewById(R.id.homework_bottomSheet_crossBtn);
                WebView detailsWebview = dialog.findViewById(R.id.homework_bottomSheet_webview);
                detailsWebview.getSettings().setJavaScriptEnabled(true);
                detailsWebview.getSettings().setBuiltInZoomControls(true);
                detailsWebview.getSettings().setLoadWithOverviewMode(true);
                detailsWebview.getSettings().setUseWideViewPort(true);
                detailsWebview.getSettings().setDefaultFontSize(100);
                detailsWebview.loadData(pesentation_link, "text/html; charset=utf-8", "UTF-8");
                headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(StudentSyllabus.this, Constants.secondaryColour)));
                headerTV.setText(getString(R.string.presentation));
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                detailsWebview.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        if (progress < 100) {
                            progressDialog.show();
                        }
                        if (progress == 100) {
                            progressDialog.dismiss();
                        }
                    } });
                dialog.show();
            }
        });

        youtubeurl=findViewById(R.id.youtubeurl);
       
        download_attachment=findViewById(R.id.download_attachment);
       
        download_video=findViewById(R.id.download_video);

        lesson=findViewById(R.id.lesson);
        topic=findViewById(R.id.topic);
        nodata=findViewById(R.id.nodata);

        data=findViewById(R.id.data);
        subtopic=findViewById(R.id.subtopic);
        generalobjective=findViewById(R.id.generalobjective);
        teachingMethod=findViewById(R.id.teachingMethod);
        previousknowledge=findViewById(R.id.previousknowledge);
        comprehensive=findViewById(R.id.comprehensive);

        subjectid = getIntent().getStringExtra("Subjectid");
        sectionid = getIntent().getStringExtra("Sectionid");
        timefrom = getIntent().getStringExtra("timefrom");
        timeto = getIntent().getStringExtra("timeto");
        date = getIntent().getStringExtra("Date");
        time = getIntent().getStringExtra("Time");
        subjects = getIntent().getStringExtra("Subject");
        subject.setText(subjects);



        titleTV.setText(getApplicationContext().getString(R.string.lessonplan));
       loaddata();
    }

    public  void  loaddata(){
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("subject_syllabus_id",subjectid);
            JSONObject object=new JSONObject(params);
            Log.e("params ", object.toString());
            System.out.println("subject_syllabus_id== "+object.toString());
            getDataFromApi(object.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();
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
                                .setSmallIcon(R.drawable.notificatin)
                                .setContentTitle(context.getApplicationContext().getString(R.string.app_name))
                                .setContentText("All Download completed");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
                context.unregisterReceiver(onDownloadComplete);
            }
        }
    };

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getsyllabusUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.print("Syllabus data result=="+result);
                        JSONObject obj = new JSONObject(result);
                        JSONObject dataArray = obj.getJSONObject("data");
                        System.out.print("Syllabus data result Length=="+dataArray.length());
                        if(dataArray.length() != 0) {
                           /* main_layout.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);*/

                                lesson.setText(dataArray.getString("lesson_name"));
                                topic.setText(dataArray.getString("topic_name"));
                                subtopic.setText(dataArray.getString("sub_topic"));
                                generalobjective.setText(dataArray.getString("general_objectives"));
                                teachingMethod.setText(dataArray.getString("teaching_method"));
                                previousknowledge.setText(dataArray.getString("previous_knowledge"));
                                comprehensive.setText(dataArray.getString("comprehensive_questions"));
                                pesentation_link=dataArray.getString("presentation");
                            dates.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,dataArray.getString("date"))+" "+dataArray.getString("time_from")+"-"+dataArray.getString("time_to"));
                            youtube=  dataArray.getString("lacture_youtube_url");
                            if(youtube.equals("")){
                                youtubeurl.setVisibility(View.GONE);
                            }else{
                                youtubeurl.setVisibility(View.VISIBLE);
                                youtubeurl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setPackage("com.google.android.youtube");
                                        startActivity(intent);
                                    }
                                });  
                            }
                            
                            attachment=  dataArray.getString("attachment");
                            if(attachment.equals("")){
                                download_attachment.setVisibility(View.GONE);
                            }else{
                                download_attachment.setVisibility(View.VISIBLE);
                                download_attachment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String urlStr = Utility.getSharedPreferences(StudentSyllabus.this, Constants.imagesUrl);
                                        urlStr += "uploads/syllabus_attachment/"+attachment;
                                        downloadID = Utility.beginDownload(StudentSyllabus.this, attachment, urlStr);
                                        Intent intent=new Intent(StudentSyllabus.this, OpenPdf.class);
                                        intent.putExtra("imageUrl",urlStr);
                                        startActivity(intent);
                                    }
                                });
                            }
                            
                            lacture_video=  dataArray.getString("lacture_video");
                            if(lacture_video.equals("")){
                                download_video.setVisibility(View.GONE);
                            }else{
                                download_video.setVisibility(View.VISIBLE);
                                download_video.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String urlStr = Utility.getSharedPreferences(StudentSyllabus.this, Constants.imagesUrl);
                                        urlStr += "uploads/syllabus_attachment/lacture_video/"+lacture_video;
                                        downloadID = Utility.beginDownload(StudentSyllabus.this, lacture_video, urlStr);
                                        Intent intent=new Intent(StudentSyllabus.this, OpenPdf.class);
                                        intent.putExtra("imageUrl",urlStr);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                          //  pullToRefresh.setVisibility(View.GONE);
                           nodata.setVisibility(View.VISIBLE);
                         //  main_layout.setVisibility(View.GONE);
                           data.setVisibility(View.GONE);
                            //Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentSyllabus.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSyllabus.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
