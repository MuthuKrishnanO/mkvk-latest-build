package com.mkvk.smartschool.adapters;

import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.mkvk.smartschool.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentOnlineQuizResultAdapter extends RecyclerView.Adapter<StudentOnlineQuizResultAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> questionlist;
    private ArrayList<String> subject_namelist;
    private ArrayList<String> select_optionlist;
    private ArrayList<String> correctlist;
    private ArrayList<String> idlist;
    private ArrayList<String> option_a;
    private ArrayList<String> option_b;
    private ArrayList<String> option_c;
    private ArrayList<String> option_d;
    private ArrayList<String> option_e;
    private ArrayList<String> marklist;
    private ArrayList<String> getmarklist;
    private ArrayList<String> remark_list;
    String is_neg_marking_marks;
    private ArrayList<String> neg_marks_list;
    private ArrayList<String> question_typelist;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();

    public StudentOnlineQuizResultAdapter(FragmentActivity studentonlineexam, ArrayList<String> questionlist, ArrayList<String> subject_namelist,
                                          ArrayList<String> select_optionlist, ArrayList<String> correctlist, ArrayList<String> idlist,
                                          ArrayList<String> option_a, ArrayList<String> option_b, ArrayList<String> option_c,
                                          ArrayList<String> option_d, ArrayList<String> option_e, ArrayList<String> question_typelist,
                                          ArrayList<String> marklist, ArrayList<String> getmarklist, String is_neg_marking_marks, ArrayList<String> neg_marks_list,
                                          ArrayList<String> remark_list){
        this.context = studentonlineexam;
        this.questionlist = questionlist;
        this.subject_namelist = subject_namelist;
        this.select_optionlist = select_optionlist;
        this.correctlist = correctlist;
        this.idlist = idlist;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.option_e = option_e;
        this.marklist = marklist;
        this.remark_list = remark_list;
        this.getmarklist = getmarklist;
        this.is_neg_marking_marks = is_neg_marking_marks;
        this.neg_marks_list = neg_marks_list;
        this.question_typelist = question_typelist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView youranswer,sno;
        TextView moption_a_image,moption_b_image,moption_c_image,moption_d_image,moption_e_image;
        TextView option_a,option_b,option_c,option_d,option_e,not_answer;
        ImageView correct_mark;
        TextView question;
        TextView moptionA,moptionB,moptionC,moptionD,moptionE;
        LinearLayout multiplechoice_layout;

        public MyViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question);

            option_a = view.findViewById(R.id.option_a);
            option_b = view.findViewById(R.id.option_b);
            option_c = view.findViewById(R.id.option_c);
            option_d = view.findViewById(R.id.option_d);
            option_e = view.findViewById(R.id.option_e);
            not_answer = view.findViewById(R.id.not_answer);
            correct_mark = view.findViewById(R.id.correct_mark);

            multiplechoice_layout = view.findViewById(R.id.multiplechoice_layout);
            sno = view.findViewById(R.id.sno);

            moption_a_image = view.findViewById(R.id.moption_a_image);
            moption_b_image = view.findViewById(R.id.moption_b_image);
            moption_c_image = view.findViewById(R.id.moption_c_image);
            moption_d_image = view.findViewById(R.id.moption_d_image);
            moption_e_image = view.findViewById(R.id.moption_e_image);

            moptionA = view.findViewById(R.id.moption_a_value);
            moptionB= view.findViewById(R.id.moption_b_value);
            moptionC = view.findViewById(R.id.moption_c_value);
            moptionD = view.findViewById(R.id.moption_d_value);
            moptionE = view.findViewById(R.id.moption_e_value);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_onlinequiz_result, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.sno.setText("Question:"+String.valueOf(position+1));
        holder.multiplechoice_layout.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.question.setText(Html.fromHtml(questionlist.get(position), Html.FROM_HTML_MODE_COMPACT));
            holder.moptionA.setText(Html.fromHtml(option_a.get(position), Html.FROM_HTML_MODE_COMPACT));
            holder.moptionB.setText(Html.fromHtml(option_b.get(position), Html.FROM_HTML_MODE_COMPACT));
            if(option_c.get(position).equals("")){
                holder.moptionC.setVisibility(View.GONE);
                holder.moption_c_image.setVisibility(View.GONE);
            }else{
                holder.moptionC.setVisibility(View.VISIBLE);
                holder.moption_c_image.setVisibility(View.VISIBLE);
                holder.moptionC.setText(Html.fromHtml(option_c.get(position), Html.FROM_HTML_MODE_COMPACT));
            }

            if(option_d.get(position).equals("")){
                holder.moptionD.setVisibility(View.GONE);
                holder.moption_d_image.setVisibility(View.GONE);
            }else{
                holder.moptionD.setVisibility(View.VISIBLE);
                holder.moption_d_image.setVisibility(View.VISIBLE);
                holder.moptionD.setText(Html.fromHtml(option_d.get(position), Html.FROM_HTML_MODE_COMPACT));
            }

            if(option_e.get(position).equals("")){
                holder.moptionE.setVisibility(View.GONE);
                holder.moption_e_image.setVisibility(View.GONE);
            }else{
                holder.moptionE.setVisibility(View.VISIBLE);
                holder.moption_e_image.setVisibility(View.VISIBLE);
                holder.moptionE.setText(Html.fromHtml(option_e.get(position), Html.FROM_HTML_MODE_COMPACT));
            }

        } else {
            holder.question.setText(Html.fromHtml(questionlist.get(position)));
            holder.moptionA.setText(Html.fromHtml(option_a.get(position)));
            holder.moptionB.setText(Html.fromHtml(option_b.get(position)));

            if(option_c.get(position).equals("")){
                holder.moptionC.setVisibility(View.GONE);
                holder.moption_c_image.setVisibility(View.GONE);
            }else{
                holder.moptionC.setVisibility(View.VISIBLE);
                holder.moption_c_image.setVisibility(View.VISIBLE);
                holder.moptionC.setText(Html.fromHtml(option_c.get(position)));
            }

            if(option_d.get(position).equals("")){
                holder.moptionD.setVisibility(View.GONE);
                holder.moption_d_image.setVisibility(View.GONE);
            }else{
                holder.moptionD.setVisibility(View.VISIBLE);
                holder.moption_d_image.setVisibility(View.VISIBLE);
                holder.moptionD.setText(Html.fromHtml(option_d.get(position)));
            }

            if(option_e.get(position).equals("")){
                holder.moptionE.setVisibility(View.GONE);
                holder.moption_e_image.setVisibility(View.GONE);
            }else{
                holder.moptionE.setVisibility(View.VISIBLE);
                holder.moption_e_image.setVisibility(View.VISIBLE);
                holder.moptionE.setText(Html.fromHtml(option_e.get(position)));
            }

        }

            if(select_optionlist.get(position).equals(correctlist.get(position))){
                holder.correct_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));
            }else{
                holder.correct_mark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_wrong));
            }
              if(correctlist.get(position).contains("option_1")){
                  holder.moption_a_image.setTextColor(Color.parseColor("#119326"));
                  holder.moptionA.setTextColor(Color.parseColor("#119326"));
              }if(correctlist.get(position).contains("option_2")){
                  holder.moption_b_image.setTextColor(Color.parseColor("#119326"));
                  holder.moptionB.setTextColor(Color.parseColor("#119326"));
              }if(correctlist.get(position).contains("option_3")){
                  holder.moption_c_image.setTextColor(Color.parseColor("#119326"));
                  holder.moptionC.setTextColor(Color.parseColor("#119326"));
              }if(correctlist.get(position).contains("option_4")){
                  holder.moption_d_image.setTextColor(Color.parseColor("#119326"));
                  holder.moptionD.setTextColor(Color.parseColor("#119326"));
              }if(correctlist.get(position).contains("option_5")){
                  holder.moption_e_image.setTextColor(Color.parseColor("#119326"));
                  holder.moptionE.setTextColor(Color.parseColor("#119326"));
              }
        System.out.println("questionlist="+questionlist.get(position)+" select_optionlist=="+select_optionlist.get(position).length());
             if(select_optionlist.get(position).isEmpty()){
                 holder.not_answer.setVisibility(View.VISIBLE);
                 holder.not_answer.setText(R.string.notanswer);
             }else{
                 holder.not_answer.setVisibility(View.GONE);
             }
             if(select_optionlist.get(position).contains("option_1")){
                holder.option_a.setVisibility(View.VISIBLE);
                 holder.not_answer.setVisibility(View.GONE);
            }else{
                holder.option_a.setVisibility(View.GONE);
             }
             if(select_optionlist.get(position).contains("option_2")){
                holder.option_b.setVisibility(View.VISIBLE);
                 holder.not_answer.setVisibility(View.GONE);
            }else{
                 holder.option_b.setVisibility(View.GONE);
                 holder.not_answer.setVisibility(View.GONE);
             }
             if(select_optionlist.get(position).contains("option_3")){
                holder.option_c.setVisibility(View.VISIBLE);
                 holder.not_answer.setVisibility(View.GONE);
            }else{
                 holder.option_c.setVisibility(View.GONE);
             }
             if(select_optionlist.get(position).contains("option_4")){
                holder.option_d.setVisibility(View.VISIBLE);
                 holder.not_answer.setVisibility(View.GONE);
            }else{
                 holder.option_d.setVisibility(View.GONE);
             }
             if(select_optionlist.get(position).contains("option_5")){
                holder.option_e.setVisibility(View.VISIBLE);
                 holder.not_answer.setVisibility(View.GONE);
            }else{
                 holder.option_e.setVisibility(View.GONE);
             }
    }

    @Override
    public int getItemCount() {
        return questionlist.size();
    }
}
