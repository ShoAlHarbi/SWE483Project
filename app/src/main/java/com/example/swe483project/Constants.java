package com.example.swe483project;

public class Constants {

    public interface ACTION {
        public static String MAIN_ACTION = "com.example.foregroundservice.action.main";
        public static final String STARTFOREGROUND_ACTION = "com.example.foregroundservice.action.startforeground";
        public static final String STOPFOREGROUND_ACTION = "com.example.foregroundservice.action.stopforeground";
        //public static final String BROADCAST_ACTION = "broadcast";
        public static final String QUICBOOT_POWERON = "android.intent.action.QUICBOOT_POWERON";
        public static final String SIM_STATE_CHANGED ="android.intent.action.SIM_STATE_CHANGED";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 5;
    }

    public interface Config {
        public static final String EMAIL = "blueshield.swe483@gmail.com";
        public static final String PASSWORD = "blueshield@SWE483";
    }

    public interface DATABASE_COLUMN {
        public static final String EMAIL = "email";
        public static final String PASSCODE = "passcode";
        public static final String SIM = "SIM";
        public static final String STATUS = "status";
    }

    public interface STATUS {
        public static final String SAFE = "safe";
        public static final String IN_DANGER = "in danger";
    }
}
