
package android_serialport_api.sample;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
				StringBuilder sb = new StringBuilder(t.length());
				for (int i = 0; i < t.length(); i++) {
					text[i] = t.charAt(i);
					sb.append(text[i]);
				}
				String sendString = sb.toString();
				try {

					mOutputStream.write(HexToByteArr(sendString));
//					mOutputStream.write('\n');

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
				//清空接收栏
				mReception.clearComposingText();

				mReception.setText(String.valueOf(HexToInt(bytesToHexString(buffer,size))));
				mReception.append("\n");

			}
		});
	}
	//字节数组转Hex字符串
	public static String bytesToHexString(byte[] bArray,int size) {
		StringBuilder sb = new StringBuilder(size);
		String sTemp;
		for (int i = 0; i < size; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	//Hex字符串转字节数组
	public static byte[] HexToByteArr(String inHex) {
		byte[] result;
		int hexlen = inHex.length();
		if (isOdd(hexlen) == 1) {
			hexlen++;
			result = new byte[(hexlen / 2)];
			inHex = "0" + inHex;
		} else {
			result = new byte[(hexlen / 2)];
		}
		int j = 0;
		for (int i = 0; i < hexlen; i += 2) {
			result[j] = HexToByte(inHex.substring(i, i + 2));
			j++;
		}
		return result;
	}
	public static int isOdd(int num) {
		return num & 1;
	}
	public static byte HexToByte(String inHex) {
		return (byte) Integer.parseInt(inHex, 16);
	}

	//Hex字符串转int
	public static int HexToInt(String inHex) {
		return Integer.parseInt(inHex, 16);
	}

	public static String IntToHex(int intHex){
		return Integer.toHexString(intHex);
	}

}
