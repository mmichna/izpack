/*
 * IzPack - Copyright 2001-2011 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/ http://izpack.codehaus.org/
 *
 * Copyright 2011 Tim Anderson
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.izforge.izpack.util;

import com.izforge.izpack.core.os.RegistryHandler;
import com.izforge.izpack.util.os.Shortcut;
import com.izforge.izpack.util.os.Unix_Shortcut;
import com.izforge.izpack.util.os.Win_RegistryHandler;
import com.izforge.izpack.util.os.Win_Shortcut;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * Verifies that the {@link TargetPlatformFactory} creates the correct {@link Shortcut} and {@link RegistryHandler}.
 *
 * @author Tim Anderson
 */
public class InstallerTargetPlatformFactoryTest
{
    /**
     * The factory.
     */
    private TargetPlatformFactory factory = new DefaultTargetPlatformFactory();


    /**
     * Verifies that the correct {@link Shortcut} is created for a platform.
     * <p/>
     * Currently:
     * <ul>
     * <li>{@link Unix_Shortcut} is created for all Unix platforms.</li>
     * <li>{@link Win_Shortcut} is created for all Windows platforms</li>
     * <li>{@link Shortcut} is created for all other platforms</li>
     * </ul>
     *
     * @throws Exception for any error
     */
    @Test
    public void testShortcuts() throws Exception
    {
        for (Platform platform : Platforms.PLATFORMS)
        {
            if (platform.isA(Platform.Name.UNIX))
            {
                checkCreate(Shortcut.class, platform, Unix_Shortcut.class);
            }
            else if (platform.isA(Platform.Name.WINDOWS))
            {
                checkCreate(Shortcut.class, platform, Win_Shortcut.class);
            }
            else
            {
                checkCreate(Shortcut.class, platform, Shortcut.class);
            }
        }
    }

    /**
     * Verifies that the correct {@link RegistryHandler} is created for a platform.
     * <p/>
     * Currently:
     * <ul>
     * <li>{@link Win_RegistryHandler} is created for all Windows platforms</li>
     * <li>{@link RegistryHandler} is created for all other platforms</li>
     * </ul>
     *
     * @throws Exception for any error
     */
    @Test
    public void testRegistryHandler() throws Exception
    {
        for (Platform platform : Platforms.PLATFORMS)
        {
            if (platform.isA(Platform.Name.WINDOWS))
            {
                checkCreate(RegistryHandler.class, platform, Win_RegistryHandler.class);
            }
            else
            {
                checkCreate(RegistryHandler.class, platform, RegistryHandler.class);
            }
        }
    }

    /**
     * Verifies that the correct object is created for a given platform.
     *
     * @param clazz    the interface
     * @param platform the platform
     * @param impl     the expected implementation class
     * @throws Exception for any error
     */
    private <T> void checkCreate(Class<T> clazz, Platform platform, Class<? extends T> impl) throws Exception
    {
        T object = factory.create(clazz, platform);
        assertEquals(impl, object.getClass());
    }
}
