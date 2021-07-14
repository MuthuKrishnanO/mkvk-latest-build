package com.mkvk.smartschool.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.model.LessonModel;
import java.util.ArrayList;

public class CourseLessonAdapter extends RecyclerView.Adapter<CourseLessonAdapter.MyViewHolder> {

    private Context context;
    ArrayList<LessonModel> lessonList = new ArrayList<>();
    private Fragment fragment;

    public CourseLessonAdapter(Context context, ArrayList<LessonModel> lessonList, Fragment fragment) {
        this.context = context;
        this.lessonList = lessonList;
        this.fragment = fragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailIV;
        TextView nameTV,durationTV,headingTV;
        LinearLayout viewContainer;
        ImageView imageview;

        public MyViewHolder(View view) {
            super(view);
            thumbnailIV = view.findViewById(R.id.imageview);
            durationTV = view.findViewById(R.id.durationTV);
            nameTV = view.findViewById(R.id.nameTV);
            headingTV = view.findViewById(R.id.headingTV);
            imageview = view.findViewById(R.id.imageview);
            viewContainer = view.findViewById(R.id.videoViewContainer);
        }
    }
    @Override
    public CourseLessonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_course_curriculum_lesson, parent, false);
        return new CourseLessonAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseLessonAdapter.MyViewHolder holder, final int position) {

        final LessonModel model = lessonList.get(position);
        Log.e("type", model.getLessonTitle());

     //   holder.nameTV.setText( model.getLessonTitle());
        if(model.getType().equals("lesson")){
            holder.imageview.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_icon));
            holder.headingTV.setText(context.getApplicationContext().getString(R.string.lessons)+": ");
            holder.nameTV.setText(model.getLessonTitle());
            String myStr2 = model.getDuration();
            System.out.println("myStr2=="+myStr2);
            if (myStr2=="null") {
                holder.durationTV.setText("");
            }else{
                holder.durationTV.setText(model.getDuration());
            }
        }else{
            holder.imageview.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_quiz));
            holder.headingTV.setText(context.getApplicationContext().getString(R.string.quiz)+": ");
            holder.nameTV.setText(model.getQuizTitle());
        }
        holder.viewContainer.setTag(position);
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }
}