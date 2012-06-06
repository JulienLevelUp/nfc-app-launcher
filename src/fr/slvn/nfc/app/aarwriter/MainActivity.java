package fr.slvn.nfc.app.aarwriter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import fr.slvn.nfc.app.aarwriter.custom.CustomTagActivity;
import fr.slvn.nfc.app.aarwriter.tools.NfcUtils;

public class MainActivity extends Activity implements View.OnClickListener {
	protected static final String EXTRA_MAKE_READONLY = "make_readonly";

	// Static
	private static final String EXTRA_PACKAGE_NAME = "package_name";

	// Views
	private Button mButton;
	private EditText mEditText;
	private ProgressBar mProgressBar;
	private CheckBox mCheckBoxReadOnly;

	// Utils
	private NfcAdapter mNfcAdapter;
	private IntentFilter[] mWaitTagFilters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED) };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mProgressBar = (ProgressBar) findViewById(R.id.main_progress);
		mEditText = (EditText) findViewById(R.id.edit_package);
		mButton = (Button) findViewById(R.id.button_write);
		mButton.setOnClickListener(this);
		mCheckBoxReadOnly = (CheckBox) findViewById(R.id.main_lock_id_lock);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();

		unFreezeUi();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_custom) {
			CustomTagActivity.launchActivity(this);
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if (v == mButton) {

			String packageName = retrievePackageName();

			if (packageName != null) {
				freezeUi();
				Toast.makeText(this, getString(R.string.help_toast), Toast.LENGTH_LONG).show();
				boolean makeReadOnly = mCheckBoxReadOnly.isChecked();
				mNfcAdapter.enableForegroundDispatch(this, getPendingIntent(packageName, makeReadOnly), mWaitTagFilters, null);
			}
		}
	}

	private String retrievePackageName() {
		String packageName = mEditText.getText().toString();

		if (TextUtils.isEmpty(packageName))
			return null;

		return packageName.replaceAll(" ", "");
	}

	private PendingIntent getPendingIntent(String packageName, boolean makeReadOnly) {
		Intent intent = new Intent(this, getClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_PACKAGE_NAME, packageName);
		intent.putExtra(EXTRA_MAKE_READONLY, makeReadOnly);
		return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
			boolean makeReadOnly = intent.getBooleanExtra(EXTRA_MAKE_READONLY, false);
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			writeApplicationRecordOnTag(packageName, tag, makeReadOnly);
		}
	}

	private void writeApplicationRecordOnTag(String packageName, Tag tag, boolean makeReadOnly) {
		NdefMessage msg = NfcUtils.getApplicationRecord(packageName);
		writeNdefMessageToTag(msg, tag,makeReadOnly);
		unFreezeUi();
	}

	private void writeNdefMessageToTag(NdefMessage message, Tag tag, boolean makeReadOnly) {
		try {
			NfcUtils.writeTag(message, tag, makeReadOnly);
			printWritingResult(true, null);
		} catch (Exception e) {
			e.printStackTrace();
			printWritingResult(false, e);
		}
	}

	private void printWritingResult(boolean result, Exception exception) {
		Toast.makeText(this, getErrorMessage(result, exception), Toast.LENGTH_LONG).show();
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

	private void freezeUi() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void unFreezeUi() {
		mProgressBar.setVisibility(View.INVISIBLE);
	}

}
