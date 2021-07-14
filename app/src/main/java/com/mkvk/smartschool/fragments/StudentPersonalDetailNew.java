package com.mkvk.smartschool.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.mkvk.smartschool.adapters.StudentProfileCustomAdapter;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.DatabaseHelperCustomNew;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import static android.widget.Toast.makeText;

@SuppressLint("ValidFragment")
public class StudentPersonalDetailNew extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    ArrayList<String> appointment_datelist = new ArrayList<String>();
    ArrayList<String> opd_noList = new ArrayList<String>();
    ArrayList<String> consultantList = new ArrayList<String>();
    ArrayList<String> refferencelist = new ArrayList<String>();
    ArrayList<String> symptomslist = new ArrayList<String>();
    private String visitid;
    ListView list;
    JSONArray jsonArray=null;
    JSONObject customArray;
    SwipeRefreshLayout pullToRefresh;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    TextView rollno, bloodgroup, mobileno, religion, caste, admDate, dob, category, email, currentAdd, permanentAdd, height, weight, asOnDate;
    LinearLayout blood_layout, mobile_layout, religion_layout, Caste_layout, admDate_layout, dob_layout,
            category_layout, email_layout, currentAdd_layout, permanentAdd_layout, height_layout, weight_layout, asOnDate_layout, myLinearLayout1, myLinearLayout2;
    public String defaultDateFormat, currency;

    RecyclerView recyclerview;
    StudentProfileCustomAdapter custom_adapter;

    @SuppressLint("ValidFragment")
    public StudentPersonalDetailNew() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if(Utility.isConnectingToInternet(getActivity())){
            params.put("student_id", Utility.getSharedPreferences(getActivity(), "studentId"));
            JSONObject obj = new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getActivity(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_list, container, false);
        defaultDateFormat = Utility.getSharedPreferences(getActivity(), "dateFormat");
        currency = Utility.getSharedPreferences(getActivity(), Constants.currency);
        category_layout = mainView.findViewById(R.id.category_layout);
        category = mainView.findViewById(R.id.category);


        email_layout = mainView.findViewById(R.id.email_layout);
        email = mainView.findViewById(R.id.email);

        bloodgroup = mainView.findViewById(R.id.bloodgroup);
        blood_layout = mainView.findViewById(R.id.blood_layout);

        mobile_layout = mainView.findViewById(R.id.mobile_layout);
        mobileno = mainView.findViewById(R.id.mobileno);

        religion_layout = mainView.findViewById(R.id.religion_layout);
        religion = mainView.findViewById(R.id.religion);

        caste = mainView.findViewById(R.id.caste);
        Caste_layout = mainView.findViewById(R.id.Caste_layout);

        admDate_layout = mainView.findViewById(R.id.admDate_layout);
        admDate = mainView.findViewById(R.id.admDate);

        currentAdd_layout = mainView.findViewById(R.id.currentAdd_layout);
        currentAdd = mainView.findViewById(R.id.currentAdd);

        dob_layout = mainView.findViewById(R.id.dob_layout);
        dob = mainView.findViewById(R.id.dob);

        permanentAdd_layout = mainView.findViewById(R.id.permanentAdd_layout);
        permanentAdd = mainView.findViewById(R.id.permanentAdd);

        height_layout = mainView.findViewById(R.id.height_layout);
        height = mainView.findViewById(R.id.height);

        weight_layout = mainView.findViewById(R.id.weight_layout);
        weight = mainView.findViewById(R.id.weight);

        asOnDate_layout = mainView.findViewById(R.id.asOnDate_layout);
        asOnDate = mainView.findViewById(R.id.asOnDate);

        list = (ListView) mainView.findViewById(R.id.list);

        pullToRefresh = mainView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this);

        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(true);
                loadData();
            }
        });
        return mainView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void getDataFromApi(String bodyParams) {
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl") + Constants.getStudentProfileUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONObject dataArray = obj.getJSONObject("student_result");
                        String defaultDateFormat = Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat");

                        admDate.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getString("admission_date")));
                        dob.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getString("dob")));
                        category.setText(dataArray.getString("category"));
                        mobileno.setText(dataArray.getString("mobileno"));
                        caste.setText(dataArray.getString("cast"));
                        religion.setText(dataArray.getString("religion"));
                        email.setText(dataArray.getString("email"));
                        currentAdd.setText(dataArray.getString("current_address"));
                        permanentAdd.setText(dataArray.getString("permanent_address"));
                        bloodgroup.setText(dataArray.getString("blood_group"));
                        height.setText(dataArray.getString("height"));
                        weight.setText(dataArray.getString("weight"));
                        asOnDate.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getString("measurement_date")));

                        JSONObject fieldsArray = obj.getJSONObject("student_fields");
                        System.out.println("fieldsArray==" + fieldsArray);
                        if (!fieldsArray.has("admission_date")) {
                            admDate_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("category")) {
                            category_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("mobile_no")) {
                            mobile_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("cast")) {
                            Caste_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("religion")) {
                            religion_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("student_email")) {
                            email_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("current_address")) {
                            currentAdd_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("permanent_address")) {
                            permanentAdd_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("blood_group")) {
                            blood_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("student_height")) {
                            height_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("student_weight")) {
                            weight_layout.setVisibility(View.GONE);
                        }
                        if (!fieldsArray.has("measurement_date")) {
                            asOnDate_layout.setVisibility(View.GONE);
                        }



                        DatabaseHelperCustomNew dataBaseHelpers = new DatabaseHelperCustomNew(getActivity());
                        dataBaseHelpers.deleteAll() ;

                        customArray = obj.getJSONObject("custom_fields");
                        Iterator<String> iter = customArray.keys();
                        System.out.println("WcustomArray== "+customArray);

                        while (iter.hasNext()){
                            String key = iter.next();
                            System.out.println("Whileloop== "+key);
                            String value = customArray.getString(key);
                            System.out.println("Whileloop value== "+value);

                            if(customArray.getString(key)!=null){
                                System.out.println("if Whileloop value== "+value);
                                DatabaseHelperCustomNew dataBaseHelpernew = new DatabaseHelperCustomNew(getActivity());
                                dataBaseHelpernew.insertUserDetails(key,value);
                            }else
                            {
                                System.out.println("else Whileloop value== "+value);
                                value="";
                                DatabaseHelperCustomNew dataBaseHelpernew = new DatabaseHelperCustomNew(getActivity());
                                dataBaseHelpernew.insertUserDetails(key,value);
                            }
                        /*   DatabaseHelperCustomNew dataBaseHelpernew = new DatabaseHelperCustomNew(getActivity());
                         // dataBaseHelper.deleteAll() ;
                            dataBaseHelpernew.insertUserDetails(key,value);
*/
                        }
                        DatabaseHelperCustomNew db = new DatabaseHelperCustomNew(getActivity());
                        ArrayList<HashMap<String, String>> myList = db.GetUsers();
                            System.out.println("myList==" +myList);
                            ListAdapter adapter = new SimpleAdapter(getActivity(), myList, R.layout.activity_listview, new String[]{"name", "location"}, new int[]{R.id.name, R.id.location});
                            list.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else { }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
}