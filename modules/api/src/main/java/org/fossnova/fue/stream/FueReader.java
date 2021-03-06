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
package org.fossnova.fue.stream;

import java.io.IOException;

/**
 * {@code application/x-www-form-urlencoded} stream reader.
 *
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 * @see FueStreamFactory
 * @see FueWriter
 */
public interface FueReader extends AutoCloseable {

    /**
     * Detects if there is next <code>Form URL Encoding</code> parsing event available.
     * Users should call this method before calling {@link #next()} method.
     * @return true if there are more <code>Form URL Encoding</code> parsing events, false otherwise
     * @throws IOException if some I/O error occurs
     */
    boolean hasNext() throws IOException;

    /**
     * Returns next <code>Form URL Encoding</code> parsing event.
     * Users should call {@link #hasNext()} before calling this method.
     * @return FueEvent next event
     * @throws IOException if some I/O error occurs
     * @throws FueException if wrong <code>Form URL Encoding</code> is detected
     */
    FueEvent next() throws IOException, FueException;

    /**
     * Returns <code>true</code> if current <code>Form URL Encoding</code> parsing event is <code>KEY</code>, false otherwise.
     * Users have to call {@link #next()} before calling this method.
     * @return true if the parsing cursor position points to <code>Form URL Encoding</code> KEY, false otherwise
     */
    boolean isKey();

    /**
     * Returns <code>true</code> if current <code>Form URL Encoding</code> parsing event is <code>VALUE</code>, false otherwise.
     * Users have to call {@link #next()} before calling this method.
     * @return true if the parsing cursor position points to <code>Form URL Encoding</code> VALUE, false otherwise
     */
    boolean isValue();

    /**
     * Converts available key data to URL decoded <code>String</code>.
     * Users have to call {@link #next()} and should call {@link #isKey()} before calling this method.
     * @return key the parsing cursor is pointing to
     * @exception IllegalStateException if cursor isn't pointing to <code>Form URL Encoding</code> key.
     */
    String getKey();

    /**
     * Converts available value data to URL decoded <code>String</code> or returns null if no value is available.
     * Users have to call {@link #next()} and should call {@link #isValue()} before calling this method.
     * @return string the parsing cursor is pointing to or null
     * @exception IllegalStateException if cursor isn't pointing to <code>Form URL Encoding</code> value.
     */
    String getValue();

    /**
     * Free resources associated with this reader. Closes underlying input stream or reader.
     * @throws IOException if some I/O error occurs
     * @throws FueException if wrong <code>Form URL Encoding</code> is detected
     */
    @Override
    void close() throws IOException, FueException;

}
