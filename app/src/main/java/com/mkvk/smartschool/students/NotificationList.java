package com.mkvk.smartschool.students;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.mkvk.smartschool.BaseActivity;
import com.mkvk.smartschool.NotificationModel;
import com.mkvk.smartschool.adapters.NotificationViewAdapter;
import com.mkvk.smartschool.utils.DatabaseHelper;
import com.mkvk.smartschool.R;
import java.util.ArrayList;

public class NotificationList extends BaseActivity  {
    NotificationViewAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_notification_list, null, false);
        mDrawerLayout.addView(contentView, 0);
        titleTV.setText(getApplicationContext().getString(R.string.notification));
        listView = findViewById(R.id.user_list);

        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<NotificationModel> modelArrayList = db.GetUsers();
        adapter = new NotificationViewAdapter(NotificationList.this, modelArrayList,listView);

        listView.setAdapter(adapter);
    }
}

