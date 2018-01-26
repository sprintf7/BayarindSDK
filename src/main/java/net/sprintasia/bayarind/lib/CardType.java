package net.sprintasia.bayarind.lib;

/**
 * Created by Nazzar on 12/20/2017.
 */

import java.util.regex.Pattern;

public enum CardType {

    UNKNOWN,
    VISA("^4[0-9]{6,}$"),
    MASTERCARD("^5[1-5][0-9]{5,}$"),
    AMEX("^3[47][0-9]{5,}$"),
    DINERS_CLUB("^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{12,15}$"),
    DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$"),
    JCB("^(?:2131|1800|35[0-9]{3})[0-9]{3,}$"),
    CHINA_UNION_PAY("^62[0-9]{14,17}$");

    private Pattern pattern;

    CardType() {
        this.pattern = null;
    }

    CardType(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static CardType detect(String cardNumber) {

        for (CardType cardType : CardType.values()) {
            if (null == cardType.pattern) continue;
            if (cardType.pattern.matcher(cardNumber).matches()) return cardType;
        }

        return UNKNOWN;
    }

}