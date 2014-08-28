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

public class QueryErrorCode {
    
    public static final String SUCCESS_true = "true";
    
    public static final String[] ERRNO_000 = {"001", "System error"};
    
    public static final String[] ERRNO_010 = {"010", "Missing query parameter"};
    public static final String[] ERRNO_020 = {"020", "Your query is empty"};
    public static final String[] ERRNO_030 = {"030", "Json syntax exception"};
    public static final String[] ERRNO_040 = {"040", "Json items is empty"};
    public static final String[] ERRNO_050 = {"050", "Not Json content-type"};
    
    public static final String[] ERRNO_100 = {"100", "Base64 decoding exception"};
    
    public static final String[] ERRNO_200 = {"200", "Reserved"};
    
    public static final String[] ERRNO_900 = {"900", "Undefined error occured"};
    
}
