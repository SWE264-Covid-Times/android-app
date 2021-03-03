//this guideline page is completely shifted to new template, no longer using this

package com.example.covidtimes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import com.example.covidtimes.R;

public class GuidelinesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidelines);

        TextView tv = findViewById(R.id.textView8);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

    }

}