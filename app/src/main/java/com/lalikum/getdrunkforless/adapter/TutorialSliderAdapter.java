package com.lalikum.getdrunkforless.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalikum.getdrunkforless.R;

public class TutorialSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public TutorialSliderAdapter(Context context) {
        this.context = context;
    }

    private int[] slide_images = {
            R.drawable.tutorial_1,
            R.drawable.tutorial_2,
            R.drawable.tutorial_3
    };

    private String[] slide_texts = {
            "Don't have enough money?",
            "Still want to party at Level 100 ???",
            "Don't worry. I can help you to find the best alcohol value booze to get the most of your money!"
    };


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout_tutorial, container, false);

        ImageView tutorialImageView = view.findViewById(R.id.tutorialImageView);
        TextView tutorialTextView = view.findViewById(R.id.tutorialTextView);

        tutorialImageView.setImageResource(slide_images[position]);
        tutorialTextView.setText(slide_texts[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
