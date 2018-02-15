package com.example.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class StepDetailsFragment extends Fragment {
    private final String BUNDLE_CURRENT_STEP_ID = "current_step_id";
    private final String BUNDLE_CURRENT_PLAYER_POSITION = "player_position";
    private final String BUNDLE_CURRENT_PLAYER_STATE = "player_state";
    private final String BUNDLE_CURRENT_PLAYER_READY = "player_ready";
    private Button nextButton;
    private Button prevButton;
    private TextView description;
    private TextView errorMessageTextView;
    private ImageView stepImageView;
    private int currentStepId;
    Recipe mRecipe;
    private int maxNumOfSteps;
    private int minNumOfSteps;
    SimpleExoPlayerView exoPlayerView;
    DataSource.Factory dataSourceFactory;
    DefaultExtractorsFactory extractorsFactory;
    SimpleExoPlayer simpleExoPlayer;
    private Bundle mSavedInstanceState;
    private long playerPosition;
    private int playBackState;
    private boolean playWhenReady;

    public interface ButtonCallBack {
        void buttonOnClick(int position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_STEP_ID, currentStepId);
        outState.putLong(BUNDLE_CURRENT_PLAYER_POSITION, playerPosition);
        //outState.putInt(BUNDLE_CURRENT_PLAYER_STATE, playBackState);
        outState.putBoolean(BUNDLE_CURRENT_PLAYER_READY, playWhenReady);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        mSavedInstanceState = savedInstanceState;
        nextButton = rootView.findViewById(R.id.btn_next_step);
        prevButton = rootView.findViewById(R.id.btn_prev_step);
        description = rootView.findViewById(R.id.tv_step_description);
        stepImageView = rootView.findViewById(R.id.iv_step_image);
        errorMessageTextView = rootView.findViewById(R.id.tv_error_message);
        Bundle bundle = getArguments();
        if (savedInstanceState != null) {
            currentStepId = savedInstanceState.getInt(BUNDLE_CURRENT_STEP_ID);
            ((ButtonCallBack) getActivity()).buttonOnClick(currentStepId);
        } else {
            currentStepId = bundle.getInt("stepNumber");
            ((ButtonCallBack) getActivity()).buttonOnClick(currentStepId);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNextStep();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrevStep();
            }
        });

        mRecipe = (Recipe) bundle.getSerializable("recipe");
        minNumOfSteps = 0;
        maxNumOfSteps = mRecipe.getSteps().size();


        exoPlayerView = rootView.findViewById(R.id.exo_player_view);
        dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakingApp"));
        extractorsFactory = new DefaultExtractorsFactory();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, new DefaultLoadControl());
        exoPlayerView.setPlayer(simpleExoPlayer);
        generateData(currentStepId);
        return rootView;
    }

    private void getNextStep() {
        simpleExoPlayer.stop();
        if (currentStepId + 1 == maxNumOfSteps) {
            Toast.makeText(getContext(), "no more", Toast.LENGTH_SHORT).show();
        } else {
            currentStepId++;
            ((ButtonCallBack) getActivity()).buttonOnClick(currentStepId);
            generateData(currentStepId);
        }
    }

    private void getPrevStep() {

        simpleExoPlayer.stop();
        if (currentStepId == minNumOfSteps) {
            Toast.makeText(getContext(), "no more", Toast.LENGTH_SHORT).show();

        } else {
            currentStepId--;
            ((ButtonCallBack) getActivity()).buttonOnClick(currentStepId);
            generateData(currentStepId);
        }

    }

    private void generateData(int stepId) {
        Step step = mRecipe.getSteps().get(stepId);
        description.setText(step.getDescription());
        if (Utility.haveNetworkConnection(getContext())) {
            Uri uri = null;
            if (!step.getThumbnailURL().isEmpty()) {
                errorMessageTextView.setVisibility(View.GONE);
                exoPlayerView.setVisibility(View.GONE);
                stepImageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(step.getThumbnailURL()).error(R.drawable.question).into(stepImageView);
            } else if (!step.getVideoURL().isEmpty()) {
                errorMessageTextView.setVisibility(View.GONE);
                stepImageView.setVisibility(View.GONE);
                exoPlayerView.setVisibility(View.VISIBLE);
                uri = Uri.parse(step.getVideoURL());
                MediaSource dashMediaSource = new ExtractorMediaSource(uri, dataSourceFactory,
                        extractorsFactory, null, null);
                simpleExoPlayer.prepare(dashMediaSource);
                //-------------------------------
                if (mSavedInstanceState != null) {
                    simpleExoPlayer.seekTo(mSavedInstanceState.getLong(BUNDLE_CURRENT_PLAYER_POSITION));
                    simpleExoPlayer.setPlayWhenReady(mSavedInstanceState.getBoolean(BUNDLE_CURRENT_PLAYER_READY));
                } else {
                    simpleExoPlayer.setPlayWhenReady(true);
                }
            } else {
                errorMessageTextView.setText(getString(R.string.no_video));
                errorMessageTextView.setVisibility(View.VISIBLE);
                stepImageView.setVisibility(View.GONE);
                exoPlayerView.setVisibility(View.GONE);
            }
        } else {
            errorMessageTextView.setText(getString(R.string.no_connection));
            errorMessageTextView.setVisibility(View.VISIBLE);
            stepImageView.setVisibility(View.GONE);
            exoPlayerView.setVisibility(View.GONE);
        }
    }

    public void releaseExoPlayer() {
        playerPosition = simpleExoPlayer.getCurrentPosition();
        playWhenReady=simpleExoPlayer.getPlayWhenReady();
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            releaseExoPlayer();
        }
    }
}
