package fr.slvn.nfc.app.aarwriter.custom;

import java.io.File;
import java.util.Date;

public class Skin2 {
	// public static final int HOME = 1;
	// public static final int WEATHER = 2;
	// public static final int TOGGLE = 3;
	// public static final int SUPERCLOCK = 4;
	// public static final int BATTERY = 5;

	public static final int STATE_NONE = 0;
	public static final int STATE_CURRENT = 1;
	public static final int STATE_DOWNLOADED = 2;
	public static final int STATE_NEW = 3;
	public static final Skin2 defaultSuperClock = new Skin2(SkinType.SUPERCLOCK, "gingerbread", -1, true);
	public static final Skin2 defaultBattery = new Skin2(SkinType.BATTERY, "gingerbat", -1, true);
	public static final Skin2 defaultWeather = new Skin2(SkinType.WEATHER, "gingerbread", -1, true);
	public static final Skin2 defaultHome = new Skin2(SkinType.HOME, "gingerbread", -1, true);
	public static final Skin2 defaultToggle = new Skin2(SkinType.TOGGLE, "gingerbread", -1, true);
	public static final String TAG_DISTANDID = "distantid";
	public static final String TAG_TYPE = "type";
	public static final String TAG_NAME = "name";

	public static final String EMPTY_TEXT = "";
	private static final String DEFAULT_PREVIEW = "default";
	private String skinInternalName = EMPTY_TEXT;
	private long skinDistantId = -1;
	private String author = EMPTY_TEXT;
	private String urlWebsite = EMPTY_TEXT;
	private String urlDonate = EMPTY_TEXT;
	private String preview = EMPTY_TEXT;
	private long count = 0;
	private String skinLabel = EMPTY_TEXT;
	private int state;
	private Date created;
	private boolean isFeatured = false;
	private String mPreviewLarge;
	private SkinType mType;
	private String mSlugyName;
	private boolean isDefault = false;

	public Skin2(SkinType type, String name, long distantId) {
		mType = type;
		skinInternalName = name;
		skinDistantId = distantId;
	}

	public Skin2(SkinType type, String name, long distantId, boolean isDefault) {
		mType = type;
		skinInternalName = name;
		skinLabel = name;
		skinDistantId = distantId;
		this.isDefault = isDefault;
		if (isDefault)
			preview = DEFAULT_PREVIEW;
	}

	public Skin2(SkinType type, String internalName, String skinLabel2, String previewUrl, String author2, String websiteUrl,
			long dlCount, int stateNew, Date creationDate, boolean isFeature, long distantDbId) {
		mType = type;
		skinInternalName = internalName;
		skinLabel = skinLabel2;
		preview = previewUrl;
		author = author2;
		urlWebsite = websiteUrl;
		count = dlCount;
		state = stateNew;
		created = creationDate;
		isFeatured = isFeature;
		skinDistantId = distantDbId;
	}

	public long getSkinDistantId() {
		return skinDistantId;
	}

	public String getSkinInternalName() {
		return skinInternalName;
	}

	public SkinType getType() {
		return mType;
	}

	public String getSkinUniqueName() {
		return null;
	}

	public String getFile() {
		return null;
	}

	public String getSlugyName() {
		return mSlugyName;
	}

	public String getPreview() {
		return null;
	}

	public String getAuthor() {
		return author;
	}

	public String getUrlWebsite() {
		return urlWebsite;
	}

	public String getUrlDonate() {
		return urlDonate;
	}

	public void setUrlDonate(String urlDonate) {
		this.urlDonate = urlDonate;
	}

	public long getCount() {
		return count;
	}

	public String getSkinLabel() {
		if (skinLabel.length() == 0)
			return getSkinInternalName();
		else
			return skinLabel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skinInternalName == null) ? 0 : skinInternalName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Skin2 other = (Skin2) obj;
		if (skinInternalName == null) {
			if (other.skinInternalName != null)
				return false;
		} else if (!skinInternalName.equals(other.skinInternalName))
			return false;
		return true;
	}

	public boolean isFeatured() {
		return isFeatured;
	}

	public String getPreviewLarge() {
		if (mType == SkinType.HOME)
			return getPreview();
		return mPreviewLarge;
	}

	public void setState(int pState) {
		state = pState;
	}

	public int getState() {
		return state;
	}

	public Date getCreationDate() {
		return created;
	}

	public String getWebMarketPage() {
		return null;
	}

	public boolean isDefault() {
		return isDefault;
	}

}