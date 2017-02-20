package com.application.upnplink.upnp.renderer;
import org.fourthline.cling.support.avtransport.impl.AVTransportStateMachine;
import org.seamless.statemachine.States;
@States({
        RendererMachineNoMediaPresent.class,
        RendererMachineStopped.class,
        RendererMachinePlaying.class
})
public interface RendererMachineStateMachine extends AVTransportStateMachine {

}

