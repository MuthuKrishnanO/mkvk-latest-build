package com.mkvk.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mkvk.smartschool.BaseActivity;
import com.mkvk.smartschool.utils.Constants;
import com.mkvk.smartschool.utils.Utility;
import com.mkvk.smartschool.R;
import com.mkvk.smartschool.adapters.StudentSyllabusTimetableAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentSyllabusTimetable extends BaseActivity {
    StudentSyllabusTimetable studentSyllabusTimetable;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    String finalmsDate="",finalmeDate="";
    String finalnsDate="",finalneDate="";
    String finalpsDate="",finalpeDate="";
    RecyclerView mondayList, tuesdayList, wednesdayList, thursdayList, fridayList, saturdayList, sundayList;
    LinearLayout mondayHeader, tuesdayHeader, wednesdayHeader, thursdayHeader, fridayHeader, saturdayHeader, sundayHeader;
    LinearLayout mondaylayout,tuesdaylayout,wednesdaylayout,thursdaylayout,fridaylayout,saturdaylayout,sundaylayout;
    static TextView date7,date6,date5,date4,date3,date2,date1;

    TextView startdate_slider;
    TextView enddate_slider;
    ImageView previousweek,nextweek;

    ArrayList<String> mondaySubject = new ArrayList<>();
    ArrayList<String> mondayTime = new ArrayList<>();
    ArrayList<String> mondaySubjectid = new ArrayList<>();
    ArrayList<String> mondaytimefrom = new ArrayList<>();
    ArrayList<String> mondaytimeto = new ArrayList<>();

    ArrayList<String> tuesdaySubject = new ArrayList<>();
    ArrayList<String> tuesdayTime = new ArrayList<>();
    ArrayList<String> tuesdaySubjectid = new ArrayList<>();
    ArrayList<String>  tuesdaytimefrom = new ArrayList<>();
    ArrayList<String>  tuesdaytimeto = new ArrayList<>();

    ArrayList<String> wednesdaySubject = new ArrayList<>();
    ArrayList<String> wednesdayTime = new ArrayList<>();
    ArrayList<String> wednesdaySubjectid= new ArrayList<>();
    ArrayList<String> wednesdaytimefrom= new ArrayList<>();
    ArrayList<String> wednesdaytimeto= new ArrayList<>();

    ArrayList<String> thursdaySubject = new ArrayList<>();
    ArrayList<String> thursdayTime = new ArrayList<>();
    ArrayList<String> thursdaySubjectid= new ArrayList<>();
    ArrayList<String> thursdaytimefrom= new ArrayList<>();
    ArrayList<String> thursdaytimeto= new ArrayList<>();

    ArrayList<String> fridaySubject = new ArrayList<>();
    ArrayList<String> fridayTime = new ArrayList<>();
    ArrayList<String> fridaySubjectid = new ArrayList<>();
    ArrayList<String> fridaytimefrom = new ArrayList<>();
    ArrayList<String> fridaytimeto = new ArrayList<>();

    ArrayList<String> saturdaySubject = new ArrayList<>();
    ArrayList<String> saturdayTime = new ArrayList<>();
    ArrayList<String> saturdaySubjectid = new ArrayList<>();
    ArrayList<String> saturdaytimefrom = new ArrayList<>();
    ArrayList<String> saturdaytimeto = new ArrayList<>();
    public String startweek;

    ArrayList<String> sundaySubject = new ArrayList<>();
    ArrayList<String> sundayTime = new ArrayList<>();
    ArrayList<String> sundaySubjectid = new ArrayList<>();
    ArrayList<String> sundaytimefrom  = new ArrayList<>();
    ArrayList<String> sundaytimeto= new ArrayList<>();
    TextView day1, day2, day3, day4, day5, day6, day7;
    String mstartdate = "",menddate = "";
    String pstartdate = "",penddate = "";
    String nstartdate = "",nenddate = "";
    JSONArray mondayArray,tuesdayArray,wednesdayArray, thursdayArray,fridayArray,saturdayArray, sundayArray;
    StudentSyllabusTimetableAdapter   Adapter1,Adapter2,Adapter3,Adapter4,Adapter5,Adapter6,Adapter7;
    public int month = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_syllabus_timetable_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.lessonplan));
        startweek = Utility.getSharedPreferences(getApplicationContext(), "startWeek");
        startdate_slider = findViewById(R.id.startdate_slider);
        enddate_slider = findViewById(R.id.enddate_slider);
        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);

        mondayList = findViewById(R.id.classTimetable_mondayList);
        tuesdayList = findViewById(R.id.classTimetable_tuesdayList);
        wednesdayList = findViewById(R.id.classTimetable_wednesdayList);
        thursdayList = findViewById(R.id.classTimetable_thursdayList);
        fridayList = findViewById(R.id.classTimetable_fridayList);
        saturdayList = findViewById(R.id.classTimetable_saturdayList);
        sundayList = findViewById(R.id.classTimetable_sundayList);

        previousweek = findViewById(R.id.previousweek);
        previousweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpreviousweek();
            }
        });

        nextweek = findViewById(R.id.nextweek);
        nextweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getnextweek();
            }
        });



        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        date3 = findViewById(R.id.date3);
        date4 = findViewById(R.id.date4);
        date5 = findViewById(R.id.date5);
        date6 = findViewById(R.id.date6);
        date7 = findViewById(R.id.date7);

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);


        Calendar c = Calendar.getInstance();
        if(startweek.equals("Sunday")){
            c.setFirstDayOfWeek(Calendar.SUNDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }else if(startweek.equals("Monday")){
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }else if(startweek.equals("Tuesday")){
            c.setFirstDayOfWeek(Calendar.TUESDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        }else if(startweek.equals("Wednesday")){
            c.setFirstDayOfWeek(Calendar.WEDNESDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        }else if(startweek.equals("Thursday")){
            c.setFirstDayOfWeek(Calendar.THURSDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        }else if(startweek.equals("Friday")){
            c.setFirstDayOfWeek(Calendar.FRIDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        }else if(startweek.equals("Saturday")){
            c.setFirstDayOfWeek(Calendar.SATURDAY);
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        }

       // DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("current date"+df.format(c.getTime()));

        startdate_slider.setText(df.format(c.getTime()));
        date1.setText(df.format(c.getTime()));
        c.add(Calendar.DATE, 1);
        date2.setText(df.format(c.getTime()));
        c.add(Calendar.DATE, 1);
        date3.setText(df.format(c.getTime()));
        c.add(Calendar.DATE, 1);
        date4.setText(df.format(c.getTime()));
        c.add(Calendar.DATE, 1);
        date5.setText(df.format(c.getTime()));
        c.add(Calendar.DATE, 1);
        date6.setText(df.format(c.getTime()));
        c.add(Calendar.DATE, 1);
        date7.setText(df.format(c.getTime()));
        enddate_slider.setText(df.format(c.getTime()));


        String startStringDate = startdate_slider.getText().toString();
        String endStringDate = enddate_slider.getText().toString();
        String oldFormat= "dd/MM/yyyy";
        String newFormat= "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        Date mystartDate = null;
        Date myendDate = null;
        try {
            mystartDate = dateFormat.parse(startStringDate);
            myendDate = dateFormat.parse(endStringDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        mstartdate = timeFormat.format(mystartDate);
        menddate = timeFormat.format(myendDate);


        if(Constants.currentLocale.equals("en")){
            if(Utility.isConnectingToInternet(getApplicationContext())){
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                params.put("date_from", mstartdate);
                params.put("date_to", menddate);
                JSONObject obj=new JSONObject(params);
                System.out.println("Params==="+obj.toString());
                Log.e("params ", obj.toString());
                getDataFromApi(obj.toString());
            }else{
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }
        }else{
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date msdate = sdf.parse(mstartdate);
                Date medate = sdf.parse(menddate);
                SimpleDateFormat finalDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                finalmsDate = finalDateFormat.format(msdate);
                finalmeDate = finalDateFormat.format(medate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(Utility.isConnectingToInternet(getApplicationContext())){
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                params.put("date_from", finalmsDate);
                params.put("date_to", finalmeDate);
                JSONObject obj=new JSONObject(params);
                System.out.println("Params==="+obj.toString());
                Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());
            }else{
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }
        }

        if(startweek.equals("Sunday")){
            day1.setText(getApplicationContext().getString(R.string.sunday));
            day2.setText(getApplicationContext().getString(R.string.monday));
            day3.setText(getApplicationContext().getString(R.string.tuesday));
            day4.setText(getApplicationContext().getString(R.string.wednesday));
            day5.setText(getApplicationContext().getString(R.string.thursday));
            day6.setText(getApplicationContext().getString(R.string.friday));
            day7.setText(getApplicationContext().getString(R.string.saturday));

           Adapter1= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date1.getText().toString());
           Adapter2= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date2.getText().toString());
           Adapter3= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date3.getText().toString());
           Adapter4= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date4.getText().toString());
           Adapter5= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date5.getText().toString());
           Adapter6= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date6.getText().toString());
           Adapter7= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date7.getText().toString());

        }else if(startweek.equals("Monday")){
            day1.setText(getApplicationContext().getString(R.string.monday));
            day2.setText(getApplicationContext().getString(R.string.tuesday));
            day3.setText(getApplicationContext().getString(R.string.wednesday));
            day4.setText(getApplicationContext().getString(R.string.thursday));
            day5.setText(getApplicationContext().getString(R.string.friday));
            day6.setText(getApplicationContext().getString(R.string.saturday));
            day7.setText(getApplicationContext().getString(R.string.sunday));

            Adapter1 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date1.getText().toString());
            Adapter2 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date2.getText().toString());
            Adapter3 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date3.getText().toString());
            Adapter4 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date4.getText().toString());
            Adapter5 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date5.getText().toString());
            Adapter6 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date6.getText().toString());
            Adapter7 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date7.getText().toString());

        }else if(startweek.equals("Tuesday")){
            day1.setText(getApplicationContext().getString(R.string.tuesday));
            day2.setText(getApplicationContext().getString(R.string.wednesday));
            day3.setText(getApplicationContext().getString(R.string.thursday));
            day4.setText(getApplicationContext().getString(R.string.friday));
            day5.setText(getApplicationContext().getString(R.string.saturday));
            day6.setText(getApplicationContext().getString(R.string.sunday));
            day7.setText(getApplicationContext().getString(R.string.monday));

           Adapter1 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date1.getText().toString());
           Adapter2 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date2.getText().toString());
           Adapter3 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date3.getText().toString());
           Adapter4 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date4.getText().toString());
           Adapter5 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date5.getText().toString());
           Adapter6 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date6.getText().toString());
           Adapter7 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date7.getText().toString());

        }else if(startweek.equals("Wednesday")){
            day1.setText(getApplicationContext().getString(R.string.wednesday));
            day2.setText(getApplicationContext().getString(R.string.thursday));
            day3.setText(getApplicationContext().getString(R.string.friday));
            day4.setText(getApplicationContext().getString(R.string.saturday));
            day5.setText(getApplicationContext().getString(R.string.sunday));
            day6.setText(getApplicationContext().getString(R.string.monday));
            day7.setText(getApplicationContext().getString(R.string.tuesday));

            Adapter1= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date1.getText().toString());
            Adapter2= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date2.getText().toString());
            Adapter3= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date3.getText().toString());
            Adapter4= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date4.getText().toString());
            Adapter5= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date5.getText().toString());
            Adapter6= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date6.getText().toString());
            Adapter7= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date7.getText().toString());

        }else if(startweek.equals("Thursday")){
            day1.setText(getApplicationContext().getString(R.string.thursday));
            day2.setText(getApplicationContext().getString(R.string.friday));
            day3.setText(getApplicationContext().getString(R.string.saturday));
            day4.setText(getApplicationContext().getString(R.string.sunday));
            day5.setText(getApplicationContext().getString(R.string.monday));
            day6.setText(getApplicationContext().getString(R.string.tuesday));
            day7.setText(getApplicationContext().getString(R.string.wednesday));

           Adapter1= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date1.getText().toString());
           Adapter2= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date2.getText().toString());
           Adapter3= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date3.getText().toString());
           Adapter4= new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date4.getText().toString());
           Adapter5 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date5.getText().toString());
           Adapter6 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date6.getText().toString());
           Adapter7 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date7.getText().toString());

        }else if(startweek.equals("Friday")){
            day1.setText(getApplicationContext().getString(R.string.friday));
            day2.setText(getApplicationContext().getString(R.string.saturday));
            day3.setText(getApplicationContext().getString(R.string.sunday));
            day4.setText(getApplicationContext().getString(R.string.monday));
            day5.setText(getApplicationContext().getString(R.string.tuesday));
            day6.setText(getApplicationContext().getString(R.string.wednesday));
            day7.setText(getApplicationContext().getString(R.string.thursday));

             Adapter1 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date1.getText().toString());
             Adapter2 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date2.getText().toString());
             Adapter3 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date3.getText().toString());
             Adapter4 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date4.getText().toString());
             Adapter5 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date5.getText().toString());
             Adapter6 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date6.getText().toString());
             Adapter7 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date7.getText().toString());

        }else if(startweek.equals("Saturday")){
            day1.setText(getApplicationContext().getString(R.string.saturday));
            day2.setText(getApplicationContext().getString(R.string.sunday));
            day3.setText(getApplicationContext().getString(R.string.monday));
            day4.setText(getApplicationContext().getString(R.string.tuesday));
            day5.setText(getApplicationContext().getString(R.string.wednesday));
            day6.setText(getApplicationContext().getString(R.string.thursday));
            day7.setText(getApplicationContext().getString(R.string.friday));

            Adapter1 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, saturdaySubject, saturdayTime, saturdaySubjectid,date1.getText().toString());
            Adapter2 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, sundaySubject, sundayTime, sundaySubjectid,date2.getText().toString());
            Adapter3 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, mondaySubject, mondayTime,mondaySubjectid,date3.getText().toString());
            Adapter4 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, tuesdaySubject, tuesdayTime,tuesdaySubjectid,date4.getText().toString());
            Adapter5 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, wednesdaySubject, wednesdayTime,wednesdaySubjectid,date5.getText().toString());
            Adapter6 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this, thursdaySubject, thursdayTime, thursdaySubjectid,date6.getText().toString());
            Adapter7 = new StudentSyllabusTimetableAdapter(StudentSyllabusTimetable.this,fridaySubject,fridayTime,fridaySubjectid,date7.getText().toString());
        }

        RecyclerView.LayoutManager LayoutManager1 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager2 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager3 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager4 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager5 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager6 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager LayoutManager7 = new LinearLayoutManager(getApplicationContext());

        mondayList.setLayoutManager(LayoutManager1);
        mondayList.setItemAnimator(new DefaultItemAnimator());
        mondayList.setAdapter(Adapter1);

        tuesdayList.setLayoutManager(LayoutManager2);
        tuesdayList.setItemAnimator(new DefaultItemAnimator());
        tuesdayList.setAdapter(Adapter2);

        wednesdayList.setLayoutManager(LayoutManager3);
        wednesdayList.setItemAnimator(new DefaultItemAnimator());
        wednesdayList.setAdapter(Adapter3);

        thursdayList.setLayoutManager(LayoutManager4);
        thursdayList.setItemAnimator(new DefaultItemAnimator());
        thursdayList.setAdapter(Adapter4);

        fridayList.setLayoutManager(LayoutManager5);
        fridayList.setItemAnimator(new DefaultItemAnimator());
        fridayList.setAdapter(Adapter5);


        saturdayList.setLayoutManager(LayoutManager6);
        saturdayList.setItemAnimator(new DefaultItemAnimator());
        saturdayList.setAdapter(Adapter6);


        sundayList.setLayoutManager(LayoutManager7);
        sundayList.setItemAnimator(new DefaultItemAnimator());
        sundayList.setAdapter(Adapter7);



        mondayHeader = findViewById(R.id.classTimetable_mondayHeader);
        tuesdayHeader = findViewById(R.id.classTimetable_tuesdayHeader);
        wednesdayHeader = findViewById(R.id.classTimetable_wednesdayHeader);
        thursdayHeader = findViewById(R.id.classTimetable_thursdayHeader);
        fridayHeader = findViewById(R.id.classTimetable_fridayHeader);
        saturdayHeader = findViewById(R.id.classTimetable_saturdayHeader);
        sundayHeader = findViewById(R.id.classTimetable_sundayHeader);

        mondaylayout = findViewById(R.id.mondaylayout);
        tuesdaylayout = findViewById(R.id.tuesdaylayout);
        wednesdaylayout = findViewById(R.id.wednesdaylayout);
        thursdaylayout = findViewById(R.id.thursdaylayout);
        fridaylayout = findViewById(R.id.fridaylayout);
        saturdaylayout = findViewById(R.id.saturdaylayout);
        sundaylayout = findViewById(R.id.sundaylayout);

        decorate();


    }

    protected void getpreviousweek() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String startDate= startdate_slider.getText().toString();
        try{
            c.setTime(sdf.parse(startDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, -7);
        String newstartDate = sdf.format(c.getTime());
        startdate_slider.setText(newstartDate);

        String endDate= enddate_slider.getText().toString();

        try{
            c.setTime(sdf.parse(endDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, -7);
        String newendDate = sdf.format(c.getTime());
        enddate_slider.setText(newendDate);

        date1.setText(newstartDate);
        date7.setText(newendDate);

        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(newendDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, -1);
        newendDate = sdf.format(c.getTime());
        date6.setText(newendDate);

        try{

            c.setTime(sdf.parse(newendDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        c.add(Calendar.DAY_OF_MONTH, -1);
        newendDate = sdf.format(c.getTime());
        date5.setText(newendDate);

        try{
            c.setTime(sdf.parse(newendDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        c.add(Calendar.DAY_OF_MONTH, -1);
        newendDate = sdf.format(c.getTime());
        date4.setText(newendDate);

        try{

            c.setTime(sdf.parse(newendDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        c.add(Calendar.DAY_OF_MONTH, -1);
        newendDate = sdf.format(c.getTime());
        date3.setText(newendDate);

        try{
            c.setTime(sdf.parse(newendDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        c.add(Calendar.DAY_OF_MONTH, -1);
        newendDate = sdf.format(c.getTime());
        date2.setText(newendDate);

        String startStringDate = startdate_slider.getText().toString();
        String endStringDate = enddate_slider.getText().toString();
        String oldFormat= "dd/MM/yyyy";
        String newFormat= "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        Date mystartDate = null;
        Date myendDate = null;
        try {
            mystartDate = dateFormat.parse(startStringDate);
            myendDate = dateFormat.parse(endStringDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        pstartdate = timeFormat.format(mystartDate);
        penddate = timeFormat.format(myendDate);

        if(Constants.currentLocale.equals("en")){
            if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            params.put("date_from", pstartdate);
            params.put("date_to", penddate);
            JSONObject obj=new JSONObject(params);
            System.out.println("Params==="+obj.toString());
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
            }else{
                makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }
        }else{
            try {
                SimpleDateFormat sdfp = new SimpleDateFormat("yyyy-MM-dd");
                Date psdate = sdfp.parse(pstartdate);
                Date pedate = sdfp.parse(penddate);
                SimpleDateFormat finalDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                finalpsDate = finalDateFormat.format(psdate);
                finalpeDate = finalDateFormat.format(pedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(Utility.isConnectingToInternet(getApplicationContext())){
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                params.put("date_from", finalpsDate);
                params.put("date_to", finalpeDate);
                JSONObject obj=new JSONObject(params);
                System.out.println("Params==="+obj.toString());
                Log.e("params ", obj.toString());
                getDataFromApi(obj.toString());
            }else{
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected void getnextweek() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String startDate= startdate_slider.getText().toString();

        try{
            c.setTime(sdf.parse(startDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 7);
        String newstartDate = sdf.format(c.getTime());
        startdate_slider.setText(newstartDate);

        String endDate= enddate_slider.getText().toString();

        try{
            c.setTime(sdf.parse(endDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 7);
        String newendDate = sdf.format(c.getTime());
        enddate_slider.setText(newendDate);

        date1.setText(newstartDate);
        date7.setText(newendDate);

        try{
            c.setTime(sdf.parse(newstartDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
         newstartDate = sdf.format(c.getTime());
        date2.setText(newstartDate);

        try{
            c.setTime(sdf.parse(newstartDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        newstartDate = sdf.format(c.getTime());
        date3.setText(newstartDate);

        try{
            c.setTime(sdf.parse(newstartDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        newstartDate = sdf.format(c.getTime());
        date4.setText(newstartDate);

        try{
            c.setTime(sdf.parse(newstartDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        newstartDate = sdf.format(c.getTime());
        date5.setText(newstartDate);

        try{
            c.setTime(sdf.parse(newstartDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        newstartDate = sdf.format(c.getTime());
        date6.setText(newstartDate);

        String startStringDate = startdate_slider.getText().toString();
        String endStringDate = enddate_slider.getText().toString();
        String oldFormat= "dd/MM/yyyy";
        String newFormat= "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        Date mystartDate = null;
        Date myendDate = null;
        try {
            mystartDate = dateFormat.parse(startStringDate);
            myendDate = dateFormat.parse(endStringDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        nstartdate = timeFormat.format(mystartDate);
        nenddate = timeFormat.format(myendDate);


        if(Constants.currentLocale.equals("en")){
            if(Utility.isConnectingToInternet(getApplicationContext())){
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                params.put("date_from", nstartdate);
                params.put("date_to", nenddate);
                JSONObject obj=new JSONObject(params);
                System.out.println("Params==="+obj.toString());
                Log.e("params ", obj.toString());
                getDataFromApi(obj.toString());
            }else{
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }else{
            try {
                SimpleDateFormat sdfp = new SimpleDateFormat("yyyy-MM-dd");
                Date nsdate = sdfp.parse(nstartdate);
                Date nedate = sdfp.parse(nenddate);
                SimpleDateFormat finalDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
                finalnsDate = finalDateFormat.format(nsdate);
                finalneDate = finalDateFormat.format(nedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(Utility.isConnectingToInternet(getApplicationContext())){
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                params.put("date_from", finalnsDate);
                params.put("date_to", finalneDate);
                JSONObject obj=new JSONObject(params);
                System.out.println("Params==="+obj.toString());
                Log.e("params ", obj.toString());
                getDataFromApi(obj.toString());
            }else{
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void decorate() {
        mondayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        tuesdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        wednesdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        thursdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        fridayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        saturdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        sundayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getlessonplanUrl;
        System.out.println("url==="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        mondaySubject.clear();
                        mondayTime.clear();
                        mondaySubjectid.clear();
                        mondaytimefrom.clear();
                        mondaytimeto.clear();

                        tuesdaySubject.clear();
                        tuesdayTime.clear();
                        tuesdaySubjectid.clear();
                        tuesdaytimefrom.clear();
                        tuesdaytimeto.clear();

                        wednesdaySubject.clear();
                        wednesdayTime.clear();
                        wednesdaySubjectid.clear();
                        wednesdaytimefrom.clear();
                        wednesdaytimeto.clear();

                        thursdaySubject.clear();
                        thursdayTime.clear();
                        thursdaySubjectid.clear();
                        thursdaytimefrom.clear();
                        thursdaytimeto.clear();

                        fridaySubject.clear();
                        fridayTime.clear();
                        fridaySubjectid.clear();
                        fridaytimefrom.clear();
                        fridaytimeto.clear();

                        saturdaySubject.clear();
                        saturdayTime.clear();
                        saturdaySubjectid.clear();
                        saturdaytimefrom.clear();
                        saturdaytimeto.clear();

                        sundaySubject.clear();
                        sundayTime.clear();
                        sundaySubjectid.clear();
                        sundaytimefrom.clear();
                        sundaytimeto.clear();

                            JSONObject dataObject = object.getJSONObject("timetable");

                             mondayArray = dataObject.getJSONArray("Monday");
                             tuesdayArray = dataObject.getJSONArray("Tuesday");
                             wednesdayArray = dataObject.getJSONArray("Wednesday");
                             thursdayArray = dataObject.getJSONArray("Thursday");
                             fridayArray = dataObject.getJSONArray("Friday");
                             saturdayArray = dataObject.getJSONArray("Saturday");
                             sundayArray = dataObject.getJSONArray("Sunday");


                            if (mondayArray.length() > 0) {
                                mondaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < mondayArray.length(); i++) {
                                    if (mondayArray.getJSONObject(i).getString("code").equals("")) {
                                        mondaySubject.add(mondayArray.getJSONObject(i).getString("name"));
                                    } else {
                                        mondaySubject.add(mondayArray.getJSONObject(i).getString("name") + " (" + mondayArray.getJSONObject(i).getString("code") + ")");
                                    }
                                    if (!mondayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        mondayTime.add(mondayArray.getJSONObject(i).getString("time_from") + " - " + mondayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        mondayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    mondaySubjectid.add(mondayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }


                            if (tuesdayArray.length() > 0) {
                                tuesdaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < tuesdayArray.length(); i++) {
                                    if(tuesdayArray.getJSONObject(i).getString("code").equals("")){
                                        tuesdaySubject.add(tuesdayArray.getJSONObject(i).getString("name"));
                                    }else{
                                        tuesdaySubject.add(tuesdayArray.getJSONObject(i).getString("name")+" ("+tuesdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!tuesdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        tuesdayTime.add(tuesdayArray.getJSONObject(i).getString("time_from") + " - " + tuesdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        tuesdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    tuesdaySubjectid.add(tuesdayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }


                            if (wednesdayArray.length() > 0) {
                                wednesdaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < wednesdayArray.length(); i++) {
                                    if(wednesdayArray.getJSONObject(i).getString("code").equals("")){
                                        wednesdaySubject.add(wednesdayArray.getJSONObject(i).getString("name"));
                                    }else{
                                        wednesdaySubject.add(wednesdayArray.getJSONObject(i).getString("name")+" ("+wednesdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!wednesdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        wednesdayTime.add(wednesdayArray.getJSONObject(i).getString("time_from") + " - " + wednesdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        wednesdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    wednesdaySubjectid.add(wednesdayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }



                            if (thursdayArray.length() > 0) {
                                thursdaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < thursdayArray.length(); i++) {
                                      if(thursdayArray.getJSONObject(i).getString("code").equals("")){
                                        thursdaySubject.add(thursdayArray.getJSONObject(i).getString("name"));

                                    }else{
                                        thursdaySubject.add(thursdayArray.getJSONObject(i).getString("name")+" ("+thursdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!thursdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        thursdayTime.add(thursdayArray.getJSONObject(i).getString("time_from") + " - " + thursdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        thursdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                   thursdaySubjectid.add(thursdayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }

                            if (fridayArray.length() > 0) {
                                fridaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < fridayArray.length(); i++) {
                                    if (fridayArray.getJSONObject(i).getString("code").equals("")) {
                                        fridaySubject.add(fridayArray.getJSONObject(i).getString("name"));
                                    } else {
                                        fridaySubject.add(fridayArray.getJSONObject(i).getString("name") + " (" + fridayArray.getJSONObject(i).getString("code") + ")");
                                    }
                                    if (!fridayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        fridayTime.add(fridayArray.getJSONObject(i).getString("time_from") + " - " + fridayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        fridayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }

                                    fridaySubjectid.add(fridayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }



                            if (saturdayArray.length() > 0) {
                                saturdaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < saturdayArray.length(); i++) {
                                    if(saturdayArray.getJSONObject(i).getString("code").equals("")){
                                        saturdaySubject.add(saturdayArray.getJSONObject(i).getString("name"));

                                    }else{
                                        saturdaySubject.add(saturdayArray.getJSONObject(i).getString("name")+" ("+saturdayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!saturdayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        saturdayTime.add(saturdayArray.getJSONObject(i).getString("time_from") + " - " + saturdayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        saturdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    saturdaySubjectid.add(saturdayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }


                            if (sundayArray.length() > 0) {
                                sundaylayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < sundayArray.length(); i++) {
                                    if(sundayArray.getJSONObject(i).getString("code").equals("")){
                                        sundaySubject.add(sundayArray.getJSONObject(i).getString("name"));
                                    }else{
                                        sundaySubject.add(sundayArray.getJSONObject(i).getString("name")+" ("+sundayArray.getJSONObject(i).getString("code")+")");
                                    }
                                    if (!sundayArray.getJSONObject(i).getString("time_from").equals("")) {
                                        sundayTime.add(sundayArray.getJSONObject(i).getString("time_from") + " - " + sundayArray.getJSONObject(i).getString("time_to"));
                                    } else {
                                        sundayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    sundaySubjectid.add(sundayArray.getJSONObject(i).getString("subject_syllabus_id"));
                                }
                            }

                        Adapter1.notifyDataSetChanged();
                        Adapter2.notifyDataSetChanged();
                        Adapter3.notifyDataSetChanged();
                        Adapter4.notifyDataSetChanged();
                        Adapter5.notifyDataSetChanged();
                        Adapter6.notifyDataSetChanged();
                        Adapter7.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentSyllabusTimetable.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentSyllabusTimetable.this);//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

}
