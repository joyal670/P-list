package com.ashtechlabs.teleporter.dialog_fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.util.Constants;
import com.ashtechlabs.teleporter.util.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewDialogFragment extends DialogFragment {

    private String imageName;
    private ImageView ivClose;
    private TouchImageView tvImageView;
    private ImageView tvProgressImage;

    public ImageViewDialogFragment() {
        // Required empty public constructor
    }

    public static ImageViewDialogFragment newInstance(String imageUrl) {
        ImageViewDialogFragment fragment = new ImageViewDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_IMAGE_PATH, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageName = getArguments().getString(Constants.EXTRA_IMAGE_PATH);
            Log.e("IMAGE URL", imageName);
        }
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_image_view, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        tvImageView = (TouchImageView) view.findViewById(R.id.tvImageView);
        tvImageView.setMinZoom(1f);


        tvProgressImage = (ImageView) view.findViewById(R.id.tvProgressImage);
        ivClose = (ImageView) view.findViewById(R.id.ivClose);

//        Glide.with(this)
//                .load(Constants.URL_BASE_DAILY_PROGRESS + imageUrl)
//                .fitCenter()
//                .into(tvImageView);
        Picasso.with(getActivity())
                .load(Constants.IMAGE_PATH + imageName)
                .error(R.drawable.ic_camera)
                .placeholder(R.drawable.ic_camera)
                .into(tvImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        tvImageView.setMinZoom(1);
                    }

                    @Override
                    public void onError() {

                    }
                });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
