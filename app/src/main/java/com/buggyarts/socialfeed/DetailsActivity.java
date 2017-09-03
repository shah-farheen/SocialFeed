package com.buggyarts.socialfeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buggyarts.socialfeed.models.DataModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class DetailsActivity extends AppCompatActivity {

    private Context mContext;
    private TextView buttonLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Gson gson = new Gson();
        mContext = this;

        TextView textTitle = findViewById(R.id.text_title);
        ImageView imageData = findViewById(R.id.image_data);
        TextView textText = findViewById(R.id.text_text);
        buttonLike = findViewById(R.id.button_like);
        TextView textName = findViewById(R.id.text_name);
        TextView textDescription = findViewById(R.id.text_description);

        Intent intent = getIntent();
        final DataModel dataModel = gson.fromJson(intent.getStringExtra("DATA"), DataModel.class);
        textTitle.setText(dataModel.getTitle());
        textName.setText(String.format("From %s", dataModel.getName()));
        textDescription.setText(dataModel.getDescription());
        changeButton(dataModel.isLiked());

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModel.setLiked(!dataModel.isLiked());
                changeButton(dataModel.isLiked());
            }
        });

        if(dataModel.getImageUrl() == null){
            imageData.setVisibility(View.GONE);
        }
        else {
            Glide.with(mContext).load(dataModel.getImageUrl()).into(imageData);
        }

        if(dataModel.getText() == null){
            textText.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageData.getLayoutParams();
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            imageData.setLayoutParams(params);
        }
        else {
            textText.setText(dataModel.getText());
        }
    }

    private void changeButton(boolean isLiked){
        if(isLiked){
            buttonLike.setText(getString(R.string.unlike));
            buttonLike.setTextColor(Color.WHITE);
            buttonLike.setBackgroundResource(R.drawable.unlike_background);
        }
        else {
            buttonLike.setText(getString(R.string.like));
            buttonLike.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            buttonLike.setBackgroundResource(R.drawable.like_background);
        }
    }
}
