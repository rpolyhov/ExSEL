package org.exsel.waitings;

import org.exsel.tools.allure.Attachments;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.exsel.tools.allure.Attach.step;


public class Try {

    private Logger logger = LoggerFactory.getLogger("Try");
    private int tryCount = 1;
    private String message = "";
    private Class<?> throwable = AssertionError.class;

    private static Try newUntil(){
        return new Try();
    }

    private Try setCount(int count){
        this.tryCount = count;
        return this;
    }

    public static Try tryCount(int count){
        return newUntil().setCount(count);
    }

    public Try message(String message){
        this.message = message;
        throwable = RuntimeException.class;
        return this;
    }

    public Try error(String message){
        this.message = message;
        throwable = AssertionError.class;
        return this;
    }

    public <S> void ofConsumer(Consumer<S> consumer){
        String exMessage = null;
        for(int i = 0; i< tryCount; i ++) {
            try {
                logger.debug(String.format("try + %d of %s - %s", i, consumer.hashCode(), message));
                consumer.accept(null);
                return;
            }catch (Throwable e){
                exMessage = e.getMessage();
                if(e.getClass().equals(Error.class))
                    throw e;
            }
        }
        Attachments.attachText("error", "after " + tryCount + " try got error \n" + message + " " + exMessage);
        if(throwable.equals(AssertionError.class))
            throw new AssertionError(exMessage);
        else
            throw new RuntimeException(exMessage);
    }

    public <T> void assertThatSupplier(String reason, Supplier<T> f, Matcher<? super T> matcher) {
        this.stepSupplierMatcher(()->isTrueStatic(this, ()-> matcher.matches(f.get())),
                reason, f, matcher);
    }

    private <T> void doAssertionError(String reason, T actual, Matcher<? super T> matcher){
        throw new AssertionError(getMatcherResultDescription(String.format("count %s ms %s", this.tryCount, reason), actual, matcher));
    }

    private static <T> String getMatcherResultDescription(String reason, T actual, Matcher<? super T> matcher){
        Description description = new StringDescription();
        description.appendText(reason)
                .appendText("\nExpected: ")
                .appendDescriptionOf(matcher)
                .appendText("\n     but: ");
        matcher.describeMismatch(actual, description);
        return description.toString();
    }

    private static boolean isTrueStatic(Try doTry, Supplier<Boolean> f) {
        for (int i = 0; i < doTry.tryCount; i++) {
            try {
                if (f.get())
                    return true;
            } catch (Error e) {
            }
//            }catch (Throwable e){
//                if(until.throwable)
//                    if(until.toThrow.length == 0)
//                        throw e;
////                    if(until.toThrow.isAssignableFrom(e.getClass()))
//                    else
//                        for(Class toThrow: until.toThrow)
//                            if(toThrow.isAssignableFrom(e.getClass()))
//                                throw e;
//            }
//            try {
//                Thread.sleep(until.pullingInterval);
//            } catch (Exception e) {}
//        }
        }
        return false;
    }

    private <T> void stepSupplierMatcher(Supplier<Boolean> s, String reason, Supplier<T> f, Matcher<? super T> matcher){
        boolean result = s.get();
        T matchTo = f.get();
        step().message(e-> {
                    if (!result)
                        doAssertionError(reason, matchTo, matcher);
                },
                MatchersHelper.getMessage(reason, matchTo, matcher));
        logger.debug("match -> " + matcher.toString());
    }
}
