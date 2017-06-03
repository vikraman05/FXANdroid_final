package comin.chaten.statens.caugfitten.servernendto.verification;


public class CheckCredentials {

    public static final int MIN_USERNAME_LENGTH = 5;
    public static final int MAX_USERNAME_LENGTH = 15;
    public static final int MIN_PASSWORD_LENGTH = 5;
    //public static final int MAX_PASSWORD_LENGTH = 20;

    public static boolean validUsername(String username) {
        if (username.length() > MAX_USERNAME_LENGTH || username.length() < MIN_USERNAME_LENGTH) {
            return false;
        }

        return containsValidCharacters(username);
    }

    public static boolean validPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH ) {
            return false;
        }

        return true;
    }

    private static boolean containsValidCharacters(String value) {
        for (Character c : value.toCharArray()) {
            if (!(Character.isDigit(c) || Character.isLetter(c))) {
                return false;
            }
        }

        return true;
    }

}
