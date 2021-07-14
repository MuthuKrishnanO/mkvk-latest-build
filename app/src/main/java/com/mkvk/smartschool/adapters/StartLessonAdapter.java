package com.mkvk.smartschool.adapters;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mkvk.smartschool.OpenPdf;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.StudentOnlineCourseQuiz;
import com.mkvk.smartschool.students.StudentOnlineQuizResult;
import com.mkvk.smartschool.students.StudentStartLessonActivity;
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

public class StartLessonAdapter extends BaseAdapter {

    private StudentStartLessonActivity context;
    private ArrayList<String> section_titleList;
    private ArrayList<String> section_idList;
    private ArrayList<String> lesson_countList;
    ArrayList<String> lessonArray;
    public String defaultDateFormat;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    long downloadID;
    RelativeLayout webview_layout;
    WebView webView;
    String url;

    public StartLessonAdapter(StudentStartLessonActivity studentTransportRoutes,
                              ArrayList<String> section_titleList,ArrayList<String> section_idList, ArrayList<String> lessonArray, ArrayList<String> lesson_countList) {
        this.context = studentTransportRoutes;
        this.section_titleList = section_titleList;
        this.section_idList = section_idList;
        this.lessonArray = lessonArray;
        this.lesson_countList = lesson_countList;
    }

    @Override
    public int getCount() {
        return section_titleList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.adapter_start_section, viewGroup, false);

        LinearLayout nameHeader = view.findViewById(R.id.course_sectionnameHeader);
        TableLayout vehicleTable = view.findViewById(R.id.course_sectionTable);
        TextView routeNameTV = (TextView) view.findViewById(R.id.course_sectionNameTV);
        TextView statusTV = (TextView) view.findViewById(R.id.course_section);
        routeNameTV.setTag(position);

        routeNameTV.setText(section_titleList.get(position));
        statusTV.setText(context.getApplicationContext().getString(R.string.section)+" "+(position+1)+": ");
        webview_layout = context.findViewById(R.id.webview_layout);
        webView=context.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new ChromeClient());
        nameHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        Log.e("DATA==", lessonArray.get(position));

        try {
            final JSONArray dataArray = new JSONArray(lessonArray.get(position));
            System.out.println("dataArray=="+dataArray);
            Log.e("DDDATA==", String.valueOf(dataArray.length()));
            if (String.valueOf(dataArray.length()).equals("0")) {
                vehicleTable.setVisibility(View.GONE);
            } else {
                vehicleTable.setVisibility(View.VISIBLE);

                for (int i = 0; i < dataArray.length(); i++) {
                    final TableRow tr = (TableRow) context.getLayoutInflater().inflate(R.layout.adapter_start_lesson, null);

                    final TextView lessonTV, duration, topic_count;
                    LinearLayout viewBtn;
                    CheckBox CheckBox;
                    final ImageView play_icon,summary;

                    viewBtn = tr.findViewById(R.id.course_lesson_detailsBtn);
                   lessonTV = (TextView) tr.findViewById(R.id.course_lessonTV);
                    duration = (TextView) tr.findViewById(R.id.duration);
                    summary = (ImageView) tr.findViewById(R.id.summary);
                    CheckBox = (android.widget.CheckBox) tr.findViewById(R.id.CheckBox);
                    play_icon = tr.findViewById(R.id.play_icon);
                    final String type = dataArray.getJSONObject(i).getString("type");
                    final String lesson_type = dataArray.getJSONObject(i).getString("lesson_type");
                    final String quiz_name = dataArray.getJSONObject(i).getString("quiz_title");
                    final String quiz_id = dataArray.getJSONObject(i).getString("quiz_id");
                    final String lesson_id = dataArray.getJSONObject(i).getString("lesson_id");
                    final String quizstatus = dataArray.getJSONObject(i).getString("quiz_status");
                    final String video = dataArray.getJSONObject(i).getString("video_url");
                    final String videoid = dataArray.getJSONObject(i).getString("video_id");
                    final String video_provider = dataArray.getJSONObject(i).getString("video_provider");
                    final String section_id =dataArray.getJSONObject(i).getString("course_section_id");
                    final String progress = dataArray.getJSONObject(i).getString("progress");
                    final String attachment = dataArray.getJSONObject(i).getString("attachment");
                    final String lessonsummary = dataArray.getJSONObject(i).getString("summary");
                    if(progress.equals("1")){
                        CheckBox.setChecked(true);
                    }else{
                        CheckBox.setChecked(false);
                    }

                    if(type.equals("lesson")){
                        lessonTV.setText(dataArray.getJSONObject(i).getString("lesson_title"));
                        CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean checkedStatus) {
                                if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                                    params.put("student_id", Utility.getSharedPreferences(context.getApplicationContext(), Constants.studentId));
                                    params.put("lesson_quiz_id", lesson_id);
                                    params.put("section_id", section_idList.get(position));
                                    params.put("lesson_quiz_type","1");
                                    JSONObject objt=new JSONObject(params);
                                    Log.e("params ", objt.toString());
                                    changeStatusApi(objt.toString());
                                } else {
                                    makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        if(lesson_type.equals("video")){
                            duration.setVisibility(View.VISIBLE);
                            summary.setVisibility(View.GONE);
                            duration.setText(dataArray.getJSONObject(i).getString("duration"));
                            play_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_icon));
                            lessonTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (video_provider.equals("html5")) {
                                        webview_layout.setVisibility(View.VISIBLE);
                                        webView.setWebViewClient(new WebViewClient() {
                                            @SuppressWarnings("deprecation")
                                            @Override
                                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }

                                            @TargetApi(Build.VERSION_CODES.M)
                                            @Override
                                            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) { }

                                            @Override
                                            public void onPageStarted(WebView view, String url, Bitmap favicon) { }

                                            @Override
                                            public void onPageFinished(WebView view, String url) { }
                                        });
                                        Log.e("Video URL", "URL " + video);
                                        webView.loadUrl(video);
                                    } else if(video_provider.equals("youtube")){
                                        if (Utility.isConnectingToInternet(context)) {
                                            url = "http://www.youtube.com/embed/" + videoid + "?autoplay=1&vq=small";
                                            Log.e("URL", url);
                                        } else {
                                            makeText(context,R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                        }
                                        webview_layout.setVisibility(View.VISIBLE);
                                        webView.setWebViewClient(new WebViewClient() {
                                            @SuppressWarnings("deprecation")
                                            @Override
                                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }

                                            @TargetApi(Build.VERSION_CODES.M)
                                            @Override
                                            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) { }

                                            @Override
                                            public void onPageStarted(WebView view, String url, Bitmap favicon) { }

                                            @Override
                                            public void onPageFinished(WebView view, String url) { }
                                        });
                                        Log.e("Video URL", "URL " + video);
                                        webView.loadUrl(url);
                                    } if (video_provider.equals("s3_bucket")) {

                                        String text = "<html><body>" + "<video width=\"1000\" height=\"500\" controls>" + "<source src=" +"\"" + video
                                                    +"\"" + ">" + "</video>" + "</body></html>";
                                        System.out.println("s3 bucket url="+text);
                                        webview_layout.setVisibility(View.VISIBLE);
                                        webView.loadDataWithBaseURL(null,text,"text/html; charset=utf-8", "utf-8", null);

                                    }else if (video_provider.equals("vimeo")) {
                                        if (Utility.isConnectingToInternet(context)) {
                                            url = "https://player.vimeo.com/video/"+videoid;
                                            Log.e("URL", url);
                                        } else {
                                            makeText(context,R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                        }
                                        webview_layout.setVisibility(View.VISIBLE);
                                        webView.setWebViewClient(new WebViewClient() {
                                            @SuppressWarnings("deprecation")
                                            @Override
                                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }

                                            @TargetApi(Build.VERSION_CODES.M)
                                            @Override
                                            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) { }

                                            @Override
                                            public void onPageStarted(WebView view, String url, Bitmap favicon) { }

                                            @Override
                                            public void onPageFinished(WebView view, String url) { }
                                        });
                                        Log.e("Video URL", "URL " + video);
                                        webView.loadUrl(url);
                                    }
                                }
                            });
                        }else if(lesson_type.equals("pdf")){
                            duration.setVisibility(View.GONE);
                            if(lessonsummary.equals("")){
                                summary.setVisibility(View.GONE);
                            }else{
                                summary.setVisibility(View.VISIBLE);
                                summary.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final Dialog dialog = new Dialog(context);
                                        dialog.setContentView(R.layout.lesson_description);
                                        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
                                        final ProgressDialog progressDialog = new ProgressDialog(context.getApplicationContext());
                                        progressDialog.setMessage("Loading Data...");
                                        progressDialog.setCancelable(false);
                                        TextView headerTV = dialog.findViewById(R.id.homework_bottomSheet_headerTV);
                                        ImageView closeBtn = dialog.findViewById(R.id.homework_bottomSheet_crossBtn);
                                        TextView details= dialog.findViewById(R.id.bottomSheet_TV);

                                        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
                                        headerTV.setText((context.getApplicationContext().getString(R.string.description)));
                                        closeBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }
                                });
                            }
                            play_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attach_file_black));
                            lessonTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                                    urlStr += "uploads/course_content/"+section_id+"/"+lesson_id+"/"+attachment;
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
                                    context.startActivity(browserIntent);
                                }
                            });
                        }else if(lesson_type.equals("text")){
                            duration.setVisibility(View.GONE);
                            if(lessonsummary.equals("")){
                                summary.setVisibility(View.GONE);
                            }else{
                                summary.setVisibility(View.VISIBLE);
                                summary.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final Dialog dialog = new Dialog(context);
                                        dialog.setContentView(R.layout.lesson_description);
                                        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
                                        final ProgressDialog progressDialog = new ProgressDialog(context.getApplicationContext());
                                        progressDialog.setMessage("Loading Data...");
                                        progressDialog.setCancelable(false);
                                        TextView headerTV = dialog.findViewById(R.id.homework_bottomSheet_headerTV);
                                        ImageView closeBtn = dialog.findViewById(R.id.homework_bottomSheet_crossBtn);
                                        TextView details= dialog.findViewById(R.id.bottomSheet_TV);

                                        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
                                        headerTV.setText((context.getApplicationContext().getString(R.string.description)));
                                        closeBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }
                                });
                            }
                            play_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attach_file_black));
                            lessonTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                                    urlStr += "uploads/course_content/"+section_id+"/"+lesson_id+"/"+attachment;
                                    Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                                    intent.putExtra("imageUrl",urlStr);
                                    context.startActivity(intent);
                                    System.out.println("imageUrl"+urlStr);
                                }
                            });
                        }else if(lesson_type.equals("document")){
                            duration.setVisibility(View.GONE);
                            if(lessonsummary.equals("")){
                                summary.setVisibility(View.GONE);
                            }else{
                                summary.setVisibility(View.VISIBLE);
                                summary.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final Dialog dialog = new Dialog(context);
                                        dialog.setContentView(R.layout.lesson_description);
                                        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
                                        final ProgressDialog progressDialog = new ProgressDialog(context.getApplicationContext());
                                        progressDialog.setMessage("Loading Data...");
                                        progressDialog.setCancelable(false);
                                        TextView headerTV = dialog.findViewById(R.id.homework_bottomSheet_headerTV);
                                        ImageView closeBtn = dialog.findViewById(R.id.homework_bottomSheet_crossBtn);
                                        TextView details= dialog.findViewById(R.id.bottomSheet_TV);

                                        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
                                        headerTV.setText((context.getApplicationContext().getString(R.string.description)));
                                        closeBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }
                                });
                            }
                            play_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attach_file_black));
                            lessonTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                                    urlStr += "uploads/course_content/"+section_id+"/"+lesson_id+"/"+attachment;
                                    downloadID = Utility.beginDownload(context, attachment, urlStr);
                                    System.out.println("imageUrl"+urlStr);
                                }
                            });
                        }

                    }else{
                        duration.setVisibility(View.GONE);
                        play_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_quiz));
                        lessonTV.setText(dataArray.getJSONObject(i).getString("quiz_title"));
                        CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean checkedStatus) {
                                if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                                    params.put("student_id", Utility.getSharedPreferences(context.getApplicationContext(), Constants.studentId));
                                    params.put("lesson_quiz_id", quiz_id);
                                    params.put("section_id", section_idList.get(position));
                                    params.put("lesson_quiz_type","2");
                                    JSONObject objt=new JSONObject(params);
                                    Log.e("params ", objt.toString());
                                    changeStatusApi(objt.toString());

                                } else {
                                    makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        lessonTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                webview_layout.setVisibility(View.GONE);

                                if(quizstatus.equals("0")){
                                    Intent intent=new Intent(context.getApplicationContext(), StudentOnlineCourseQuiz.class);
                                    intent.putExtra("quiz_id", quiz_id);
                                    intent.putExtra("quiz_name", quiz_name);
                                    context.startActivity(intent);
                                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);

                                }else {

                                    Intent intent=new Intent(context.getApplicationContext(), StudentOnlineQuizResult.class);
                                    intent.putExtra("quiz_id", quiz_id);
                                    intent.putExtra("quiz_name", quiz_name);
                                    context.startActivity(intent);
                                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);

                                }

                            }
                        });
                    }

                    context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    vehicleTable.addView(tr);
                    context.registerForContextMenu(tr);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  view.setTag(viewHolder);
        return view;
    }

    private void changeStatusApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl") + Constants.markAsCompleteUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        String success = object.getString("status");

                        if (success.equals("1")) {
                            //context.finish();
                           // context.startActivity(context.getIntent());
                         Toast.makeText(context.getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context.getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context.getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(context); //Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
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
            return BitmapFactory.decodeResource(context.getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)context.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            context.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            this.mOriginalSystemUiVisibility = context.getWindow().getDecorView().getSystemUiVisibility();
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)context.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            context.getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }
    }

}
