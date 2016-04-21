package computing.mobile.instameet;

import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        if(cbLocSharing.isChecked())
            locSharing = true;
        else
            locSharing = false;

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

}
