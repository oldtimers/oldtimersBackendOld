package pl.pazurkiewicz.oldtimers_rally.model;

public enum UserGroupEnum {
    ROLE_OWNER(Constants.OWNER_VALUE), //  3
    ROLE_JUDGE(Constants.JUDGE_VALUE), // 1
    ROLE_ORGANIZER(Constants.ORGANIZER_VALUE), // 2
    ROLE_ADMIN(Constants.ADMIN_VALUE); // 4

    UserGroupEnum(String userGroupString) {
    }

    public static class Constants {
        public static final String OWNER_VALUE = "ROLE_OWNER";
        public static final String JUDGE_VALUE = "ROLE_JUDGE";
        public static final String ORGANIZER_VALUE = "ROLE_ORGANIZER";
        public static final String ADMIN_VALUE = "ROLE_ADMIN";
    }
}
