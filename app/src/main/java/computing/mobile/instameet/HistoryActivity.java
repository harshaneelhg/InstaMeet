package computing.mobile.instameet;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }
    @Override
    public void onBackPressed() {
        HistoryActivity.this.startActivity(new Intent(HistoryActivity.this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataOperator dop = new DataOperator(getApplicationContext(),UserHistoryData.UserHistoryDataEntry.TABLE_NAME);
        UserGlobalData ugd = UserGlobalData.getInstance();
        cursor = dop.getHistory(dop, ugd.username);
        cursor.moveToLast();
        ListView lv = (ListView) this.findViewById(R.id.historyListView);
        ArrayList items = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.row_layout,R.id.rowTextView,items);
        lv.setAdapter(adapter);
        while(cursor.moveToPrevious()){
            adapter.add(cursor.getString(0) + "  " + cursor.getString(1));
        }
        try {
            adapter.add(cursor.getString(0) + "  " + cursor.getString(1));
        }catch (Exception e){
            e.printStackTrace();
        }
        lv.smoothScrollToPosition(0);
    }
}