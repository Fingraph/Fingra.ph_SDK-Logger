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

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonLoggerRunnable implements Runnable{
    
    private static final String SDK_LOG_SEPERATOR = "||";
    private static final String LOG_EMPTY_VALUE = "NULL";
    
    private static final String LOG_CMD_KEY = "cmd";
    private static final String[] LOG_COMMON_KEYS = {   
        LOG_CMD_KEY,
        "appkey",
        "session",
        "utctime",
        "localtime",
        "token",
        "country",
        "language",
        "device",
        "osversion",
        "resolution",
        "appversion"
    };
    private static final String[] LOG_EVENT_KEYS = {   
        LOG_CMD_KEY,
        "appkey",
        "eventkey",
        "session",
        "utctime",
        "localtime",
        "token",
        "country",
        "language",
        "device",
        "osversion",
        "resolution",
        "appversion"
    };
    
    @SuppressWarnings("unused")
    private Thread thisThread;
    private BlockingQueue<JsonElement> jsonQueue;
    @SuppressWarnings("rawtypes")
    private Class hostClass;
    
    @SuppressWarnings("rawtypes")
    public JsonLoggerRunnable(Class host){
        jsonQueue = new ArrayBlockingQueue<JsonElement>(100);
        hostClass = host;
    }
    
    @Override
    public void run() {
        thisThread = Thread.currentThread();
        while(true) {
            try {
                parseJSON(jsonQueue.take());
            }
            catch (InterruptedException ignore) {}
            catch (Exception e) {
                e.printStackTrace();
            }
            
            if (jsonQueue.isEmpty()) {
                try {
                    Thread.sleep(new Random().nextInt(500));
                    //System.out.println("wait~");
                }
                catch (InterruptedException ignore) {}
            }
        }
    }
    
    public void putElement(JsonElement jE) {
        try {
            jsonQueue.put(jE);
        }
        catch (InterruptedException ignore) {}
        //thisThread.interrupt();
    }
    
    public void destroy(){
    }
    
    public void parseJSON(JsonElement jElement) throws Exception{
        if (jElement == null) return;
        if (jElement.isJsonArray()) {
            for(JsonElement jE : jElement.getAsJsonArray()) {
                try {
                    parseJSON(jE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            JsonObject jObject = jElement.getAsJsonObject();
            
            COMMAND_TYPE cmd;
            StringBuffer sb = new StringBuffer();
            
            // item - "cmd"
            cmd = COMMAND_TYPE.getTypeByName(jObject.get(LOG_CMD_KEY).getAsString());
            
            switch (cmd) {
                case START:
                case PAGEVIEW:
                case END:
                    getLogFromJsonobject(jObject, LOG_COMMON_KEYS, sb);
                    break;
                case EVENT:
                    getLogFromJsonobject(jObject, LOG_EVENT_KEYS, sb);
                    break;
                default:
                    break;
            }
            
            if (sb.charAt(sb.length()-1) == SDK_LOG_SEPERATOR.charAt(1) && sb.charAt(sb.length()-2) == SDK_LOG_SEPERATOR.charAt(0)) { 
                sb.delete(sb.length()-2, sb.length());
            }
            
            if (hostClass == LoggerHelperAndroid.class) {
                LoggerHelperAndroid.log(sb.toString());
            } else if (hostClass == LoggerHelperIphone.class) {
                LoggerHelperIphone.log(sb.toString());  
            }
            
            sb = null;
        }
        
    }
    
    private String getLogFromJsonobject(JsonObject jObject, String[] keys, StringBuffer sb){
        String jString = null;
        
        for (String key : keys){
            JsonElement jElement = jObject.remove(key);
            
            if (jElement == null){
                //not exist key/value pair -> NULL Text append
                sb.append(LOG_EMPTY_VALUE);
                sb.append(SDK_LOG_SEPERATOR);
                continue;
            }
            
            jString = jElement.getAsString();
            if (jString.isEmpty()) {
                sb.append(LOG_EMPTY_VALUE);
                sb.append(SDK_LOG_SEPERATOR);
                continue;
            } else {
                sb.append(jString);
            }
            
            sb.append(SDK_LOG_SEPERATOR);
        }
        
        return jString;
    };
    
    private enum COMMAND_TYPE {
        START("STARTSESS"),
        PAGEVIEW("PAGEVIEW"),
        EVENT("EVENT"),
        END("ENDSESS");
        
        private String value;
        
        private COMMAND_TYPE(String cmd_type) {
            this.value = cmd_type;
        }
        
        public String getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return this.getValue();
        }
        
        public static COMMAND_TYPE getTypeByName(String cmd_type) {
            for (COMMAND_TYPE t : COMMAND_TYPE.values()) {
                if (t.value.equals(cmd_type)) return t; 
            }
            return null;
        }
    }
    
}
