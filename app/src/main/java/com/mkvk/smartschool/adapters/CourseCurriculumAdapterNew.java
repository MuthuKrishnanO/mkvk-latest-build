package com.mkvk.smartschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.model.LessonModel;
import com.mkvk.smartschool.model.SectionModel;
import java.util.ArrayList;

public class CourseCurriculumAdapterNew extends RecyclerView.Adapter<CourseCurriculumAdapterNew.MyViewHolder>  {
    private Context context;
    private ArrayList<SectionModel> sectionList = new ArrayList<>();
    private Fragment fragment;

    public CourseCurriculumAdapterNew(Context context, ArrayList<SectionModel> sectionList, Fragment fragment) {
        this.context = context;
        this.sectionList = sectionList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CourseCurriculumAdapterNew.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_course_curriculum, parent, false);
        return new CourseCurriculumAdapterNew.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseCurriculumAdapterNew.MyViewHolder holder, int position) {
        SectionModel model = sectionList.get(position);
            System.out.println("section model="+model.getSection_title());
        holder.sectionNameTV.setText(context.getApplicationContext().getString(R.string.section)+" "+(position+1)+": "+model.getSection_title());

        ArrayList<LessonModel> lessonList = model.getLessons();

        CourseLessonAdapter adapter = new CourseLessonAdapter(context,lessonList,fragment);
        holder.lessonLV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        holder.lessonLV.setItemAnimator(new DefaultItemAnimator());
        holder.lessonLV.setAdapter(adapter);

        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.lessonLV.getVisibility() == View.VISIBLE) {
                    holder.arrow.setImageResource(R.drawable.ic_add_black);
                    holder.lessonLV.setVisibility(View.GONE);
                } else {
                    holder.arrow.setImageResource(R.drawable.ic_minus_black);
                    holder.lessonLV.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sectionNameTV;
        CardView viewContainer;
        RecyclerView lessonLV;
        ImageView arrow;
        public MyViewHolder(@NonNull View view) {
            super(view);
            viewContainer = view.findViewById(R.id.viewContainer);
            sectionNameTV = view.findViewById(R.id.sectionNameTV);
            lessonLV = view.findViewById(R.id.lessonLV);
            arrow = view.findViewById(R.id.arrow);
        }
    }
}
