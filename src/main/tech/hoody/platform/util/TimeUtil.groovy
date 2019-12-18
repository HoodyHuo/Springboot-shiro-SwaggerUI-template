package tech.hoody.platform.util

import java.text.SimpleDateFormat

class TimeUtil {
    static Date parseJsTimeStrToDate(String str){
        if(str==null || str==""){
            return null
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(str)
    }
}
