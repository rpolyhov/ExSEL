package org.exsel.waitings;

import org.hamcrest.Matcher;

public class MatchersHelper {

    public static <T> String getMessage(String reason, T actual, Matcher<? super T> matcher){
        return String.format(("".equals(reason) ? (reason + " %s") : ("Ожидаем, что: %s")), matcher.toString());
    }
}
