package com.application.upnplink.upnp;


import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;


/**
 * Created by Jerome on 24/02/2017.
 */

public class SetActionInvocation extends ActionInvocation {

    SetActionInvocation(Service service, String actionName, int instanceNum, String argumentName, String argumentValue) {
        //super(service.getAction("SetAVTransportURI"));
        super(service.getAction(actionName));
        try {

            // Throws InvalidValueException if the value is of wrong type
            UnsignedIntegerFourBytes instanceID = new UnsignedIntegerFourBytes(instanceNum);
            setInput("InstanceID", instanceID);
            setInput(argumentName, argumentValue);

            //setInput(inputActionArgumentValue);
            //setInput("InstanceID", instanceID);
            //setInput("CurrentURI", "http://192.168.43.150/video/Kaamelott_S01E01.avi");

        } catch (InvalidValueException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
