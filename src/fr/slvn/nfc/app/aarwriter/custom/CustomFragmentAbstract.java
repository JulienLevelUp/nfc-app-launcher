package fr.slvn.nfc.app.aarwriter.custom;

import fr.slvn.nfc.app.aarwriter.R;
import fr.slvn.nfc.app.aarwriter.tools.NfcUtils;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.widget.Toast;

public abstract class CustomFragmentAbstract extends Fragment {

	// Utils
	protected IntentFilter[] mWaitTagFilters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED) };
	protected NfcAdapter mNfcAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
		super.onAttach(activity);
	}

	public abstract void onNewIntent(Intent intent);

	protected void writeNdefMessageToTag(NdefMessage message, Tag tag) {
		try {
			NfcUtils.writeTag(message, tag);
			printWritingResult(true, null);
		} catch (Exception e) {
			e.printStackTrace();
			printWritingResult(false, e);
		}
	}

	protected void printWritingResult(boolean result, Exception exception) {
		Toast.makeText(getActivity(), getErrorMessage(result, exception), Toast.LENGTH_LONG).show();
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
	
	public abstract int getCustomId();
	
}
