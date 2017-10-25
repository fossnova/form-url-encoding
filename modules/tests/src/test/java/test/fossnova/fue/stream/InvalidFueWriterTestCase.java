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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.fossnova.fue.stream.FueException;
import org.fossnova.fue.stream.FueWriter;
import org.junit.Test;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public final class InvalidFueWriterTestCase extends AbstractFueTestCase {

    @Test
    public void valueFirst() throws Exception {
        final FueWriter writer = getFueWriter();
        try {
            writer.writeValue( "data" );
            fail();
        } catch ( final FueException e ) {
            assertEquals( "Expecting Form URL Encoding KEY", e.getMessage() );
        }
    }

    @Test
    public void nullKey() throws Exception {
        final FueWriter writer = getFueWriter();
        try {
            writer.writeKey( null );
            fail();
        } catch ( final IllegalArgumentException e ) {
            assertEquals( "Parameter cannot be null or empty string", e.getMessage() );
        }
    }

    @Test
    public void emptyKey() throws Exception {
        final FueWriter writer = getFueWriter();
        try {
            writer.writeKey( "" );
            fail();
        } catch ( final IllegalArgumentException e ) {
            assertEquals( "Parameter cannot be null or empty string", e.getMessage() );
        }
    }

    @Test
    public void keyValueValue() throws Exception {
        final FueWriter writer = getFueWriter();
        try {
            writer.writeKey( "key 1" );
            writer.writeValue( "value 1" );
            writer.writeValue( "value 2" );
            fail();
        } catch ( final FueException e ) {
            assertEquals( "Expecting '&'", e.getMessage() );
        }
    }

}
