package com.mkvk.smartschool.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.StudentAttendance;
import java.util.ArrayList;

public class StudentSubjectAttendenceAdapter extends RecyclerView.Adapter<StudentSubjectAttendenceAdapter.MyViewHolder> {

    private StudentAttendance context;
    private ArrayList<String> subjectList;
    private ArrayList<String> time_toList;
    private ArrayList<String> timeList;
    private ArrayList<String> roomList;
    private ArrayList<String> typeList;
    private ArrayList<String> remarkList;

    public StudentSubjectAttendenceAdapter(StudentAttendance studentExamSchedule, ArrayList<String> subjectList, ArrayList<String> timeList, ArrayList<String> roomList, ArrayList<String> typeList, ArrayList<String> remarkList) {
        this.context = studentExamSchedule;
        this.subjectList = subjectList;
        this.timeList = timeList;
        this.roomList = roomList;
        this.typeList = typeList;
        this.remarkList = remarkList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subject,time,roomno,attendence,remark;

        public MyViewHolder(View view) {
            super(view);

            subject = view.findViewById(R.id.subject);
            time = (TextView) view.findViewById(R.id.time);
            roomno = (TextView) view.findViewById(R.id.roomno);
            attendence = (TextView) view.findViewById(R.id.attendence);
            remark = (TextView) view.findViewById(R.id.remark);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_subject_atten, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.subject.setText(subjectList.get(position));
        holder.time.setText(timeList.get(position));
        holder.roomno.setText("(Room Number: "+roomList.get(position)+")");


        if(remarkList.get(position).equals("")){
            holder.remark.setVisibility(View.GONE);
        }else{
            holder.remark.setVisibility(View.VISIBLE);
            holder.remark.setText("("+remarkList.get(position)+")");
        }

        if(typeList.get(position).equals("Present")){
            holder.attendence.setText("P");
            holder.attendence.setTextColor(Color.parseColor("#66aa18"));
        }else if(typeList.get(position).equals("Absent")){
            holder.attendence.setText("A");
            holder.attendence.setTextColor(Color.parseColor("#FA0000"));
        }else if(typeList.get(position).equals("Half Day")){
            holder.attendence.setText("F");
            holder.attendence.setTextColor(Color.parseColor("#ff9500"));


        }else if(typeList.get(position).equals("Late")){
            holder.attendence.setText("L");
            holder.attendence.setTextColor(Color.parseColor("#5A3429"));
        }else if(typeList.get(position).equals("Holiday")){
            holder.attendence.setText("H");
            holder.attendence.setTextColor(Color.parseColor("#5b5b5b"));
        }else{
            holder.attendence.setText("-");
        }

    }
    @Override
    public int getItemCount() {
        return subjectList.size();
    }
}
