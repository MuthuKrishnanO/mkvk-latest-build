package com.mkvk.smartschool.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.mkvk.smartschool.students.StudentSyllabuslesson;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentSyllabusLessonAdapter extends BaseAdapter {

    private StudentSyllabuslesson context;
    private ArrayList<String> NameList;
    private ArrayList<String> total_completeList;
    private ArrayList<String> totalList;
    ArrayList<String> topicArray;
    public String defaultDateFormat;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    public StudentSyllabusLessonAdapter(StudentSyllabuslesson studentTransportRoutes,
                                        ArrayList<String> NameList, ArrayList<String> topicArray,ArrayList<String> total_completeList,ArrayList<String> totalList) {

        this.context = studentTransportRoutes;
        this.NameList = NameList;
        this.total_completeList = total_completeList;
        this.totalList = totalList;
        this.topicArray = topicArray;
    }
    @Override
    public int getCount() {
        return NameList.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_student_lesson, viewGroup, false);


        LinearLayout nameHeader = view.findViewById(R.id.studentAdapter_nameHeader);
        TableLayout vehicleTable = view.findViewById(R.id.studentAdapter_vehicleTable);
        TextView routeNameTV = (TextView) view.findViewById(R.id.studentAdapter_routeNameTV);
        TextView statusTV = (TextView) view.findViewById(R.id.studentAdapter_status);
        TextView count = (TextView) view.findViewById(R.id.count);
        routeNameTV.setTag(position);

        routeNameTV.setText(NameList.get(position));
        count.setText(String.valueOf(position+1));

        if (totalList.get(position).equals("0")) {
            statusTV.setText("No Status");
        }else {
            Float total_comp = Float.parseFloat(total_completeList.get(position));
            Float total = Float.parseFloat(totalList.get(position));
            Float complete = (total_comp / total);
            Float complete_per = (complete * 100);
           statusTV.setText(String.valueOf(Math.round(complete_per)) + "% Completed");
        }
        nameHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        Log.e("DATA==",topicArray.get(position));

        try {
            final JSONArray dataArray = new JSONArray(topicArray.get(position));
            Log.e("DDDATA==", String.valueOf(dataArray.length()));
              if(String.valueOf(dataArray.length()).equals("0")){
                 vehicleTable.setVisibility(View.GONE);
              }else{
                  vehicleTable.setVisibility(View.VISIBLE);

                  for(int i=0; i<dataArray.length(); i++) {
                      final TableRow tr = (TableRow) context.getLayoutInflater().inflate(R.layout.adapter_student_lesson_topics, null);

                      TextView vehicleTV,status,topic_count;
                      LinearLayout viewBtn;

                      viewBtn = tr.findViewById(R.id.studentTransportAdapter_detailsBtn);
                      vehicleTV = (TextView) tr.findViewById(R.id.studentTransportAdapter_vehicleTV);
                      status = (TextView) tr.findViewById(R.id.status);
                      topic_count = (TextView) tr.findViewById(R.id.topic_count);
                    vehicleTV.setText(dataArray.getJSONObject(i).getString("name"));
                    topic_count.setText((String.valueOf(position+1)+"."+String.valueOf(i+1)));
                    if(dataArray.getJSONObject(i).getString("status").equals("1")){
                        status.setText("Complete"+" ("+(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("complete_date")))+")");
                    }else{
                        status.setText("Incomplete");
                    }
                      vehicleTable.addView(tr);
                    context.registerForContextMenu(tr);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

      //  view.setTag(viewHolder);
        return view;
    }

    static class ViewHolder {
        private TextView routeNameTV,statusTV,count;
        private LinearLayout nameHeader;
        private TableLayout vehicleTable;
    }

}
