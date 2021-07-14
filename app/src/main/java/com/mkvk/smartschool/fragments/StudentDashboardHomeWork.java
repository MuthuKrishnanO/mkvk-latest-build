package com.mkvk.smartschool.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mkvk.smartschool.adapters.StudentHomeworkAdapter;
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

public class StudentDashboardHomeWork extends Fragment {

    RecyclerView homeworkListView;
    ArrayList<String> homeworkIdList = new ArrayList<String>();
    ArrayList<String> homeworkTitleList = new ArrayList<String>();
    ArrayList<String> homeworkSubjectNameList = new ArrayList<String>();
    ArrayList<String> homeworkHomeworkDateList = new ArrayList<String>();
    ArrayList<String> homeworkSubmissionDateList = new ArrayList<String>();
    ArrayList<String> homeworkEvaluationDateList = new ArrayList<String>();
    ArrayList<String> homeworkEvaluationByList = new ArrayList<String>();
    ArrayList<String> homeworkCreatedByList = new ArrayList<String>();
    ArrayList<String> homeworkDocumentList = new ArrayList<String>();
    ArrayList<String> homeworkClassList = new ArrayList<String>();
    ArrayList<String> homeworkStatusList = new ArrayList<String>();
    public String defaultDateFormat, currency;
    SwipeRefreshLayout pullToRefresh;
    StudentHomeworkAdapter adapter;

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    public StudentDashboardHomeWork() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void loadData() {

        adapter = new StudentHomeworkAdapter(getActivity(), homeworkIdList, homeworkTitleList, homeworkSubjectNameList,
                homeworkHomeworkDateList, homeworkSubmissionDateList, homeworkEvaluationDateList, homeworkEvaluationByList,
                homeworkCreatedByList, homeworkDocumentList, homeworkClassList, homeworkStatusList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        homeworkListView.setLayoutManager(mLayoutManager);
        homeworkListView.setItemAnimator(new DefaultItemAnimator());
        homeworkListView.setAdapter(adapter);

        if(Utility.isConnectingToInternet(getActivity())){
            params.put("student_id", Utility.getSharedPreferences(getActivity(), Constants.studentId));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());

        }else{
            makeText(getActivity(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.student_homework_activity, container, false);
        homeworkListView = (RecyclerView) mainView.findViewById(R.id.studentHostel_listview);
        defaultDateFormat = Utility.getSharedPreferences(getActivity(), "dateFormat");
        currency = Utility.getSharedPreferences(getActivity(), Constants.currency);
        loadData();
        pullToRefresh = mainView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loadData();
            }
        });

        return mainView;

    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+Constants.getHomeworkUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();

                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("homeworklist");

                        if(dataArray.length() != 0) {

                            for(int i = 0; i < dataArray.length(); i++) {
                                homeworkIdList.add(dataArray.getJSONObject(i).getString("id"));
                                homeworkTitleList.add(dataArray.getJSONObject(i).getString("description"));
                                homeworkSubjectNameList.add(dataArray.getJSONObject(i).getString("name"));
                                homeworkHomeworkDateList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("homework_date")));
                                homeworkSubmissionDateList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("submit_date")));
                                homeworkCreatedByList.add(dataArray.getJSONObject(i).getString("staff_created"));
                                homeworkEvaluationByList.add(dataArray.getJSONObject(i).getString("staff_evaluated"));
                                String fileName = "";
                                String filePath = dataArray.getJSONObject(i).getString("document");
                                if(!filePath.isEmpty()) {
                                    String extension = filePath.substring(filePath.lastIndexOf("."));
                                    fileName = dataArray.getJSONObject(i).getString("id") + extension;
                                }
                                homeworkDocumentList.add(fileName);
                                homeworkClassList.add(dataArray.getJSONObject(i).getString("class") + " " + dataArray.getJSONObject(i).getString("section") );
                                homeworkEvaluationDateList.add( Utility.parseDate("yyyy-MM-dd", defaultDateFormat,dataArray.getJSONObject(i).getString("evaluation_date")));
                                homeworkStatusList.add(dataArray.getJSONObject(i).getString("homework_evaluation_id"));
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.noData), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    pullToRefresh.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(getActivity().getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getActivity().getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getActivity().getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
