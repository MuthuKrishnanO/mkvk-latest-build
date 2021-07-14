package com.mkvk.smartschool.adapters;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.core.app.NotificationCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkvk.smartschool.students.StudentUploadHomework;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.OpenPdf;
import com.mkvk.smartschool.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StudentHomeworkAdapter extends RecyclerView.Adapter<StudentHomeworkAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> homeworkIdList;
    private ArrayList<String> homeworkTitleList;
    private ArrayList<String> homeworkSubjectNameList;
    private ArrayList<String> homeworkHomeworkDateList;
    private ArrayList<String> homeworkSubmissionDateList;
    private ArrayList<String> homeworkEvaluationDateList;
    private ArrayList<String> homeworkEvaluationByList;
    private ArrayList<String> homeworkCreatedByList;
    private ArrayList<String> homeworkDocumentList;
    private ArrayList<String> homeworkClasssList;
    private ArrayList<String> homeworkStatusList;
    long downloadID;
    Boolean your_date_is_outdated;

    public StudentHomeworkAdapter(FragmentActivity studentHomework, ArrayList<String> homeworkIdList,
                                  ArrayList<String> homeworkTitleList, ArrayList<String> homeworkSubjectNameList,
                                  ArrayList<String> homeworkHomeworkDateList, ArrayList<String> homeworkSubmissionDateList,
                                  ArrayList<String> homeworkEvaluationDateList, ArrayList<String> homeworkEvaluationByList,
                                  ArrayList<String> homeworkCreatedByList, ArrayList<String> homeworkDocumentList,
                                  ArrayList<String> homeworkClasssList, ArrayList<String> homeworkStatusList) {

        this.context = studentHomework;
        this.homeworkIdList = homeworkIdList;
        this.homeworkTitleList = homeworkTitleList;
        this.homeworkSubjectNameList = homeworkSubjectNameList;
        this.homeworkHomeworkDateList = homeworkHomeworkDateList;
        this.homeworkSubmissionDateList = homeworkSubmissionDateList;
        this.homeworkEvaluationDateList = homeworkEvaluationDateList;
        this.homeworkEvaluationByList = homeworkEvaluationByList;
        this.homeworkCreatedByList = homeworkCreatedByList;
        this.homeworkDocumentList = homeworkDocumentList;
        this.homeworkClasssList = homeworkClasssList;
        this.homeworkStatusList = homeworkStatusList;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectNameTV, homeworkDateTV, submissionDateTV, evaluationDateTV, statusTV;
        TextView evaluatedByTV, createdByTV, classTV, evaluationDateHeadTV, evaluatedByHeadTV;
        ImageView downloadBtn,uploadBtn;
        LinearLayout detailsBtn;
        public CardView containerView;
        LinearLayout headLay;

        public MyViewHolder(View view) {
            super(view);
            subjectNameTV = (TextView) view.findViewById(R.id.adapter_student_homework_subjectNameTV);
            classTV = (TextView) view.findViewById(R.id.adapter_student_homework_class);
            homeworkDateTV = (TextView) view.findViewById(R.id.adapter_student_homework_homeworkDateTV);
            submissionDateTV = (TextView) view.findViewById(R.id.adapter_student_homework_submissionDateTV);
            evaluationDateTV = (TextView) view.findViewById(R.id.adapter_student_homework_evluationDateTV);
            evaluatedByTV = (TextView) view.findViewById(R.id.adapter_student_homework_evaluatedByTV);
            createdByTV = (TextView) view.findViewById(R.id.adapter_student_homework_createdByTV);
            downloadBtn = (ImageView) view.findViewById(R.id.adapter_student_homework_downloadBtn);
            uploadBtn = (ImageView) view.findViewById(R.id.adapter_student_homework_uploadBtn);
            detailsBtn = (LinearLayout) view.findViewById(R.id.adapter_student_homework_detailsBtn);
            headLay = view.findViewById(R.id.adapter_student_homework_headLayout);
            statusTV = view.findViewById(R.id.adapter_student_homework_statusTV);
            evaluationDateHeadTV = view.findViewById(R.id.adapter_student_homework_evaluationDateHeadTV);
            evaluatedByHeadTV = view.findViewById(R.id.adapter_student_homework_evaluatedByHeadTV);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_homework, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        //DECORATE
        holder.headLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        holder.subjectNameTV.setText(homeworkSubjectNameList.get(position));
        holder.homeworkDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,homeworkHomeworkDateList.get(position)));
        holder.submissionDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,homeworkSubmissionDateList.get(position)));
        holder.evaluationDateTV.setText(homeworkEvaluationDateList.get(position));
        holder.evaluatedByTV.setText(homeworkEvaluationByList.get(position));
        holder.createdByTV.setText(homeworkCreatedByList.get(position));
        holder.classTV.setText(homeworkClasssList.get(position));
        context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if(homeworkEvaluationByList.get(position).equals("0")){
            holder.evaluatedByTV.setText("");
        }

        if(homeworkEvaluationDateList.get(position).equals("0")){
            holder.evaluatedByTV.setText("");
        }

        if(homeworkDocumentList.get(position).isEmpty()) {
            holder.downloadBtn.setVisibility(View.GONE);
        } else {
            holder.downloadBtn.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                urlStr += "uploads/homework/"+homeworkDocumentList.get(position);
                downloadID = Utility.beginDownload(context, homeworkDocumentList.get(position), urlStr);

                Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                intent.putExtra("imageUrl",urlStr);
                context.startActivity(intent);
            }
        });

        if(homeworkEvaluationByList.get(position).equals("null")) {
            holder.statusTV.setVisibility(View.GONE);
            holder.evaluatedByTV.setVisibility(View.GONE);
            holder.evaluationDateTV.setVisibility(View.GONE);
            holder.evaluationDateHeadTV.setVisibility(View.GONE);
            holder.evaluatedByHeadTV.setVisibility(View.GONE);
        } else {
            holder.statusTV.setVisibility(View.VISIBLE);
            holder.evaluatedByTV.setVisibility(View.VISIBLE);
            holder.evaluationDateTV.setVisibility(View.VISIBLE);
            holder.evaluationDateHeadTV.setVisibility(View.VISIBLE);
            holder.evaluatedByHeadTV.setVisibility(View.VISIBLE);
            int status=Integer.parseInt(homeworkStatusList.get(position));
            //STATUS
            if(status>0) {
                holder.statusTV.setVisibility(View.VISIBLE);
                holder.statusTV.setText("Complete");
                holder.uploadBtn.setVisibility(View.GONE);
                holder.statusTV.setBackgroundResource(R.drawable.green_border);
            } else if(homeworkStatusList.get(position).equals("0")) {
                holder.statusTV.setVisibility(View.VISIBLE);
                holder.statusTV.setText("Incomplete");
                holder.statusTV.setBackgroundResource(R.drawable.red_border);
                Calendar c = Calendar.getInstance();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String getCurrentDate = sdf.format(c.getTime());
                final String submissiondate = homeworkSubmissionDateList.get(position);
                if (submissiondate.compareTo(getCurrentDate) > 0){
                    holder.uploadBtn.setVisibility(View.VISIBLE);
                    holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context.getApplicationContext(), StudentUploadHomework.class);
                            intent.putExtra("Homework_ID",homeworkIdList.get(position));
                            context.startActivity(intent);
                        }
                    });
                }else{
                    holder.uploadBtn.setVisibility(View.GONE);
                }
            } else {
                holder.statusTV.setVisibility(View.GONE);
            }
        }
        holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewClick) {
                View view = context.getLayoutInflater().inflate(R.layout.fragment_homework_bottom_sheet, null);
                view.setMinimumHeight(500);

                TextView headerTV = view.findViewById(R.id.homework_bottomSheet_headerTV);
                ImageView crossBtn = view.findViewById(R.id.homework_bottomSheet_crossBtn);
                WebView detailsWebview = view.findViewById(R.id.homework_bottomSheet_webview);

                headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
                headerTV.setText(context.getString(R.string.homeworkDetails));
                detailsWebview.loadData(homeworkTitleList.get(position), "text/html; charset=utf-8", "UTF-8");

                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(view);
                dialog.show();

                crossBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
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
                context.unregisterReceiver(onDownloadComplete);
            }
        }
    };
    @Override
    public int getItemCount() {
        return homeworkIdList.size();
    }
}
