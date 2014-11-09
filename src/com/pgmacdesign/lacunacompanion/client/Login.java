package com.pgmacdesign.lacunacompanion.client;

import org.json.JSONObject;

import com.pgmacdesign.lacunacompanion.L;
import com.pgmacdesign.lacunacompanion.MainActivity;
import com.pgmacdesign.lacunacompanion.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button loginButton = (Button) findViewById(R.id.loginButton);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Get entered empire name and password
				EditText empireNameField = (EditText) findViewById(R.id.empireNameField);
				String empireName = empireNameField.getText().toString();
				
				EditText passWordField = (EditText) findViewById(R.id.passWordField);
				String empirePassword = passWordField.getText().toString();

				// Get selected item from Spinner
				Spinner selectServerSpinner = (Spinner) findViewById(R.id.selectServer);
				int indexValue = selectServerSpinner.getSelectedItemPosition();

				if (empireName.length() <= 0 || empireName == "") {
					Toast.makeText(Login.this,"Please enter your empire name.",Toast.LENGTH_SHORT).show();
				}
				else if (empirePassword.length() <= 0 || empirePassword == "") {
					Toast.makeText(Login.this,"Please enter your empire password.",Toast.LENGTH_SHORT).show();
				}
				else {
					// Set selectedServer and apiKey based on selected item in Spinner. Server defaults to US1.
					String selectedServer = null;
					String apiKey = null;
					if (indexValue == 0) {
						Toast.makeText(Login.this,"Please select a server.",Toast.LENGTH_SHORT).show();
					}
					else if (indexValue == 1) {
						selectedServer = "us1";
						apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
					}
					else if (indexValue == 2) {
						selectedServer = "pt";
						apiKey = "a6f619a8-1cd7-429b-8fbf-83ede625612c";
					}
					else {
						selectedServer = "us1";
						apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
					}
					
					final String SELECTED_SERVER = selectedServer;
					final String API_KEY = apiKey;

					// Doing this stops it from crashing randomly when no server is selected.
					if (selectedServer != null && apiKey != null) {
						
						// Initialize the Client class.
						ClientSide.setContext(Login.this);
						ClientSide.login(empireName, empirePassword, SELECTED_SERVER, API_KEY);
						JSONObject status   = ClientSide.STATUS;
						
						// Make sure that we don't run into a NullPointerException.
						if (status != null) {
							// Get the home planet id.
							JSONObject empire   = JsonParser.getJO(status, "empire");
							String homePlanetId = JsonParser.getS(empire, "home_planet_id");
							
							L.m(homePlanetId);
							
							Intent intent = new Intent (Login.this, MainActivity.class);
							startActivity(intent);
							finish();
							
							/*
							Intent intent = new Intent(Login.this,PlanetResourceView.class);
							intent.putExtra("planetId", homePlanetId);

							Login.this.startActivity(intent);
							finish();
							*/
						}
					}
				}
			}
		});
	}
}
