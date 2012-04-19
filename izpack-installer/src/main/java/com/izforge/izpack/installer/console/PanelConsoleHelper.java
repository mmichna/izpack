/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2003 Jonathan Halliday
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

package com.izforge.izpack.installer.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.izforge.izpack.api.data.AutomatedInstallData;
import com.izforge.izpack.api.handler.AbstractUIHandler;
import com.izforge.izpack.api.substitutor.VariableSubstitutor;
import com.izforge.izpack.core.substitutor.VariableSubstitutorImpl;
import com.izforge.izpack.installer.automation.PanelAutomationHelper;
import com.izforge.izpack.util.Housekeeper;

/**
 * Abstract class implementing basic functions needed by all panel console helpers.
 *
 * @see PanelAutomationHelper
 * @author Mounir El Hajj
 */
abstract public class PanelConsoleHelper implements AbstractUIHandler
{

    private VariableSubstitutor variableSubstitutor;

    public int askEndOfConsolePanel()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true)
            {
                System.out.println("press 1 to continue, 2 to quit, 3 to redisplay");
                String strIn = br.readLine();
                if (strIn.equals("1"))
                {
                    return 1;
                }
                else if (strIn.equals("2"))
                {
                    return 2;
                }
                else if (strIn.equals("3"))
                {
                    return 3;
                }
                else if (strIn.equals("3")) { return 3; }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 2;
    }

    protected final VariableSubstitutor getVariableSubstitutor(AutomatedInstallData installData)
    {
        if (variableSubstitutor == null)
            variableSubstitutor = new VariableSubstitutorImpl(installData.getVariables());
        return variableSubstitutor;
    }

    /**
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#emitNotification(java.lang.String)
     */
    public void emitNotification(String message)
    {
        System.out.println(message);
    }

    /**
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#emitWarning(java.lang.String,
     * java.lang.String)
     */
    public boolean emitWarning(String title, String message)
    {
        System.err.println("[ WARNING: " + formatTitle(title) + message + " ]");
        // default: continue
        return true;
    }

    /**
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#emitError(java.lang.String,
     * java.lang.String)
     */
    public void emitError(String title, String message)
    {
        System.err.println("[ ERROR: " + formatTitle(title) + message + " ]");
    }

    private String formatTitle(String title)
    {
        if (title == null || title.isEmpty()) return "";
        return title + ": ";
    }

    /**
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#emitErrorAndBlockNext(java.lang.String,
     * java.lang.String)
     */
    public void emitErrorAndBlockNext(String title, String message)
    {
        emitError(title, message);
        Housekeeper.getInstance().shutDown(10);
    }

    /**
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#askQuestion(java.lang.String,
     * java.lang.String, int)
     */
    public int askQuestion(String title, String question, int choices)
    {
        // don't know what to answer
        return AbstractUIHandler.ANSWER_CANCEL;
    }

    /**
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#askQuestion(java.lang.String,
     * java.lang.String, int, int)
     */
    public int askQuestion(String title, String question, int choices, int default_choice)
    {
        return default_choice;
    }
}
