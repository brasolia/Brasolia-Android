package br.com.brasolia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import br.com.brasolia.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView imgProfile,btClose;
    TextView tvName, tvEmail;
    User u;
    ImageLoader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        imgProfile = (CircleImageView) findViewById(R.id.main_profile_picture);
        btClose = (CircleImageView) findViewById(R.id.btCloseProfile);
        tvName = (TextView) findViewById(R.id.tvNameProfile);
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        // -----------------------------------------------------------------------------------------

        u = User.getUser(ProfileActivity.this);

        tvName.setText(u.getName());
        tvEmail.setText(u.getEmail());

        loader = ImageLoader.getInstance();
        loader.displayImage(u.getPhoto(), imgProfile);

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}
