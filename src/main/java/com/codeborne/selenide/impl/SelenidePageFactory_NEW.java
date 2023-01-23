//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.codeborne.selenide.impl;

import com.codeborne.selenide.*;
import com.codeborne.selenide.ex.PageObjectException;
import org.exsel.ui.FindByParametrisedAnnotationHandler;
import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import static org.exsel.ui.FindByParametrisedAnnotationHandler.getFindByAnnotation;
import static org.exsel.ui.FindByParametrisedAnnotationHandler.isFindByParametrisedAnnotationPresent;

@ParametersAreNonnullByDefault
public class SelenidePageFactory_NEW implements PageObjectFactory {
    private static final Logger logger = LoggerFactory.getLogger(SelenidePageFactory_OLD.class);

    public SelenidePageFactory_NEW() {
    }

    @CheckReturnValue
    @Nonnull
    public <PageObjectClass> PageObjectClass page(Driver driver, Class<PageObjectClass> pageObjectClass) {
        try {
            Constructor<PageObjectClass> constructor = pageObjectClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return this.page(driver, constructor.newInstance());
        } catch (ReflectiveOperationException var4) {
            throw new PageObjectException("Failed to create new instance of " + pageObjectClass, var4);
        }
    }

    @CheckReturnValue
    @Nonnull
    public <PageObjectClass, T extends PageObjectClass> PageObjectClass page(Driver driver, T pageObject) {
        Type[] types = pageObject.getClass().getGenericInterfaces();
        this.initElements(driver, (WebElementSource)null, pageObject, types);
        return pageObject;
    }

    public void initElements(Driver driver, @Nullable WebElementSource searchContext, Object page, Type[] genericTypes) {
        for(Class<?> proxyIn = page.getClass(); proxyIn != Object.class; proxyIn = proxyIn.getSuperclass()) {
            this.initFields(driver, searchContext, page, proxyIn, genericTypes);
        }

    }

    protected void initFields(Driver driver, @Nullable WebElementSource searchContext, Object page, Class<?> proxyIn, Type[] genericTypes) {
        Field[] fields = proxyIn.getDeclaredFields();
        Field[] var7 = fields;
        int var8 = fields.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            Field field = var7[var9];
            // PRN заменить полностью этот if
            if (!isInitialized(page, field)) {
                By selector;
                String xpath;
                if (field.getName().equals("parent"))
                    break;
                if (isFindByParametrisedAnnotationPresent(field)) {
                    selector = new FindByParametrisedAnnotationHandler(field).buildBy();
                } else if ((xpath = getFindByAnnotation(field)) != null) {
                    selector = By.xpath(xpath);
                } else
                    selector = findSelector(driver, field);

                Object value = decorate(page.getClass().getClassLoader(), driver, searchContext, field, selector, genericTypes);
                if (value != null) {
                    setFieldValue(page, field, value);
                }
            }
        }

    }

    @Nonnull
    protected By findSelector(Driver driver, Field field) {
        return (new Annotations(field)).buildBy();
    }

    protected boolean shouldCache(Field field) {
        return (new Annotations(field)).isLookupCached();
    }

    protected void setFieldValue(Object page, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(page, value);
        } catch (IllegalAccessException var5) {
            throw new PageObjectException("Failed to assign field " + field + " to value " + value, var5);
        }
    }

    @CheckReturnValue
    protected boolean isInitialized(Object page, Field field) {
        try {
            field.setAccessible(true);
            return field.get(page) != null;
        } catch (IllegalAccessException var4) {
            throw new PageObjectException("Failed to access field " + field + " in " + page, var4);
        }
    }

    @CheckReturnValue
    @Nonnull
    public ElementsContainer createElementsContainer(Driver driver, @Nullable WebElementSource searchContext, Field field, By selector) {
        try {
            WebElementSource self = new ElementFinder(driver, searchContext, selector, 0);
            if (this.shouldCache(field)) {
                self = new LazyWebElementSnapshot((WebElementSource)self);
            }

            return this.initElementsContainer(driver, field, (WebElementSource)self);
        } catch (ReflectiveOperationException var6) {
            throw new PageObjectException("Failed to create elements container for field " + field.getName(), var6);
        }
    }

    @CheckReturnValue
    @Nonnull
    ElementsContainer initElementsContainer(Driver driver, Field field, WebElementSource self) throws ReflectiveOperationException {
        Type var6 = field.getGenericType();
        Type[] var10000;
        if (var6 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)var6;
            var10000 = parameterizedType.getActualTypeArguments();
        } else {
            var10000 = new Type[0];
        }

        Type[] genericTypes = var10000;
        return this.initElementsContainer(driver, field, self, field.getType(), genericTypes);
    }

    @CheckReturnValue
    @Nonnull
    public ElementsContainer initElementsContainer(Driver driver, Field field, WebElementSource self, Class<?> type, Type[] genericTypes) throws ReflectiveOperationException {
        if (Modifier.isInterface(type.getModifiers())) {
            throw new IllegalArgumentException("Cannot initialize field " + field + ": " + type + " is interface");
        } else if (Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalArgumentException("Cannot initialize field " + field + ": " + type + " is abstract");
        } else {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            ElementsContainer result = (ElementsContainer)constructor.newInstance();
            this.initElements(driver, self, result, genericTypes);
            return result;
        }
    }

    @CheckReturnValue
    @Nullable
    public final Object decorate(ClassLoader loader, Driver driver, @Nullable WebElementSource searchContext, Field field, By selector) {
        Type[] classGenericTypes = field.getDeclaringClass().getGenericInterfaces();
        return this.decorate(loader, driver, searchContext, field, selector, classGenericTypes);
    }

    @Nullable
    @CheckReturnValue
    private String alias(Field field) {
        As alias = (As)field.getAnnotation(As.class);
        return alias == null ? null : alias.value();
    }

    @CheckReturnValue
    @Nullable
    public Object decorate(ClassLoader loader, Driver driver, @Nullable WebElementSource searchContext, Field field, By selector, Type[] genericTypes) {
        String alias = this.alias(field);
        if (ElementsContainer.class.equals(field.getDeclaringClass()) && "self".equals(field.getName())) {
            if (searchContext != null) {
                return ElementFinder.wrap(SelenideElement.class, searchContext);
            } else {
                logger.warn("Cannot initialize field {}", field);
                return null;
            }
        } else if (WebElement.class.isAssignableFrom(field.getType())) {
            return this.decorateWebElement(driver, searchContext, selector, field, alias);
        } else if (!ElementsCollection.class.isAssignableFrom(field.getType()) && !this.isDecoratableList(field, genericTypes, WebElement.class)) {
            if (ElementsContainer.class.isAssignableFrom(field.getType())) {
                return this.createElementsContainer(driver, searchContext, field, selector);
            } else {
                //PRN тупо добавил  ||this.isDecoratableList2(field, genericTypes, ElementsContainer.class)
                return (this.isDecoratableList(field, genericTypes, ElementsContainer.class)||this.isDecoratableList2(field, genericTypes, ElementsContainer.class)) ? this.createElementsContainerList(driver, searchContext, field, genericTypes, selector) : this.defaultFieldDecorator(driver, searchContext).decorate(loader, field);
            }

        } else {
            return this.createElementsCollection(driver, searchContext, selector, field, alias);
        }
    }

    @Nonnull
    protected SelenideElement decorateWebElement(Driver driver, @Nullable WebElementSource searchContext, By selector, Field field, @Nullable String alias) {
        return this.shouldCache(field) ? LazyWebElementSnapshot.wrap(new ElementFinder(driver, searchContext, selector, 0, alias)) : ElementFinder.wrap(driver, SelenideElement.class, searchContext, selector, 0, alias);
    }

    @Nonnull
    protected ElementsCollection createElementsCollection(Driver driver, @Nullable WebElementSource searchContext, By selector, Field field, @Nullable String alias) {
        CollectionSource collection = new BySelectorCollection(driver, searchContext, selector);
        if (alias != null) {
            ((CollectionSource)collection).setAlias(alias);
        }

        if (this.shouldCache(field)) {
            collection = new LazyCollectionSnapshot((CollectionSource)collection);
        }

        return new ElementsCollection((CollectionSource)collection);
    }

    @CheckReturnValue
    @Nonnull
    protected DefaultFieldDecorator defaultFieldDecorator(Driver driver, @Nullable WebElementSource searchContext) {
        SearchContext context = searchContext == null ? driver.getWebDriver() : searchContext.getWebElement();
        return new DefaultFieldDecorator(new DefaultElementLocatorFactory((SearchContext)context));
    }

    @CheckReturnValue
    @Nonnull
    protected List<ElementsContainer> createElementsContainerList(Driver driver, @Nullable WebElementSource searchContext, Field field, Type[] genericTypes, By selector) {
        Class<?> listType = this.getListGenericType(field, genericTypes);
        if (listType == null) {
            throw new IllegalArgumentException("Cannot detect list type for " + field);
        } else {
            CollectionSource collection = new BySelectorCollection(driver, searchContext, selector);
            if (this.shouldCache(field)) {
                collection = new LazyCollectionSnapshot((CollectionSource)collection);
            }

            return new ElementsContainerCollection(this, driver, field, listType, genericTypes, (CollectionSource)collection);
        }
    }

    @CheckReturnValue
    protected boolean isDecoratableList(Field field, Type[] genericTypes, Class<?> type) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        } else {
            Class<?> listType = this.getListGenericType(field, genericTypes);
            return listType != null && type.isAssignableFrom(listType) && (field.getAnnotation(FindBy.class) != null || field.getAnnotation(FindBys.class) != null);
        }
    }
    // PRN добавить метод
    protected boolean isDecoratableList2(Field field, Type[] genericTypes, Class<?> type) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        Class<?> listType = getListGenericType(field, genericTypes);

        return listType != null && type.isAssignableFrom(listType)
                && ((field.getAnnotation(FindByPar.class) != null)||isFindByParametrisedAnnotationPresent(field)||getFindByAnnotation(field)!=null);
    }

    @CheckReturnValue
    @Nullable
    protected Class<?> getListGenericType(Field field, Type[] genericTypes) {
        Type fieldType = field.getGenericType();
        if (!(fieldType instanceof ParameterizedType)) {
            return null;
        } else {
            Type[] actualTypeArguments = ((ParameterizedType)fieldType).getActualTypeArguments();
            Type firstType = actualTypeArguments[0];
            if (firstType instanceof TypeVariable) {
                int indexOfType = this.indexOf(field.getDeclaringClass(), firstType);
                return (Class)genericTypes[indexOfType];
            } else if (firstType instanceof Class) {
                Class<?> classType = (Class)firstType;
                return classType;
            } else {
                throw new IllegalArgumentException("Cannot detect list type of " + field);
            }
        }
    }

    protected int indexOf(Class<?> klass, Type firstArgument) {
        Object[] objects = Arrays.stream(klass.getTypeParameters()).toArray();

        for(int i = 0; i < objects.length; ++i) {
            if (objects[i].equals(firstArgument)) {
                return i;
            }
        }

        return -1;
    }
}

