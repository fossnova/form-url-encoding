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

import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;

import org.fossnova.fue.stream.FueEvent;
import org.fossnova.fue.stream.FueException;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
final class FueReader implements org.fossnova.fue.stream.FueReader {

    private static final String RESERVED_CHARS = "!#$'(),/:;?@[]";
    private static final char AMPERSAND = '&';
    private static final char EQUALS = '=';

    private final String encoding;
    private final Reader in;
    private final FueGrammarAnalyzer analyzer;
    private char[] buffer = new char[ 1024 ];
    private int position;
    private int limit;
    private String s;
    private boolean closed;

    FueReader( final Reader in, final String encoding ) {
        this.in = in;
        analyzer = new FueGrammarAnalyzer();
        this.encoding = encoding;
    }

    @Override
    public void close() throws IOException, FueException {
        if ( closed ) return; // idempotent
        closed = true;
        analyzer.currentEvent = null;
        in.close();
    }

    @Override
    public String getKey() {
        if ( analyzer.currentEvent != FueEvent.KEY ) {
            throw new IllegalStateException();
        }
        return s;
    }

    @Override
    public String getValue() {
        if ( analyzer.currentEvent != FueEvent.VALUE ) {
            throw new IllegalStateException();
        }
        return s;
    }

    @Override
    public boolean isKey() {
        return analyzer.currentEvent == FueEvent.KEY;
    }

    @Override
    public boolean isValue() {
        return analyzer.currentEvent == FueEvent.VALUE;
    }

    @Override
    public boolean hasNext() throws IOException {
        return hasMoreData();
    }

    @Override
    public FueEvent next() throws IOException, FueException {
        ensureOpen();
        s = null;
        int currentChar;
        while ( true ) {
            currentChar = position < limit ? buffer[ position++ ] : read();
            if ( currentChar == EQUALS ) {
                analyzer.putEquals();
            } else if ( currentChar == AMPERSAND ) {
                analyzer.putAmpersand();
                if ( analyzer.currentEvent != null ) {
                    return analyzer.currentEvent;
                }
            } else {
                if ( currentChar != -1 ) {
                    readString();
                } else if ( analyzer.isEmpty() ) {
                    throw new IllegalStateException("No more FORM URL Encoding tokens available");
                }
                if ( analyzer.isEmpty() ) {
                    analyzer.putKey();
                } else {
                    analyzer.putValue();
                }
                return analyzer.currentEvent;
            }
        }
    }

    private void readString() throws IOException, FueException {
        int stringOffset = position - 1;
        int stringLength;
        char currentChar = buffer[ stringOffset ];
        assertNotReservedCharacter( currentChar );
        do {
            while ( position < limit ) {
                currentChar = buffer[ position++ ];
                assertNotReservedCharacter( currentChar );
                if ( !isStringEnd( currentChar ) ) continue;
                position--;
                break;
            }
            stringLength = position - stringOffset;
            if ( position < limit ) break;
            if ( stringOffset != 0 ) {
                System.arraycopy( buffer, stringOffset, buffer, 0, stringLength );
                position = stringLength;
                limit = stringLength;
                stringOffset = 0;
            } else if ( limit == buffer.length ) doubleBuffer();
        } while ( hasMoreData() );
        s = URLDecoder.decode( new String( buffer, stringOffset, stringLength ), encoding );
    }

    private boolean isStringEnd( final int c ) {
        return ( c == EQUALS ) || ( c == AMPERSAND ) || ( c == -1 );
    }

    private boolean hasMoreData() throws IOException {
        if ( position == limit ) {
            if ( limit == buffer.length ) {
                limit = 0;
                position = 0;
            }
            fillBuffer();
        }
        if ( position == limit ) {
            analyzer.currentEvent = null;
            analyzer.finished = true;
        }
        return position != limit;
    }

    private void fillBuffer() throws IOException {
        int read;
        do {
            read = in.read( buffer, limit, buffer.length - limit );
            if ( read == -1 ) return;
            limit += read;
        } while ( limit != buffer.length );
    }

    private int read() throws IOException {
        return hasMoreData() ? buffer[ position++ ] : -1;
    }

    private void ensureOpen() {
        if ( closed ) {
            throw new IllegalStateException( "Form URL Encoded reader have been closed" );
        }
    }

    private void assertNotReservedCharacter( final char c ) throws FueException {
        for ( int i = 0; i < RESERVED_CHARS.length(); i++ )
            if ( RESERVED_CHARS.charAt( i ) == c )
                throw newFueException( "Reserved character cannot appear in Form URL Encoded string: " + c );
    }

    private void doubleBuffer() {
        final char[] oldData = buffer;
        buffer = new char[ oldData.length * 2 ];
        System.arraycopy( oldData, 0, buffer, 0, oldData.length );
    }

    private FueException newFueException( final String message ) {
        return analyzer.newFueException( message );
    }

}
