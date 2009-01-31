package com.atlassian.templaterenderer.velocity.introspection;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.Validate;

/**
 * Utiliies for validating annotation frameword objects.
 */
class AnnotationValidationUtils 
{
    /**
     * Asserts that a collection does not contain any null elements.
     * @param collection Collection to test
     * @param assertionMessage Message to use in the generated exception if the assertion fails.
     */
    public static void assertContainsNoNulls(Collection collection, String assertionMessage)
    {
        Validate.isTrue(!CollectionUtils.exists(collection, PredicateUtils.nullPredicate()), assertionMessage);
    }
}
