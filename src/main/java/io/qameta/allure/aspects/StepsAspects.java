package io.qameta.allure.aspects;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.exsel.annotations.Assume;
import org.exsel.annotations.Bug;
import org.exsel.annotations.MaxTimeOut;
import org.exsel.ui.config.TestConfigState;
import org.exsel.ui.listeners.TestNGListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static io.qameta.allure.util.AspectUtils.getParameters;
import static io.qameta.allure.util.AspectUtils.getParametersMap;
import static io.qameta.allure.util.NamingUtils.processNameTemplate;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static org.exsel.tools.allure.Attach.assume;
import static org.exsel.tools.allure.Attach.doAssume;

@Aspect
public class StepsAspects {
    public static Logger LOGGER = LoggerFactory.getLogger(StepsAspects.class);
    private static AllureLifecycle lifecycle;
    private static ThreadLocal<ArrayDeque<String>> assumeDeque = new ThreadLocal<>();
    //private static ThreadLocal<ArrayDeque<String>> stepDeque = new ThreadLocal<>();
    private static ThreadLocal<HashMap<String, Boolean>> stepDeque = new ThreadLocal<>();
    private static ThreadLocal<Boolean> failTest = new ThreadLocal<>();
    public static ThreadLocal<Integer> maxTimeoutInt = new ThreadLocal<>();
    public static ThreadLocal<Boolean> overlap = new ThreadLocal<>();
    public static ThreadLocal<ArrayDeque<Integer>> maxTimeoutDeque = new ThreadLocal<>();
    Logger logger = LoggerFactory.getLogger(StepsAspects.class);
    public Boolean isMaxTimeout;
    public String uuid;
    public Bug bugAnnotation;
    public Step stepAnnotation;
    Throwable finalExeption;

    @Pointcut("(@annotation(org.exsel.annotations.MaxTimeOut) && execution(* *(..))||@annotation(org.exsel.annotations.Assume) && execution(* *(..)))||(@annotation(org.exsel.annotations.Bug) && execution(* *(..)))||(@annotation(io.qameta.allure.Step) && execution(* *(..)))")
    public void aroundPointcut() {
    }


    //@SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
    @Around("aroundPointcut()")
    public Object step(final ProceedingJoinPoint joinPoint) throws Throwable {
      /*  LOGGER.debug(joinPoint.getSignature().toString());


        for (Object signatureArg: joinPoint.getArgs()) {
            System.out.println("Arg: " + signatureArg);
            LOGGER.debug("Arg: " + signatureArg);
        }*/


        Boolean over = false;
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Boolean isStep = methodSignature.getMethod().isAnnotationPresent(Step.class);
        Boolean isBug = methodSignature.getMethod().isAnnotationPresent(Bug.class);
        Boolean isAssumed = methodSignature.getMethod().isAnnotationPresent(Assume.class);
        isMaxTimeout = methodSignature.getMethod().isAnnotationPresent(MaxTimeOut.class);
        if (isMaxTimeout)
            over = methodSignature.getMethod().getAnnotation(MaxTimeOut.class).overlap();
        bugAnnotation = methodSignature.getMethod().getAnnotation(Bug.class);
        stepAnnotation = methodSignature.getMethod().getAnnotation(Step.class);
        if (isStep) {
            if (stepDeque.get() == null) stepDeque.set(new HashMap<>());
            stepDeque.get().put(joinPoint.getSignature().toString(), false);
            if (stepDeque.get().size() == 1)
                System.out.println(joinPoint.toShortString().replace("execution(", "").replace("))", ")") + " started...");

            logger.debug("Start Step " + methodSignature.getMethod().getName() + " " + stepAnnotation.value());
            uuid = UUID.randomUUID().toString();
            final String name = Optional.of(stepAnnotation.value())
                    .filter(v -> !v.isEmpty())
                    .map(value -> processNameTemplate(value, getParametersMap(methodSignature, joinPoint.getArgs())))
                    .orElse(methodSignature.getName());

            final StepResult result = new StepResult()
                    .setName(name)
                    .setParameters(getParameters(methodSignature, joinPoint.getArgs()));
            getLifecycle().startStep(uuid, result);
        }
        if (isMaxTimeout) {
            if (maxTimeoutDeque.get() == null)
                maxTimeoutDeque.set(new ArrayDeque<>());
            //Если у метода аннотация MaxTimeOut с overlap=true, тогда запоминаем и устанавливаем maxTimeoutInt
            //иначе (overlap=false)
            // если overlap уже был true, тогда значение берем из очереди
            // иначе берем значение из параметра
            if (methodSignature.getMethod().getAnnotation(MaxTimeOut.class).overlap()) {
                overlap.set(true);
                maxTimeoutInt.set(methodSignature.getMethod().getAnnotation(MaxTimeOut.class).seconds());
            } else {
                if ((overlap.get() != null) && (overlap.get()))
                    maxTimeoutInt.set(maxTimeoutDeque.get().size() == 0 ? methodSignature.getMethod().getAnnotation(MaxTimeOut.class).seconds() : maxTimeoutDeque.get().getLast());
                else
                    maxTimeoutInt.set(methodSignature.getMethod().getAnnotation(MaxTimeOut.class).seconds());
            }
            maxTimeoutDeque.get().add(maxTimeoutInt.get());

        }
        if (isAssumed) {
            // Запоминаем метод, в которм был применен @Assume
            if (assumeDeque.get() == null)
                assumeDeque.set(new ArrayDeque<>());
            assumeDeque.get().add(joinPoint.getSignature().toString());
        }
        try {
            final Object proceed = joinPoint.proceed();
            if (isStep)
                if (stepDeque.get().containsKey(joinPoint.getSignature().toString()) && stepDeque.get().get(joinPoint.getSignature().toString()).booleanValue())
                    getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
                else getLifecycle().updateStep(s -> s.setStatus(Status.PASSED));
            return proceed;
        } catch (Throwable e) {
            // При наличии аннотации @Bug  в любом случае выбрасываем  AssertionError с комментарием, указанным в value()
            if (isBug) {
                finalExeption = new AssertionError(
                        (e.getMessage() + "\n\t" + "\n\t" + (methodSignature.getMethod().getAnnotation(Bug.class).isActive() ? "Неисправлен дефект: " : "Исправлен дефект: ") +
                                methodSignature.getMethod().getAnnotation(Bug.class).value()));
                finalExeption.setStackTrace(e.getStackTrace());
                e = finalExeption;
            }

            if (isStep) {
                for (Map.Entry<String, Boolean> entry : stepDeque.get().entrySet())
                    entry.setValue(true);
                Throwable finalE1 = e;
                getLifecycle().updateStep(s -> s
                        .setStatus(getStatus(finalE1).orElse(Status.BROKEN))
                        .setStatusDetails(getStatusDetails(finalE1).orElse(null)));
                StringBuilder sb = new StringBuilder();
                sb.append("\n").append(e.getMessage());
                for (StackTraceElement el : e.getStackTrace())
                    sb
                            .append("\n\t")
                            .append(el.toString());
                sb.append("\n\t");

                getLifecycle().addAttachment("error", "text/plain", ".txt", sb.toString().getBytes(StandardCharsets.UTF_8));
                try {
                    TestNGListener.doScreenShot();
                } catch (Exception e1) {
                }

            }
            // Если метод помечен @Assume или метод выше помечен этой аннтотацией
            //тогда не поднимаем ошибку сразу, накапливаем
            if (isAssumed || (assumeDeque.get() != null && (assumeDeque.get().size() > 0))) {
                Throwable finalE = e;
                assume().method(a -> {
                    try {
                        throw finalE;
                    } catch (Throwable throwable) {
                        throw new Error(finalE.getMessage());
                    }
                });
                return null;

            }
            // Иначе поднимаем ошибку,
            // и отображаем также все ошибки, которые поднималиь раннее, но были погашены @Assume
            else {
                if (doAssume() != null) {
                    Throwable finalE = e;
                    assume().method(a -> {
                        try {
                            throw finalE;
                        } catch (Throwable throwable) {
                            throw new Error(finalE.getMessage());
                        }
                    });
                }
                throw e;
            }
        } finally {
            //getLifecycle().stopStep(uuid);
            if (isStep) {
                //getLifecycle().stopStep(uuid);
                getLifecycle().stopStep();
                stepDeque.get().remove(joinPoint.getSignature().toString());
                if (stepDeque.get().size() == 0)
                    System.out.println(joinPoint.toShortString().replace("execution(", "").replace("))", ")") + " ...finished!");
            }
            if (isMaxTimeout) {
                if (over)
                    overlap.set(false);
                if (maxTimeoutDeque.get().size() > 0)
                    maxTimeoutDeque.get().removeLast();
                try {
                    Integer value = maxTimeoutDeque.get().getLast();
                    maxTimeoutInt.set(value);
                } catch (NoSuchElementException nsee) {
                    maxTimeoutInt.set(TestConfigState.getMaxTimeoutDefault());
                }
            }

            if (isAssumed) {
                //Помечаем, что в конце теста нужно поднять ошибку, если соответсвующий параметр true
                if (methodSignature.getMethod().getAnnotation(Assume.class).failTest())
                    failTest.set(true);
                //После завершения метода удаляем его из очереди @Assume
                assumeDeque.get().remove(joinPoint.getSignature().toString());
            }
        }
    }

    /**
     * For tests only.
     *
     * @param allure allure lifecycle to set.
     */
    public static void setLifecycle(final AllureLifecycle allure) {
        lifecycle = allure;
    }

    public static AllureLifecycle getLifecycle() {
        if (Objects.isNull(lifecycle)) {
            lifecycle = Allure.getLifecycle();
        }
        return lifecycle;
    }

    @After("@annotation(org.testng.annotations.Test) && execution(* *(..))")
    public void afterTest(JoinPoint joinPoint) throws Throwable {
        Throwable er = doAssume();
        // Если в тесте подписалась ошибка, и не было
        if (er != null && ((failTest.get() != null && failTest.get())))
            throw er;
    }


}