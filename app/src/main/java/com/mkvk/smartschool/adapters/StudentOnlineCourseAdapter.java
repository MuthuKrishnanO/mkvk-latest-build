package com.mkvk.smartschool.adapters;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.students.CoursePayment;
import com.mkvk.smartschool.students.StudentCourseDetail;
import com.mkvk.smartschool.students.StudentStartLessonActivity;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StudentOnlineCourseAdapter extends RecyclerView.Adapter<StudentOnlineCourseAdapter.MyViewHolder> {

    private FragmentActivity context;
    RecyclerView recyclerView;
    ArrayList<String> courseidList;
    ArrayList<String> coursetitleList;
    ArrayList<String> coursedescriptionList;
    ArrayList<String> course_thumbnailList;
    ArrayList<String> coursediscountList;
    ArrayList<String> free_courselist;
    ArrayList<String> course_progresslist;
    ArrayList<String> classlist;
    ArrayList<String> teacherlist;
    ArrayList<String> total_lessonlist;
    ArrayList<String> total_hour_countlist;
    ArrayList<String> updated_datelist;
    ArrayList<String> imagelist;
    ArrayList<String> course_priceList;
    ArrayList<String> paidstatuslist;
    private int lastPosition = -1;
    public StudentOnlineCourseAdapter(FragmentActivity studentonlineexam, RecyclerView recyclerView,ArrayList<String> courseidList,ArrayList<String> coursetitleList,
                                      ArrayList<String> coursedescriptionList, ArrayList<String> course_thumbnailList,ArrayList<String> coursediscountList,ArrayList<String> free_courselist,
                                      ArrayList<String> course_progresslist,ArrayList<String> classlist,ArrayList<String> teacherlist,ArrayList<String> total_lessonlist,ArrayList<String> total_hour_countlist,
                                      ArrayList<String> updated_datelist,ArrayList<String> imagelist,ArrayList<String> course_priceList,ArrayList<String> paidstatuslist) {
        this.context = studentonlineexam;
        this.recyclerView = recyclerView;
        this.courseidList = courseidList;
        this.coursetitleList = coursetitleList;
        this.coursedescriptionList = coursedescriptionList;
        this.course_thumbnailList = course_thumbnailList;
        this.coursediscountList = coursediscountList;
        this.free_courselist = free_courselist;
        this.course_progresslist = course_progresslist;
        this.classlist = classlist;
        this.teacherlist = teacherlist;
        this.total_lessonlist = total_lessonlist;
        this.total_hour_countlist = total_hour_countlist;
        this.updated_datelist = updated_datelist;
        this.paidstatuslist = paidstatuslist;
        this.imagelist = imagelist;
        this.course_priceList = course_priceList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView coursename,coursediscount,coursehours,courseprogress,course_details,course_startlesson,courseprice,course_createdby,course_createddate;
            ImageView coursethumnail,course_createdimage;
            ProgressBar progressBar;
            LinearLayout hourlayout;
            WebView coursedescription;

        public MyViewHolder(View view) {
            super(view);
            coursename = view.findViewById(R.id.coursename);
            courseprogress = view.findViewById(R.id.course_progress);
            progressBar = view.findViewById(R.id.progressBar);
            coursethumnail = view.findViewById(R.id.coursethumnail);
            coursehours = view.findViewById(R.id.coursehours);
            hourlayout = view.findViewById(R.id.hourlayout);
            course_details = view.findViewById(R.id.course_details);
            course_startlesson = view.findViewById(R.id.course_startlesson);
            coursediscount = view.findViewById(R.id.coursediscount);
            courseprice = view.findViewById(R.id.courseprice);
            course_createdby = view.findViewById(R.id.course_createdby);
            course_createddate = view.findViewById(R.id.course_createddate);
            course_createdimage = view.findViewById(R.id.course_createdimage);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_courselist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        holder.coursename.setText(coursetitleList.get(position));
        holder.courseprogress.setText(course_progresslist.get(position));
        if(total_hour_countlist.get(position).equals("00:00:00")){
            holder.hourlayout.setVisibility(View.GONE);
        }else{
            holder.hourlayout.setVisibility(View.VISIBLE);
            holder.coursehours.setText(total_hour_countlist.get(position));
        }
        holder.course_createdby.setText(teacherlist.get(position));
        holder.course_createddate.setText("Last Updated "+Utility.parseDate("yyyy-MM-dd", defaultDateFormat,updated_datelist.get(position)));
        Integer progressvalueint=(int)(Double.parseDouble(course_progresslist.get(position)));
        holder.progressBar.setProgress(progressvalueint);
        if(progressvalueint==100){
            holder.progressBar.getProgressDrawable().setColorFilter(
                    context.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
        }else if(progressvalueint>0 && progressvalueint<100){
            holder.progressBar.getProgressDrawable().setColorFilter(
                    context.getResources().getColor(R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        holder.courseprogress.setText(progressvalueint+"%");
        if(free_courselist.get(position).equals("1")){
            holder.courseprice.setText("FREE");

            if(Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")){
                holder.course_startlesson.setVisibility(View.GONE);
            }else{
                holder.course_startlesson.setVisibility(View.VISIBLE);
                holder.course_startlesson.setText("Start Lesson");
                holder.course_startlesson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context.getApplicationContext(), StudentStartLessonActivity.class);
                        intent.putExtra("CourseId",courseidList.get(position));
                        intent.putExtra("CourseTitle",coursetitleList.get(position));
                        intent.putExtra("course_name",coursetitleList.get(position));
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                    }
                });
            }
        }else{
            if(coursediscountList.get(position).equals("")){
                holder.courseprice.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency) +course_priceList.get(position));
            }else if(coursediscountList.get(position).equals("0.00")){
                holder.courseprice.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency) +course_priceList.get(position));
            }else{
                holder.coursediscount.setVisibility(View.VISIBLE);
                holder.courseprice.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency)+course_priceList.get(position));
                holder.courseprice.setPaintFlags(holder.courseprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                double discount = Double.parseDouble(coursediscountList.get(position));
                double ePer = Double.parseDouble(course_priceList.get(position));
                double per = (discount / 100.00f) * ePer;
                double value=ePer-per;
                DecimalFormat df = new DecimalFormat("0.00");
                System.out.println("DecimalFormat="+df.format(Float.parseFloat(String.valueOf(value))));
                holder.coursediscount.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency)+df.format(Float.parseFloat(String.valueOf(value))));
                //holder.coursediscount.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency)+String.valueOf(value));

            }
            if(course_progresslist.get(position).equals("0")){
                if(paidstatuslist.get(position).equals("1")){
                    if(Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")){
                        holder.course_startlesson.setVisibility(View.GONE);
                    }else{
                        holder.course_startlesson.setText("Start Lesson");
                        holder.course_startlesson.setVisibility(View.VISIBLE);
                        holder.course_startlesson.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(context.getApplicationContext(), StudentStartLessonActivity.class);
                                intent.putExtra("CourseId",courseidList.get(position));
                                intent.putExtra("course_name",coursetitleList.get(position));
                                context.startActivity(intent);
                                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            }
                        });
                    }

                }else{
                    if(Utility.getSharedPreferencesBoolean(context, Constants.showCoursePaymentBtn))  {
                        Log.e("testing", "testing 1");
                        holder.course_startlesson.setVisibility(View.VISIBLE);
                        holder.course_startlesson.setText("Buy Now");
                        holder.course_startlesson.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(context.getApplicationContext(), CoursePayment.class);
                                intent.putExtra("CourseId",courseidList.get(position));
                                context.startActivity(intent);
                                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            }
                        });

                    } else {
                        holder.course_startlesson.setVisibility(View.GONE);
                    }

                }
            }else{
                if(Utility.getSharedPreferences(context.getApplicationContext(), Constants.loginType).equals("parent")){
                    holder.course_startlesson.setVisibility(View.GONE);
                }else{
                    holder.course_startlesson.setVisibility(View.VISIBLE);
                    holder.course_startlesson.setText("Start Lesson");
                    holder.course_startlesson.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context.getApplicationContext(), StudentStartLessonActivity.class);
                            intent.putExtra("CourseId",courseidList.get(position));
                            intent.putExtra("course_name",coursetitleList.get(position));
                            context.startActivity(intent);
                            context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                        }
                    });
                }
            }
        }

        String imgUrl = Utility.getSharedPreferences(context.getApplicationContext(), "imagesUrl")+"uploads/staff_images/"+imagelist.get(position);
        Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.course_createdimage);



        String thumnail = Utility.getSharedPreferences(context.getApplicationContext(), "imagesUrl")+"uploads/course/course_thumbnail/"+course_thumbnailList.get(position);
        Glide.with(context)
                .load(thumnail)
                .into(holder.coursethumnail);
        holder.course_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(), StudentCourseDetail.class);
                intent.putExtra("CourseId",courseidList.get(position));
                if(free_courselist.get(position).equals("1")){
                    intent.putExtra("paidlist",paidstatuslist.get(position));
                    intent.putExtra("course_progress",course_progresslist.get(position));
                    intent.putExtra("discount","");
                    intent.putExtra("price",holder.courseprice.getText().toString());
                }else{
                    if(coursediscountList.get(position).equals("")){
                        intent.putExtra("discount","");
                        intent.putExtra("course_progress",course_progresslist.get(position));
                        intent.putExtra("paidlist",paidstatuslist.get(position));
                        intent.putExtra("price",holder.courseprice.getText().toString());
                    }else if(coursediscountList.get(position).equals("0.00")){
                        intent.putExtra("discount","");
                        intent.putExtra("course_progress",course_progresslist.get(position));
                        intent.putExtra("paidlist",paidstatuslist.get(position));
                        intent.putExtra("price",holder.courseprice.getText().toString());
                    }else{
                        intent.putExtra("paidlist",paidstatuslist.get(position));
                        intent.putExtra("course_progress",course_progresslist.get(position));
                        intent.putExtra("discount",holder.coursediscount.getText().toString());
                        intent.putExtra("price",holder.courseprice.getText().toString());
                    }
                }
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
            }
        });

    }
    @Override
    public int getItemCount() {
        return courseidList.size();
    }
    public void setFadeAnimation(int position, View view) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;
    }

}
