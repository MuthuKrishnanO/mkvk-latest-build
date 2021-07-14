package com.mkvk.smartschool.adapters;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mkvk.smartschool.students.StudentSyllabus;
import com.mkvk.smartschool.R;
import java.util.ArrayList;

public class StudentSyllabusTimetableAdapter extends RecyclerView.Adapter<StudentSyllabusTimetableAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> timeList;
    private ArrayList<String> subjectList;
    private ArrayList<String> Subjectid;
    private String date;

    public StudentSyllabusTimetableAdapter(FragmentActivity studentClassTimetable, ArrayList<String> Subject,
                                           ArrayList<String> timeList, ArrayList<String> Subjectid, String Date) {
        this.context = studentClassTimetable;
        this.timeList = timeList;
        this.subjectList =Subject;
        this.Subjectid = Subjectid;
        this.date = Date;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTV, subjectTV;
        LinearLayout headlayout,syllabusTV;
        public MyViewHolder(View view) {
            super(view);

            timeTV = (TextView) view.findViewById(R.id.adapter_student_classTimetable_timeTV);
            subjectTV = (TextView) view.findViewById(R.id.adapter_student_classTimetable_subjectTV);
            syllabusTV = (LinearLayout) view.findViewById(R.id.adapter_student_classTimetable_syllabusTV);
            headlayout = (LinearLayout) view.findViewById(R.id.headlayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_lessonplan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.timeTV.setText(timeList.get(position));
        holder.subjectTV.setText(subjectList.get(position));
        holder.headlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(), StudentSyllabus.class);
                intent.putExtra("Subjectid",Subjectid.get(position));
                intent.putExtra("Date",date);
                intent.putExtra("Time",timeList.get(position));
                intent.putExtra("Subject",subjectList.get(position));
                context.startActivity(intent);
                System.out.println("Subjectid== "+Subjectid.get(position));
                System.out.println("Time== "+timeList.get(position));
                System.out.println("Date== "+date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }
}

