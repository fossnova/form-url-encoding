/*
 * Copyright (c) 2012-2020, FOSS Nova Software foundation (FNSF),
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

import static java.lang.Math.min;

import org.fossnova.fue.stream.FueException;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
final class FueWriter implements org.fossnova.fue.stream.FueWriter {

    private static final char AMPERSAND = '&';
    private static final char EQUALS = '=';

    private final FueGrammarAnalyzer analyzer;
    private final Writer out;
    private final char[] buffer = new char[ 1024 ];
    private final String encoding;
    private int limit;
    private boolean closed;

    FueWriter( final Writer out, final String encoding ) {
        this.out = out;
        this.encoding = encoding;
        analyzer = new FueGrammarAnalyzer();
    }

    @Override
    public void close() throws IOException, FueException {
        if ( closed ) return; // idempotent
        closed = true;
        try {
            flush();
            analyzer.finished = true;
        } finally {
            out.close();
        }
    }

    @Override
    public void flush() throws IOException {
        if ( limit > 0 ) {
            out.write( buffer, 0, limit );
            limit = 0;
            out.flush();
        }
    }

    @Override
    public FueWriter writeKey( final String data ) throws IOException, FueException {
        if ( ( data == null ) || ( data.length() == 0 ) ) {
            throw new IllegalArgumentException( "Parameter cannot be null or empty string" );
        }
        writeOptionalAmpersand();
        analyzer.putKey();
        encode( data );
        return this;
    }

    @Override
    public FueWriter writeValue( final String data ) throws IOException, FueException {
        writeOptionalEquals();
        analyzer.putValue();
        if ( data != null ) {
            encode( data );
        }
        return this;
    }

    private void writeOptionalAmpersand() throws IOException, FueException {
        if ( analyzer.isAmpersandExpected() ) {
            analyzer.putAmpersand();
            write( AMPERSAND );
        }
    }

    private void writeOptionalEquals() throws IOException, FueException {
        if ( analyzer.isEqualsExpected() ) {
            analyzer.putEquals();
            write( EQUALS );
        }
    }

    private void write( final char c ) throws IOException {
        if ( limit == buffer.length ) {
            out.write( buffer, 0, limit );
            limit = 0;
        }
        buffer[ limit++ ] = c;
    }

    private void encode( final String s ) throws IOException {
        final String encodedString = URLEncoder.encode( s, encoding );
        int count;
        int dataBegin = 0;
        int dataEnd = encodedString.length();
        while ( dataBegin < dataEnd ) {
            count = min( dataEnd - dataBegin, buffer.length - limit );
            encodedString.getChars( dataBegin, dataBegin + count, buffer, limit );
            dataBegin += count;
            limit += count;
            if ( limit == buffer.length )  {
                out.write( buffer, 0, buffer.length );
                limit = 0;
            }
        }
    }

}
