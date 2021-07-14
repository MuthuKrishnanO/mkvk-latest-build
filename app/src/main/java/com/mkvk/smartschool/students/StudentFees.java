package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.adapters.StudentFeesAdapter;
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

public class StudentFees extends BaseActivity {

    RecyclerView feesList;
    StudentFeesAdapter adapter;
    TextView gtAmtTV, gtDiscountTV, gtFineTV, gtPaidTV, gtBalanceTV,gtamtfineTV;
    CardView grandTotalLay;
    ArrayList <String> feesIdList = new ArrayList<String>();
    ArrayList <String> feesCodeList = new ArrayList<String>();
    ArrayList <String> feesnameList = new ArrayList<String>();
    ArrayList <String> dueDateList = new ArrayList<String>();
    ArrayList <String> amtList = new ArrayList<String>();
    ArrayList <String> amtfineList = new ArrayList<String>();
    ArrayList <String> paidAmtList = new ArrayList<String>();
    ArrayList <String> discAmtList = new ArrayList<String>();
    ArrayList <String> fineAmtList = new ArrayList<String>();
    ArrayList <String> balanceAmtList = new ArrayList<String>();
    ArrayList <String> statusList = new ArrayList<String>();
    ArrayList <String> feesDepositIdList = new ArrayList<String>();
    ArrayList <String> feesDetails = new ArrayList<String>();
    ArrayList <String> feesTypeId = new ArrayList<String>();
    ArrayList <String> feesCat = new ArrayList<String>();
    ArrayList <String> discountNameList = new ArrayList<String>();
    ArrayList <String> discountAmtList = new ArrayList<String>();
    ArrayList <String> discountpayment_idList = new ArrayList<String>();
    ArrayList <String> discountStatusList = new ArrayList<String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    TextView headerTV;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.students_fees_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.fees));

        feesList = (RecyclerView) findViewById(R.id.studentFees_listview);

        gtAmtTV = findViewById(R.id.fees_amtTV);
        gtamtfineTV = findViewById(R.id.fees_amtfineTV);
        gtDiscountTV = findViewById(R.id.fees_discountTV);
        gtFineTV = findViewById(R.id.fees_fineTV);
        gtPaidTV = findViewById(R.id.fees_paidTV);
        gtBalanceTV = findViewById(R.id.fees_balance);
        grandTotalLay = findViewById(R.id.feesAdapter_containerCV);

        headerTV = findViewById(R.id.fees_headTV);

        adapter = new StudentFeesAdapter(StudentFees.this, feesIdList, feesnameList,feesCodeList, dueDateList, amtList,
                paidAmtList, balanceAmtList, feesDepositIdList, statusList, feesDetails, feesTypeId, feesCat,
                discountNameList, discountAmtList, discountStatusList,discountpayment_idList,discAmtList,fineAmtList,amtfineList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        feesList.setLayoutManager(mLayoutManager);
        feesList.setItemAnimator(new DefaultItemAnimator());
        feesList.setAdapter(adapter);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });
        loaddata();
        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
    }
    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();
        // do some stuff here
    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getFeesUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        feesIdList.clear();
                        feesCodeList.clear();
                        dueDateList.clear();
                        amtList.clear();
                        paidAmtList.clear();
                        discAmtList.clear();
                        fineAmtList.clear();
                        balanceAmtList.clear();
                        feesDepositIdList.clear();
                        feesTypeId.clear();
                        feesCat.clear();
                        statusList.clear();
                        feesDetails.clear();
                        amtfineList.clear();

                        String success = "1"; //object.getString("success");
                        if (success.equals("1")) {

                            if(object.getString("pay_method").equals("0")) {
                                Log.e("test", "testing");
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showPaymentBtn, false);
                            } else {
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showPaymentBtn, true);
                            }
                            String  currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
                            JSONObject grandTotalDetails = object.getJSONObject("grand_fee");
                            gtAmtTV.setText(currency + grandTotalDetails.getString("amount"));
                            gtamtfineTV.setText("+"+grandTotalDetails.getString("fee_fine"));
                            gtDiscountTV.setText(currency + grandTotalDetails.getString("amount_discount"));
                            gtFineTV.setText(currency + grandTotalDetails.getString("amount_fine"));
                            gtPaidTV.setText(currency + grandTotalDetails.getString("amount_paid"));
                            gtBalanceTV.setText(currency + grandTotalDetails.getString("amount_remaining"));

                            JSONArray dataArray = object.getJSONArray("student_due_fee");

                            if(dataArray.length() != 0) {
                                grandTotalLay.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.noData, Toast.LENGTH_LONG).show();
                            }

                            for(int i = 0; i < dataArray.length(); i++) {

                                JSONArray feesArray = dataArray.getJSONObject(i).getJSONArray("fees");

                                for(int j = 0; j<feesArray.length(); j++) {
                                    feesIdList.add(feesArray.getJSONObject(j).getString("id"));
                                    feesnameList.add(feesArray.getJSONObject(j).getString("name") + "-" + feesArray.getJSONObject(j).getString("type") );
                                    feesCodeList.add(feesArray.getJSONObject(j).getString("code"));

                                    dueDateList.add(feesArray.getJSONObject(j).getString("due_date"));
                                    amtList.add( currency + feesArray.getJSONObject(j).getString("amount"));
                                    amtfineList.add("+"+feesArray.getJSONObject(j).getString("fees_fine_amount"));
                                    paidAmtList.add(currency + feesArray.getJSONObject(j).getString("total_amount_paid"));
                                    discAmtList.add(currency + feesArray.getJSONObject(j).getString("total_amount_discount"));
                                    fineAmtList.add(currency + feesArray.getJSONObject(j).getString("total_amount_fine"));
                                    balanceAmtList.add(currency + feesArray.getJSONObject(j).getString("total_amount_remaining"));
                                    feesDepositIdList.add(feesArray.getJSONObject(j).getString("student_fees_deposite_id"));
                                    feesTypeId.add(feesArray.getJSONObject(j).getString("fee_groups_feetype_id"));
                                    feesCat.add("fees");

                                    discountNameList.add("");
                                    discountAmtList.add("");
                                    discountStatusList.add("");

                                    statusList.add(feesArray.getJSONObject(j).getString("status").substring(0, 1).toUpperCase() + feesArray.getJSONObject(j).getString("status").substring(1));
                                    JSONObject feesDetailsJson;
                                    try {
                                        feesDetailsJson = new JSONObject(feesArray.getJSONObject(j).getString("amount_detail"));
                                    } catch (JSONException e) {
                                        feesDetailsJson = new JSONObject();
                                    }
                                    feesDetails.add(feesDetailsJson.toString());
                                }
                            }
                            JSONArray discountArray = object.getJSONArray("student_discount_fee");

                            for (int i = 0; i < discountArray.length(); i++) {
                                feesIdList.add(discountArray.getJSONObject(i).getString("id")+"discount");
                                discountNameList.add(discountArray.getJSONObject(i).getString("code"));
                                discountAmtList.add(discountArray.getJSONObject(i).getString("amount"));
                                //discountpayment_idList.add(discountArray.getJSONObject(i).getString("payment_id"));

                                 if(discountArray.getJSONObject(i).getString("payment_id").equals("")){
                                     discountStatusList.add(discountArray.getJSONObject(i).getString("status").substring(0, 1).toUpperCase() + discountArray.getJSONObject(i).getString("status").substring(1));
                                 }else {
                                     discountStatusList.add(discountArray.getJSONObject(i).getString("status").substring(0, 1).toUpperCase() + discountArray.getJSONObject(i).getString("status").substring(1) + " - " + discountArray.getJSONObject(i).getString("payment_id"));
                                 }
                                //discountStatusList.add(discountArray.getJSONObject(i).getString("status")+ " - "+discountArray.getJSONObject(i).getString("payment_id"));
                                feesCat.add("discount");
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    pd.dismiss();
                    pullToRefresh.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentFees.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentFees.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
}
