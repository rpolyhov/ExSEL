//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.exsel.selenide;

import com.codeborne.selenide.As;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.codeborne.selenide.impl.*;
import org.exsel.ui.FindByParametrisedAnnotationHandler;
import org.exsel.ui.annotations.FindByPar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.exsel.ui.FindByParametrisedAnnotationHandler.getFindByAnnotation;
import static org.exsel.ui.FindByParametrisedAnnotationHandler.isFindByParametrisedAnnotationPresent;

@ParametersAreNonnullByDefault
public class SelenidePageFactoryModified extends SelenidePageFactory {
    private static final Logger logger = LoggerFactory.getLogger(SelenidePageFactory_OLD.class);

    public SelenidePageFactoryModified() {
    }


    @Override
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



    @CheckReturnValue
    @Nullable
    @Override
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

    private String alias(Field field) {
        As alias = (As)field.getAnnotation(As.class);
        return alias == null ? null : alias.value();
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


}

