package com.application.upnplink.upnp.renderer;

import org.fourthline.cling.support.avtransport.impl.state.AbstractState;
import org.fourthline.cling.support.avtransport.impl.state.Stopped;
import org.fourthline.cling.support.model.AVTransport;
import org.fourthline.cling.support.model.SeekMode;

import java.net.URI;

public class RendererMachineStopped extends Stopped {

    public RendererMachineStopped(AVTransport transport) {
        super(transport);
    }

    public void onEntry() {
        super.onEntry();
        // Optional: Stop playing, release resources, etc.
    }

    public void onExit() {
        // Optional: Cleanup etc.
    }

    @Override
    public Class<? extends AbstractState> setTransportURI(URI uri, String metaData) {
        // This operation can be triggered in any state, you should think
        // about how you'd want your player to react. If we are in Stopped
        // state nothing much will happen, except that you have to set
        // the media and position info, just like in MyRendererNoMediaPresent.
        // However, if this would be the MyRendererPlaying state, would you
        // prefer stopping first?
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState> stop() {
        /// Same here, if you are stopped already and someone calls STOP, well...
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState> play(String speed) {
        // It's easier to let this classes' onEntry() method do the work
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState> next() {
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState> previous() {
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState> seek(SeekMode unit, String target) {
        // Implement seeking with the stream in stopped state!
        return RendererMachineStopped.class;
    }
}