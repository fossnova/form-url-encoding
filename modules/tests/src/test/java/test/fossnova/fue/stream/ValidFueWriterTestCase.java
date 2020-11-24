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
package test.fossnova.fue.stream;

import java.io.ByteArrayOutputStream;

import org.fossnova.fue.stream.FueStreamFactory;
import org.fossnova.fue.stream.FueWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public final class ValidFueWriterTestCase {

    private ByteArrayOutputStream baos;

    private FueWriter writer;

    @Before
    public void init() throws Exception {
        baos = new ByteArrayOutputStream();
        writer = FueStreamFactory.getInstance().newFueWriter( baos );
    }

    @After
    public void destroy() {
        baos = null;
        writer = null;
    }

    @Test
    public void simpleData() throws Exception {
        writer.writeKey( "a" );
        writer.writeValue( "b" );
        writer.flush();
        writer.close();
        Assert.assertEquals( "a=b", getWriterOutput() );
    }

    @Test
    public void moreComplexObject() throws Exception {
        writer.writeKey( "key 0" );
        writer.writeValue( "value 0" );
        writer.writeKey( "key 1" );
        writer.writeValue( "value 1" );
        writer.writeKey( "key 2" );
        writer.writeValue( "value 2" );
        writer.flush();
        writer.close();
        final String expected = "key+0=value+0&key+1=value+1&key+2=value+2";
        Assert.assertEquals( expected, getWriterOutput() );
    }

    @Test
    public void emptyValues() throws Exception {
        writer.writeKey( "key 0" );
        writer.writeValue( null );
        writer.writeKey( "key 1" );
        writer.writeValue( "" );
        writer.writeKey( "key 2" );
        writer.writeValue( null );
        writer.flush();
        writer.close();
        final String expected = "key+0=&key+1=&key+2=";
        Assert.assertEquals( expected, getWriterOutput() );
    }

    @Test
    public void onlyKeys() throws Exception {
        writer.writeKey( "key 0" );
        writer.writeKey( "key 1" );
        writer.writeKey( "key 2" );
        writer.flush();
        writer.close();
        final String expected = "key+0&key+1&key+2";
        Assert.assertEquals( expected, getWriterOutput() );
    }

    @Test
    public void controlsEncoding() throws Exception {
        writer.writeKey( " encoded " );
        writer.writeValue( "!#$&'()*+,/:;=?@[]" );
        writer.flush();
        writer.close();
        final String expected = "+encoded+=%21%23%24%26%27%28%29*%2B%2C%2F%3A%3B%3D%3F%40%5B%5D";
        Assert.assertEquals( expected, getWriterOutput() );
    }

    private String getWriterOutput() throws Exception {
        return baos.toString( "UTF-8" );
    }
}
