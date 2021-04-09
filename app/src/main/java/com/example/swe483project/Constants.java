package com.example.swe483project;

public class Constants {

    public interface ACTION {
        public static String MAIN_ACTION = "com.example.foregroundservice.action.main";
        public static final String STARTFOREGROUND_ACTION = "com.example.foregroundservice.action.startforeground";
        public static final String STOPFOREGROUND_ACTION = "com.example.foregroundservice.action.stopforeground";
        //public static final String BROADCAST_ACTION = "broadcast";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 5;
    }
}
