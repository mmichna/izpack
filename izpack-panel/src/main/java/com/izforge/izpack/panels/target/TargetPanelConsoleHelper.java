/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2002 Jan Blok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.panels.target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import com.izforge.izpack.api.data.AutomatedInstallData;
import com.izforge.izpack.api.data.ResourceManager;
import com.izforge.izpack.installer.console.PanelConsole;
import com.izforge.izpack.installer.console.PanelConsoleHelper;
import com.izforge.izpack.panels.path.PathInputPanel;

/**
 * The Target panel console helper class.
 *
 * @author Mounir El Hajj
 */
public class TargetPanelConsoleHelper extends PanelConsoleHelper implements PanelConsole
{
    public boolean runGeneratePropertiesFile(AutomatedInstallData installData, PrintWriter printWriter)
    {
        printWriter.println(AutomatedInstallData.INSTALL_PATH + "=");
        return true;
    }

    public boolean runConsoleFromProperties(AutomatedInstallData installData, Properties p)
    {
        String strTargetPath = p.getProperty(AutomatedInstallData.INSTALL_PATH);
        if (strTargetPath == null || "".equals(strTargetPath.trim()))
        {
            System.err.println("Missing mandatory target path!");
            return false;
        }
        else
        {
            try
            {
                strTargetPath = getVariableSubstitutor(installData).substitute(strTargetPath);
            }
            catch (Exception e)
            {
                // ignore
            }
            installData.setInstallPath(strTargetPath);
            return true;
        }
    }

    public boolean runConsole(AutomatedInstallData idata)
    {

        ResourceManager resourceManager = ResourceManager.getInstance();
        String strDefaultPath = PathInputPanel.loadDefaultInstallDir(
                resourceManager, getVariableSubstitutor(idata), idata);

        String strTargetPath = "";

        System.out.println("Select target path [" + strDefaultPath + "] ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            String strIn = reader.readLine();
            if (!strIn.trim().equals(""))
            {
                strTargetPath = strIn;
            }
            else
            {
                strTargetPath = strDefaultPath;
            }
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }

        try
        {
            strTargetPath = getVariableSubstitutor(idata).substitute(strTargetPath);
        }
        catch (Exception e)
        {
            // ignore
        }

        idata.setInstallPath(strTargetPath);
        int i = askEndOfConsolePanel();
        if (i == 1)
        {
            return true;
        }
        else if (i == 2)
        {
            return false;
        }
        else
        {
            return runConsole(idata);
        }

    }
}
