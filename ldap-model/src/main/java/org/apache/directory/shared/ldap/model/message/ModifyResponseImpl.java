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
package org.apache.directory.shared.ldap.model.message;


/**
 * ModifyResponse implementation
 * 
 */
public class ModifyResponseImpl extends AbstractResultResponse implements ModifyResponse
{
    static final long serialVersionUID = 4132526905748233730L;


    /**
     * Creates a ModifyResponse as a reply to an ModifyRequest.
     */
    public ModifyResponseImpl()
    {
        super( -1, TYPE );
    }


    /**
     * Creates a ModifyResponse as a reply to an ModifyRequest.
     * 
     * @param id the sequence id for this response
     */
    public ModifyResponseImpl( final int id )
    {
        super( id, TYPE );
    }


    /**
     * Get a String representation of a ModifyResponse
     * 
     * @return A ModifyResponse String
     */
    public String toString()
    {

        StringBuilder sb = new StringBuilder();

        sb.append( "    Modify Response\n" );
        sb.append( super.toString() );

        return super.toString( sb.toString() );
    }
}
