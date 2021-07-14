package com.mkvk.smartschool.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.StudentCoursePerformance;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import java.util.ArrayList;

public class StudentCoursePerAdapter extends RecyclerView.Adapter<StudentCoursePerAdapter.MyViewHolder> {

    private StudentCoursePerformance context;
    private ArrayList<String> quiz_titleList;
    private ArrayList<String> correct_answerList;
    private ArrayList<String> idList;
    private ArrayList<String> wrong_answerList;
    private ArrayList<String> not_answerList;
    private ArrayList<String> percentageList;
    private ArrayList<String> total_questionList;

    public StudentCoursePerAdapter(StudentCoursePerformance context, ArrayList<String> quiz_titleList, ArrayList<String> idList, ArrayList<String> correct_answerList, ArrayList<String> wrong_answerList,
                                   ArrayList<String> not_answerList, ArrayList<String> percentageList, ArrayList<String> total_questionList) {

        this.context = context;
        this.quiz_titleList = quiz_titleList;
        this.idList = idList;
        this.correct_answerList = correct_answerList;
        this.wrong_answerList = wrong_answerList;
        this.not_answerList = not_answerList;
        this.percentageList = percentageList;
        this.total_questionList = total_questionList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        PieChart pieChart;
        TextView quiz_name,quiz_no,correct_count,wrong_count,noanswer_count;
        public MyViewHolder(View view) {
            super(view);
            pieChart = view.findViewById(R.id.piechart);
            quiz_name = view.findViewById(R.id.quiz_name);
            quiz_no = view.findViewById(R.id.quiz_no);
            correct_count = view.findViewById(R.id.correct_count);
            wrong_count = view.findViewById(R.id.wrong_count);
            noanswer_count = view.findViewById(R.id.noanswer_count);
        }
    }

    @Override
    public StudentCoursePerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewTypnotepe) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.courseperformance_data, parent, false);
        return new StudentCoursePerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentCoursePerAdapter.MyViewHolder holder, final int position) {
        holder.quiz_no.setText("Quiz "+(position+1));
        holder.quiz_name.setText(quiz_titleList.get(position));
        holder.correct_count.setText(correct_answerList.get(position));
        holder.wrong_count.setText(wrong_answerList.get(position));
        holder.noanswer_count.setText(not_answerList.get(position));
        // Set the data and color to the pie chart
        holder.pieChart.addPieSlice(
                new PieModel(
                        "Correct Answer",
                        Integer.parseInt(correct_answerList.get(position)),
                        Color.parseColor("#66AA18")));
        holder.pieChart.addPieSlice(
                new PieModel(
                        "Wrong Answer",
                        Integer.parseInt(wrong_answerList.get(position)),
                        Color.parseColor("#EF5350")));
        holder.pieChart.addPieSlice(
                new PieModel(
                        "Not Attempted",
                        Integer.parseInt(not_answerList.get(position)),
                        Color.parseColor("#C8C4C4")));
        // To animate the pie chart
        holder.pieChart.startAnimation();
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }
}