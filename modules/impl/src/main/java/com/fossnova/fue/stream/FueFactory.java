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
package com.fossnova.fue.stream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public final class FueFactory extends org.fossnova.fue.stream.FueFactory {

    private static final String defaultCharset = Charset.defaultCharset().name();

    private FueReader newFueReader( final Reader reader, final String charsetName ) {
        assertNotNullParameter( reader );
        return new FueReader( reader, charsetName );
    }

    @Override
    public FueReader newFueReader( final InputStream stream ) throws UnsupportedEncodingException {
        return newFueReader( stream, defaultCharset );
    }

    @Override
    public FueReader newFueReader( final InputStream stream, final String charsetName ) throws UnsupportedEncodingException {
        assertNotNullParameter( stream );
        assertNotNullParameter( charsetName );
        return newFueReader( new InputStreamReader( stream, charsetName ), charsetName );
    }

    private FueWriter newFueWriter( final Writer writer, final String charsetName ) {
        assertNotNullParameter( writer );
        return new FueWriter( writer, charsetName );
    }

    @Override
    public FueWriter newFueWriter( final OutputStream stream ) throws UnsupportedEncodingException {
        return newFueWriter( stream, defaultCharset );
    }

    @Override
    public FueWriter newFueWriter( final OutputStream stream, final String charsetName ) throws UnsupportedEncodingException {
        assertNotNullParameter( stream );
        assertNotNullParameter( charsetName );
        return newFueWriter( new OutputStreamWriter( stream, charsetName ), charsetName );
    }

    private static void assertNotNullParameter( final Object o ) {
        if ( o == null ) {
            throw new NullPointerException( "Parameter cannot be null" );
        }
    }
}
