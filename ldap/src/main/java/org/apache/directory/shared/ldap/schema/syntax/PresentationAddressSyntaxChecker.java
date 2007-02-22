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
package org.apache.directory.shared.ldap.schema.syntax;


/**
 * A SyntaxChecker which verifies that a value is a PresentationAddressSyntax.
 * 
 * This syntax is defined in RFC 1278, but it has not been implemented.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev: 488616 $
 */
public class PresentationAddressSyntaxChecker extends BinarySyntaxChecker
{
    /** the Apache assigned internal OID for this syntax checker */
    private static final String SC_OID = "1.3.6.1.4.1.1466.115.121.1.43";


    /**
     * Private default constructor to prevent unnecessary instantiation.
     */
    public PresentationAddressSyntaxChecker()
    {
        super( SC_OID );
    }

    /**
     * 
     * Creates a new instance of PresentationAddressSyntaxChecker.
     * 
     * @param oid the oid to associate with this new SyntaxChecker
     *
     */
    protected PresentationAddressSyntaxChecker( String oid )
    {
        super( oid );
    }
    
    /**
     * @see org.apache.directory.shared.ldap.schema.syntax.SyntaxChecker#isValidSyntax(Object)
     */
    public boolean isValidSyntax( Object value )
    {
        return true;
    }
}
