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
package org.fossnova.fue.stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.fossnova.finder.FactoryFinder;

/**
 * Defines an abstract implementation of a factory for getting
 * {@code application/x-www-form-urlencoded} streaming readers and writers.
 * All created readers and writers are not thread safe.
 *
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public abstract class FueStreamFactory {

    private static final FueStreamFactory factory = FactoryFinder.find( FueStreamFactory.class );

    /**
     * All implementations must provide public default constructor overriding this one.
     */
    protected FueStreamFactory() {
    }

    /**
     * Returns {@code application/x-www-form-urlencoded} streams factory instance.
     * 
     * @return factory instance
     */
    public static FueStreamFactory getInstance() {
        return factory;
    }

    /**
     * Creates new {@code application/x-www-form-urlencoded} streaming reader with default character set.
     * 
     * @param in input stream
     * @return reader instance
     */
    public abstract FueReader newFueReader( InputStream in );

    /**
     * Creates new {@code application/x-www-form-urlencoded} streaming writer with default character set.
     * 
     * @param out output stream
     * @return writer instance
     */
    public abstract FueWriter newFueWriter( OutputStream out );

    /**
     * Creates new {@code application/x-www-form-urlencoded} streaming reader with specified character set.
     *
     * @param in input stream
     * @param charset character set name
     * @return reader instance
     */
    public abstract FueReader newFueReader( InputStream in, Charset charset );

    /**
     * Creates new {@code application/x-www-form-urlencoded} streaming writer with specified character set.
     * 
     * @param out output stream
     * @param charset character set name
     * @return writer instance
     */
    public abstract FueWriter newFueWriter( OutputStream out, Charset charset );

    /**
     * Creates new {@code application/x-www-form-urlencoded} streaming reader with specified character set.
     * It is user responsibility that <code>in</code> reader's encoding is the same as specified <code>charset</code> encoding.
     *
     * @param in reader
     * @param charset character set name
     * @return reader instance
     */
    public abstract FueReader newFueReader( Reader in, Charset charset );

    /**
     * Creates new {@code application/x-www-form-urlencoded} streaming writer with specified character set.
     * It is user responsibility that <code>out</code> writer's encoding is the same as specified <code>charset</code> encoding.
     *
     * @param out writer
     * @param charset character set name
     * @return writer instance
     */
    public abstract FueWriter newFueWriter( Writer out, Charset charset );

}
