package xyz.thinktest.fastestapi.http.metadata;

import java.util.ArrayList;

public class MetaList extends ArrayList<Meta>{

    MetaList(){}

    public static MetaList newEmptyInstance(){
        return new MetaList();
    }
}
