/*
 * Copyright  2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.izforge.izpack.util.file.types.selectors;

import com.izforge.izpack.api.data.AutomatedInstallData;
import com.izforge.izpack.util.file.types.Parameter;
import com.izforge.izpack.util.regex.Regexp;
import com.izforge.izpack.util.regex.RegularExpression;

import java.io.*;

/**
 * Selector that filters files based on a regular expression.
 */
public class ContainsRegexpSelector extends BaseExtendSelector
{

    private String userProvidedExpression = null;
    private RegularExpression myRegExp = null;
    private Regexp myExpression = null;
    /**
     * Key to used for parameterized custom selector
     */
    public static final String EXPRESSION_KEY = "expression";

    /**
     * Creates a new <code>ContainsRegexpSelector</code> instance.
     */
    public ContainsRegexpSelector()
    {
    }

    /**
     * @return a string describing this object
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(
                "{containsregexpselector expression: ");
        buf.append(userProvidedExpression);
        buf.append("}");
        return buf.toString();
    }

    /**
     * The regular expression used to search the file.
     *
     * @param theexpression this must match a line in the file to be selected.
     */
    public void setExpression(String theexpression)
    {
        this.userProvidedExpression = theexpression;
    }

    /**
     * When using this as a custom selector, this method will be called.
     * It translates each parameter into the appropriate setXXX() call.
     *
     * @param parameters the complete set of parameters for this selector
     */
    public void setParameters(Parameter[] parameters)
    {
        super.setParameters(parameters);
        if (parameters != null)
        {
            for (Parameter parameter : parameters)
            {
                String paramname = parameter.getName();
                if (EXPRESSION_KEY.equalsIgnoreCase(paramname))
                {
                    setExpression(parameter.getValue());
                }
                else
                {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * Checks that an expression was specified.
     */
    public void verifySettings()
    {
        if (userProvidedExpression == null)
        {
            setError("The expression attribute is required");
        }
    }

    /**
     * Tests a regular expression against each line of text in the file.
     *
     * @param basedir  the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file     is a java.io.File object the selector can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(AutomatedInstallData idata, File basedir, String filename, File file)
            throws Exception
    {
        String teststr = null;
        BufferedReader in = null;

        // throw BuildException on error

        validate();

        if (file.isDirectory())
        {
            return true;
        }

        if (myRegExp == null)
        {
            myRegExp = new RegularExpression();
            myRegExp.setPattern(userProvidedExpression);
            myExpression = myRegExp.getRegexp();
        }

        try
        {
            in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));

            teststr = in.readLine();

            while (teststr != null)
            {

                if (myExpression.matches(teststr))
                {
                    return true;
                }
                teststr = in.readLine();
            }

            return false;
        }
        catch (IOException ioe)
        {
            throw new Exception("Could not read file " + filename);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Exception e)
                {
                    throw new Exception("Could not close file "
                            + filename);
                }
            }
        }
    }
}

