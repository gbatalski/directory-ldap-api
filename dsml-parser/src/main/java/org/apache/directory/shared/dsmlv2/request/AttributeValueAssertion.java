/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.shared.dsmlv2.request;


import org.apache.directory.shared.ldap.codec.api.LdapConstants;
import org.apache.directory.shared.ldap.model.entry.*;
import org.apache.directory.shared.util.Strings;


/**
 * A class to store an attribute value assertion. 
 * The grammar is :
 * 
 * AttributeValueAssertion ::= SEQUENCE {
 *           attributeDesc   AttributeDescription,
 *           assertionValue  AssertionValue }
 *
 * AttributeDescription ::= LDAPString
 * 
 * AssertionValue ::= OCTET STRING
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AttributeValueAssertion
{
    // ~ Instance fields
    // ----------------------------------------------------------------------------

    /** The attribute description */
    private String attributeDesc;

    /** The assertion value */
    private Value<?> assertionValue;

    /**
     *
     * Helper method to render an object which can be a String or a byte[]
     *
     * @return A string representing the object
     */
    public static String dumpObject( Object object )
    {
        if ( object != null )
        {
            if ( object instanceof String )
            {
                return (String) object;
            }
            else if ( object instanceof byte[] )
            {
                return Strings.dumpBytes((byte[]) object);
            }
            else if ( object instanceof StringValue)
            {
                return ( (StringValue) object ).get();
            }
            else if ( object instanceof BinaryValue )
            {
                return Strings.dumpBytes(((BinaryValue) object).get());
            }
            else
            {
                return "<unknown type>";
            }
        }
        else
        {
            return "";
        }
    }


    // ~ Methods
    // ------------------------------------------------------------------------------------

    /**
     * Get the assertion value
     * 
     * @return Returns the assertionValue.
     */
    public Value<?> getAssertionValue()
    {
        return assertionValue;
    }


    /**
     * Set the assertion value
     * 
     * @param assertionValue The assertionValue to set.
     */
    public void setAssertionValue( Value<?> assertionValue )
    {
        this.assertionValue = assertionValue;
    }


    /**
     * Get the attribute description
     * 
     * @return Returns the attributeDesc.
     */
    public String getAttributeDesc()
    {
        return attributeDesc;
    }


    /**
     * Set the attribute description
     * 
     * @param attributeDesc The attributeDesc to set.
     */
    public void setAttributeDesc( String attributeDesc )
    {
        this.attributeDesc = attributeDesc;
    }


    /**
     * Get a String representation of an AttributeValueAssertion
     * 
     * @param tabs The spacing to be put before the string
     * @return An AttributeValueAssertion String
     */
    public String toString( String tabs )
    {
        StringBuffer sb = new StringBuffer();

        sb.append( tabs ).append( "AttributeValueAssertion\n" );
        sb.append( tabs ).append( "    Assertion description : '" );
        sb.append( attributeDesc != null ? attributeDesc : "null" );
        sb.append( "'\n" );
        sb.append( tabs ).append( "    Assertion value : '" ).append( dumpObject(assertionValue) ).append( "'\n" );

        return sb.toString();
    }


    /**
     * Get a String representation of an AttributeValueAssertion, as of RFC
     * 2254.
     * 
     * @param filterType The filter type
     * @return An AttributeValueAssertion String
     */
    public String toStringRFC2254( int filterType )
    {
        StringBuffer sb = new StringBuffer();

        sb.append( attributeDesc );

        switch ( filterType )
        {
            case LdapConstants.EQUALITY_MATCH_FILTER:
                sb.append( '=' );
                break;

            case LdapConstants.LESS_OR_EQUAL_FILTER:
                sb.append( "<=" );
                break;

            case LdapConstants.GREATER_OR_EQUAL_FILTER:
                sb.append( ">=" );
                break;

            case LdapConstants.APPROX_MATCH_FILTER:
                sb.append( "~=" );
                break;
        }

        sb.append( dumpObject(assertionValue) );

        return sb.toString();
    }


    /**
     * Get a String representation of an AttributeValueAssertion
     * 
     * @return An AttributeValueAssertion String
     */
    public String toString()
    {
        return toString( "" );
    }
}
