/*
 * Copyright (c) 2012, FOSS Nova Software foundation (FNSF),
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

import java.io.IOException;

import org.fossnova.fue.stream.FueReader;
import org.junit.Test;

/**
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public final class InvalidFueReaderTestCase extends AbstractFueTestCase {

    @Test
    public void read_equals() throws IOException {
        final FueReader reader = getFueReader( "=" );
        assertFueException( reader, "Expecting Form URL Encoding KEY" );
    }

    @Test
    public void read_ampersand() throws IOException {
        final FueReader reader = getFueReader( "&" );
        assertFueException( reader, "Expecting Form URL Encoding KEY" );
    }

    @Test
    public void read_key_equals_equals() throws IOException {
        final FueReader reader = getFueReader( "a==" );
        assertKeyState( reader, "a" );
        assertFueException( reader, "Expecting & or EOF" );
    }

    @Test
    public void read_key_ampersand_ampersand() throws IOException {
        final FueReader reader = getFueReader( "a&&" );
        assertKeyState( reader, "a" );
        assertValueState( reader, null );
        assertFueException( reader, "Expecting Form URL Encoding KEY" );
    }

    @Test
    public void read_key_containing_reserved_chars() throws IOException {
        final String reservedChars = "!#$'(),/:;?@[]";
        for ( int i = 0; i < reservedChars.length(); i++ ) {
            final FueReader reader = getFueReader( String.valueOf( reservedChars.charAt( i ) ) );
            assertFueException( reader, "Reserver character cannot appear in Form URL Encoded string: " + reservedChars.charAt( i ) );
        }
    }
}
