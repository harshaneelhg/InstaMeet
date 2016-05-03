package computing.mobile.instameet;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RewardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            UserGlobalData ugd = UserGlobalData.getInstance();
            String text = "Thank you for using Instameet!!";
            if(ugd.rewards == 0){
                text += "Oops, your reward points are zero. You can gain points by hanging out with more people or by attending events suggested by us.";
            }
            else{
                text += "Your reward points are " + Integer.toString(ugd.rewards) + ". You can redeem your points at one of our registered stores.";
            }
            TextView txt_view = (TextView) findViewById(R.id.rewards);
            txt_view.setText(text);
        }
        catch (Exception exp){

        }
    }

}
