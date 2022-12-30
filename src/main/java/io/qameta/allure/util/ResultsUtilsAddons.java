package io.qameta.allure.util;

import io.qameta.allure.Description;
import io.qameta.allure.model.ExecutableItem;
import io.qameta.allure.util.ResultsUtils;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Дополнения обработчика аннотация io.qameta.allure.Description и org.testng.annotations.Test
 *
 * Используются в модифицированном io.qameta.allure.testng.AllureTestNg
 *
 */
public class ResultsUtilsAddons{

    /**
     * Параметризует атрибут аннотации org.testng.annotations.Test description (имя теста, отображаемое в Allure отчете)
     *
     * @param method    - тестовый метод
     * @param methodArgs    - список аргументов тестового метода (как они пердаются)
     * @param item - объект, на основании которого формируется Allure отчет для тестового метода
     * @return - Параметризованный текст атрибута
     */
    public static void processName(Method method, Object[] methodArgs, ExecutableItem item) {
        if (method.isAnnotationPresent(Test.class)) {
            String desc = method.getAnnotation(Test.class).description();
            if(!"".equals(desc))
                item.setName(handleDescriptionText(desc, methodArgs, item));
        }
    }

    /**
     * Параметризует аннотацию io.qameta.allure.Description (вызывается исходная реализация или модифицированная)
     *
     * @param method    - тестовый метод
     * @param methodArgs    - список аргументов тестового метода (как они пердаются)
     * @param item - объект, на основании которого формируется Allure отчет для тестового метода
     * @return - Параметризованный текст аннотации
     */
    public static void processDescription(ClassLoader classLoader, Method method, Object[] methodArgs, ExecutableItem item) {
        if (method.isAnnotationPresent(Description.class)) {
            Description description = method.getAnnotation(Description.class);
            if (description.useJavaDoc())
                ResultsUtils.processDescription(classLoader, method, item);
            else
                item.withDescription(handleDescriptionText(description.value(), methodArgs, item));
        }
    }

    /**
     * Параметризация текст, на основании переданных в метод параметров
     *
     * @param desc - исходный текст
     * @param methodArgs - передаваемые в метод парамтры
     * @param item - объект, на основании которого формируется Allure отчет для данного метода
     * @return - параметризованный текст
     */
    public static String handleDescriptionText(String desc, Object[] methodArgs, ExecutableItem item){
        try {
            int index = -1;
            // записи типа: "{0}"
            Pattern pattern = Pattern.compile("\\{\\d\\}($|[^\\.])");
            // записи типа: "{1}."
            Pattern dotPattern = Pattern.compile("\\{\\d\\}\\.");
            Matcher pat = pattern.matcher(desc);
            Matcher dot = dotPattern.matcher(desc);

            while (pat.find()) {
                index = Integer.valueOf(desc.substring(pat.start(), pat.end()).split("\\{")[1].split("\\}")[0]);
                desc = desc.replace(
                        desc.substring(pat.start(), pat.end() - 1), item.getParameters().get(index).getValue());
                pat = pattern.matcher(desc);
            }

            while (dot.find()) {
                index = Integer.valueOf(desc.substring(dot.start(), dot.end()).split("\\{")[1].split("\\}")[0]);
                desc = handleInvokedText(methodArgs[index], desc, index);
                dot = dotPattern.matcher(desc);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return desc;
    }

    /**
     * Параметризация текста, с вызовом методов, передаваемых в качестве параметрой в тестовый метод
     *
     * @param o - объект-параметр тестового метода
     * @param desc - исходное текстовое описание
     * @param index - индекс передаваемого параметра
     * @return - Параметризованный текст
     */
    private static String handleInvokedText(Object o, String desc, int index){
        try {
            if (desc.contains(String.format("{%s}.",index))){
                String methodName = desc.split("\\{" + index + "\\}\\.")[1].split("\\(\\)")[0];
                desc = desc.replace( String.format("{%s}.%s()", index, methodName), o.getClass().getMethod(methodName).invoke(o).toString());
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return desc;
    }
}
