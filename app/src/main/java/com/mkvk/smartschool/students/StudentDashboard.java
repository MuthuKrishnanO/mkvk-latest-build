package com.mkvk.smartschool.students;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.mkvk.smartschool.AboutSchool;
import com.mkvk.smartschool.Login;
import com.mkvk.smartschool.TakeUrl;
import com.mkvk.smartschool.adapters.LoginChildListAdapter;
import com.mkvk.smartschool.fragments.StudentDashboardNotice;
import com.mkvk.smartschool.fragments.StudentDashboardFees;
import com.mkvk.smartschool.fragments.StudentDashboardFragment;
import com.mkvk.smartschool.fragments.StudentDashboardHomeWork;
import com.mkvk.smartschool.fragments.StudentDashboardReportCard;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.DatabaseHelper;
import com.mkvk.smartschool.utils.DrawerArrowDrawable;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import static android.widget.Toast.makeText;

public class StudentDashboard extends AppCompatActivity {

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA};
    private boolean sentToSettings = false;
    public DrawerArrowDrawable drawerArrowDrawable;
    ImageView drawerIndicator;
    public float offset;
    public boolean flipped;
    public DrawerLayout drawer;
    protected FrameLayout mDrawerLayout, actionBar;
    ImageView actionBarLogo;
    BottomNavigationView bottomNavigation;
    private NavigationView navigationView;
    private RelativeLayout drawerHead;
    private TextView classTV, nameTV, childDetailsTV,hotlist_hot;
    private ImageView profileImageIV;
    private LinearLayout switchChildBtn;
    ArrayList<String> moduleCodeList = new ArrayList<String>();
    ArrayList<String> moduleStatusList = new ArrayList<String>();
    public Map<String, String> headers = new HashMap<String, String>();
    FrameLayout viewContainer;
    LayoutInflater inflater;
    FrameLayout chatBtn,notification_alert;
    View contentView;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<String> childIdList = new ArrayList<String>();
    ArrayList<String> childNameList = new ArrayList<String>();
    ArrayList<String> childClassList = new ArrayList<String>();
    ArrayList<String> childImageList = new ArrayList<String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> aparams = new Hashtable<String, String>();
    public Map<String, String> logoutparams = new Hashtable<String, String>();
    LoginChildListAdapter studentListAdapter;
    JSONArray modulesJson;
    TextView unread_count,version_name;
    String device_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard_activity);

        drawerIndicator = findViewById(R.id.drawer_indicator);
        actionBar = findViewById(R.id.actionBar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        unread_count = findViewById(R.id.unread_count);
        actionBarLogo = findViewById(R.id.actionBar_logo);

       notification_alert = findViewById(R.id.notification_alert);

        prepareNavList();
        setUpDrawer();
        decorate();
        setUpPermission();
        device_token = FirebaseInstanceId.getInstance().getToken()+"";
        Log.e(" logout DEVICE TOKEN", device_token);
        DatabaseHelper db = new DatabaseHelper(StudentDashboard.this);
        int profile_counts = db.getProfilesCount();
        db.close();
        if(String.valueOf(profile_counts).equals("0")){
            unread_count.setVisibility(View.GONE);
        }else{
            unread_count.setText(String.valueOf(profile_counts));
        }

        params.put("site_url", Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
        JSONObject obj=new JSONObject(params);
        Log.e("params", obj.toString());
        getDataFromApi(obj.toString());

        if(Utility.getSharedPreferences(getApplicationContext(), "role").equals("parent")) {
            if(Utility.getSharedPreferencesBoolean(getApplicationContext(), "hasMultipleChild")) {
               switchChildBtn.setVisibility(View.VISIBLE);
            } else {
                switchChildBtn.setVisibility(View.GONE);
            }
        } else {
            switchChildBtn.setVisibility(View.GONE);
        }

        switchChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChildList();
            }
        });


        notification_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(StudentDashboard.this);
                db.updatestatus("0","1");

                Intent intent=new Intent(StudentDashboard.this,NotificationList.class);
                startActivity(intent);
            }
        });


        viewContainer = findViewById(R.id.studentDashboard_frame);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new StudentDashboardFragment());

                        return true;

                    case R.id.navigation_homework:
                        loadFragment(new StudentDashboardHomeWork());

                        return true;

                    case R.id.navigation_fees:
                        loadFragment(new StudentDashboardFees());

                        return true;

                    case R.id.navigation_noticeBoard:
                        loadFragment(new StudentDashboardNotice());

                        return true;

                    case R.id.navigation_reportCard:
                        loadFragment(new StudentDashboardReportCard());

                        return true;
                }
                return false;
            }
        });
        loadFragment(new StudentDashboardFragment());
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        DatabaseHelper db = new DatabaseHelper(StudentDashboard.this);
        int profile_counts = db.getProfilesCount();
        db.close();
        if(String.valueOf(profile_counts).equals("0")){
            unread_count.setVisibility(View.GONE);
        }else{
            unread_count.setText(String.valueOf(profile_counts));
        }
    }

    private void showChildList() {

        View view = getLayoutInflater().inflate(R.layout.fragment_login_bottom_sheet, null);
        view.setMinimumHeight(500);

        TextView headerTV = view.findViewById(R.id.login_bottomSheet_header);
        ImageView crossBtn = view.findViewById(R.id.login_bottomSheet_crossBtn);
        RecyclerView childListView = view.findViewById(R.id.login_bottomSheet_listview);

        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        headerTV.setText(getString(R.string.childList));

        Log.e("ImageList", childImageList.toString());
        Log.e("Class List", childClassList.toString());
        Log.e("ID List", childIdList.toString());

        studentListAdapter = new LoginChildListAdapter(StudentDashboard.this, childIdList, childNameList, childClassList, childImageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        childListView.setLayoutManager(mLayoutManager);
        childListView.setItemAnimator(new DefaultItemAnimator());
        childListView.setAdapter(studentListAdapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(StudentDashboard.this);

        dialog.setContentView(view);
        dialog.show();

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("parent_id", Utility.getSharedPreferences(getApplicationContext(), "userId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getStudentsListFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
        Log.e("Child Name", childNameList.toString());
    }

    private void getStudentsListFromApi (String bodyParams) {

        childIdList.clear(); childNameList.clear(); childClassList.clear(); childImageList.clear();

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.parent_getStudentList;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        JSONArray dataArray = object.getJSONArray("childs");
                        if (dataArray.length() != 0) {

                            for(int i = 0; i < dataArray.length(); i++) {
                                childIdList.add(dataArray.getJSONObject(i).getString("id"));
                                childNameList.add(dataArray.getJSONObject(i).getString("firstname") + " " +  dataArray.getJSONObject(i).getString("lastname") );
                                childClassList.add(dataArray.getJSONObject(i).getString("class") + "-" +  dataArray.getJSONObject(i).getString("section"));
                                childImageList.add(dataArray.getJSONObject(i).getString("image"));
                            }
                            studentListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StudentDashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getModulesFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getModuleUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                moduleCodeList.add(modulesJson.getJSONObject(i).getString("short_code"));
                                moduleStatusList.add(modulesJson.getJSONObject(i).getString("status"));
                            }
                            setMenu(navigationView.getMenu(), bottomNavigation.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void setUpPermission() {
        if(ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[3])){
                ActivityCompat.requestPermissions(StudentDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            } else {
                ActivityCompat.requestPermissions(StudentDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.permissionStatus, true);
        }
    }

    private void setUpDrawer() {
        //HEADER
        View headerLayout = navigationView.getHeaderView(0);

        Menu menu = navigationView.getMenu();
        RelativeLayout tracks = (RelativeLayout) menu.findItem(R.id.nav_log_version).getActionView();
        TextView version_name = (TextView) tracks.findViewById(R.id.version_name);
        version_name.setText(getApplicationContext().getString(R.string.version)+" on "+Utility.getSharedPreferences(getApplicationContext(), Constants.app_ver));

        classTV = headerLayout.findViewById(R.id.drawer_userClass);
        nameTV = headerLayout.findViewById(R.id.drawer_userName);
        profileImageIV = headerLayout.findViewById(R.id.drawer_logo);
        drawerHead = headerLayout.findViewById(R.id.drawer_head);
        switchChildBtn = headerLayout.findViewById(R.id.drawer_switchChildBtn);
        childDetailsTV = headerLayout.findViewById(R.id.drawer_studentDetailsTV);
        //HEADER
        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });
        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }
    private void decorate() {

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        String appLogo = Utility.getSharedPreferences(this, Constants.appLogo)+"?"+new Random().nextInt(11);

        Picasso.with(getApplicationContext()).load(Utility.getSharedPreferences(this, "userImage")).placeholder(R.drawable.placeholder_user).into(profileImageIV);
        Picasso.with(getApplicationContext()).load(appLogo).fit().centerInside().placeholder(null).into(actionBarLogo);

        nameTV.setText(Utility.getSharedPreferences(this, Constants.userName));
        classTV.setText(Utility.getSharedPreferences(this, Constants.classSection));
        childDetailsTV.setText("Child - " + Utility.getSharedPreferences(getApplicationContext(), "studentName")
                + "\n" + Utility.getSharedPreferences(this, Constants.classSection));

        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")) {
            classTV.setVisibility(View.GONE);
            childDetailsTV.setVisibility(View.VISIBLE);
        } else {
            classTV.setVisibility(View.VISIBLE);
            childDetailsTV.setVisibility(View.GONE);
        }

        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));

        drawerHead.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
    }

    private void prepareNavList() {
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("user", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getModulesFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        Intent dashboard = new Intent(StudentDashboard.this, StudentDashboard.class);
                        startActivity(dashboard);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_profile:
                        Intent profile = new Intent(StudentDashboard.this, StudentProfileDetailsNew.class);
                        startActivity(profile);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_fees:
                        Intent fees = new Intent(StudentDashboard.this, StudentFees.class);
                        startActivity(fees);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_online_course:
                        aparams.put("site_url", Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
                        aparams.put("addontype","ssoclc");
                        JSONObject ocobj=new JSONObject(aparams);
                        Log.e("CheckAddon params", ocobj.toString());
                        CheckAddon(ocobj.toString(),"ssoclc");
                        break;

                    case R.id.nav_zoomliveclasses:
                        aparams.put("site_url", Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
                        aparams.put("addontype","sszlc");
                        JSONObject zobj=new JSONObject(aparams);
                        Log.e("CheckAddon params", zobj.toString());
                        CheckAddon(zobj.toString(),"sszlc");
                        break;

                    case R.id.nav_livegmeetclass:
                        aparams.put("site_url", Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
                        aparams.put("addontype", "ssglc");
                        JSONObject gobj=new JSONObject(aparams);
                        Log.e("CheckAddon params", gobj.toString());
                        CheckAddon(gobj.toString(),"ssglc");
                        break;

                    case R.id.nav_timetable:
                        Intent classTimeTable = new Intent(StudentDashboard.this, StudentClassTimetable.class);
                        startActivity(classTimeTable);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_syllabus:
                        Intent syllabus = new Intent(StudentDashboard.this, StudentSyllabusTimetable.class);
                        startActivity(syllabus);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_syllabus_status:
                        Intent syllabus_status = new Intent(StudentDashboard.this, StudentSyllabusStatus.class);
                        startActivity(syllabus_status);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_homework:
                        Intent homework = new Intent(StudentDashboard.this, StudentHomework.class);
                        startActivity(homework);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_onlineexam:
                        Intent onlineexam = new Intent(StudentDashboard.this, StudentOnlineExam.class);
                        startActivity(onlineexam);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_applyleave:
                        Intent applyleave = new Intent(StudentDashboard.this, StudentAppyLeave.class);
                        startActivity(applyleave);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_downloadCenter:
                        Intent download = new Intent(StudentDashboard.this, StudentDownloads.class);
                        startActivity(download);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_attendance:
                        Intent attendance = new Intent(StudentDashboard.this, StudentAttendance.class);
                        startActivity(attendance);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_examination:
                        Intent examination = new Intent(StudentDashboard.this, StudentExaminationList.class);
                        startActivity(examination);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_noticeBoard:
                        Intent notice = new Intent(StudentDashboard.this, StudentNoticeBoard.class);
                        startActivity(notice);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_timeLine:
                        Intent timeline = new Intent(StudentDashboard.this, StudentTimeline.class);
                        startActivity(timeline);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_documents:
                        Intent doc = new Intent(StudentDashboard.this, StudentDocuments.class);
                        startActivity(doc);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_teachers:
                        Intent teacher = new Intent(StudentDashboard.this, StudentTeachersList.class);
                        startActivity(teacher);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;


                    case R.id.nav_library:
                        Intent booksIssued = new Intent(StudentDashboard.this, StudentLibraryBookIssued.class);
                        startActivity(booksIssued);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_transportRoute:
                        Intent transport = new Intent(StudentDashboard.this, StudentTransportRoutes.class);
                        startActivity(transport);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_hostel:
                        Intent hostel = new Intent(StudentDashboard.this, StudentHostel.class);
                        startActivity(hostel);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;


                    case R.id.nav_about:
                        Intent about = new Intent(StudentDashboard.this, AboutSchool.class);
                        startActivity(about);
                        overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        break;

                    case R.id.nav_logout:
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentDashboard.this);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.logoutMsg);
                        builder.setTitle("");
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if (Utility.isConnectingToInternet(getApplicationContext())) {
                                    logoutparams.put("deviceToken", device_token);
                                    JSONObject obj=new JSONObject(logoutparams);
                                    Log.e("params ", obj.toString());
                                    System.out.println("Logout Details=="+obj.toString());
                                    loginOutApi(obj.toString());
                                } else {
                                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
                return false;
            }
        });
    }
    private void setMenu(final Menu navMenu, Menu bottomNavMenu) {

        if(moduleCodeList.contains("live_classes")) {
            System.out.println("live CLasses Integration");
            navMenu.findItem(R.id.nav_zoomliveclasses).setVisible(true);
        } else {
            System.out.println("live CLasses not Integration");
            navMenu.findItem(R.id.nav_zoomliveclasses).setVisible(false);
        }

        if(moduleCodeList.contains("gmeet_live_classes")) {
            navMenu.findItem(R.id.nav_livegmeetclass).setVisible(true);
        } else {
            navMenu.findItem(R.id.nav_livegmeetclass).setVisible(false);
        }

        if(moduleCodeList.contains("online_course")) {
            navMenu.findItem(R.id.nav_online_course).setVisible(true);
        } else {
            navMenu.findItem(R.id.nav_online_course).setVisible(false);
        }

        for (int i = 0; i<moduleCodeList.size(); i++) {
            if (moduleCodeList.get(i).equals("fees") && moduleStatusList.get(i).equals("0")) {
                System.out.println("fees Hide");
                navMenu.findItem(R.id.nav_fees).setVisible(false);
                bottomNavMenu.findItem(R.id.navigation_fees).setVisible(false);
            } if (moduleCodeList.get(i).equals("student_attendance") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_attendance).setVisible(false);
            } if (moduleCodeList.get(i).equals("examinations") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_examination).setVisible(false);
                bottomNavMenu.findItem(R.id.navigation_reportCard).setVisible(false);
            } if (moduleCodeList.get(i).equals("download_center") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_downloadCenter).setVisible(false);
            } if (moduleCodeList.get(i).equals("library") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_library).setVisible(false);
            } if (moduleCodeList.get(i).equals("transport_routes") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_transportRoute).setVisible(false);
            } if (moduleCodeList.get(i).equals("hostel_rooms") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_hostel).setVisible(false);
            } if (moduleCodeList.get(i).equals("homework") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_homework).setVisible(false);
                bottomNavMenu.findItem(R.id.navigation_homework).setVisible(false);
            } if (moduleCodeList.get(i).equals("communicate") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_noticeBoard).setVisible(false);
                bottomNavMenu.findItem(R.id.navigation_noticeBoard).setVisible(false);
            }if (moduleCodeList.get(i).equals("online_examination") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_onlineexam).setVisible(false);
            }if (moduleCodeList.get(i).equals("class_timetable") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_timetable).setVisible(false);
            }if (moduleCodeList.get(i).equals("attendance") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_attendance).setVisible(false);
            }if (moduleCodeList.get(i).equals("teachers_rating") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_teachers).setVisible(false);
            }if (moduleCodeList.get(i).equals("notice_board") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_noticeBoard).setVisible(false);
            }if (moduleCodeList.get(i).equals("lesson_plan") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_syllabus).setVisible(false);
            }if (moduleCodeList.get(i).equals("syllabus_status") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_syllabus_status).setVisible(false);
            }if (moduleCodeList.get(i).equals("apply_leave") && moduleStatusList.get(i).equals("0")) {
                navMenu.findItem(R.id.nav_applyleave).setVisible(false);
            } if(moduleCodeList.get(i).equals("live_classes") && moduleStatusList.get(i).equals("0")) {
                System.out.println("live CLasses Hide");
                navMenu.findItem(R.id.nav_zoomliveclasses).setVisible(false);
            }if(moduleCodeList.get(i).equals("gmeet_live_classes") && moduleStatusList.get(i).equals("0")) {
                System.out.println("google_meeting Hide");
                navMenu.findItem(R.id.nav_livegmeetclass).setVisible(false);
            }if(moduleCodeList.get(i).equals("online_course") && moduleStatusList.get(i).equals("0")) {
                System.out.println("google_meeting Hide");
                navMenu.findItem(R.id.nav_online_course).setVisible(false);
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.studentDashboard_frame, fragment);
        transaction.commit();
    }

    //RUNTIME PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults
        );
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){

            if(ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[3])){

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDashboard.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs to access to your storage, camera and call permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(StudentDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

                if(Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentDashboard.this);
                    builder.setTitle("Allow Notifications");
                    builder.setMessage("For smooth functioning of app, please provide Auto-Start permission and allow notification access.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {

                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("STATUS", "1");
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {

            } else {
                Log.e("PERMISSION MANAGER", "PERMISSION MISSING");
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
                Log.e("PERMISSION MANAGER", "PERMISSION MISSING");
            }
        }
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

    private void goToSettings() {
        sentToSettings = true;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    private void loginOutApi (String bodyParams) {
        DatabaseHelper dataBaseHelpers = new DatabaseHelper(StudentDashboard.this);
        dataBaseHelpers.deleteAll() ;

        final ProgressDialog pd = new ProgressDialog(StudentDashboard.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(StudentDashboard.this, "apiUrl")+ Constants.logoutUrl;
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
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), "isLoggegIn", false);
                            Intent logout = new Intent(StudentDashboard.this, Login.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            logout.putExtra("EXIT", true);
                            startActivity(logout);
                            finish();
                        } else {
                            Intent intent=new Intent(StudentDashboard.this, TakeUrl.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(StudentDashboard.this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                       // Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(StudentDashboard.this,TakeUrl.class);
                        startActivity(intent);
                        finish();

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(StudentDashboard.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(StudentDashboard.this, "accessToken"));
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
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StudentDashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDataFromApi(String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        try {

            SecretKey key = Utility.generateKey();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.decryptMsg(Utility.encryptMsg(key), key ), new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    if (result != null) {
                        pd.dismiss();
                        try {

                            JSONObject object = new JSONObject(result);

                            if(object.getString("status").equals("0")) {
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.isLoggegIn, false);

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentDashboard.this);
                                builder.setCancelable(false);
                                //builder.setMessage(R.string.verificationMessage);
                                builder.setMessage(object.getString("msg"));
                                builder.setTitle("");
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Utility.isConnectingToInternet(getApplicationContext())) {
                                            logoutparams.put("deviceToken", device_token);
                                            JSONObject obj=new JSONObject(logoutparams);
                                            Log.e("params ", obj.toString());
                                            System.out.println("Logout Details=="+obj.toString());
                                            loginOutApi(obj.toString());
                                        } else {
                                            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                android.app.AlertDialog alert = builder.create();
                                alert.show();
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
                     Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(StudentDashboard.this);
            //Adding request to the queue
            requestQueue.add(stringRequest);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException |
                IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException  exp) {
            Log.e("ENCRYPTION", exp.toString());
        }
    }

    private void CheckAddon(String bodyParams, final String type) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        try {
            SecretKey key = Utility.generateKey();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.decryptMsg(Utility.encryptAddonMsg(key), key ), new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    if (result != null) {
                        pd.dismiss();
                        try {

                            JSONObject object = new JSONObject(result);
                            if(object.getString("status").equals("0")) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentDashboard.this);
                                builder.setCancelable(false);
                                builder.setMessage(R.string.verificationMessage);
                                //builder.setMessage(object.getString("msg"));
                                builder.setTitle("");
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                    }
                                });

                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            }else{
                                if(type.equals("sszlc")){
                                    Intent liveclasses = new Intent(StudentDashboard.this, StudentLiveClasses.class);
                                    startActivity(liveclasses);
                                    overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                                }else if(type.equals("ssoclc")){
                                    Intent online_course = new Intent(StudentDashboard.this, StudentOnlineCourse.class);
                                    startActivity(online_course);
                                    overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                                }else if(type.equals("ssglc")){
                                    Intent gmeetliveclasses = new Intent(StudentDashboard.this, StudentGmeetLiveClasses.class);
                                    startActivity(gmeetliveclasses);
                                    overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                                }
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
                       Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(StudentDashboard.this);

            //Adding request to the queue
            requestQueue.add(stringRequest);


        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException |
                IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException  exp) {
            Log.e("ENCRYPTION", exp.toString());
        }
    }

}
