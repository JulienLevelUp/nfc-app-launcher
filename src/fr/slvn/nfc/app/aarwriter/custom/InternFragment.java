package fr.slvn.nfc.app.aarwriter.custom;

import android.app.Fragment;
import android.content.Intent;

public abstract class InternFragment extends Fragment {

	public abstract void onNewIntent(Intent intent);

}
