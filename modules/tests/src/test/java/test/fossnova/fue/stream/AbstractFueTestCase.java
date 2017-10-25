/*
 * Copyright (c) 2012-2017, FOSS Nova Software foundation (FNSF),
 * and individual contributors as indicated by the @author tags.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package test.fossnova.fue.stream;

import static org.fossnova.fue.stream.FueEvent.KEY;
import static org.fossnova.fue.stream.FueEvent.VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.fossnova.fue.stream.FueException;
import org.fossnova.fue.stream.FueStreamFactory;
import org.fossnova.fue.stream.FueReader;
import org.fossnova.fue.stream.FueWriter;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
abstract class AbstractFueTestCase {

    static void assertFinalState( final FueReader reader ) throws Exception {
        assertFalse( reader.hasNext() );
        assertNotKeyException( reader );
        assertNotValueException( reader );
    }

    static void assertKeyState( final FueReader reader, final String expected ) throws Exception {
        assertTrue( reader.hasNext() );
        assertEquals( KEY, reader.next() );
        assertTrue( reader.isKey() );
        assertFalse( reader.isValue() );
        assertEquals( expected, reader.getKey() );
        assertNotValueException( reader );
    }

    static void assertValueState( final FueReader reader, final String expected ) throws Exception {
        assertTrue( reader.hasNext() );
        assertEquals( VALUE, reader.next() );
        assertFalse( reader.isKey() );
        assertTrue( reader.isValue() );
        if ( expected != null ) {
            assertEquals( expected, reader.getValue() );
        } else {
            assertNull( reader.getValue() );
        }
        assertNotKeyException( reader );
    }

    static void assertFueException( final FueReader reader, final String expected ) throws Exception {
        assertTrue( reader.hasNext() );
        try {
            reader.next();
            fail();
        } catch ( final FueException e ) {
            assertEquals( expected, e.getMessage() );
        }
    }

    static FueReader getFueReader( final String data ) throws Exception {
        final ByteArrayInputStream bais = new ByteArrayInputStream( data.getBytes() );
        return FueStreamFactory.getInstance().newFueReader( bais );
    }

    static FueWriter getFueWriter() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return FueStreamFactory.getInstance().newFueWriter( baos );
    }

    private static void assertNotKeyException( final FueReader reader ) throws Exception {
        try {
            reader.getKey();
            fail();
        } catch ( final IllegalStateException e ) {
        }
    }

    private static void assertNotValueException( final FueReader reader ) throws Exception {
        try {
            reader.getValue();
            fail();
        } catch ( final IllegalStateException e ) {
        }
    }

}
