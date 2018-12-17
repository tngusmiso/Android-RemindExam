package mobile.cse.a2012final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        final Intent intent = getIntent();

        final EditText etText = findViewById(R.id.etText);
        etText.setText(intent.getStringExtra("Text"));

        Button btOk = findViewById(R.id.btOk);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SubActivity.this, MainActivity.class);
                intent1.putExtra("Changed", etText.getText().toString());
                setResult(RESULT_OK,intent1);
                finish();
            }
        });
        Button btCancel = findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SubActivity.this, MainActivity.class);
                setResult(RESULT_CANCELED,intent2);
                finish();
            }
        });

    }
}
