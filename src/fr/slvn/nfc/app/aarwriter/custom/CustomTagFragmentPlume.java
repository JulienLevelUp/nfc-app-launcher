package fr.slvn.nfc.app.aarwriter.custom;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import fr.slvn.nfc.app.aarwriter.R;
import fr.slvn.nfc.app.aarwriter.tools.NfcUtils;

public class CustomTagFragmentPlume extends CustomFragmentAbstract implements OnClickListener {
	private static final String EXTRA_TWITTER_PROFILE = "twitter_profile";

	private Button mButton;
	private ProgressBar mProgressBar;
	private EditText mProfileName;

	@Override
	public void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			String twitterProfile = intent.getStringExtra(EXTRA_TWITTER_PROFILE);
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			writeApplicationRecordOnTag(twitterProfile, tag);
		}
	}

	private void writeApplicationRecordOnTag(String profileName, Tag tag) {
		NdefMessage msg = NfcUtils.getPlumeMessage(profileName);
		writeNdefMessageToTag(msg, tag);
		unFreezeUi();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_tag_fragment_lu_plume, null);
		mButton = (Button) view.findViewById(R.id.button_write);
		mProgressBar = (ProgressBar) view.findViewById(R.id.main_progress);
		mButton.setOnClickListener(this);

		mProfileName = (EditText) view.findViewById(R.id.ctf_lu_plume_profile_name);
		
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();

		unFreezeUi();
	}


	@Override
	public void onClick(View v) {
		if (v == mButton) {
			String profileName = retrieveProfileInformation();

			if (profileName != null ) {
				freezeUi();
				Toast.makeText(getActivity(), getString(R.string.help_toast), Toast.LENGTH_LONG).show();
				mNfcAdapter.enableForegroundDispatch(getActivity(), getPendingIntent(profileName), mWaitTagFilters, null);
			}
		}
	}

	private String retrieveProfileInformation() {
		return mProfileName.getText().toString();
	}

	private void freezeUi() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void unFreezeUi() {
		mProgressBar.setVisibility(View.INVISIBLE);
	}

	private PendingIntent getPendingIntent(String profileName) {
		Intent intent = new Intent(getActivity(), getActivity().getClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_TWITTER_PROFILE, profileName);
		return PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
	}

	@Override
	public int getCustomId() {
		return R.id.customfragmentid_plume;
	}

}
