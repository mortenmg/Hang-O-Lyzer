package com.morten.shared;

/**
 * Constants that are used in both the Application and the Wearable modules.
 */
public final class Constants {

    private Constants() {};

    public static final int WATCH_ONLY_ID = 2;
    public static final int PHONE_ONLY_ID = 3;
    public static final int BOTH_ID = 4;

    public static final int DRINK_ID = 42;

    public static final String BOTH_PATH = "/both";
    public static final String WATCH_ONLY_PATH = "/watch-only";
    public static final String KEY_NOTIFICATION_ID = "notification-id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    public static final String ACTION_DISMISS
            = "com.example.android.wearable.synchronizednotifications.DISMISS";
}