package com.mkvk.smartschool.adapters;

import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mkvk.smartschool.students.StudentSyllabuslesson;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentSyllabusStatusAdapter extends RecyclerView.Adapter<StudentSyllabusStatusAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> subject_nameList;
    private ArrayList<String> total_completeList;
    private ArrayList<String> classList;
    private ArrayList<String> idList;
    private ArrayList<String> subjectidList;
    private ArrayList<String> totalList;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    ArrayList<String> lessonidList = new ArrayList<String>();
    ArrayList<String> lesson_total_completeList = new ArrayList<String>();
    ArrayList<String> lesson_nameList = new ArrayList<String>();
    StudentLessonTopicAdapter adapter;

    public StudentSyllabusStatusAdapter(FragmentActivity studentonlineexam, ArrayList<String> subject_nameList, ArrayList<String> total_completeList,
                                        ArrayList<String> idList ,ArrayList<String> subjectidList ,ArrayList<String> totalList) {

        this.context = studentonlineexam;
        this.subject_nameList = subject_nameList;
        this.total_completeList = total_completeList;
        this.idList = idList;
        this.subjectidList = subjectidList;
        this.totalList = totalList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subject,status;
        LinearLayout lesson_topic,nameHeader;

        public MyViewHolder(View view) {
            super(view);
            subject = view.findViewById(R.id.adapter_student_subject_nameTV);
            status = view.findViewById(R.id.adapter_student_status_nameTV);
            lesson_topic = view.findViewById(R.id.adapter_student_lesson);
            nameHeader = view.findViewById(R.id.adapter_student_examList_nameHeader);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_syllabs_status, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.nameHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.subject.setText(subject_nameList.get(position));

        if (totalList.get(position).equals("0")) {
            holder.status.setText(totalList.get(position)+"% Completed");
        }else {
            Float total_comp = Float.parseFloat(total_completeList.get(position));
            Float total = Float.parseFloat(totalList.get(position));
            Float complete = (total_comp / total);
            Float complete_per = (complete * 100);
            System.out.println("total_comp== " + total_comp + " total== " + total + " complete==" + complete + " complete_per==" + complete_per);
            holder.status.setText(String.valueOf(Math.round(complete_per)) + "% Completed");
        }
                holder.lesson_topic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context.getApplicationContext(), StudentSyllabuslesson.class);
                        intent.putExtra("SubjectList",subjectidList.get(position));
                        intent.putExtra("SectionIdlist",idList.get(position));
                        context.startActivity(intent);

                    }
                });
    }
    @Override
    public int getItemCount() {
        return idList.size();
    }

}
