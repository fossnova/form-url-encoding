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

import static com.fossnova.fue.stream.FueConstants.AMPERSAND;
import static com.fossnova.fue.stream.FueConstants.EQUALS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;

import org.fossnova.fue.stream.FueWriter;

/**
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
final class FueWriterImpl implements FueWriter {

    private FueGrammarAnalyzer analyzer = new FueGrammarAnalyzer();

    private final String encoding;
    
    private Writer out;

    private boolean closed;

    FueWriterImpl( final Writer out, final String encoding ) {
        this.out = out;
        this.encoding = encoding;
    }

    private void ensureOpen() {
        if ( closed ) {
            throw new UnsupportedOperationException( "Form URL Encoding writer have been closed" );
        }
    }

    public void close() {
        analyzer = null;
        out = null;
        closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public FueWriter flush() throws IOException {
        ensureOpen();
        out.flush();
        return this;
    }

    public FueWriter writeKey( final String data ) throws IOException {
        if ( data == null || data.length() == 0 ) {
            throw new IllegalArgumentException( "Parameter cannot be null or empty string" );
        }
        ensureOpen();
        writeOptionalAmpersand();
        analyzer.push( FueGrammarToken.KEY );
        out.write( encode( data ) );
        return this;
    }

    public FueWriter writeValue( final String data ) throws IOException {
        ensureOpen();
        writeOptionalEquals();
        analyzer.push( FueGrammarToken.VALUE );
        if ( data != null ) {
            out.write( encode( data ) );
        }
        return this;
    }

    private void writeOptionalAmpersand() throws IOException {
        if ( analyzer.isAmpersandExpected() ) {
            analyzer.push( FueGrammarToken.AMPERSAND );
            out.write( AMPERSAND );
        }
    }

    private void writeOptionalEquals() throws IOException {
        if ( analyzer.isEqualsExpected() ) {
            analyzer.push( FueGrammarToken.EQUALS );
            out.write( EQUALS );
        }
    }

    private String encode( final String s ) throws UnsupportedEncodingException {
        return URLEncoder.encode( s, encoding );
    }
}
