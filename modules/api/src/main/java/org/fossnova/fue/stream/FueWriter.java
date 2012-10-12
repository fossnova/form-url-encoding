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

import java.io.Closeable;
import java.io.IOException;

/**
 * <code>Form URL Encoding</code> writer.
 * 
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 * @see FueFactory
 * @see FueReader
 */
public interface FueWriter extends Closeable {

    /**
     * Writes URL encoded <code>key</code> string.
     * 
     * @param data to URL encode
     * @throws IOException if I/O error occurs
     * @return this writer instance
     */
    FueWriter writeKey( String data ) throws IOException;

    /**
     * Writes URL encoded <code>value</code> string.
     * 
     * @param data to URL encode
     * @throws IOException if I/O error occurs
     * @return this writer instance
     */
    FueWriter writeValue( String data ) throws IOException;

    /**
     * Writes all cached data.
     * 
     * @throws IOException if I/O error occurs
     * @return this writer instance
     */
    FueWriter flush() throws IOException;

    /**
     * Free resources associated with this writer. Never closes underlying input stream or writer.
     *
     * @throws IOException if I/O error occurs
     */
    void close();
}
