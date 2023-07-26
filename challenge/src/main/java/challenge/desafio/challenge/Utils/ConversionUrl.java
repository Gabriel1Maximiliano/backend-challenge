package challenge.desafio.challenge.Utils;

public class ConversionUrl {

    private static final String allowedAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private char[] allowedCharacters = allowedAlphabet.toCharArray();
    private int base = allowedCharacters.length;

    public String encodeUrl(Long input){

        int amountOfChar = (int) (Math.log(input) / Math.log(base)) + 1;
        StringBuilder encodedString = new StringBuilder(amountOfChar);

        while (input > 0) {
            encodedString.insert(0, allowedCharacters[(int) (input % base)]);
            input = input / base;
        }

        return encodedString.toString();
    }

    public long decodeUrl(String input) {
        char[] charactersToDecode = input.toCharArray();
        int length = charactersToDecode.length;

        long decoded = 0;
        long basePow = 1;

        for (int i = length - 1; i >= 0; i--) {
            int index = allowedAlphabet.indexOf(charactersToDecode[i]);
            decoded += index * basePow;
            basePow *= base;
        }
        return decoded;
    }
}
