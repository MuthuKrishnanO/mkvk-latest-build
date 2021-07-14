package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.mkvk.smartschool.BaseActivity;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentOnlineCourseAdapter;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentOnlineCourse extends BaseActivity {
    StudentOnlineCourseAdapter adapter;
    RecyclerView recyclerview;
    private ProgressBar loadingPB;
    public NestedScrollView nestedSV;
    ArrayList<String> courseidList = new ArrayList<String>();
    ArrayList<String> coursetitleList = new ArrayList<String>();
    ArrayList<String> coursedescriptionList = new ArrayList<String>();
    ArrayList<String> course_thumbnailList = new ArrayList<String>();
    ArrayList<String> course_priceList = new ArrayList<String>();
    ArrayList<String> coursediscountList = new ArrayList<String>();
    ArrayList<String> free_courselist = new ArrayList<String>();
    ArrayList<String> course_progresslist = new ArrayList<String>();
    ArrayList<String> classlist = new ArrayList<String>();
    ArrayList<String> teacherlist = new ArrayList<String>();
    ArrayList<String> total_lessonlist = new ArrayList<String>();
    ArrayList<String> total_hour_countlist = new ArrayList<String>();
    ArrayList<String> updated_datelist = new ArrayList<String>();
    ArrayList<String> imagelist = new ArrayList<String>();
    ArrayList<String> paidstatuslist = new ArrayList<String>();
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_online_course, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.course_list));
        pullToRefresh = findViewById(R.id.pullToRefresh);
        recyclerview=findViewById(R.id.recyclerview);
        adapter = new StudentOnlineCourseAdapter(StudentOnlineCourse.this,recyclerview,courseidList,coursetitleList,coursedescriptionList,
                course_thumbnailList,coursediscountList,free_courselist,course_progresslist,classlist,
                teacherlist,total_lessonlist,total_hour_countlist,updated_datelist,imagelist,course_priceList,paidstatuslist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });
        loaddata();
    }

    public  void  loaddata(){
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.courselistUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                 pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        String pay_method = obj.getString("pay_method");
                        if(pay_method.equals("0")) {
                            Log.e("test", "testing");
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showCoursePaymentBtn, false);
                        } else {
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showCoursePaymentBtn, true);
                        }
                        JSONArray dataArray = obj.getJSONArray("course_list");
                        courseidList.clear();
                        coursetitleList.clear();
                        coursedescriptionList.clear();
                        course_thumbnailList.clear();
                        course_priceList.clear();
                        coursediscountList.clear();
                        free_courselist.clear();
                        course_progresslist.clear();
                        classlist.clear();
                        teacherlist.clear();
                        total_lessonlist.clear();
                        total_hour_countlist.clear();
                        updated_datelist.clear();
                        paidstatuslist.clear();
                        imagelist.clear();

                        if(dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                courseidList.add(dataArray.getJSONObject(i).getString("id"));
                                coursetitleList.add(dataArray.getJSONObject(i).getString("title"));
                                coursedescriptionList .add(dataArray.getJSONObject(i).getString("description"));
                                course_thumbnailList.add(dataArray.getJSONObject(i).getString("course_thumbnail"));
                                course_priceList.add(dataArray.getJSONObject(i).getString("price"));
                                coursediscountList.add(dataArray.getJSONObject(i).getString("discount"));
                                free_courselist.add(dataArray.getJSONObject(i).getString("free_course"));
                                course_progresslist.add(dataArray.getJSONObject(i).getString("course_progress"));
                                classlist.add(dataArray.getJSONObject(i).getString("class"));
                                teacherlist.add(dataArray.getJSONObject(i).getString("name")+" "+dataArray.getJSONObject(i).getString("surname"));
                                total_lessonlist.add(dataArray.getJSONObject(i).getString("total_lesson"));
                                total_hour_countlist.add(dataArray.getJSONObject(i).getString("total_hour_count"));
                                updated_datelist.add(dataArray.getJSONObject(i).getString("updated_date"));
                                paidstatuslist.add(dataArray.getJSONObject(i).getString("paidstatus"));
                                imagelist.add(dataArray.getJSONObject(i).getString("image"));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            pullToRefresh.setVisibility(View.GONE);
                            makeText(StudentOnlineCourse.this, R.string.noData, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                   pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentOnlineCourse.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineCourse.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
