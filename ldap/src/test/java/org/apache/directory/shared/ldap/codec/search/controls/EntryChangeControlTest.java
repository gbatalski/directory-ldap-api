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
package org.apache.directory.shared.ldap.codec.search.controls;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;

import org.apache.directory.junit.tools.Concurrent;
import org.apache.directory.junit.tools.ConcurrentJunitRunner;
import org.apache.directory.shared.asn1.DecoderException;
import org.apache.directory.shared.asn1.ber.Asn1Decoder;
import org.apache.directory.shared.ldap.codec.search.controls.entryChange.EntryChangeDecoder;
import org.apache.directory.shared.ldap.codec.search.controls.entryChange.EntryChangeDecorator;
import org.apache.directory.shared.ldap.codec.search.controls.entryChange.EntryChangeControlContainer;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.apache.directory.shared.util.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test the EntryChangeControlTest codec
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrent()
public class EntryChangeControlTest
{
    /**
     * Test the decoding of a EntryChangeControl
     */
    @Test
    public void testDecodeEntryChangeControlSuccess()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x0D );
        bb.put( new byte[]
            { 
            0x30, 0x0B,                     // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x08,             //     changeType ENUMERATED {
                                            //         modDN (8)
                                            //     }
              0x04, 0x03, 'a', '=', 'b',    //     previousDN LDAPDN OPTIONAL, -- modifyDN ops. only
              0x02, 0x01, 0x10              //     changeNumber INTEGER OPTIONAL } -- if supported
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }

        EntryChangeDecorator entryChange = container.getEntryChangeControl();
        assertEquals( ChangeType.MODDN, entryChange.getChangeType() );
        assertEquals( "a=b", entryChange.getPreviousDn().toString() );
        assertEquals( 16, entryChange.getChangeNumber() );
    }


    /**
     * Test the decoding of a EntryChangeControl
     */
    @Test
    public void testDecodeEntryChangeControlSuccessLongChangeNumber()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x13 );
        bb.put( new byte[]
            { 
            0x30, 0x11,                     // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x08,             //     changeType ENUMERATED {
                                            //         modDN (8)
                                            //     }
              0x04, 0x03, 'a', '=', 'b',    //     previousDN LDAPDN OPTIONAL, -- modifyDN ops. only
              0x02, 0x07,                   //     changeNumber INTEGER OPTIONAL } -- if supported
                0x12, 0x34, 0x56, 0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }

        EntryChangeDecorator entryChange = container.getEntryChangeControl();
        assertEquals( ChangeType.MODDN, entryChange.getChangeType() );
        assertEquals( "a=b", entryChange.getPreviousDn().toString() );
        assertEquals( 5124095576030430L, entryChange.getChangeNumber() );
    }


    /**
     * Test the decoding of a EntryChangeControl with a add and a change number
     */
    @Test
    public void testDecodeEntryChangeControlWithADDAndChangeNumber()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x08 );
        bb.put( new byte[]
            { 
            0x30, 0x06,             // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x01,     //     changeType ENUMERATED {
                                    //         Add (1)
                                    //     }
              0x02, 0x01, 0x10      //     changeNumber INTEGER OPTIONAL -- if supported
                                    // }
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }

        EntryChangeDecorator entryChange = container.getEntryChangeControl();
        assertEquals( ChangeType.ADD, entryChange.getChangeType() );
        assertNull( entryChange.getPreviousDn() );
        assertEquals( 16, entryChange.getChangeNumber() );
    }


    /**
     * Test the decoding of a EntryChangeControl with a add so we should not
     * have a PreviousDN
     */
    @Test
    public void testDecodeEntryChangeControlWithADDAndPreviousDNBad()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x0D );
        bb.put( new byte[]
            { 
            0x30, 0x0B,                     // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x01,             //     changeType ENUMERATED {
                                            //         ADD (1)
                                            //     }
              0x04, 0x03, 'a', '=', 'b',    //     previousDN LDAPDN OPTIONAL, --
                                            //     modifyDN ops. only
              0x02, 0x01, 0x10              //     changeNumber INTEGER OPTIONAL -- if supported
                                            // }
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            // We should fail, because we have a previousDN with a ADD
            assertTrue( true );
            return;
        }

        fail( "A ADD operation should not have a PreviousDN" );
    }


    /**
     * Test the decoding of a EntryChangeControl with a add and nothing else
     */
    @Test
    public void testDecodeEntryChangeControlWithADD()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x05 );
        bb.put( new byte[]
            { 
            0x30, 0x03,                 // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x01,         //     changeType ENUMERATED {
                                        //         ADD (1)
                                        //     }
                                        // }
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }

        EntryChangeDecorator entryChange = container.getEntryChangeControl();
        assertEquals( ChangeType.ADD, entryChange.getChangeType() );
        assertNull( entryChange.getPreviousDn() );
        assertEquals( EntryChangeDecorator.UNDEFINED_CHANGE_NUMBER, entryChange.getChangeNumber() );
    }


    /**
     * Test the decoding of a EntryChangeControl with a wrong changeType and
     * nothing else
     */
    @Test
    public void testDecodeEntryChangeControlWithWrongChangeType()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x05 );
        bb.put( new byte[]
            { 
            0x30, 0x03,                 // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x03,         //     changeType ENUMERATED {
                                        //         BAD Change Type
                                        //     }
                                        // }
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            // We should fail because the ChangeType is not known
            assertTrue( true );
            return;
        }

        fail( "The changeType is unknown" );
    }


    /**
     * Test the decoding of a EntryChangeControl with a wrong changeNumber
     */
    @Test
    public void testDecodeEntryChangeControlWithWrongChangeNumber()
    {
        Asn1Decoder decoder = new EntryChangeDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x1C );
        bb.put( new byte[]
            { 
            0x30, 0x1A,                     // EntryChangeNotification ::= SEQUENCE {
              0x0A, 0x01, 0x08,             //     changeType ENUMERATED {
                                            //         modDN (8)
                                            //     }
              0x04, 0x03, 'a', '=', 'b',    //     previousDN LDAPDN OPTIONAL, -- modifyDN ops. only
              0x02, 0x10,                   //     changeNumber INTEGER OPTIONAL -- if supported
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
            } );
        bb.flip();

        EntryChangeControlContainer container = new EntryChangeControlContainer();
        container.setEntryChangeControl( new EntryChangeDecorator() );
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            // We should fail because the ChangeType is not known
            assertTrue( true );
            return;
        }

        fail( "The changeNumber is incorrect" );
    }


    /**
     * Test encoding of a EntryChangeControl.
     */
    @Test
    public void testEncodeEntryChangeControl() throws Exception
    {
        ByteBuffer bb = ByteBuffer.allocate( 0x2A );
        bb.put( new byte[]
            { 
            0x30, 0x28,                            // Control
              0x04, 0x17,                          // OID (SyncRequestValue)
                '2', '.', '1', '6', '.', '8', '4', '0', 
                '.', '1', '.', '1', '1', '3', '7', '3', 
                '0', '.', '3', '.', '4', '.', '7',
              0x04, 0x0D,
                0x30, 0x0B,                        // EntryChangeNotification ::= SEQUENCE {
                  0x0A, 0x01, 0x08,                //     changeType ENUMERATED {
                                                   //         modDN (8)
                                                   //     }
                  0x04, 0x03, 'a', '=', 'b',       //     previousDN LDAPDN OPTIONAL, -- modifyDN ops. only
                  0x02, 0x01, 0x10,                //     changeNumber INTEGER OPTIONAL -- if supported
            } );

        String expected = Strings.dumpBytes(bb.array());
        bb.flip();

        EntryChangeDecorator entry = new EntryChangeDecorator();
        entry.setChangeType( ChangeType.MODDN );
        entry.setChangeNumber( 16 );
        entry.setPreviousDn( new Dn( "a=b" ) );
        bb = entry.encode( ByteBuffer.allocate( entry.computeLength() ) );
        String decoded = Strings.dumpBytes(bb.array());
        assertEquals( expected, decoded );
    }


    /**
     * Test encoding of a EntryChangeControl with a long changeNumber.
     */
    @Test
    public void testEncodeEntryChangeControlLong() throws Exception
    {
        ByteBuffer bb = ByteBuffer.allocate( 0x30 );
        bb.put( new byte[]
            { 
            0x30, 0x2E,                            // Control
              0x04, 0x17,                          // OID (SyncRequestValue)
                '2', '.', '1', '6', '.', '8', '4', '0', 
                '.', '1', '.', '1', '1', '3', '7', '3', 
                '0', '.', '3', '.', '4', '.', '7',
              0x04, 0x13,
                0x30, 0x11,                        // EntryChangeNotification ::= SEQUENCE {
                  0x0A, 0x01, 0x08,                //     changeType ENUMERATED {
                                                   //         modDN (8)
                                                   //     }
                  0x04, 0x03, 'a', '=', 'b',       //     previousDN LDAPDN OPTIONAL, -- modifyDN ops. only
                  0x02, 0x07,                      //     changeNumber INTEGER OPTIONAL -- if supported
                    0x12, 0x34, 0x56, 0x78, (byte)0x9a, (byte)0xbc, (byte)0xde
            } );

        String expected = Strings.dumpBytes(bb.array());
        bb.flip();

        EntryChangeDecorator entry = new EntryChangeDecorator();
        entry.setChangeType( ChangeType.MODDN );
        entry.setChangeNumber( 5124095576030430L );
        entry.setPreviousDn( new Dn( "a=b" ) );
        bb = entry.encode( ByteBuffer.allocate( entry.computeLength() ) );
        String decoded = Strings.dumpBytes(bb.array());
        assertEquals( expected, decoded );
    }
}
