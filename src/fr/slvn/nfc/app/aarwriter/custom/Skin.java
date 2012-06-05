package fr.slvn.nfc.app.aarwriter.custom;

public class Skin {
	public static final String TAG_DISTANDID = "distantid";
	public static final String TAG_TYPE = "type";
	public static final String TAG_NAME = "name";

	public enum SkinType {
		HOME, WEATHER, TOGGLE, SUPERCLOCK, BATTERY
	};

	private SkinType mType;
	private String mInternalName;
	private long mDistantId;

	public Skin(SkinType type, String name, long distantId) {
		mType = type;
		mInternalName = name;
		mDistantId = distantId;
	}

	public SkinType getType() {
		return mType;
	}

	public String getInternalName() {
		return mInternalName;
	}

	public long getDistantId() {
		return mDistantId;
	}

}
