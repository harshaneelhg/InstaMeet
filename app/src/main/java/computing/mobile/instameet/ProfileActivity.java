package computing.mobile.instameet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.BoolRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        UserGlobalData ugd = UserGlobalData.getInstance();

        TextView txtUsername = (TextView) this.findViewById(R.id.textViewUsername);
        txtUsername.setText(ugd.username);

        EditText password = (EditText) this.findViewById(R.id.editTextPassword);
        password.setText(ugd.password);

        EditText txtDispName = (EditText) this.findViewById(R.id.editTextDisplayName);
        txtDispName.setText(ugd.displayName);

        EditText txtPhoneNum = (EditText) this.findViewById(R.id.editTextPhoneNumber);
        txtPhoneNum.setText(ugd.phone);

        CheckBox cbDiscover = (CheckBox) this.findViewById(R.id.checkBoxDiscoverable);
        if(ugd.discover)
            cbDiscover.setChecked(true);
        else
            cbDiscover.setChecked(false);

        CheckBox cbLocSharing = (CheckBox) this.findViewById(R.id.checkBoxLocSharing);
        if(ugd.locSharing)
            cbLocSharing.setChecked(true);
        else
            cbLocSharing.setChecked(false);

    }

    public void updateProfile(View v){
        UserGlobalData ugd = UserGlobalData.getInstance();
        String password = ((EditText) this.findViewById(R.id.editTextPassword)).getText().toString();
        String displayName = ((EditText) this.findViewById(R.id.editTextDisplayName)).getText().toString();
        String phone = ((EditText) this.findViewById(R.id.editTextPhoneNumber)).getText().toString();
        CheckBox cbDiscover = (CheckBox) this.findViewById(R.id.checkBoxDiscoverable);
        CheckBox cbLocSharing = (CheckBox) this.findViewById(R.id.checkBoxLocSharing);
        boolean discover;
        boolean locSharing;
        if(cbDiscover.isChecked())
            discover = true;
        else
            discover = false;
        ugd = UserGlobalData.getInstance();
        if(cbLocSharing.isChecked()) {
            locSharing = true;
            ugd.locSharing = true;
            GPSTracker gps = new GPSTracker(ProfileActivity.this, this);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.d("GPS", "GPS started...");
            Location l  = gps.getLocation();
            if(l != null) {
                ugd.location = Double.toString(l.getLatitude()) + " " + Double.toString(l.getLongitude());
            }
        }
        else {
            locSharing = false;
            ugd.locSharing = false;
            ugd.location = null;
        }


        if(password.equals("")) {
            Toast.makeText(ProfileActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(displayName.equals("")) {
            Toast.makeText(ProfileActivity.this, "Display name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.equals("")) {
            Toast.makeText(ProfileActivity.this, "Phone number cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(phone.length() != 10){
            Toast.makeText(ProfileActivity.this, "Phone number must be of length 10", Toast.LENGTH_LONG).show();
            return;
        }

        new APIClient().updateUser(ugd.username, ugd.password, password, ugd.email, displayName, phone, discover, locSharing);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                UserGlobalData ugd = UserGlobalData.getInstance();
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSTracker gps = new GPSTracker(ProfileActivity.this, this);
                    Log.d("GPS", "GPS started...");
                    Location l = gps.getLocation();
                    if (l != null) {
                        ugd.location = Double.toString(l.getLatitude()) + " " + Double.toString(l.getLongitude());
                    }

                } else {
                    ugd.location = null;
                }
                return;
            }
        }
    }
}
