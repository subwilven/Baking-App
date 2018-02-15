package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.R.id.tv_step_num;
import static com.example.android.bakingapp.R.id.tv_step_short_description;

/**
 * Created by eslam on 28-Oct-17.
 */

public class StepsAdapter  extends RecyclerView.Adapter<StepsAdapter.StepHolder>{
    private int selectedPosition=0;
    private ArrayList<Step> mSteps;
    HandleStepOnClick mHandleStepOnClick;
    private boolean mTwoPane;
    public StepsAdapter(HandleStepOnClick handleStepOnClick)
    {
        mHandleStepOnClick=handleStepOnClick;
    }
    public interface  HandleStepOnClick
    {
        void OnClickStep(Step step, int position);
    }
    public void swapAdapter( ArrayList<Step> steps)
    {
        mSteps =steps;
        notifyDataSetChanged();
    }
    //to decicde  if will use the state changing or not
    public void isTwoPane(boolean isTwoPane)
    {
        mTwoPane=isTwoPane;
    }
    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        int layoutId=R.layout.list_item_step;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(layoutId,parent,false);
        return  new StepHolder(view);
    }
    public void changeTheActiveItem(int newPosition)
    {
        notifyItemChanged(selectedPosition);
        selectedPosition=newPosition;
        notifyItemChanged(selectedPosition);
    }
    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.setShortDescription(step.getShortDescription());
        holder.setNumTextView(String.valueOf(position));
        if(mTwoPane) holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        if(mSteps!=null)return mSteps.size();
        return 0;
    }

    public class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(tv_step_short_description)
        TextView shortDescriptionTextView;
        @BindView(tv_step_num)
        TextView numTextView;

        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        public void setShortDescription(String shortDescription){shortDescriptionTextView.setText(shortDescription);}
        public void setNumTextView(String num){numTextView.setText(num+". ");}
        @Override
        public void onClick(View view) {
            mHandleStepOnClick.OnClickStep(mSteps.get(getAdapterPosition()),getAdapterPosition());

            if(mTwoPane) changeTheActiveItem(getAdapterPosition());
        }
    }
}
