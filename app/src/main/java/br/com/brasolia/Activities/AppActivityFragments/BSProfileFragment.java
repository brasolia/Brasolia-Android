package br.com.brasolia.Activities.AppActivityFragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.brasolia.Activities.MessageActivity;
import br.com.brasolia.Connectivity.BSImageStorage;
import br.com.brasolia.R;

/**
 * Created by cayke on 12/04/17.
 */

public class BSProfileFragment extends Fragment {
    TextView tvNameProfile;
    LinearLayout shareBrasolia, reviewBrasolia, facebook, instagram, sendMessage, logout;
    ImageView profilePicture;

    Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getActivity());

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = rootView.getContext();

        //region casts
        tvNameProfile = (TextView) rootView.findViewById(R.id.fragment_profile_name);
        shareBrasolia = (LinearLayout) rootView.findViewById(R.id.fragment_profile_share);
        reviewBrasolia = (LinearLayout) rootView.findViewById(R.id.fragment_profile_rate_app);
        facebook = (LinearLayout) rootView.findViewById(R.id.fragment_profile_follow_face);
        instagram = (LinearLayout) rootView.findViewById(R.id.fragment_profile_follow_insta);
        sendMessage = (LinearLayout) rootView.findViewById(R.id.fragment_profile_send_message);
        logout = (LinearLayout) rootView.findViewById(R.id.fragment_profile_logout);
        profilePicture = (ImageView) rootView.findViewById(R.id.fragment_profile_avatar);

        final Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/BebasNeue_bold.ttf");
        tvNameProfile.setTypeface(type);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            logout.setVisibility(View.GONE);
        }

        //endregion

        //region listeners
        LinearLayout back = rootView.findViewById(R.id.fragment_profile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                tvNameProfile.setText("");
                profilePicture.setImageDrawable(getContext().getResources().getDrawable(R.drawable.profile));
                logout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show();

            }
        });

        shareBrasolia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.share_logo);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_TEXT, "O Brasólia é um guia cultural de Brasília. Baixe agora na App Store ou Google Play Store!");

                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

        reviewBrasolia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "com.google.android.googlequicksearchbox";
//                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/319921444873986"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/brasolia")));
                }
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.instagram.com/_u/appbrasolia");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.instagram.com/appbrasolia")));
                }
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageActivity.class);
                startActivity(i);
            }
        });

        //endregion

        updateUserInfo();

        return rootView;
    }

    public void updateUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvNameProfile.setText(user.getDisplayName());


            //todo firebase genertates a facebook profile picture url that expires, we need to search a workaround for this
            BSImageStorage.setImageWithPathToImageViewDownloadingIfNecessary(user.getPhotoUrl().getPath(), profilePicture, R.drawable.profile, 500, 500, null);
        }
        else {
            tvNameProfile.setText("");
            Picasso.with(getContext()).load(R.drawable.profile).resize(598, 416).into(profilePicture);
        }
    }
}
