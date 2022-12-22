package org.exsel.ui;

import com.codeborne.selenide.ElementsContainer;
import org.exsel.ui.annotations.IParentable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.List;
public class PageDecorator {

    private static Logger LOG = LoggerFactory.getLogger(PageDecorator.class);


    /**
     * Просаживаем WebDriver в объекты, реализующие интерфейс IDriverable
     *
     * @param object    - рутовый объект, в котором ищем объекты, в которые можно просадить WebDriver
     * @param rootClass - для какого класса или наследника можно вызывать рекурсию
     * @param <T>       - тип объекта, в котором производит поиск объектов для просадки
     */

    public static <T> void setParentToObject(T object, Class rootClass) {
        if (object.getClass() == Object.class)
            return;
        try {
            Field[] fields = object.getClass().getDeclaredFields();

            for (Field f : fields) {
                f.setAccessible(true);
                if (rootClass.isInstance(f.get(object)))
                    setParentToObject(f.get(object), rootClass);

                else if (isElementContainerList(f)) {

                    return;
                }
                if (f.get(object) instanceof IParentable)
                    ((IParentable) f.get(object)).setParent(object);

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean isElementContainerList(Field field) {
        if (!isParametrizedList(field)) {
            return false;
        } else {
            Class listParameterClass = getGenericParameterClass(field);
            return isElementsContainerElement(listParameterClass);
        }
    }

    private static boolean isParametrizedList(Field field) {
        return isList(field) && hasGenericParameter(field);
    }

    private static boolean hasGenericParameter(Field field) {
        return field.getGenericType() instanceof ParameterizedType;
    }
    public static boolean isList(Field f) {
        return List.class.isAssignableFrom(f.getType());
    }

    public static Class getGenericParameterClass(Field field) {
        Type genericType = field.getGenericType();
        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    public static boolean isElementsContainerElement(Class<?> clazz) {
        return ElementsContainer.class.isAssignableFrom(clazz);
    }





}
