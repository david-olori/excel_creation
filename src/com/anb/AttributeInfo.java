package com.anb;



import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created with IntelliJ IDEA.
 * User: davidolori
 * Date: 1/12/15
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeInfo {


        private String _name;

        private String _displayName;

        private boolean _requiredAttribute;

        private boolean _requiredValue;

        public AttributeInfo(String name, String displayName, boolean requiredAttribute, boolean requiredValue)
        {
            _name = name;
            _displayName = displayName;
            _requiredAttribute = requiredAttribute;
            _requiredValue = requiredValue;
        }

        public AttributeInfo(String name, String displayName)
        {
            this(name, displayName, true, true);
        }

        public AttributeInfo(String name, boolean requiredAttribute, boolean requiredValue)
        {
            this(name, name, requiredAttribute, requiredValue);
        }

        public AttributeInfo(String name)
        {
            this(name, name);
        }

        public String getName()
        {
            return _name;
        }

        public String getDisplayName()
        {
            return _displayName;
        }

        public boolean isRequiredAttribute()
        {
            return _requiredAttribute;
        }

        public boolean isRequiredValue()
        {
            return _requiredValue;
        }

        public boolean equals(Object obj)
        {
            if (obj == this)
                return true;

            if (obj == null || !(obj instanceof AttributeInfo))
                return false;
            AttributeInfo other = (AttributeInfo)obj;

            return new EqualsBuilder()
                    .append(this.getName(), other.getName())
                    .append(this.getDisplayName(), other.getDisplayName())
                    .append(this.isRequiredAttribute(), other.isRequiredAttribute())
                    .append(this.isRequiredValue(), other.isRequiredValue()).isEquals();
        }

        public int hashCode()
        {
            return new HashCodeBuilder()
                    .append(this.getName())
                    .append(this.getDisplayName())
                    .append(this.isRequiredAttribute())
                    .append(this.isRequiredValue()).toHashCode();
        }

        public String toString()
        {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("name", this.getName())
                    .append("displayName", this.getDisplayName())
                    .append("requiredAttribute", this.isRequiredAttribute())
                    .append("requiredValue", this.isRequiredValue()).toString();
        }


    }

