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
package com.fossnova.fue.stream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public final class FueStreamFactory extends org.fossnova.fue.stream.FueStreamFactory {

    private static final Charset DEFAULT_CHARSET = Charset.forName( "UTF-8" );

    public FueStreamFactory() {
    }

    @Override
    public FueReader newFueReader( final Reader reader, final Charset charset ) {
        assertNotNullParameter( reader );
        return new FueReader( reader, charset.name() );
    }

    @Override
    public FueReader newFueReader( final InputStream stream ) {
        return newFueReader( stream, DEFAULT_CHARSET );
    }

    @Override
    public FueReader newFueReader( final InputStream stream, final Charset charset ) {
        assertNotNullParameter( stream );
        assertNotNullParameter( charset );
        return newFueReader( new InputStreamReader( stream, charset ), charset );
    }

    @Override
    public FueWriter newFueWriter( final Writer writer, final Charset charset ) {
        assertNotNullParameter( writer );
        return new FueWriter( writer, charset.name() );
    }

    @Override
    public FueWriter newFueWriter( final OutputStream stream ) {
        return newFueWriter( stream, DEFAULT_CHARSET );
    }

    @Override
    public FueWriter newFueWriter( final OutputStream stream, final Charset charset ) {
        assertNotNullParameter( stream );
        assertNotNullParameter( charset );
        return newFueWriter( new OutputStreamWriter( stream, charset ), charset );
    }

    private static void assertNotNullParameter( final Object o ) {
        if ( o == null ) {
            throw new NullPointerException( "Parameter cannot be null" );
        }
    }

}