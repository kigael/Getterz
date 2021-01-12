package com.getterz.format.check;

public class FormatCheck {

    public static boolean name(String input){
        return !input.matches("^[\\p{L} .'-]+$");
    }

    public static boolean profileName(String input){
        return !input.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");
//        Username consists of alphanumeric characters (a-zA-Z0-9), lowercase, or uppercase.
//        Username allowed of the dot (.), underscore (_), and hyphen (-).
//        The dot (.), underscore (_), or hyphen (-) must not be the first or last character.
//        The dot (.), underscore (_), or hyphen (-) does not appear consecutively, e.g., java..regex
//        The number of characters must be between 5 to 20.
    }

    public static boolean password(String input){
        return !input.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$");
//        ^ represents starting character of the string.
//        (?=.*[0-9]) represents a digit must occur at least once.
//        (?=.*[a-z]) represents a lower case alphabet must occur at least once.
//        (?=.*[A-Z]) represents an upper case alphabet that must occur at least once.
//        (?=.*[@#$%^&-+=()] represents a special character that must occur at least once.
//        (?=\\S+$) white spaces don’t allowed in the entire string.
//        .{8, 20} represents at least 8 characters and at most 20 characters.
//        $ represents the end of the string.
    }

    public static boolean emailAddress(String input){
        return !input.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean cellNumber(String input){
        return !input.matches("^\\+(?:[0-9] ?){6,14}[0-9]$");
    }

    public static boolean jobName(String input){
        return input.isEmpty() || input.isBlank();
    }

    public static boolean cryptoWallet(String input){
        return !input.matches("^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$");
    }

    public static boolean tagName(String input){
        return !input.matches("^[a-zA-Z0-9._-]+$");
    }

    public static boolean location(String input){
        return !input.matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
    }

    public static boolean productName(String input) {
        return !input.matches("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,255}[a-zA-Z0-9]$");
    }

    public static boolean coordinate(Double latitude, Double longitude) {
        return !(-90<=latitude&&latitude<=90&&-180<=longitude&&longitude<=180);
    }

    public static boolean address(String input) {
        return input.isEmpty() || input.isBlank();
    }
}
