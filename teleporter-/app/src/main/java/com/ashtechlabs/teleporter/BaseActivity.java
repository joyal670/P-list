package com.ashtechlabs.teleporter;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.dialog_fragments.ProgressDialogFragment;
import com.ashtechlabs.teleporter.ui.Dashboard_Main;
import com.ashtechlabs.teleporter.util.CommonUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.prechat.ZopimChatActivity;


public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {


	private ProgressDialogFragment mProgressDialogFragment;
//	public GoogleApiClient mGoogleApiClient;
	public AutocompleteSessionToken autocompleteSessionToken;
	public PlacesClient placesClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GlobalPreferManager.initializePreferenceManager(getApplicationContext());
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.enableAutoManage(this, 100 /* clientId */, this)
//				.addApi(Places.GEO_DATA_API)
//				.build();
		mProgressDialogFragment = ProgressDialogFragment.newInstance();

		autocompleteSessionToken= AutocompleteSessionToken.newInstance();
		placesClient= Places.createClient(this);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case android.R.id.home:
				// Open the navigation drawer when the home icon is selected from the toolbar.
				super.onBackPressed();
				return true;


			case R.id.action_home:
				startActivity(new Intent(getApplicationContext(), Dashboard_Main.class));
				return true;


		}
		return super.onOptionsItemSelected(item);
	}

	public void launchCallFragment() {

		CommonUtils.showCallDialog(this, "Call", "Need to call?");
	}

	public void startChat() {


		VisitorInfo visitorInfo = new VisitorInfo.Builder()
				.phoneNumber(GlobalPreferManager.getString(GlobalPreferManager.Keys.MOBILE_NUM, ""))
				.email(GlobalPreferManager.getString(GlobalPreferManager.Keys.EMAIL, ""))
				.name(GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_NAME, ""))
				.build();

		// visitor info can be set at any point when that information becomes available
		ZopimChat.setVisitorInfo(visitorInfo);

		// set pre chat fields as mandatory
		PreChatForm preChatForm = new PreChatForm.Builder()
				.name(PreChatForm.Field.REQUIRED)
				.email(PreChatForm.Field.REQUIRED)
				.phoneNumber(PreChatForm.Field.REQUIRED)
				.build();

		// build chat config
		ZopimChat.SessionConfig config = new ZopimChat.SessionConfig().preChatForm(preChatForm).department("My memory");

		// start chat activity with config
		ZopimChatActivity.startActivity(this, config);

		// Sample breadcrumb
		ZopimChat.trackEvent("Started chat with pre-set visitor information");
	}

	public void showProgress() {
		mProgressDialogFragment.show(getSupportFragmentManager(), null);
	}

	public void dismissProgress() {
		mProgressDialogFragment.dismiss();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}