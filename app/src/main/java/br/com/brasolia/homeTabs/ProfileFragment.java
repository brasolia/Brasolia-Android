package br.com.brasolia.homeTabs;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.brasolia.LoginActivity;
import br.com.brasolia.MessageActivity;
import br.com.brasolia.ProfileActivity;
import br.com.brasolia.R;
import br.com.brasolia.databinding.FragmentProfileBinding;
import br.com.brasolia.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matheus on 15/07/2016.
 */
public class ProfileFragment extends Fragment {
    private SharedPreferences sp;
    User user;
    TextView tvNameProfile;
    LinearLayout exit, userProfile, shareBrasolia, reviewBrasolia, facebook, instagram, sendMessage;
    CircleImageView profilePicture;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public ProfileFragment() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getContext());

        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View rootView = binding.getRoot();
        user = User.getUser(getActivity());
        binding.setUser(user);

        // SCREEN ELEMENTS -------------------------------------------------------------------------
        tvNameProfile = (TextView) rootView.findViewById(R.id.tvNameProfileFragment);
        userProfile = (LinearLayout) rootView.findViewById(R.id.userProfile);
        shareBrasolia = (LinearLayout) rootView.findViewById(R.id.shareBrasolia);
        reviewBrasolia = (LinearLayout) rootView.findViewById(R.id.reviewBrasolia);
        facebook = (LinearLayout) rootView.findViewById(R.id.facebook);
        instagram = (LinearLayout) rootView.findViewById(R.id.instagram);
        sendMessage = (LinearLayout) rootView.findViewById(R.id.sendMessage);
        exit = (LinearLayout) rootView.findViewById(R.id.profile_exit);
        profilePicture = (CircleImageView) rootView.findViewById(R.id.profile_picture);
        // -----------------------------------------------------------------------------------------


        if (user == null) {
            userProfile.setVisibility(View.GONE);
            exit.setVisibility(View.GONE);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        sp.edit().putBoolean("ShowFirstSteps", false).commit();


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("fromComments", true); // trying to see if user is login from profile fragment
                    startActivity(i);
                }
            }
        });





        /*
        LinearLayout tickets = (LinearLayout) rootView.findViewById(R.id.ingressos);

        tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), TicketActivity.class);
                startActivity(it);

            }
        });
        */

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), ProfileActivity.class);
                startActivity(it);

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
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "O Brasólia é um guia cultural de Brasília. Baixe agora na App Store ou Google Play Store!");
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
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
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/brasolia/?fref=ts"));
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

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Você deseja realmente sair do Brasólia?")
                            .setCancelable(false)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    logoff();

                                }
                            })
                            .setNegativeButton("Não", null)
                            .show();
                }
            }
        });

        return rootView;
    }

    public void onResume() {
        super.onResume();

        user = User.getUser(getActivity());

        if (user != null) {
            userProfile.setVisibility(View.VISIBLE);
            exit.setVisibility(View.VISIBLE);

            tvNameProfile.setText(user.getName());

            String imageUrl = user.getPhoto();

            if (imageUrl != null & imageUrl != "") {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(imageUrl, profilePicture);
            }
        }


    }


    public void logoff() {

        sp.edit().remove("cookie").commit();
        SharedPreferences sp2 = getActivity().getSharedPreferences("brasolia", getActivity().MODE_PRIVATE);
        sp2.edit().clear().commit();
        User.setUser(getContext(), null);
        LoginManager.getInstance().logOut();

        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();


    }
}
