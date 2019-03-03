package com.example.viewstask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateChipsForCustomGroup((CustomViewGroup)findViewById(R.id.firstCustomGroup), 7);

        generateChipsForCustomGroup((CustomViewGroup)findViewById(R.id.secondCustomGroup), 3);
    }

    private void generateChipsForCustomGroup(CustomViewGroup customViewGroup, int childrenCount) {
        for(int i = 0 ; i < childrenCount; i++){
            Chip chip = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Entry);
            chip.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            chip.setCheckable(false);
            chip.setText(getString(R.string.chipName, i));
            chip.setCloseIconVisible(true);

            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click on the close icon.
                    ((ViewGroup)view.getParent()).removeView(view);
                }
            });

            customViewGroup.addView(chip);
        }
    }
}
