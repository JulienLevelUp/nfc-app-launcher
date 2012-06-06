package fr.slvn.nfc.app.aarwriter.tools;

import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import fr.slvn.nfc.app.aarwriter.custom.Skin;

public class NfcUtils {

	public static NdefMessage getApplicationRecord(String packageName) {
		return new NdefMessage(new NdefRecord[] { NdefRecord.createApplicationRecord(packageName) });
	}

	public static void writeTag(NdefMessage message, Tag tag) throws Exception {
		Ndef ndef = Ndef.get(tag);
		if (ndef != null) {
			writeInNdef(message, ndef);
		} else {
			formatNdef(message, tag);
		}
	}

	private static void writeInNdef(NdefMessage message, Ndef ndef) throws Exception {
		ndef.connect();
		if (!ndef.isWritable()) {
			throw new IOException("Tag is read-only.");
		}
		if (ndef.getMaxSize() < message.toByteArray().length) {
			throw new IOException("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + message.toByteArray().length
					+ " bytes.");
		}
		ndef.writeNdefMessage(message);
	}

	private static void formatNdef(NdefMessage message, Tag tag) throws Exception {
		NdefFormatable format = NdefFormatable.get(tag);
		if (format != null) {
			format.connect();
			format.format(message);
		} else {
			throw new FormatException("Tag doesn't support NDEF.");
		}
	}

	public static NdefMessage getBWMessage(Skin[] skins) {
		return getBWMessage(getBWJsonMessage(skins));
	}

	public static String getBWJsonMessage(Skin[] skins) {
		StringBuilder request = new StringBuilder();
		JSONArray list = new JSONArray();
		for (int i = 0; i < skins.length; i++) {
			Skin skin = skins[i];
			try {
				JSONObject json = new JSONObject();
				json.put(Skin.TAG_DISTANDID, skin.getDistantId());
				json.put(Skin.TAG_NAME, skin.getInternalName());
				json.put(Skin.TAG_TYPE, skin.getType().ordinal());
				list.put(json);
			} catch (JSONException e) {
			}

		}
		request.append(list.toString());
		String jsonMessage = request.toString();
		return jsonMessage;
	}

	public static NdefMessage getBWMessage(String jsonMessage) {
		NdefMessage msg = new NdefMessage(new NdefRecord[] { createMimeRecord("application/com.levelup.beautifulwidgets",
				jsonMessage.toString().getBytes()) });
		return msg;
	}

	public static NdefMessage getPlumeMessage(String profileName) {

		String mSharedURL = "https://twitter.com/#!/" + profileName;
		NdefRecord record = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, mSharedURL.getBytes(Charset.forName("US-ASCII")),
				new byte[0], new byte[0]);
		return new NdefMessage(new NdefRecord[] { record });
	}

	/**
	 * Creates a custom MIME type encapsulated in an NDEF record
	 */
	public static NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
		return mimeRecord;
	}

}
