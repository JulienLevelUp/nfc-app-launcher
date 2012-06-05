package fr.slvn.nfc.app.aarwriter.custom;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import fr.slvn.nfc.app.aarwriter.R;

public class CustomTagActivity extends Activity implements OnNavigationListener {
	private InternFragment fragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.custom_tag_activity);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.custom_fragments_list,
				android.R.layout.simple_spinner_dropdown_item);
		getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
		getActionBar().setDisplayShowTitleEnabled(false);
	}

	public static void launchActivity(Context context) {
		Intent intent = new Intent(context, CustomTagActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		fragment.onNewIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		FragmentManager fragmentManager = getFragmentManager();
		InternFragment newFragment;
		switch (itemPosition) {
		case 0:
			newFragment = (CustomTagFragmentBW) fragmentManager.findFragmentById(R.id.content);
			if (newFragment == null)
				newFragment = new CustomTagFragmentBW();
			break;
		case 1:
			newFragment = (CustomTagFragmentBW) fragmentManager.findFragmentById(R.id.content);
			if (newFragment == null)
				newFragment = new CustomTagFragmentPlume();
			break;
		default:
			return false;
		}
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (fragment != null)
			fragmentTransaction.remove(fragment);
		fragment = newFragment;
		fragmentTransaction.add(R.id.content, fragment);
		fragmentTransaction.commit();
		return true;
	}
}
