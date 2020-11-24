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

import java.io.Flushable;
import java.io.IOException;

/**
 * {@code application/x-www-form-urlencoded} stream writer.
 *
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 * @see FueStreamFactory
 * @see FueReader
 */
public interface FueWriter extends Flushable, AutoCloseable {

    /**
     * Writes URL encoded <code>key</code> string.
     * @param data to URL encode
     * @return this writer instance
     * @throws IOException if I/O error occurs
     * @throws FueException if wrong Form URL Encoding is detected
     */
    FueWriter writeKey( String data ) throws IOException, FueException;

    /**
     * Writes URL encoded <code>value</code> string.
     * @param data to URL encode
     * @return this writer instance
     * @throws IOException if I/O error occurs
     * @throws FueException if wrong Form URL Encoding is detected
     */
    FueWriter writeValue( String data ) throws IOException, FueException;

    /**
     * Writes all cached data.
     * @throws IOException if I/O error occurs
     * @throws FueException if wrong Form URL Encoding is detected
     */
    void flush() throws IOException;

    /**
     * Free resources associated with this writer. Closes underlying input stream or writer.
     * @throws IOException if I/O error occurs
     * @throws FueException if wrong Form URL Encoding is detected
     */
    @Override
    void close() throws IOException, FueException;

}
