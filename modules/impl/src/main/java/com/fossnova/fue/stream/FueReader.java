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
import java.io.PushbackReader;
import java.io.Reader;
import java.net.URLDecoder;

import org.fossnova.fue.stream.FueEvent;
import org.fossnova.fue.stream.FueException;

/**
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
final class FueReader implements org.fossnova.fue.stream.FueReader {

    private static final String reservedChars = "!#$'(),/:;?@[]";

    private final String encoding;

    private FueGrammarAnalyzer analyzer = new FueGrammarAnalyzer();

    private PushbackReader in;

    private String s;

    FueReader( final Reader in, final String encoding ) {
        this.in = new PushbackReader( in );
        this.encoding = encoding;
    }

    @Override
    public void close() {
        analyzer = null;
        in = null;
        s = null;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public String getKey() {
        if ( !isCurrentEvent( FueEvent.KEY ) ) {
            throw new IllegalStateException( "Current event isn't KEY" );
        }
        return s;
    }

    @Override
    public String getValue() {
        if ( !isCurrentEvent( FueEvent.VALUE ) ) {
            throw new IllegalStateException( "Current event isn't VALUE" );
        }
        return s;
    }

    @Override
    public boolean hasNext() throws IOException {
        final int nextChar = in.read();
        if ( nextChar != -1 ) {
            in.unread( nextChar );
            return true;
        } else {
            analyzer.setFinished();
        }
        return false;
    }

    @Override
    public FueEvent next() throws IOException {
        if ( !hasNext() ) {
            throw new FueException( "No more data available" );
        }
        s = null;
        analyzer.ensureCanContinue();
        int nextCharacter = -1;
        boolean exitLoop = false;
        while ( !exitLoop ) {
            switch ( nextCharacter = in.read() ) {
                case FueConstants.EQUALS: {
                    analyzer.push( FueGrammarToken.EQUALS );
                }
                    break;
                case FueConstants.AMPERSAND: {
                    if ( !analyzer.isEmpty() ) {
                        exitLoop = true;
                    }
                    analyzer.push( FueGrammarToken.AMPERSAND );
                }
                    break;
                default: {
                    if ( analyzer.isEmpty() ) {
                        analyzer.push( FueGrammarToken.KEY );
                    } else {
                        analyzer.push( FueGrammarToken.VALUE );
                    }
                    if ( nextCharacter != -1 ) {
                        in.unread( nextCharacter );
                        readString();
                    }
                    exitLoop = true;
                }
            }
        }
        return analyzer.getCurrentEvent();
    }

    @Override
    public boolean isKey() {
        return isCurrentEvent( FueEvent.KEY );
    }

    @Override
    public boolean isValue() {
        return isCurrentEvent( FueEvent.VALUE );
    }

    private void readString() throws IOException {
        final StringBuilder retVal = new StringBuilder();
        int currentChar = -1;
        while ( true ) {
            currentChar = in.read();
            if ( isStringEnd( currentChar ) ) {
                if ( currentChar != -1 ) {
                    in.unread( currentChar );
                }
                break;
            }
            if ( isReservedCharacter( currentChar ) ) {
                throw new FueException( "Reserver character cannot appear in Form URL Encoded string: " + ( char ) currentChar );
            }
            retVal.appendCodePoint( currentChar );
        }
        if ( retVal.length() != 0 ) {
            s = URLDecoder.decode( retVal.toString(), encoding );
        }
    }

    private boolean isStringEnd( final int c ) {
        return ( c == FueConstants.EQUALS ) || ( c == FueConstants.AMPERSAND ) || ( c == -1 );
    }

    private boolean isCurrentEvent( final FueEvent event ) {
        return analyzer.getCurrentEvent() == event;
    }

    private boolean isReservedCharacter( final int c ) {
        for ( int i = 0; i < reservedChars.length(); i++ ) {
            if ( reservedChars.codePointAt( i ) == c ) return true;
        }
        return false;
    }
}
