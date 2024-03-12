package xyz.think.fastest.http.metadata;

import java.util.ArrayList;

public class MetaList extends ArrayList<Meta>{

    private static final long serialVersionUID = -4190494594419907797L;

    MetaList(){}

    public void copy(Headers headers){
        this.clear();
        this.addAll(headers);
    }

    public static MetaList newEmpty(){
        return new MetaList();
    }
}
