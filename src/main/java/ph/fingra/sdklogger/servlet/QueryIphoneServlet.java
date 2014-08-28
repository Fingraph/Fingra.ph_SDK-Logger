/**
 * Copyright 2014 tgrape Inc.
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

package ph.fingra.sdklogger.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ph.fingra.sdklogger.util.JsonLoggerRunnable;
import ph.fingra.sdklogger.util.LoggerHelperIphone;
import ph.fingra.sdklogger.util.QueryErrorCode;

public class QueryIphoneServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(QueryIphoneServlet.class);
    
    private JsonLoggerRunnable jsonLoggerRunnable;
    private Thread thread;
    
    public QueryIphoneServlet() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        jsonLoggerRunnable = new JsonLoggerRunnable(LoggerHelperIphone.class);
        thread = new Thread(jsonLoggerRunnable);
        thread.start();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");
        
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
        
        String req_content_type = "";
        String ret_code = "";
        @SuppressWarnings("unused")
        String message = "";
        int status_code = HttpServletResponse.SC_OK;
        
        /*
         * HTTP REQUEST Content-Type(Header) Check - "application/json"
         */
        req_content_type = request.getContentType();
        if (req_content_type != null) req_content_type = req_content_type.toUpperCase();
        else req_content_type = "undefined";
        
        if (req_content_type.contains("JSON")){
            
            Gson gson = new Gson();
            JsonElement jElement = null;
            
            // debug - Request Body 
            //logger.info(request.getReader().readLine());
            //request.getReader().reset();
            
            try {
                jElement = gson.fromJson(request.getReader(), JsonElement.class);
                
                jsonLoggerRunnable.putElement(jElement);
                
                ret_code = QueryErrorCode.SUCCESS_true;
                status_code = HttpServletResponse.SC_OK;
            } catch (JsonSyntaxException jse) {
                ret_code = QueryErrorCode.ERRNO_030[0];
                message = QueryErrorCode.ERRNO_030[1];
                status_code = HttpServletResponse.SC_BAD_REQUEST;
                
                logger.error("JsonSyntaxException");
                jse.printStackTrace();
            } catch (JsonIOException jie) {
                ret_code = QueryErrorCode.ERRNO_030[0];
                message = QueryErrorCode.ERRNO_030[1];
                status_code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                
                logger.error("JsonIOException");
                jie.printStackTrace();
            }
            
        } else {
            
            ret_code = QueryErrorCode.ERRNO_050[0];
            message = QueryErrorCode.ERRNO_050[1];
            status_code = HttpServletResponse.SC_BAD_REQUEST;
            
            logger.error("Invalid Content-Type - " + req_content_type);
            
        }
        
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(status_code);
        response.getWriter().write(ret_code);
    }
    
}
