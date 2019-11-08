
package android_serialport_api.sample;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConsoleActivity extends SerialPortActivity {

	EditText mReception;
	EditText Emission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);

		mReception = (EditText) findViewById(R.id.EditTextReception);
		Emission = (EditText) findViewById(R.id.EditTextEmission);

		final Button buttonSend = (Button) findViewById(R.id.ButtonSent1);
		buttonSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CharSequence t = Emission.getText();
				if (t.equals("")) {
					Toast.makeText(getApplicationContext(), "请输入发送的内容",
							Toast.LENGTH_SHORT).show();
					return;
				}
				char[] text = new char[t.length()];
				for (int i = 0; i < t.length(); i++) {
					text[i] = t.charAt(i);
				}
				try {

					mOutputStream.write(new String(text).getBytes());
					mOutputStream.write('\n');

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		final Button buttonToNext = findViewById(R.id.ToNextActivity);
		buttonToNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String id = mReception.getText().toString();
				if (id.equals("")){
					Toast.makeText(ConsoleActivity.this,"还未收到ID",Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(ConsoleActivity.this,"id:"+id,Toast.LENGTH_SHORT).show();
				Intent intentToNext = new Intent(ConsoleActivity.this,UpdateActivity.class);
				intentToNext.putExtra("mId",id);
				startActivity(intentToNext);
			}
		});
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (mReception != null){
					mReception.clearComposingText();
				}

					mReception.append(new String(buffer, 0, size));
					mReception.append("\n");

			}
		});
	}
}
