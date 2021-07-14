package com.mkvk.smartschool.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mkvk.smartschool.R;
import java.util.ArrayList;

public class StudentLessonTopicAdapter extends RecyclerView.Adapter<StudentLessonTopicAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> lesson_nameList;
    private ArrayList<String> lesson_total_completeList;
    private ArrayList<String> lessonidList;



    public StudentLessonTopicAdapter(Context applicationContext, ArrayList<String> topicnameList, ArrayList<String> topicidList,
                                     ArrayList<String> statusList) {
        this.context = applicationContext;

        this.lesson_nameList = topicnameList;
        this.lesson_total_completeList = topicidList;
        this.lessonidList = statusList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView Exam, Percentage,subject,Roomno;
        RecyclerView topic_recyclerview;
        LinearLayout viewdetail;
        public MyViewHolder(View view) {
            super(view);
            Exam = (TextView) view.findViewById(R.id.Exam);
            Percentage = (TextView) view.findViewById(R.id.Percentage);
            subject = (TextView) view.findViewById(R.id.subject);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_consolidate_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.Exam.setText(lesson_nameList.get(position));
        holder.Percentage.setText(lesson_total_completeList.get(position));


    }

    @Override
    public int getItemCount() {
        return lessonidList.size();
    }
}

