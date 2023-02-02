package org.exsel.example;

import org.exsel.ui.ElementsContainerWrapper;
import org.exsel.ui.PageDecorator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.page;

public class Service {
    public static <T extends Object> T initPage(Class<T> clazz) {
        T page= null;
        try {
            page = clazz.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Field[] fields = getFields(page.getClass(), WebPage.class);
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                f.set(page, page(f.getType()));
                PageDecorator.setParentToObject(f.get(page), ElementsContainerWrapper.class);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return page;
    }

    public static Field[] getFields(Class clazz, Class... args) {
        if (clazz == Object.class)
            return new Field[]{};
        Set<Field> fields = new HashSet<>();

        while (clazz != Object.class) {
            for (Class parentClass : args)
                fields.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(
                        f -> Arrays.stream(f.getType().getInterfaces()).filter(
                                g -> g.equals(parentClass)).collect(Collectors.toList()).size() > 0).collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        }
        return fields.stream().toArray(Field[]::new);
    }

    public static boolean isOrChildOfClass(Class clazz, Class classOf) {
        while (clazz != Object.class) {
            if (clazz == null)
                return false;
            if (clazz.equals(classOf)) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }
}
