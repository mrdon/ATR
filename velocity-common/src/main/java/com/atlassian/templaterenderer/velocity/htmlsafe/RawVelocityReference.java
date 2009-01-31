package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

/**
 *  Represents a raw velocity template reference such as those passed to
 * {@link org.apache.velocity.app.event.ReferenceInsertionEventHandler}s
 */
final class RawVelocityReference
{
    private static final Pattern REFERENCE_SUGAR = Pattern.compile("[\\!\\$\\{\\}]");

    private final String referenceString;

    public RawVelocityReference(String referenceString)
    {
        Validate.notNull(referenceString, "referenceString must not be null");
        this.referenceString = referenceString;
    }

    /**
     * @return true if this reference represents a scalar context value.
     */
    public boolean isScalar()
    {
        return !referenceString.contains(".");
    }

    /**
     * @return the scalar component of the raw reference. If the reference has no non-scalar component, this object
     * is returned.
     */
    public RawVelocityReference getScalarComponent()
    {
        if (isScalar())
            return this;

        return new RawVelocityReference(referenceString.substring(0, referenceString.indexOf(".")));
    }

    /**
     * Returns the scalar component name of this reference
     *
     * <dl>
     * <dt>$foo</dt>
     * <dd>foo</dd>
     * <dt>${bar}<dt>
     * <dd>bar</dt>
     * <dt>$myVar.someMethod()</dt>
     * <dd>myVar</dd>
     * </dl>
     *
     * @return The scalar component name
     *
     */
    public String getBaseReferenceName()
    {
        return REFERENCE_SUGAR.matcher(getScalarComponent().referenceString).replaceAll("");
    }
}
