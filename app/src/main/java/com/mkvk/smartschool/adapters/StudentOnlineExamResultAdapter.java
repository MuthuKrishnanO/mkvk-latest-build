package com.mkvk.smartschool.adapters;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mkvk.smartschool.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentOnlineExamResultAdapter extends RecyclerView.Adapter<StudentOnlineExamResultAdapter.MyViewHolder> {

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

    public StudentOnlineExamResultAdapter(FragmentActivity studentonlineexam, ArrayList<String> questionlist, ArrayList<String> subject_namelist,
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
        TextView subject,marks,attempted,duration,status,neg_marks,sno;
        ImageView optionA_image,optionB_image,optionC_image,optionD_image,optionE_image,true_image,false_image;
        ImageView moption_a_image,moption_b_image,moption_c_image,moption_d_image,moption_e_image;
        WebView question,Correct,optionA,optionB,optionC,optionD,optionE,descriptive_answer,teacher_remark;
        WebView moptionA,moptionB,moptionC,moptionD,moptionE;
        LinearLayout multiplechoice_layout,truefalse_layout,singlechoice_layout,descriptive_layout,remark_layout;

        public MyViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question);
            question.getSettings().setJavaScriptEnabled(true);
            question.getSettings().setBuiltInZoomControls(true);
            question.getSettings().setLoadWithOverviewMode(true);
            question.getSettings().setUseWideViewPort(true);
            question.getSettings().setDefaultFontSize(40);

            subject = view.findViewById(R.id.subject);
            marks = view.findViewById(R.id.marks);
            neg_marks = view.findViewById(R.id.neg_marks);
            descriptive_answer = view.findViewById(R.id.descriptive_answer);
            descriptive_answer.getSettings().setJavaScriptEnabled(true);
            descriptive_answer.getSettings().setBuiltInZoomControls(true);
            descriptive_answer.getSettings().setLoadWithOverviewMode(true);
            descriptive_answer.getSettings().setUseWideViewPort(true);
            descriptive_answer.getSettings().setDefaultFontSize(40);

            teacher_remark = view.findViewById(R.id.teacher_remark);
            teacher_remark.getSettings().setJavaScriptEnabled(true);
            teacher_remark.getSettings().setBuiltInZoomControls(true);
            teacher_remark.getSettings().setLoadWithOverviewMode(true);
            teacher_remark.getSettings().setUseWideViewPort(true);
            teacher_remark.getSettings().setDefaultFontSize(40);

            multiplechoice_layout = view.findViewById(R.id.multiplechoice_layout);
            truefalse_layout = view.findViewById(R.id.truefalse_layout);
            singlechoice_layout = view.findViewById(R.id.singlechoice_layout);
            descriptive_layout = view.findViewById(R.id.descriptive_layout);
            remark_layout = view.findViewById(R.id.remark_layout);
            sno = view.findViewById(R.id.sno);

            optionA_image = view.findViewById(R.id.optionA_image);
            optionB_image = view.findViewById(R.id.optionB_image);
            optionC_image = view.findViewById(R.id.optionC_image);
            optionD_image = view.findViewById(R.id.optionD_image);
            optionE_image = view.findViewById(R.id.optionE_image);

            moption_a_image = view.findViewById(R.id.moption_a_image);
            moption_b_image = view.findViewById(R.id.moption_b_image);
            moption_c_image = view.findViewById(R.id.moption_c_image);
            moption_d_image = view.findViewById(R.id.moption_d_image);
            moption_e_image = view.findViewById(R.id.moption_e_image);

            true_image = view.findViewById(R.id.true_image);
            false_image = view.findViewById(R.id.false_image);

            optionA = view.findViewById(R.id.optionA);
            optionB= view.findViewById(R.id.optionB);
            optionC = view.findViewById(R.id.optionC);
            optionD = view.findViewById(R.id.optionD);
            optionE = view.findViewById(R.id.optionE);

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
                .inflate(R.layout.adapter_student_exam_result, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.question.loadDataWithBaseURL(null,questionlist.get(position),"text/html; charset=utf-8", "utf-8", null);
        holder.subject.setText(subject_namelist.get(position));
        holder.sno.setText("Q."+String.valueOf(position+1));


        if(is_neg_marking_marks.equals("1")){
            holder.neg_marks.setVisibility(View.VISIBLE);
            holder.neg_marks.setText("(Negative: "+neg_marks_list.get(position)+")");
        }else{
            holder.neg_marks.setVisibility(View.GONE);
        }


        if(question_typelist.get(position).equals("multichoice")){
            holder.multiplechoice_layout.setVisibility(View.VISIBLE);
            holder.singlechoice_layout.setVisibility(View.GONE);
            holder.truefalse_layout.setVisibility(View.GONE);
            holder.descriptive_layout.setVisibility(View.GONE);

            if(select_optionlist.get(position).equals(correctlist.get(position))){
                holder.marks.setText("(Marks: 1.00/"+marklist.get(position)+")");
            }else{
                holder.marks.setText("(Marks: 0.00/"+marklist.get(position)+")");
            }
            holder.moptionA.loadDataWithBaseURL(null,option_a.get(position),"text/html; charset=utf-8", "utf-8", null);
            holder.moptionB.loadDataWithBaseURL(null,option_b.get(position),"text/html; charset=utf-8", "utf-8", null);
            holder.moptionC.loadDataWithBaseURL(null,option_c.get(position),"text/html; charset=utf-8", "utf-8", null);
            holder.moptionD.loadDataWithBaseURL(null,option_d.get(position),"text/html; charset=utf-8", "utf-8", null);

            if(option_e.get(position).equals("")){
                holder.moptionE.setVisibility(View.GONE);
                holder.moption_e_image.setVisibility(View.GONE);
            }else{
                holder.moptionE.setVisibility(View.VISIBLE);
                holder.moption_e_image.setVisibility(View.VISIBLE);
                holder.moptionE.loadDataWithBaseURL(null,option_e.get(position),"text/html; charset=utf-8", "utf-8", null);
            }

            if(select_optionlist.get(position).equals("")){
                if(correctlist.get(position).indexOf("opt_a") >= 0){
                    holder.moption_a_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }if(correctlist.get(position).indexOf("opt_b") >= 0){
                    holder.moption_b_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }if(correctlist.get(position).indexOf("opt_c") >= 0){
                    holder.moption_c_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }if(correctlist.get(position).indexOf("opt_d") >= 0){
                    holder.moption_d_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }if(correctlist.get(position).indexOf("opt_e") >= 0){
                    holder.moption_e_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }
            }else {
                    if (select_optionlist.get(position).indexOf("opt_a") >= 0 && correctlist.get(position).indexOf("opt_a") >= 0) {
                        holder.moption_a_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if(select_optionlist.get(position).indexOf("opt_a") >= 0 && correctlist.get(position).indexOf("opt_a") <= 0){
                        holder.moption_a_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }else if(correctlist.get(position).indexOf("opt_a") >= 0 && select_optionlist.get(position).indexOf("opt_a") <=0){
                        holder.moption_a_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                    }else{
                        holder.moption_a_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radiobutton_black));
                    }

                    if (select_optionlist.get(position).indexOf("opt_b") >= 0 && correctlist.get(position).indexOf("opt_b") >= 0) {
                        holder.moption_b_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if(select_optionlist.get(position).indexOf("opt_b") >= 0 && correctlist.get(position).indexOf("opt_b") <= 0){
                        holder.moption_b_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }else if(correctlist.get(position).indexOf("opt_b") >= 0 && select_optionlist.get(position).indexOf("opt_b") <=0){
                        holder.moption_b_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                    }else{
                        holder.moption_b_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radiobutton_black));
                    }

                    if (select_optionlist.get(position).indexOf("opt_c") >= 0 && correctlist.get(position).indexOf("opt_c") >= 0) {
                        holder.moption_c_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if(select_optionlist.get(position).indexOf("opt_c") >= 0 && correctlist.get(position).indexOf("opt_c") <= 0 ){
                        holder.moption_c_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }else if(correctlist.get(position).indexOf("opt_c") >= 0 && select_optionlist.get(position).indexOf("opt_c") <=0){
                        holder.moption_c_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                    }else{
                        holder.moption_c_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radiobutton_black));
                    }

                    if (select_optionlist.get(position).indexOf("opt_d") >= 0 && correctlist.get(position).indexOf("opt_d") >= 0) {
                        holder.moption_d_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if(select_optionlist.get(position).indexOf("opt_d") >= 0 && correctlist.get(position).indexOf("opt_d") <= 0 ){
                        holder.moption_d_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }else if(correctlist.get(position).indexOf("opt_d") >= 0 && select_optionlist.get(position).indexOf("opt_d") <=0){
                        holder.moption_d_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                    }else{
                        holder.moption_d_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radiobutton_black));
                    }

                    if (select_optionlist.get(position).indexOf("opt_e") >= 0 && correctlist.get(position).indexOf("opt_e") >= 0) {
                        holder.moption_e_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if(select_optionlist.get(position).indexOf("opt_e") >= 0 && correctlist.get(position).indexOf("opt_e") <= 0 ){
                        holder.moption_e_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }else if(correctlist.get(position).indexOf("opt_e") >= 0 && select_optionlist.get(position).indexOf("opt_e") <=0){
                        holder.moption_e_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                    }else{
                        holder.moption_e_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radiobutton_black));
                    }
            }
        }else if(question_typelist.get(position).equals("singlechoice")){
            holder.singlechoice_layout.setVisibility(View.VISIBLE);
            holder.multiplechoice_layout.setVisibility(View.GONE);
            holder.truefalse_layout.setVisibility(View.GONE);
            holder.descriptive_layout.setVisibility(View.GONE);

            if(select_optionlist.get(position).equals(correctlist.get(position))){
                holder.marks.setText("(Marks: 1.00/"+marklist.get(position)+")");
            }else{
                holder.marks.setText("(Marks: 0.00/"+marklist.get(position)+")");
            }
            holder.optionA.loadDataWithBaseURL(null,option_a.get(position),"text/html; charset=utf-8", "utf-8", null);
            holder.optionB.loadDataWithBaseURL(null,option_b.get(position),"text/html; charset=utf-8", "utf-8", null);
            holder.optionC.loadDataWithBaseURL(null,option_c.get(position),"text/html; charset=utf-8", "utf-8", null);
            holder.optionD.loadDataWithBaseURL(null,option_d.get(position),"text/html; charset=utf-8", "utf-8", null);
            if(option_e.get(position).equals("")){
                holder.optionE.setVisibility(View.GONE);
                holder.optionE_image.setVisibility(View.GONE);
            }else{
                holder.optionE.setVisibility(View.VISIBLE);
                holder.optionE_image.setVisibility(View.VISIBLE);
                holder.optionE.loadDataWithBaseURL(null,option_e.get(position),"text/html; charset=utf-8", "utf-8", null);
            }


            if(select_optionlist.get(position).equals("")){
                if(correctlist.get(position).equals("opt_a")){
                    holder.optionA_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }else if(correctlist.get(position).equals("opt_b")){
                    holder.optionB_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }else if(correctlist.get(position).equals("opt_c")){
                    holder.optionC_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }else if(correctlist.get(position).equals("opt_d")){
                    holder.optionD_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }else if(correctlist.get(position).equals("opt_e")){
                    holder.optionE_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }
            }else {
                if (select_optionlist.get(position).equals(correctlist.get(position))) {
                    if (correctlist.get(position).equals("opt_a")) {
                        holder.optionA_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    } else if (correctlist.get(position).equals("opt_b")) {
                        holder.optionB_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    } else if (correctlist.get(position).equals("opt_c")) {
                        holder.optionC_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    } else if (correctlist.get(position).equals("opt_d")) {
                        holder.optionD_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if (correctlist.get(position).equals("opt_e")) {
                        holder.optionE_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }
                } else {
                    if (select_optionlist.get(position).equals("opt_a")) {
                        holder.optionA_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    } else if (select_optionlist.get(position).equals("opt_b")) {
                        holder.optionB_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    } else if (select_optionlist.get(position).equals("opt_c")) {
                        holder.optionC_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    } else if (select_optionlist.get(position).equals("opt_d")) {
                        holder.optionD_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }else if (select_optionlist.get(position).equals("opt_e")) {
                        holder.optionE_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                    }
                    if (correctlist.get(position).equals("opt_a")) {
                        holder.optionA_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    } else if (correctlist.get(position).equals("opt_b")) {
                        holder.optionB_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    } else if (correctlist.get(position).equals("opt_c")) {
                        holder.optionC_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    } else if (correctlist.get(position).equals("opt_d")) {
                        holder.optionD_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }else if (correctlist.get(position).equals("opt_e")) {
                        holder.optionE_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                    }
                }
            }
        }else if(question_typelist.get(position).equals("descriptive")){
            holder.descriptive_layout.setVisibility(View.VISIBLE);
            holder.singlechoice_layout.setVisibility(View.GONE);
            holder.truefalse_layout.setVisibility(View.GONE);
            holder.multiplechoice_layout.setVisibility(View.GONE);
            holder.descriptive_answer.loadDataWithBaseURL(null,select_optionlist.get(position),"text/html; charset=utf-8", "utf-8", null);
            if(remark_list.get(position).equals("")){
                holder.remark_layout.setVisibility(View.GONE);
            }else{
                holder.remark_layout.setVisibility(View.VISIBLE);
                holder.teacher_remark.loadDataWithBaseURL(null,remark_list.get(position),"text/html; charset=utf-8", "utf-8", null);
            }
            if(select_optionlist.get(position).equals("")){
                holder.marks.setText("(0.00/"+marklist.get(position)+")");
            }else{
                holder.marks.setText("("+getmarklist.get(position)+")");
            }


        }else if(question_typelist.get(position).equals("true_false")) {
            holder.truefalse_layout.setVisibility(View.VISIBLE);
            holder.singlechoice_layout.setVisibility(View.GONE);
            holder.multiplechoice_layout.setVisibility(View.GONE);
            holder.descriptive_layout.setVisibility(View.GONE);

            if(select_optionlist.get(position).equals(correctlist.get(position))){
                holder.marks.setText("(Marks: 1.00/"+marklist.get(position)+")");
            }else{
                holder.marks.setText("(Marks: 0.00/"+marklist.get(position)+")");
            }

            if(select_optionlist.get(position).equals("")){
                if(correctlist.get(position).equals("true")){
                    holder.true_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }else{
                    holder.true_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.radio_button));
                }
            }else if(correctlist.get(position).equals(select_optionlist.get(position))) {
                if (correctlist.get(position).equals("true")){
                    holder.true_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                }else{
                    holder.false_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                }
            }else if(correctlist.get(position).equals("false") && select_optionlist.get(position).equals("true")) {
                holder.false_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
                holder.true_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
            }else if(correctlist.get(position).equals("true") && select_optionlist.get(position).equals("false")) {
                holder.false_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wrong_mark));
                holder.true_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_mark));
            }
        }
    }

    @Override
    public int getItemCount() {
        return questionlist.size();
    }
}
