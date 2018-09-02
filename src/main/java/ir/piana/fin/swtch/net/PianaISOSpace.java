package ir.piana.fin.swtch.net;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PianaISOSpace {
    public static final String DEFAULT_SPACE = "request_message";

    private Map<String, List<ISOMsg>> isoMap = new LinkedHashMap<>();
    private Map<String, List<ISORequestListener>> observerMap = new LinkedHashMap<>();
    private static final PianaISOSpace isoSpace = new PianaISOSpace();

    private  PianaISOSpace() {

    }

    public static PianaISOSpace getInstance() {
        return isoSpace;
    }

    public PianaISOSpace addObserver(String space, ISORequestListener observer) {
        List<ISORequestListener> observers = isoSpace.observerMap.get(space);
        if(observers == null) {
            observers = new ArrayList<>();
            isoSpace.observerMap.put(space, observers);
        }
        observers.add(observer);
        return this;
    }

    public void put(String space, ISOMsg isoMsg) {
        List<ISORequestListener> observers = isoSpace.observerMap.get(space);
        if(observers == null)
            return;
        for(ISORequestListener observer : observers) {
            observer.process(isoMsg.getSource(), isoMsg);
        }
    }
}
