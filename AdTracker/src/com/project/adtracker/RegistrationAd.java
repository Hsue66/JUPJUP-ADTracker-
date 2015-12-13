package com.project.adtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.adtracker.async.AddJsp;

public class RegistrationAd extends Activity {
	
	
	RegisterInfo regInfo = new RegisterInfo();
	
	EditText st_name;
	EditText st_addr;
	EditText st_phone;
	EditText st_coupon;
	String successMsg="������ ��ϵǾ����ϴ�.";
	String failMsg="������Ͽ� �����Ͽ����ϴ�.";
	Button btn_send;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_registration);

		st_name = (EditText) findViewById(R.id.edit_name);
		st_addr = (EditText) findViewById(R.id.edit_addr);
		st_phone = (EditText) findViewById(R.id.edit_phone);
		st_coupon = (EditText) findViewById(R.id.edit_coupon);

		clearText();

		btn_send = (Button) findViewById(R.id.btn_send);

		btn_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (textChecked()) {
					regInfo.setName(st_name.getText().toString());
					regInfo.setAddress(st_addr.getText().toString());
					regInfo.setPhone(st_phone.getText().toString());
					regInfo.setCoupon(st_coupon.getText().toString());
					new AddJsp(regInfo).execute();
					Toast.makeText(RegistrationAd.this, successMsg,
							Toast.LENGTH_LONG).show();
					clearText();
				} else
					Toast.makeText(RegistrationAd.this, failMsg,
							Toast.LENGTH_LONG).show();

			}
		});

	}

	public void clearText()
	{
		st_name.setText("");
		st_name.setHint("�����̸��� �Է����ּ���.");
		
		st_addr.setText("");
		st_addr.setHint("�����ּҸ� �Է����ּ���.");
		
		st_phone.setText("");
		st_phone.setHint("���Թ�ȣ�� �Է����ּ���.");
		
		st_coupon.setText("");
		st_coupon.setHint("���� ������ �Է����ּ���.");
	}
	
	public boolean textChecked()
	{
		if(st_name.getText().toString().equals("") || st_addr.getText().toString().equals("") ||
				st_phone.getText().toString().equals("") || st_coupon.getText().toString().equals(""))
			return false;
		else
			return true;
	}
}
