package org.exsel.waitings;

import lombok.SneakyThrows;
import org.exsel.asserts.SoftHamcrestAssert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static org.exsel.tools.allure.Attach.step;
import static org.hamcrest.core.Is.is;

public class Until {

    private static Logger log = LoggerFactory.getLogger(Until.class);
    private SoftHamcrestAssert softAssert;

//    private static final ThreadLocal<Until> untilThreadLocal = new ThreadLocal<>();

//    private static Until getUntil(){
//        if(untilThreadLocal.get() == null)
//            untilThreadLocal.set(new Until());
//        return untilThreadLocal.get();
//    }

    private static Until newUntil() {
        return new Until();
    }

    public static boolean isTrueStatic(Object o, String methodName, int timeOut) {
        return isTrueStatic(o, methodName, timeOut, 1000);
    }

    private static boolean isTrueStatic(Object o, String methodName, int timeOut, int pullingInterval) {
        long timeToFinish = System.currentTimeMillis() + timeOut;
        while (timeToFinish > System.currentTimeMillis()) {
            try {
                //if ((Boolean) o.getClass().getDeclaredMethod(methodName).invoke(o))
                if ((Boolean) o.getClass().getMethod(methodName).invoke(o))
                    return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            }
            try {
                Thread.sleep(pullingInterval);
            } catch (Exception e) {
            }
        }
        return false;
    }


    /**
     * Ожидаем, когда функция f вернет true, в случае ошибки или по таймауту, возвращается false
     *
     * @param f       - метод, который должен вернуть true
     * @param timeOut - время ожидания события true
     * @return - дождались события или нет
     */
    public static boolean isTrueStatic(int timeOut, Supplier<Boolean> f) {
        return isTrueStatic(timeOut, 1000, f);
    }


    /**
     * Ожидаем, когда функция f вернет true, в случае ошибки или по таймауту, возвращается false
     *
     * @param f               - метод, который должен вернуть true
     * @param timeOut         - время ожидания события true
     * @param pullingInterval - интервал опроса события
     * @return - дождались события или нет
     */
    public static boolean isTrueStatic(long timeOut, int pullingInterval, Supplier<Boolean> f) {
        return isTrueStatic(
                newUntil()
                        .setTimeout(timeOut)
                        .setPullingInterval(pullingInterval)
                , f);
    }

    /**
     * Ожидаем, когда функция f вернет true, в случае ошибки или по таймауту, возвращается false
     *
     * @param until - объект содержит параметры ожидания
     * @param f     - метод, который должен вернуть true
     * @return - дождались события или нет
     */
    private static boolean isTrueStatic(Until until, Supplier<Boolean> f) {
        long timeToFinish = System.currentTimeMillis() + until.timeout;
        while (timeToFinish > System.currentTimeMillis()) {
            try {
                if (f.get())
                    return true;
            } catch (Throwable e) {
                if (until.throwableThrow && until.ignore.length != 0) {
                    for (Class ignore : until.ignore)
                        if (ignore.isAssignableFrom(e.getClass()))
                            until.skip = true;
                }

                if (!until.skip && until.throwableThrow && until.toThrow.length != 0)
                    for (Class toThrow : until.toThrow)
                        if (toThrow.isAssignableFrom(e.getClass()))
                            throw e;
            }
            try {
                Thread.sleep(until.pullingInterval);
                if (until.ifNot != null)
                    until.ifNot.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Until assertTimeout(Long millis) {
        return newUntil()
                .setTimeout(millis);
    }

    public static Until assertTimeout(int millis) {
        return assertTimeout(Long.valueOf(millis));
    }

    public static Until assertTimeout(Long millis, int pullingInterval) {
        return newUntil()
                .setTimeout(millis)
                .setPullingInterval(pullingInterval);
    }

    public static Until assertTimeout(int millis, int pullingInterval) {
        return assertTimeout(Long.valueOf(millis), pullingInterval);
    }

    private long timeout = 1000;
    private int pullingInterval = 500;
    private boolean throwableThrow = true;
    private boolean skip = false;
    private Class[] toThrow = {};
    private Class[] ignore = {};
    private Runnable ifNot;

    private Until setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    private Until setPullingInterval(int pullingInterval) {
        this.pullingInterval = pullingInterval;
        return this;
    }

    public Until throwableThrow() {
        this.throwableThrow = true;
        return this;
    }

    public Until throwableIgnore() {
        this.throwableThrow = false;
        return this;
    }

    public Until throwableIgnore(Class ignore) {
        return throwableIgnore(new Class[]{ignore});
    }

    public Until throwableIgnore(Class... ignore) {
        this.throwableThrow = true;
        this.ignore = ignore;
        return this;
    }

    public Until throwableThrow(Boolean value) {
        this.throwableThrow = value;
        return this;
    }

    public Until throwableThrow(Class toThrow) {
        return throwableThrow(new Class[]{toThrow});
    }

    public Until throwableThrow(Class... toThrow) {
        this.throwableThrow = true;
        this.toThrow = toThrow;
        return this;
    }

    public Until ifNot(Runnable ifNotRunnable) {
        this.ifNot = ifNotRunnable;
        return this;
    }

    private boolean isTrue(Supplier<Boolean> f, long timeOut, int pullingInterval) {
        this.setTimeout(timeOut);
        this.setPullingInterval(pullingInterval);
        return isTrueStatic(this, f);
    }

    public boolean isTrue(Supplier<Boolean> f) {
        return isTrue(f, this.timeout, this.pullingInterval);
    }

    public <T> void assertThat(Supplier<T> actual, Matcher<? super T> matcher) {
        //matcher.matches(actual.get());
        this.stepMatcher(() -> isTrueStatic(this, () -> matcher.matches(actual.get())),
                "", actual.get(), matcher);
    }

    public <T> Until assertThat(T actual, Matcher<? super T> matcher) {
        this.stepMatcher(() -> isTrueStatic(this, () -> matcher.matches(actual)),
                "", actual, matcher);
        return this;
    }

    public <T extends Object> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        this.stepMatcher(() -> isTrueStatic(this, () -> matcher.matches(actual)),
                reason, actual, matcher);
    }

    public <T> void assertThat(T actual) {
        Matcher<? super T> matcher = is(true);
        this.stepMatcher(() -> isTrueStatic(this, () -> matcher.matches(actual)),
                "", actual, matcher);
    }

    public void assertThat(Supplier<Boolean> f) {
        // TODO + stepLog
        MatcherAssert.assertThat(String.format("timeout %s ms", this.timeout), isTrueStatic(this, f), is(true));
    }

    public <T> void assertThat(String reason, Supplier<Boolean> f) {
        String intReason = "";
        boolean actual = false;
        try {
            actual = isTrueStatic(this, f);
        } catch (Throwable e) {
            intReason = e.getMessage();
        }

        MatcherAssert.assertThat(String.format("timeout %s [%s] ms %s", reason, intReason, this.timeout), actual, is(true));
    }

    @SneakyThrows
    public void doThat(String reason, Supplier<Boolean> f) {
        // TODO + stepLog
        if (!isTrueStatic(this, f))
            throw new Exception(String.format("timeout %s ms %s", this.timeout, reason));
    }

    public static Until setTimeout(int millis, int pullingInterval) {
        return assertTimeout(Long.valueOf(millis), pullingInterval);
    }


    public void assertThatFalse(Supplier<Boolean> f) {
        // TODO + stepLog
        MatcherAssert.assertThat(String.format("timeout %s ms", this.timeout), isTrueStatic(this, f), is(false));
    }

    public void assertThatFalse(String reason, Supplier<Boolean> f) {
        // TODO + stepLog
        MatcherAssert.assertThat(String.format("timeout %s ms %s", reason, this.timeout), isTrueStatic(this, f), is(false));
    }

    public <T> void assertThatSupplier(String reason, Supplier<T> f, Matcher<? super T> matcher) {
        this.stepSupplierMatcher(() -> isTrueStatic(this, () -> matcher.matches(f.get())),
                reason, f, matcher);
    }


//    public Until doIf(String reason, Supplier<Boolean> f){
//        return this;
//    }
//
//    public void ifNot(Consumer c){
//
//    }


    public SoftAssert setSoftAssert() {
        return setSoftAssert(new SoftHamcrestAssert());
    }

    public SoftAssert setSoftAssert(SoftHamcrestAssert softAssert) {
        this.softAssert = softAssert;
        return this.new SoftAssert(this);
    }

    public class SoftAssert<T> {

        private Until until;

        public SoftAssert(Until until) {
            this.until = until;
        }

        public SoftAssert assertThat(final String reason, T actual, final Matcher<? super T> matcher) {
            if (!isTrueStatic(until, () -> matcher.matches(actual)))
                softAssert.assertThat(reason, actual, matcher);
            return this;
        }

        public SoftAssert assertThat(T actual, final Matcher<? super T> matcher) {
            if (!isTrueStatic(until, () -> matcher.matches(actual)))
                softAssert.assertThat(actual, matcher);
            return this;
        }

        public SoftAssert assertThat(String reason, Supplier<Boolean> f) {
            if (!isTrueStatic(until, f))
                softAssert.assertThat(reason, f, is(true));
            return this;
        }

        public SoftAssert assertThat(Supplier<Boolean> f) {
            if (!isTrueStatic(until, f))
                softAssert.assertThat(f, is(true));
            return this;
        }

        public SoftAssert assertThat(final String reason, Supplier<T> f, final Matcher<? super T> matcher) {
            try {
                //assertTimeout((int) timeout, pullingInterval).assertThat(f, matcher);
                until.assertThat(f, matcher);
            } catch (AssertionError e) {
                softAssert.assertThat(reason, f.get(), matcher);
            }
            return this;
        }

        public SoftAssert softAssertThat(final String reason, Supplier<String> f, final Matcher<? super String> matcher) {
            try {
                until.assertThat(f, matcher);
            } catch (AssertionError e) {
                softAssert.assertThat(reason, f.get(), matcher);
            }
            return this;
        }

        /*   public SoftAssert softAssertThat(final String reason, Supplier<String> f, final Matcher<? super String> matcher) {
               try {
                   assertTimeout((int) timeout, pullingInterval).assertThat(f, matcher);
               } catch (AssertionError e) {
                   softAssert.assertThat(reason, f.get(), matcher);
               }
               return this;
           }*/
        public void assertAll() {
            softAssert.assertAll();
        }
    }

    private static <T> String getMatcherResultDescription(String reason, T actual, Matcher<? super T> matcher) {
        Description description = new StringDescription();
        description.appendText(reason)
                .appendText("\nExpected: ")
                .appendDescriptionOf(matcher)
                .appendText("\n     but: ");
        matcher.describeMismatch(actual, description);
        return description.toString();
    }

    private <T> void stepMatcher(Supplier<Boolean> s, String reason, T actual, Matcher<? super T> matcher) {
        boolean result = s.get();
        step().message(e -> {
                    if (!result)
                        doAssertionError(reason, actual, matcher);
                },
                MatchersHelper.getMessage(reason, actual, matcher));
        log.debug("match -> " + matcher.toString());
    }

    private <T> void stepSupplierMatcher(Supplier<Boolean> s, String reason, Supplier<T> f, Matcher<? super T> matcher) {
        boolean result = s.get();
        //T matchTo = f.get();
        step().message(e -> {
                    if (!result)
                        doAssertionError(reason, f.get(), matcher);
                },
                MatchersHelper.getMessage(reason, null/*matchTo,*/, matcher));
        log.debug("match -> " + matcher.toString());
    }

    private <T> void doAssertionError(String reason, T actual, Matcher<? super T> matcher) {
        throw new AssertionError(getMatcherResultDescription(String.format("timeout %s ms %s", this.timeout, reason), actual, matcher));
    }
}
