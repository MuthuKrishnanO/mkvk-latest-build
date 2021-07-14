package com.mkvk.smartschool.adapters;

import android.content.Intent;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mkvk.smartschool.students.StudentExamSchedule;
import com.mkvk.smartschool.students.StudentExamSchedule_ExamList;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import java.util.ArrayList;

public class StudentExamSchedule_ExamListAdapter extends RecyclerView.Adapter<StudentExamSchedule_ExamListAdapter.MyViewHolder> {

    StudentExamSchedule_ExamList context;
    ArrayList<String> examNameList;
    ArrayList<String> examIdList;

    public StudentExamSchedule_ExamListAdapter(StudentExamSchedule_ExamList studentExamSchedule_examList,
                                               ArrayList<String> examIdList, ArrayList<String> examNameList) {
        this.context = studentExamSchedule_examList;
        this.examIdList = examIdList;
        this.examNameList = examNameList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView examNameTV;
        public CardView viewContainer;
        public LinearLayout nameHeader, viewBtn;

        public MyViewHolder(View view) {
            super(view);
            examNameTV = (TextView) view.findViewById(R.id.adapter_student_examSchedule_examList_nameTV);
            viewContainer = (CardView) view.findViewById(R.id.adapter_student_exam_schedule_exam_list);
            nameHeader = view.findViewById(R.id.adapter_student_examSchedule_examList_nameHeader);
            viewBtn = view.findViewById(R.id.adapter_studentHostel_viewBtn);
        }
     }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_exam_schedule_exam_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.examNameTV.setText(examNameList.get(position));
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent asd = new Intent(context.getApplicationContext(), StudentExamSchedule.class);
                asd.putExtra("examId", examIdList.get(position));
                context.startActivity(asd);
            }
        });
        holder.nameHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
    }

    @Override
    public int getItemCount() {
        return examNameList.size();
    }
}