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

import org.fossnova.fue.stream.FueReader;
import org.junit.Test;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public final class ValidFueReaderTestCase extends AbstractFueTestCase {

    @Test
    public void noData() throws Exception {
        final FueReader reader = getFueReader( "" );
        assertFinalState( reader );
        reader.close();
    }

    @Test
    public void simpleData() throws Exception {
        final FueReader reader = getFueReader( "a=b" );
        assertKeyState( reader, "a" );
        assertValueState( reader, "b" );
        assertFinalState( reader );
        reader.close();
    }

    @Test
    public void moreComplexObject() throws Exception {
        final FueReader reader = getFueReader( "key+0=value+0&key+1=value+1&key+2=&key+3=" );
        assertKeyState( reader, "key 0" );
        assertValueState( reader, "value 0" );
        assertKeyState( reader, "key 1" );
        assertValueState( reader, "value 1" );
        assertKeyState( reader, "key 2" );
        assertValueState( reader, null );
        assertKeyState( reader, "key 3" );
        assertValueState( reader, null );
        assertFinalState( reader );
        reader.close();
    }

    @Test
    public void controlsEncoding() throws Exception {
        final FueReader reader = getFueReader( "+encoded+=%21%23%24%26%27%28%29*%2B%2C%2F%3A%3B%3D%3F%40%5B%5D" );
        assertKeyState( reader, " encoded " );
        assertValueState( reader, "!#$&'()*+,/:;=?@[]" );
        assertFinalState( reader );
        reader.close();
    }

}
