package fr.slvn.nfc.app.aarwriter.custom;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import fr.slvn.nfc.app.aarwriter.R;
import fr.slvn.nfc.app.aarwriter.custom.Skin.SkinType;
import fr.slvn.nfc.app.aarwriter.tools.NfcUtils;

public class CustomTagFragmentBW extends InternFragment implements OnClickListener {

	private static final String EXTRA_JSON_MESSAGE = "json_message";
	private Button mButton;
	private ProgressBar mProgressBar;

	private Spinner mSkin1Type;
	private EditText mSkin1Name;
	private EditText mSkin1Id;

	// private Spinner mSkin2Type;
	// private EditText mSkin2Name;
	// private EditText mSkin2Id;
	// private TextView mSkin2TypeTitle;
	// private TextView mSkin2NameTitle;
	// private TextView mSkin2IdTitle;
	// private CheckBox mSkin2Activated;

	// Utils
	private NfcAdapter mNfcAdapter;
	private IntentFilter[] mWaitTagFilters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED) };

	private void freezeUi() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private String getErrorMessage(boolean result, Exception exception) {

		StringBuilder sb = new StringBuilder();

		if (result)
			sb.append(getString(R.string.error_success));
		else
			sb.append(getString(R.string.error_fail));

		if (exception != null) {
			sb.append(exception.getMessage());
		}

		return sb.toString();
	}

	private PendingIntent getPendingIntent(Skin[] skins) {
		Intent intent = new Intent(getActivity(), getActivity().getClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_JSON_MESSAGE, NfcUtils.getBWJsonMessage(skins));
		return PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
	}

	@Override
	public void onAttach(Activity activity) {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		if (v == mButton) {
			Skin[] skins = retrieveSkinsInformations();

			if (skins != null && skins.length > 0) {
				freezeUi();
				Toast.makeText(getActivity(), getString(R.string.help_toast), Toast.LENGTH_LONG).show();
				mNfcAdapter.enableForegroundDispatch(getActivity(), getPendingIntent(skins), mWaitTagFilters, null);
			}
		}
		// else if (v == mSkin2Activated) {
		// if (mSkin2Activated.isChecked()) {
		// mSkin2Id.setEnabled(true);
		// mSkin2IdTitle.setEnabled(true);
		// mSkin2Name.setEnabled(true);
		// mSkin2NameTitle.setEnabled(true);
		// mSkin2Type.setEnabled(true);
		// mSkin2TypeTitle.setEnabled(true);
		// } else {
		// mSkin2Id.setEnabled(false);
		// mSkin2IdTitle.setEnabled(false);
		// mSkin2Name.setEnabled(false);
		// mSkin2NameTitle.setEnabled(false);
		// mSkin2Type.setEnabled(false);
		// mSkin2TypeTitle.setEnabled(false);
		// }
		// }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_tag_fragment_levelup, null);
		mButton = (Button) view.findViewById(R.id.button_write);
		mProgressBar = (ProgressBar) view.findViewById(R.id.main_progress);
		mButton.setOnClickListener(this);

		mSkin1Type = (Spinner) view.findViewById(R.id.ctf_lu_bw_skin1_type_content);
		mSkin1Name = (EditText) view.findViewById(R.id.ctf_lu_bw_skin1_name_content);
		mSkin1Id = (EditText) view.findViewById(R.id.ctf_lu_bw_skin1_id_content);

		// mSkin2Type = (Spinner) view.findViewById(R.id.ctf_lu_bw_skin2_type_content);
		// mSkin2TypeTitle = (TextView) view.findViewById(R.id.ctf_lu_bw_skin2_type_title);
		// mSkin2Type.setEnabled(false);
		// mSkin2Name = (EditText) view.findViewById(R.id.ctf_lu_bw_skin2_name_content);
		// mSkin2NameTitle = (TextView) view.findViewById(R.id.ctf_lu_bw_skin2_name_title);
		// mSkin2Id = (EditText) view.findViewById(R.id.ctf_lu_bw_skin2_id_content);
		// mSkin2IdTitle = (TextView) view.findViewById(R.id.ctf_lu_bw_skin2_id_title);
		// mSkin2Activated = (CheckBox) view.findViewById(R.id.ctf_lu_bw_skin2_activated);
		// mSkin2Activated.setOnClickListener(this);

		return view;
	}

	public void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			String jsonMessage = intent.getStringExtra(EXTRA_JSON_MESSAGE);
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			writeApplicationRecordOnTag(jsonMessage, tag);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		unFreezeUi();
	}

	private void printWritingResult(boolean result, Exception exception) {
		Toast.makeText(getActivity(), getErrorMessage(result, exception), Toast.LENGTH_LONG).show();
	}

	private Skin[] retrieveSkinsInformations() {
		Skin[] res = new Skin[0];
		SkinType type = SkinType.values()[mSkin1Type.getSelectedItemPosition() + 1];
		String name = mSkin1Name.getText().toString();
		String distantIdS = mSkin1Id.getText().toString();
		if (!distantIdS.isEmpty() && !name.isEmpty()) {
			long distantId = Long.parseLong(distantIdS);
			Skin skin1 = new Skin(type, name, distantId);
			// if (mSkin2Activated.isChecked()) {
			// SkinType type2 = SkinType.values()[mSkin2Type.getSelectedItemPosition() + 1];
			// String name2 = mSkin2Name.getText().toString();
			// String distantIdS2 = mSkin2Id.getText().toString();
			// if (!distantIdS2.isEmpty() && !name2.isEmpty()) {
			// long distantId2 = Long.parseLong(distantIdS2);
			// Skin skin2 = new Skin(type2, name2, distantId2);
			// res = new Skin[2];
			// res[0] = skin1;
			// res[1] = skin2;
			// } else {
			// res = new Skin[1];
			// res[0] = skin1;
			// }
			// } else {
			res = new Skin[1];
			res[0] = skin1;
			// }
		}
		return res;
	}

	private void unFreezeUi() {
		mProgressBar.setVisibility(View.INVISIBLE);
	}

	private void writeApplicationRecordOnTag(String jsonMessage, Tag tag) {
		NdefMessage msg = NfcUtils.getBWMessage(jsonMessage);
		writeNdefMessageToTag(msg, tag);
		unFreezeUi();
	}

	private void writeNdefMessageToTag(NdefMessage message, Tag tag) {
		try {
			NfcUtils.writeTag(message, tag);
			printWritingResult(true, null);
		} catch (Exception e) {
			e.printStackTrace();
			printWritingResult(false, e);
		}
	}

}
