package org.exsel.ui;

import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * Created by sbt-konovalov-gv on 29.01.2018.
 */
public class FindByParametrisedAnnotationHandler extends AbstractAnnotations {

    private Field field;

    public FindByParametrisedAnnotationHandler(Field field) {
        this.field = field;
    }

    @Override
    public By buildBy(){
        return handle(this.field);
//        try {
//            return handle(this.field);
//        }catch (NullPointerException e){
//            throw new NullPointerException(e.getMessage());
//        }
    }

    @Override
    public boolean isLookupCached() {
        return false;
    }

    public static boolean isFindByParametrisedAnnotationPresent(Field f){
        if(f.isAnnotationPresent(FindByPar.class))
            return true;
        else
            return (!f.isAnnotationPresent(FindBy.class) && f.getType().isAnnotationPresent(FindByPar.class));
    }

    private static FindByPar getFindByParametrisedAnnotation(Field f){
        if(f.isAnnotationPresent(FindByPar.class))
            return f.getAnnotation(FindByPar.class);
        else if (!f.isAnnotationPresent(FindBy.class) && f.getType().isAnnotationPresent(FindByPar.class))
            return f.getType().getAnnotation(FindByPar.class);
        throw new NoSuchElementException("Not found FindByPar annotation");
    }


    public static By handle(Field field){
        By by = null;
        // Если поле аннотировано FindBy - ем
        //if(field.isAnnotationPresent(FindByPar.class))
        if(isFindByParametrisedAnnotationPresent(field))
            by = buildByImpl(field);
//            // Если класс аннотирован FindBy - ем
//        else if(field.getType().isAnnotationPresent(ReactFindBy.class))
//            by = buildByClassImpl(field);
        return by;
    }



    private static By buildByImpl(Field field) {
        if(isFindByParametrisedAnnotationPresent(field)) {
            String xpath = null;
            Class<?> clazz;
            if(List.class.isAssignableFrom(field.getType()))
                clazz = getGenericParameterClass(field);
            else
                clazz = field.getType();

            while(clazz != Object.class) {
                try {
                    xpath = clazz.getAnnotation(FindByPar.class).xpath();
                    if(!".".equals(xpath))
                        break;
                }catch (Exception e){}
                clazz = clazz.getSuperclass();
            }

            FindByPar annotation = getFindByParametrisedAnnotation(field);

//            String param1 = annotation.param1();
//            String param2 = annotation.param2();
//            String param3 = annotation.param3();
//
//            Pattern pat = Pattern.compile("%s");
//            Matcher mat = pat.matcher(xpath);
//
//            int count = 0;
//            while(1==1){
//                if (mat.find())
//                    count++;
//                else
//                    break;
//            }
//
//            if("null".equals(annotation.param2()) && count > 1)
//                param2 = param1;
//            if("null".equals(annotation.param3()) && count > 2)
//                param3 = param1;

            String param1 = annotation.param1();
            String param2 = annotation.param2();
            String param3 = annotation.param3();

            return By.xpath(String.format(xpath, param1, param2, param3));
        }
        throw new RuntimeException(String.format("Cannot determine how to locate field %s", field.getName()));
    }
    public static String getFindByAnnotation(Field f) {
        try {
            if (f.isAnnotationPresent(FindBy.class))
                return f.getAnnotation(FindBy.class).xpath();
            return ((FindBy) getAnnotation(isList(f) ? getGenericParameterClass(f) : f.getType())).xpath();
        } catch (Exception e) {
            return null;
        }
    }
    private static <T extends Annotation> T getAnnotation(Class<?> clazz) {
        if (clazz.isAnnotationPresent(FindBy.class))
            return (T) clazz.getAnnotation(FindBy.class);
        else if (clazz == Object.class) return null;
        return getAnnotation(clazz.getSuperclass());
    }
    public static Class getGenericParameterClass(Field field) {
        Type genericType = field.getGenericType();
        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }
    private static boolean isList(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

//    private static By buildByClassImpl(Field field) {
//        Class<?> clazz = field.getType();
//        while (clazz != Object.class) {
//            if (clazz.isAnnotationPresent(ReactFindBy.class)) {
//                // TODO - создаеми By
//                //return buildByFromFindBy(clazz.getAnnotation(FindBy.class));
//                return null;
//            }
//            clazz = clazz.getSuperclass();
//        }
//        throw new RuntimeException(String.format("Cannot determine how to locate instance of %s", field.getType()));
//    }
}
