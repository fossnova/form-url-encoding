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

import org.fossnova.fue.stream.FueEvent;
import org.fossnova.fue.stream.FueException;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
final class FueGrammarAnalyzer {

    private static final byte EMPTY = 0;
    private static final byte KEY = 1;
    private static final byte EQUALS = 2;
    private byte stack;
    private boolean canWriteAmpersand;
    private boolean canWriteEquals;
    FueEvent currentEvent;
    boolean finished;

    FueGrammarAnalyzer() {
    }

    boolean isAmpersandExpected() {
        return canWriteAmpersand;
    }

    boolean isEqualsExpected() {
        return canWriteEquals;
    }

    void putKey() throws FueException {
        // preconditions
        if ( stack != EMPTY ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        canWriteAmpersand = true;
        canWriteEquals = true;
        currentEvent = FueEvent.KEY;
        stack = KEY;
    }

    void putEquals() throws FueException {
        // preconditions
        if ( !canWriteEquals ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        canWriteAmpersand = true;
        canWriteEquals = false;
        currentEvent = null;
        stack = EQUALS;
    }

    void putValue() throws FueException {
        // preconditions
        if ( stack != EQUALS ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        canWriteAmpersand = true;
        canWriteEquals = false;
        currentEvent = FueEvent.VALUE;
        stack = EMPTY;
    }

    void putAmpersand() throws FueException {
        // preconditions
        if ( !canWriteAmpersand ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        canWriteAmpersand = false;
        canWriteEquals = false;
        currentEvent = stack != EMPTY ? FueEvent.VALUE : null;
        stack = EMPTY;
    }

    private String getExpectingTokensMessage() {
        if ( stack == EMPTY ) {
            return canWriteAmpersand ? "Expecting '&'" : "Expecting Form URL Encoding KEY";
        } else if ( stack == KEY ) {
            return "Expecting '=' or '&'";
        } else if ( stack == EQUALS ) {
            return "Expecting '&' or Form URL Encoding VALUE";
        }
        throw new IllegalStateException();
    }

    boolean isEmpty() {
        return stack == EMPTY;
    }

    FueException newFueException( final String s ) {
        currentEvent = null;
        return new FueException( s );
    }

}
