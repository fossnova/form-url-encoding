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
import java.io.UnsupportedEncodingException;

import org.fossnova.finder.FactoryFinder;

/**
 * Defines an abstract implementation of a factory for getting <code>Form URL Encoding</code> readers and
 * writers. All readers and writers returned by the factory are not thread safe.
 * 
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public abstract class FueFactory {

    /**
     * All implementations must provide public default constructor overriding this one.
     */
    protected FueFactory() {
    }

    /**
     * Returns <code>Form URL Encoding</code> factory instance.
     * 
     * @return factory instance
     */
    public static FueFactory newInstance() {
        final FueFactory fueFactory = FactoryFinder.find( FueFactory.class );
        if ( fueFactory == null ) {
            throw new IllegalStateException( "Factory not configured: " + FueFactory.class.getName() );
        }
        return fueFactory;
    }

    /**
     * Creates new <code>Form URL Encoding</code> reader with default character set.
     * 
     * @param stream input
     * @return reader instance
     * @throws UnsupportedEncodingException if default character set is not supported
     */
    public abstract FueReader newFueReader( InputStream stream ) throws UnsupportedEncodingException;

    /**
     * Creates new <code>Form URL Encoding</code> writer with default character set.
     * 
     * @param stream output
     * @return writer instance
     * @throws UnsupportedEncodingException if default character set is not supported
     */
    public abstract FueWriter newFueWriter( OutputStream stream ) throws UnsupportedEncodingException;

    /**
     * Creates new <code>Form URL Encoding</code> reader  with specified character set.
     * 
     * @param stream input
     * @param charsetName character set name
     * @return reader instance
     * @throws UnsupportedEncodingException if specified character set is not supported
     */
    public abstract FueReader newFueReader( InputStream stream, String charsetName ) throws UnsupportedEncodingException;

    /**
     * Creates new <code>Form URL Encoding</code> writer with specified character set.
     * 
     * @param stream output
     * @param charsetName character set name
     * @return writer instance
     * @throws UnsupportedEncodingException if specified character set is not supported
     */
    public abstract FueWriter newFueWriter( OutputStream stream, String charsetName ) throws UnsupportedEncodingException;
}
