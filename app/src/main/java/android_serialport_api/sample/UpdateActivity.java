package android_serialport_api.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UpdateActivity extends AppCompatActivity {

    TextView mUpdateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mUpdateText = findViewById(R.id.updateText);
        Intent intent = getIntent();
        String showText = intent.getStringExtra("mId");
        mUpdateText.setText(showText);
    }
}
