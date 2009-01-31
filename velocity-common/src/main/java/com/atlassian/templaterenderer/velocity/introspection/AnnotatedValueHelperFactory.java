package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.Validate;

/**
 * Factory for {@link AnnotatedValueHelper}s.
 */
class AnnotatedValueHelperFactory {

    /**
     * Factory method for annotated value helper strategies.
     *
     * @param object Object to wrap
     * @param classResolver Class resolving strategy to use
     * @return helper for the provided value
     */
    static AnnotatedValueHelper getValueHelper(Object object, ObjectClassResolver classResolver)
    {
        Validate.notNull(classResolver, "classResolver must not be null");

        if (object instanceof BoxedValue)
            return new DefaultAnnotatedValueHelper(object, classResolver);

        return new NonAnnotatedValueHelper(object, classResolver);
    }

    /**
     * Optimized strategy for providing transparent access to non-boxed objects. get() and unbox() return the same value
     * and all annotation based methods act as if the wrapped value is not annotated.
     */
    static final class NonAnnotatedValueHelper implements AnnotatedValueHelper
    {
        private final Object targetObject;
        private final ObjectClassResolver classResolver;

        NonAnnotatedValueHelper(Object targetObject, ObjectClassResolver classResolver)
        {
            this.targetObject = targetObject;
            this.classResolver = classResolver;
        }

        public Object get()
        {
            return targetObject;
        }

        public Collection<Annotation> getAnnotations()
        {
            return Collections.emptyList();
        }

        public AnnotationBoxedElement getBoxedValueWithInheritedAnnotations()
        {
            return null;
        }

        public Class getTargetClass()
        {
            return classResolver.resolveClass(targetObject);
        }

        public Object unbox()
        {
            return targetObject;
        }

        public boolean isBoxedValue()
        {
            return false;
        }
    }

    /**
     * Helper object for transparent processing of boxed and non-boxed values.
     * If an object is not boxed, it is more optimal to use a {@link AnnotatedValueHelperFactory.NonAnnotatedValueHelper}
     */
    static final class DefaultAnnotatedValueHelper implements AnnotatedValueHelper
    {
        private final Object originalObject;
        private final Object targetObject;
        private final ObjectClassResolver classResolver;
        private final boolean boxedValue;

        DefaultAnnotatedValueHelper(Object targetObject, ObjectClassResolver classResolver)
        {
            this.originalObject = targetObject;
            this.targetObject = BoxingUtils.unboxObject(targetObject);
            this.classResolver = classResolver;
            this.boxedValue = (originalObject instanceof BoxedValue);
        }

        /**
         * @return the original object with which this object was constructed
         */
        public Object get()
        {
            return originalObject;
        }

        /**
         * @return unboxed object if this object was constructed with a {@link BoxedValue}; the original object otherwise.
         */
        public Object unbox()
        {
            return targetObject;
        }

        public Collection<Annotation> getAnnotations()
        {
            if (originalObject instanceof AnnotatedElement)
                return Arrays.asList(((AnnotatedElement) originalObject).getAnnotations());

            return Collections.emptyList();
        }

        public AnnotationBoxedElement getBoxedValueWithInheritedAnnotations()
        {
            if (!(originalObject instanceof AnnotatedValue))
                return null;

            AnnotatedValue annotatedValue = (AnnotatedValue) originalObject;

            Collection<Annotation> inheritableAnnotations = annotatedValue.getCollectionInheritableAnnotations();

            return new AnnotatedValue(targetObject, inheritableAnnotations);
        }

        public Class getTargetClass()
        {
           return classResolver.resolveClass(targetObject);
        }

        public boolean isBoxedValue()
        {
            return boxedValue;
        }
    }
}
