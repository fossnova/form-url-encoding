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

import static com.fossnova.fue.stream.FueGrammarToken.AMPERSAND;
import static com.fossnova.fue.stream.FueGrammarToken.EQUALS;
import static com.fossnova.fue.stream.FueGrammarToken.KEY;
import static com.fossnova.fue.stream.FueGrammarToken.VALUE;

import java.util.LinkedList;

import org.fossnova.fue.stream.FueEvent;
import org.fossnova.fue.stream.FueException;

/**
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
final class FueGrammarAnalyzer {

    private boolean canWriteAmpersand; // TODO: remove this field - see 0 1 2 TODO replacing all these

    private FueEvent currentEvent; // TODO: remove this field - see 0 1 2 TODO replacing all these

    private boolean finished; // TODO: remove this field - see 0 1 2 TODO replacing all these

    // TODO: remove - replace with byte 0 1 2 representing states
    private final LinkedList< FueGrammarToken > stack = new LinkedList< FueGrammarToken >();

    void push( final FueGrammarToken event ) {
        ensureCanContinue();
        if ( event == KEY ) {
            putKey();
        } else if ( event == EQUALS ) {
            putEquals();
        } else if ( event == VALUE ) {
            putValue();
        } else if ( event == AMPERSAND ) {
            putAmpersand();
        } else {
            throw new IllegalStateException();
        }
    }

    FueEvent getCurrentEvent() {
        return currentEvent;
    }

    boolean isAmpersandExpected() {
        return canWriteAmpersand;
    }

    boolean isEqualsExpected() {
        return isLastOnStack( KEY );
    }

    private void putKey() {
        // preconditions
        if ( !stack.isEmpty() ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        stack.add( KEY );
        currentEvent = FueEvent.KEY;
        canWriteAmpersand = true;
    }

    private void putValue() {
        // preconditions
        if ( !isLastOnStack( EQUALS ) ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        stack.clear();
        currentEvent = FueEvent.VALUE;
        canWriteAmpersand = true; // TODO: needed?
    }

    private void putEquals() {
        // preconditions
        if ( !isLastOnStack( KEY ) ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        stack.add( EQUALS );
        currentEvent = null;
    }

    private void putAmpersand() {
        // preconditions
        if ( !canWriteAmpersand ) {
            throw newFueException( getExpectingTokensMessage() );
        }
        // implementation
        currentEvent = !isEmpty() ? FueEvent.VALUE : null;
        canWriteAmpersand = false;
        stack.clear();
    }

    private String getExpectingTokensMessage() {
        if ( isEmpty() ) {
            if ( !canWriteAmpersand ) {
                return "Expecting " + FueConstants.KEY;
            } else {
                return "Expecting " + FueConstants.AMPERSAND;
            }
        }
        if ( isLastOnStack( KEY ) ) {
            return "Expecting " + FueConstants.EQUALS;
        }
        if ( isLastOnStack( EQUALS ) ) {
            return "Expecting " + FueConstants.AMPERSAND + " or EOF";
        }
        throw new IllegalStateException();
    }

    private boolean isLastOnStack( final FueGrammarToken event ) {
        return !isEmpty() && ( stack.getLast() == event );
    }

    void ensureCanContinue() {
        if ( finished ) {
            throw newFueException( getExpectingTokensMessage() );
        }
    }

    void setFinished() {
        finished = true;
        currentEvent = null;
    }

    boolean isFinished() {
        return finished;
    }

    boolean isEmpty() {
        return stack.size() == 0;
    }

    private FueException newFueException( final String s ) {
        setCannotContinue();
        return new FueException( s );
    }

    private void setCannotContinue() {
        finished = true;
    }
}
