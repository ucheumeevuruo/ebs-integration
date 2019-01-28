/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  org.apache.commons.dbutils.PropertyHandler
 *  org.apache.commons.lang3.StringUtils
 *  org.plexada.si.siebel.annotations.BusinessField
 *  org.plexada.si.siebel.utils.ColumnHandler
 */
package com.plexadasi.siebel;

import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.apache.commons.dbutils.PropertyHandler;
import org.apache.commons.lang3.StringUtils;
import org.plexada.si.siebel.annotations.BusinessField;
import org.plexada.si.siebel.utils.ColumnHandler;

public class BeanProcessor {
    protected static final int PROPERTY_NOT_FOUND = -1;
    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap();
    private static final ServiceLoader<ColumnHandler> COLUMN_HANDLERS = ServiceLoader.load(ColumnHandler.class);
    private static final ServiceLoader<PropertyHandler> PROPERTY_HANDLERS = ServiceLoader.load(PropertyHandler.class);

    public <T> T toBean(SiebelPropertySet propertySet, Class<? extends T> type) throws SiebelException {
        T bean = this.newInstance(type);
        bean = this.populateBean(bean, propertySet, type);
        return bean;
    }

    public SiebelPropertySet beanFieldsToPropertySet(Class<?> type) throws SiebelException {
        return this.beanFieldsToPropertySet(type, this.propertyDescriptors(type));
    }

    protected <T> T newInstance(Class<T> c) throws SiebelException {
        try {
            return c.newInstance();
        }
        catch (InstantiationException e) {
            throw new SiebelException(-3000, 3000, "Cannot create " + c.getName() + ": " + e.getMessage());
        }
        catch (IllegalAccessException e) {
            throw new SiebelException(-3000, 3000, "Cannot create " + c.getName() + ": " + e.getMessage());
        }
    }

    public <T> T populateBean(T bean, SiebelPropertySet columnToProperty, Class<?> type) throws SiebelException {
        PropertyDescriptor[] props = this.propertyDescriptors(type);
        return this.populateBean(bean, props, columnToProperty, type);
    }

    private <T> T populateBean(T bean, PropertyDescriptor[] props, SiebelPropertySet columnToProperty, Class<?> type) throws SiebelException {
        Enumeration e = columnToProperty.getPropertyNames();
        block0 : while (e.hasMoreElements()) {
            String columnName = (String)e.nextElement();
            for (PropertyDescriptor prop : props) {
                String annotationProp;
                Annotation annotation;
                String propertyName = prop.getName();
                if (!BeanProcessor.hasAnnotation(propertyName, type, BusinessField.class) || !(annotationProp = ((BusinessField)(annotation = this.processAnnotation(propertyName, type))).value()).equalsIgnoreCase(columnName)) continue;
                Class propType = prop.getPropertyType();
                Object value = null;
                if (propType != null && (value = this.processColumn(columnToProperty, columnName, propType)) == null && propType.isPrimitive()) {
                    value = primitiveDefaults.get(propType);
                }
                this.callSetter(bean, prop, value);
                continue block0;
            }
        }
        return bean;
    }

    private void callSetter(Object target, PropertyDescriptor prop, Object value) throws SiebelException {
        Method setter = this.getWriteMethod(target, prop, value);
        if (setter == null || setter.getParameterTypes().length != 1) {
            return;
        }
        try {
            Class firstParam = setter.getParameterTypes()[0];
            for (PropertyHandler handler : PROPERTY_HANDLERS) {
                if (!handler.match(firstParam, value)) continue;
                value = handler.apply(firstParam, value);
                break;
            }
            if (!this.isCompatibleType(value, firstParam)) {
                SiebelException se = new SiebelException();
                se.setMessage("Cannot set " + prop.getName() + ": incompatible types, cannot convert " + value.getClass().getName() + " to " + firstParam.getName());
                throw se;
            }
            setter.invoke(target, value);
        }
        catch (IllegalArgumentException e) {
            throw new SiebelException(-3000, 3000, "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
        catch (IllegalAccessException e) {
            throw new SiebelException(-3000, 3000, "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
        catch (InvocationTargetException e) {
            throw new SiebelException(-3000, 3000, "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    private PropertyDescriptor[] propertyDescriptors(Class<?> c) throws SiebelException {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);
        }
        catch (IntrospectionException e) {
            // empty catch block
        }
        return beanInfo.getPropertyDescriptors();
    }

    private boolean isCompatibleType(Object value, Class<?> type) {
        if (value == null || type.isInstance(value)) {
            return true;
        }
        return false;
    }

    private static boolean hasAnnotation(String fieldName, Class<?> type, Class annotation) {
        try {
            return type.getDeclaredField(fieldName).isAnnotationPresent(annotation);
        }
        catch (NoSuchFieldException e) {
        }
        catch (SecurityException e) {
            // empty catch block
        }
        return false;
    }

    private SiebelPropertySet beanFieldsToPropertySet(Class<?> type, PropertyDescriptor[] props) throws SiebelException {
        SiebelPropertySet propertySet = new SiebelPropertySet();
        for (PropertyDescriptor prop : props) {
            String propertyName = prop.getName();
            if (!BeanProcessor.hasAnnotation(propertyName, type, BusinessField.class)) continue;
            Annotation annotation = this.processAnnotation(propertyName, type);
            propertySet.setProperty(((BusinessField)annotation).value(), propertyName);
        }
        return propertySet;
    }

    private Annotation processAnnotation(String propertyName, Class<?> type) throws SiebelException {
        Field field = null;
        try {
            field = type.getDeclaredField(propertyName);
        }
        catch (NoSuchFieldException e) {
            throw new SiebelException(-3000, 3000, "The field '" + propertyName + "' does not exist in the entity " + type.getSimpleName());
        }
        catch (SecurityException ex) {
            throw new SiebelException(-3000, 3000, "Cannot access the private field " + propertyName);
        }
        return field.getAnnotation(BusinessField.class);
    }

    public Object processColumn(SiebelPropertySet sps, String index, Class<?> propType) throws SiebelException {
        Object retval = sps.getProperty(index);
        if (!propType.isPrimitive() && !StringUtils.isNotBlank((CharSequence)String.valueOf(retval))) {
            return null;
        }
        for (ColumnHandler handler : COLUMN_HANDLERS) {
            if (!handler.match(propType)) continue;
            retval = handler.apply(sps, index);
            break;
        }
        return retval;
    }

    protected Method getWriteMethod(Object target, PropertyDescriptor prop, Object value) {
        Method method = prop.getWriteMethod();
        return method;
    }

    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }
}

