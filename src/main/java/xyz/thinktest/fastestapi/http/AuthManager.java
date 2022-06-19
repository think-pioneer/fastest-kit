package xyz.thinktest.fastestapi.http;

import xyz.thinktest.fastestapi.http.metadata.Headers;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: aruba
 * @date: 2022-02-15
 */
public enum AuthManager {
    MANAGE;
    ConcurrentHashMap<Requester, Headers> auth;
    AuthManager(){
        auth = new ConcurrentHashMap<>();
    }

    public static void set(Requester requester, Headers headers){
        MANAGE.auth.put(requester, headers);
    }

    public static Headers get(Requester requester){
        return MANAGE.auth.get(requester);
    }

    public static void delete(Requester requester){
        MANAGE.auth.remove(requester);
    }

    public static Requester getRequester(Headers headers){
        Set<Requester> requesters  =  MANAGE.auth.keySet();
        for(Requester requester:requesters){
            if(get(requester).equals(headers)){
                return requester;
            }
        }
        return null;
    }
}
