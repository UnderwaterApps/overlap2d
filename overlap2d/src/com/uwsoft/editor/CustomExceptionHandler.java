/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.SystemUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.uwsoft.editor.utils.AppConfig;


public class CustomExceptionHandler implements UncaughtExceptionHandler {

    //private UncaughtExceptionHandler defaultUEH;
    private final static String sendURL = "http://overlap2d.com/error_report.php";

    /* 
     * if any of the parameters is null, the respective functionality 
     * will not be used 
     */
    public CustomExceptionHandler() {
        //this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static void showErrorDialog() {
        new Thread(new Runnable() {
            public void run() {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Overlap2D just crashed, see stacktrace in overlog.txt file", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                });
            }
        }).start();
    }

    public static void sendError(String stacktrace) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("error", stacktrace);
        parameters.put("system", SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);
        parameters.put("version", AppConfig.getInstance().version);
        HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
        httpGet.setUrl(sendURL);
        httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
        Gdx.net.sendHttpRequest(httpGet, new HttpResponseListener() {
            public void handleHttpResponse(HttpResponse httpResponse) {
                //showErrorDialog();
            }

            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });


    }

    public void uncaughtException(Thread t, Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        String filename = "overlog.txt";
        writeToFile(stacktrace, filename);
        printWriter.close();

        sendError(stacktrace);

        showErrorDialog();
        //defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            //String localPath = DataManager.getMyDocumentsLocation();
            String localPath = "";//DataManager.getInstance().getRootPath();
            System.out.println(localPath + File.separator + filename);
            BufferedWriter bos = new BufferedWriter(new FileWriter(localPath + File.separator + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
