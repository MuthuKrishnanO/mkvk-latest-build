package com.mkvk.smartschool.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.mkvk.smartschool.OpenPdf;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.StudentAppyLeave;
import com.mkvk.smartschool.students.StudentEditLeave;
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

public class StudentApplyLeaveAdapter extends RecyclerView.Adapter<StudentApplyLeaveAdapter.MyViewHolder> {

    private StudentAppyLeave context;
    private ArrayList<String> nameList;
    private ArrayList<String> fromList;
    private ArrayList<String> toList;
    private ArrayList<String> statuslist;
    private ArrayList<String> idlist;
    private ArrayList<String> apply_datelist;
    private ArrayList<String> docslist;
    private ArrayList<String> reasonlist;
    private ArrayList<String> sapplylist;
    private ArrayList<String> sfromlist;
    private ArrayList<String> stolist;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    private boolean isapplyDateSelected = false;
    private boolean isfromDateSelected = false;
    private boolean istoDateSelected = false;
    String apply_date="";
    String from_date="";
    String to_date="";
    public String defaultDateFormat;
    long downloadID;

    public StudentApplyLeaveAdapter(StudentAppyLeave studentapplyleave, ArrayList<String> nameList, ArrayList<String> fromList,
                                    ArrayList<String> toList, ArrayList<String> statuslist, ArrayList<String> idlist,ArrayList<String> apply_datelist,ArrayList<String> docslist,
                                    ArrayList<String> reasonlist,ArrayList<String> sfromlist,ArrayList<String> stolist,ArrayList<String> sapplylist) {

        this.context = studentapplyleave;
        this.nameList = nameList;
        this.fromList = fromList;
        this.toList = toList;
        this.statuslist = statuslist;
        this.idlist = idlist;
        this.apply_datelist = apply_datelist;
        this.docslist = docslist;
        this.reasonlist = reasonlist;
        this.stolist = stolist;
        this.sfromlist = sfromlist;
        this.sapplylist = sapplylist;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fromdate,todate,Pending,Approve,Applied_date;
        ImageView delete,edit,downloadBtn;
        RelativeLayout studentleave_headLayout;

        public MyViewHolder(View view) {
            super(view);
            fromdate = view.findViewById(R.id.fromdate);
            todate = view.findViewById(R.id.todate);
            Applied_date = view.findViewById(R.id.applied_date);
            studentleave_headLayout = view.findViewById(R.id.adapter_studentleave_headLayout);
            Approve = view.findViewById(R.id.Approve);
            delete = view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);
            downloadBtn = view.findViewById(R.id.downloadBtn);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_applyleavet, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.studentleave_headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.fromdate.setText(fromList.get(position));
        holder.todate.setText(toList.get(position));
        holder.Applied_date.setText("Apply Date - "+apply_datelist.get(position));

       if(statuslist.get(position).equals("0")){
           holder.delete.setVisibility(View.VISIBLE);
           holder.edit.setVisibility(View.VISIBLE);
           holder.Approve.setText("Pending");
           holder.Approve.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.yellow_border));
       }else {
           holder.Approve.setText("Approved");
           holder.delete.setVisibility(View.GONE);
           holder.edit.setVisibility(View.GONE);
           holder.Approve.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_border));
       }

        if(docslist.get(position).equals("")){
            holder.downloadBtn.setVisibility(View.GONE);
        }else{
            holder.downloadBtn.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                urlStr += "uploads/student_leavedocuments/"+docslist.get(position);
                downloadID = Utility.beginDownload(context, docslist.get(position), urlStr);
                Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                intent.putExtra("imageUrl",urlStr);
                context.startActivity(intent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage(R.string.deleteMsg);
                builder.setTitle("");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                            params.put("leave_id", idlist.get(position));
                            JSONObject obj=new JSONObject(params);
                            Log.e("params ", obj.toString());
                            deleteDataFromApi(obj.toString());//Api Call
                            ((Activity)context).finish();
                        } else {
                            makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                        }


                        Intent intent = new Intent(context, StudentAppyLeave.class);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context.getApplicationContext(), StudentEditLeave.class);
                intent.putExtra("fromlist",sfromlist.get(position));
                intent.putExtra("tolist",stolist.get(position));
                intent.putExtra("applylist",sapplylist.get(position));
                intent.putExtra("reasonlist",reasonlist.get(position));
                intent.putExtra("idlist",idlist.get(position));
                context.startActivity(intent);
            }
        });
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

            }
        }
    };
    private void deleteDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl")+Constants.deleteLeaveUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        String message = object.getString("msg");
                        pd.dismiss();
                        Toast.makeText(context.getApplicationContext()," "+message, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

    @Override
    public int getItemCount() {
        return idlist.size();
    }
}