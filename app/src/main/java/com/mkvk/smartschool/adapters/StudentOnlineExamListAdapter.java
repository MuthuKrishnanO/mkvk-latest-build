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
import com.google.android.material.snackbar.Snackbar;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.StudentOnlineExamQuestionsNew;
import com.mkvk.smartschool.students.StudentOnlineExamResult;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StudentOnlineExamListAdapter extends RecyclerView.Adapter<StudentOnlineExamListAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> examList;
    private ArrayList<String> exam_fromList;
    private ArrayList<String> exam_toList;
    private ArrayList<String> durationList;
    private ArrayList<String> attemptList;
    private ArrayList<String> attemptslist;
    private ArrayList<String> onlineexam_idlist;
    private ArrayList<String> publish_resultlist;
    private ArrayList<String> is_submittedlist;
    private ArrayList<String> is_quizlist;
    private ArrayList<String> attemptedlist;
    private ArrayList<String> is_marks_displaylist;
    private ArrayList<String> is_neg_markinglist;
    private ArrayList<String> passing_percentagelist;
    private ArrayList<String> total_descriptivelist;
    private ArrayList<String> total_questionlist;
    private ArrayList<String> onlineexam_student_idlist;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    RecyclerView recyclerView;

    public StudentOnlineExamListAdapter(FragmentActivity studentonlineexam,RecyclerView recyclerView,ArrayList<String> examList, ArrayList<String> exam_fromList,
                                        ArrayList<String> exam_toList, ArrayList<String> durationList, ArrayList<String> attemptList,
                                        ArrayList<String> attemptslist, ArrayList<String> onlineexam_idlist, ArrayList<String> publish_resultlist,ArrayList<String> is_submittedlist,
                                        ArrayList<String> onlineexam_student_idlist,ArrayList<String> is_quizlist,ArrayList<String> attemptedlist,
                                        ArrayList<String> is_marks_displaylist,ArrayList<String> is_neg_markinglist,ArrayList<String> passing_percentagelist,ArrayList<String> total_questionlist,ArrayList<String> total_descriptivelist) {
        this.context = studentonlineexam;
        this.recyclerView = recyclerView;
        this.examList = examList;
        this.exam_fromList = exam_fromList;
        this.exam_toList = exam_toList;
        this.durationList = durationList;
        this.attemptList = attemptList;
        this.attemptslist = attemptslist;
        this.attemptedlist = attemptedlist;
        this.is_quizlist = is_quizlist;
        this.onlineexam_idlist = onlineexam_idlist;
        this.publish_resultlist = publish_resultlist;
        this.is_submittedlist = is_submittedlist;
        this.is_neg_markinglist = is_neg_markinglist;
        this.is_marks_displaylist = is_marks_displaylist;
        this.onlineexam_student_idlist = onlineexam_student_idlist;
        this.passing_percentagelist = passing_percentagelist;
        this.total_questionlist = total_questionlist;
        this.total_descriptivelist = total_descriptivelist;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView examname,datefrom,dateto,totalattempts,attempted,duration,quiz,status,passing_per,descriptive_ques,total_ques;
        LinearLayout startexam,viewresult;
        RelativeLayout headLayout;

        public MyViewHolder(View view) {
            super(view);
            startexam = view.findViewById(R.id.adapter_student_onlineexam_startexam);
            viewresult = view.findViewById(R.id.adapter_student_onlineexam_viewresult);
            examname = view.findViewById(R.id.adapter_student_onlineexam_name);
            datefrom = view.findViewById(R.id.datefrom);
            headLayout = view.findViewById(R.id.adapter_student_headLayout);
            dateto = view.findViewById(R.id.dateto);
            totalattempts = view.findViewById(R.id.totalattempts);
            attempted = view.findViewById(R.id.attempted);
            duration = view.findViewById(R.id.duration);
            quiz = view.findViewById(R.id.quiz);
            status = view.findViewById(R.id.status);
            passing_per = view.findViewById(R.id.passing_per);
            descriptive_ques = view.findViewById(R.id.descriptive_ques);
            total_ques = view.findViewById(R.id.total_ques);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_examlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String defaultDatetimeFormat = Utility.getSharedPreferences(context.getApplicationContext(), "datetimeFormat");

        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.examname.setText(examList.get(position));

        holder.datefrom.setText(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDatetimeFormat,exam_fromList.get(position)));
        holder.dateto.setText(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDatetimeFormat,exam_toList.get(position)));
        holder.duration.setText(durationList.get(position));

        holder.totalattempts.setText(attemptList.get(position));
        holder.passing_per.setText(passing_percentagelist.get(position));
        holder.attempted.setText(attemptslist.get(position));
        holder.total_ques.setText(total_questionlist.get(position));
        holder.descriptive_ques.setText(total_descriptivelist.get(position));

        if (Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")) {
            holder.startexam.setVisibility(View.GONE);
        }


        if(is_quizlist.get(position).equals("1")) {
            holder.quiz.setText("Yes");
            holder.status.setText("Available");
            if(attemptedlist.get(position).equals("1")){
                holder.startexam.setVisibility(View.GONE);
                holder.viewresult.setVisibility(View.VISIBLE);

                holder.viewresult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamResult.class);
                        intent.putExtra("OnlineExam_students_Id",onlineexam_student_idlist.get(position));
                        intent.putExtra("exams_id",onlineexam_idlist.get(position));
                        context.startActivity(intent);
                        System.out.println("onlineexam_idlist=="+onlineexam_idlist.get(position));
                        System.out.println("onlineexam_student_idlist=="+onlineexam_student_idlist.get(position));
                    }
                });

            }else{
                holder.viewresult.setVisibility(View.GONE);
                holder.viewresult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar = Snackbar.make(recyclerView, "You have submitted the Exam",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
                Calendar c = Calendar.getInstance();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String getCurrentDateTime = sdf.format(c.getTime());
                final String getstartTime=exam_fromList.get(position);
                final String getendTime=exam_toList.get(position);

                if (getCurrentDateTime.compareTo(getstartTime) < 0||getCurrentDateTime.compareTo(getendTime)>0) {
                    holder.startexam.setVisibility(View.GONE);

                }else{
                    if (Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")) {
                        holder.startexam.setVisibility(View.GONE);
                    }else{
                        if (Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")) {
                            holder.startexam.setVisibility(View.GONE);
                        }else{
                            holder.startexam.setVisibility(View.VISIBLE);
                        }
                    }
                }
                holder.startexam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(attemptList.get(position).equals(attemptslist.get(position))){
                            Snackbar snackbar = Snackbar.make(recyclerView, "You have reached total limits",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }else{

                             if (getCurrentDateTime.compareTo(getstartTime) > 0||getCurrentDateTime.compareTo(getstartTime) == 0) {
                                Date d1 = null;
                                Date d2 = null;
                                try {
                                    d1 = sdf.parse(getCurrentDateTime);
                                    d2 = sdf.parse(getendTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long diff = d2.getTime() - d1.getTime();

                                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diff),
                                        TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                                        TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));

                                String duration= durationList.get(position);
                                String time_duration= hms;
                                System.out.println("time_duration= "+time_duration+" duration= "+duration);

                                String gettimedurationTime=time_duration;
                                String getdurationTime=duration;

                                if (gettimedurationTime.compareTo(getdurationTime) < 0) {
                                    Log.d("Return","gettimedurationTime");
                                    Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                    intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                    intent.putExtra("durationList",gettimedurationTime);
                                    intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                    context.startActivity(intent);
                                } else {
                                    Log.d("Return","getdurationTime");
                                    Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                    intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                    intent.putExtra("durationList",durationList.get(position));
                                    intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                    context.startActivity(intent);
                                }
                            } else if (getCurrentDateTime.compareTo(getendTime) < 0||getCurrentDateTime.compareTo(getendTime) == 0) {

                                Date d1 = null;
                                Date d2 = null;
                                try {
                                    d1 = sdf.parse(getCurrentDateTime);
                                    d2 = sdf.parse(getendTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long diff = d2.getTime() - d1.getTime();
                                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diff),
                                        TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                                        TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));

                                String time_duration= hms;
                                String duration= durationList.get(position);
                                System.out.println("time_duration= "+time_duration+" duration= "+duration);
                                String gettimedurationTime=sdf.format(time_duration);
                                String getdurationTime=sdf.format(duration);

                                if (gettimedurationTime.compareTo(getdurationTime) < 0) {
                                    Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                    intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                    intent.putExtra("durationList",gettimedurationTime);
                                    intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                    context.startActivity(intent);
                                    Log.d("Return","gettimedurationTime");
                                } else {
                                    Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                    intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                    intent.putExtra("durationList",durationList.get(position));
                                    intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                    context.startActivity(intent);
                                    Log.d("Return","getdurationTime");
                                }
                            } else {
                                 Snackbar snackbar = Snackbar.make(recyclerView, "You have reached total attemps or exam date passed, Please contact to administrator.",Snackbar.LENGTH_SHORT);
                                 snackbar.show();
                                 Log.d("Return","getMyTime older than getCurrentDateTime");
                            }
                        }
                    }
                });
            }

        }else{
            holder.quiz.setText("No");
            if(publish_resultlist.get(position).equals("1")){
                holder.status.setText("Result Published");
                holder.startexam.setVisibility(View.GONE);
                holder.viewresult.setVisibility(View.VISIBLE);

                holder.viewresult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamResult.class);
                        intent.putExtra("OnlineExam_students_Id",onlineexam_student_idlist.get(position));
                        intent.putExtra("exams_id",onlineexam_idlist.get(position));
                        context.startActivity(intent);
                        System.out.println("onlineexam_idlist=="+onlineexam_idlist.get(position));
                        System.out.println("onlineexam_student_idlist=="+onlineexam_student_idlist.get(position));
                    }
                });


            }else{

                if (attemptedlist.get(position).equals("1")){
                    holder.status.setText("Available");
                    holder.startexam.setVisibility(View.GONE);
                    holder.viewresult.setVisibility(View.VISIBLE);

                    holder.viewresult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar snackbar = Snackbar.make(recyclerView, "You have submitted the Exam",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                }else {
                    holder.status.setText("Available");
                    holder.viewresult.setVisibility(View.GONE);
                    Calendar c = Calendar.getInstance();
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final String getCurrentDateTime = sdf.format(c.getTime());
                    final String getstartTime=exam_fromList.get(position);
                    final String getendTime=exam_toList.get(position);
                    Log.d("getCurrentDateTime",getCurrentDateTime);
                    System.out.println("getCurrentDateTime="+getCurrentDateTime);

                    if (getCurrentDateTime.compareTo(getstartTime) < 0||getCurrentDateTime.compareTo(getendTime)>0) {
                        holder.startexam.setVisibility(View.GONE);
                    }else{
                        if (Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")) {
                            holder.startexam.setVisibility(View.GONE);
                        }else{
                            holder.startexam.setVisibility(View.VISIBLE);
                        }
                    }
                    holder.startexam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(attemptList.get(position).equals(attemptslist.get(position))){
                                Snackbar snackbar = Snackbar.make(recyclerView, "You have reached total limits",Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }else{
                                if (getCurrentDateTime.compareTo(getstartTime) > 0||getCurrentDateTime.compareTo(getstartTime) == 0) {
                                    Date d1 = null;
                                    Date d2 = null;
                                    try {
                                        d1 = sdf.parse(getCurrentDateTime);
                                        d2 = sdf.parse(getendTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long diff = d2.getTime() - d1.getTime();

                                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diff),
                                            TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                                            TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));

                                    String duration= durationList.get(position);
                                    String time_duration= hms;
                                    System.out.println("time_duration= "+time_duration+" duration= "+duration);

                                    String gettimedurationTime=time_duration;
                                    String getdurationTime=duration;

                                    if (gettimedurationTime.compareTo(getdurationTime) < 0) {
                                        Log.d("Return","gettimedurationTime");
                                        Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                        intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                        intent.putExtra("durationList",gettimedurationTime);
                                        intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                        context.startActivity(intent);
                                    } else {
                                        Log.d("Return","getdurationTime ");
                                        Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                        intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                        intent.putExtra("durationList",durationList.get(position));
                                        intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                        context.startActivity(intent);
                                    }
                                } else if (getCurrentDateTime.compareTo(getendTime) < 0||getCurrentDateTime.compareTo(getendTime) == 0) {
                                    Date d1 = null;
                                    Date d2 = null;
                                    try {
                                        d1 = sdf.parse(getCurrentDateTime);
                                        d2 = sdf.parse(getendTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long diff = d2.getTime() - d1.getTime();
                                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(diff),
                                            TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                                            TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));


                                    String time_duration= hms;
                                    String duration= durationList.get(position);
                                    System.out.println("time_duration= "+time_duration+" duration= "+duration);

                                    String gettimedurationTime=sdf.format(time_duration);
                                    String getdurationTime=sdf.format(duration);

                                    if (gettimedurationTime.compareTo(getdurationTime) < 0) {
                                        Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                        intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                        intent.putExtra("durationList",gettimedurationTime);
                                        intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                        context.startActivity(intent);
                                        Log.d("Return","gettimedurationTime");
                                    } else {
                                        Intent intent=new Intent(context.getApplicationContext(), StudentOnlineExamQuestionsNew.class);
                                        intent.putExtra("Online_Exam_Id",onlineexam_idlist.get(position));
                                        intent.putExtra("durationList",durationList.get(position));
                                        intent.putExtra("onlineexam_student_idlist",onlineexam_student_idlist.get(position));
                                        context.startActivity(intent);
                                        Log.d("Return","getdurationTime ");
                                    }
                                } else {
                                    Snackbar snackbar = Snackbar.make(recyclerView, "You have reached total attemps or exam date passed, Please contact to administrator.",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    Log.d("Return","getMyTime older than getCurrentDateTime");
                                }
                            }
                        }
                    });
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return onlineexam_idlist.size();
    }


}
