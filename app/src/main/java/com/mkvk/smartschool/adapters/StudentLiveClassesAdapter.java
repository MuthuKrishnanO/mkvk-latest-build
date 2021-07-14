package com.mkvk.smartschool.adapters;

import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.LiveClasses;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentLiveClassesAdapter extends RecyclerView.Adapter<StudentLiveClassesAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> titleList;
    private ArrayList<String> dateList;
    private ArrayList<String> classList;
    private ArrayList<String> idList;
    private ArrayList<String> join_url;
    private ArrayList<String> statuslist;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();

    public StudentLiveClassesAdapter(FragmentActivity studentonlineexam, ArrayList<String> titleList, ArrayList<String> dateList,
                                     ArrayList<String> classList,ArrayList<String> idList,ArrayList<String> join_url,ArrayList<String> statuslist) {

        this.context = studentonlineexam;
        this.titleList = titleList;
        this.dateList = dateList;
        this.classList = classList;
        this.idList = idList;
        this.join_url = join_url;
        this.statuslist = statuslist;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titlename,date,classes,status;
        LinearLayout joinclass;
        RelativeLayout headLayout;

        public MyViewHolder(View view) {
            super(view);
            joinclass = view.findViewById(R.id.adapter_student_joinclass);
            titlename = view.findViewById(R.id.adapter_student_titlename);
            headLayout = view.findViewById(R.id.adapter_student_headLayout);
            status = view.findViewById(R.id.status);
            date = view.findViewById(R.id.date);
            classes = view.findViewById(R.id.classes);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_liveclass, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.titlename.setText(titleList.get(position));
        holder.date.setText(dateList.get(position));
        holder.classes.setText(classList.get(position));

        if(statuslist.get(position).equals("0")){
            holder.status.setText("Awaited");
            holder.status.setBackgroundResource(R.drawable.yellow_border);
            holder.joinclass.setVisibility(View.VISIBLE);
            holder.joinclass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context.getApplicationContext(), LiveClasses.class);
                    intent.putExtra("JoinUrl",join_url.get(position));
                    context.startActivity(intent);

                    if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                        params.put("student_id", Utility.getSharedPreferences(context.getApplicationContext(), Constants.studentId));
                        params.put("conference_id", idList.get(position));
                        JSONObject obj=new JSONObject(params);
                        Log.e("parametres ", obj.toString());
                        getDataFromApi(obj.toString());
                    } else {
                        makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(statuslist.get(position).equals("2")){
            holder.status.setText("Finished");
            holder.joinclass.setVisibility(View.GONE);
            holder.status.setBackgroundResource(R.drawable.green_border);
        }else{
          holder.status.setText("Cancelled");
          holder.joinclass.setVisibility(View.GONE);
          holder.status.setBackgroundResource(R.drawable.red_border);
        }
    }
    @Override
    public int getItemCount() {
        return idList.size();
    }

    private void getDataFromApi (String bodyParams) {
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl")+Constants.livehistoryUrl;
        Log.e("livehistoryUrl==", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                 if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        Toast.makeText(context,""+obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(context.getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context.getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context.getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }
}
