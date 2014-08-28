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

package ph.fingra.sdklogger.util;

import org.apache.log4j.Logger;

public class LoggerHelper {
    
    private static LoggerHelper instance = new LoggerHelper();
    
    public static final Logger logger = Logger.getLogger(LoggerHelper.class);
    
    private LoggerHelper(){
        System.out.println("LoggerHelper initial");
    }
    
    public static LoggerHelper getInstance(){
        return instance;
    }
    
    public static void log(String message){
        
        logger.info(message);
        
    }
}
