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
package org.fossnova.fue.stream;

/**
 * <code>Form URL Encoding</code> exception.
 * 
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public final class FueException extends RuntimeException {

    /**
     * Serialization version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public FueException() {
    }

    /**
     * Constructor.
     * 
     * @param msg message
     */
    public FueException( final String msg ) {
        super( msg );
    }

    /**
     * Constructor.
     * 
     * @param msg message
     * @param t reason
     */
    public FueException( final String msg, final Throwable t ) {
        super( msg, t );
    }

    /**
     * Constructor.
     * 
     * @param t reason
     */
    public FueException( final Throwable t ) {
        super( t );
    }
}
