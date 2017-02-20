package com.application.upnplink.upnp.renderer;


import org.fourthline.cling.support.avtransport.impl.state.AbstractState;
import org.fourthline.cling.support.avtransport.impl.state.Playing;
import org.fourthline.cling.support.model.AVTransport;
import org.fourthline.cling.support.model.SeekMode;

import java.net.URI;

public class RendererMachinePlaying extends Playing {

    public RendererMachinePlaying(AVTransport transport) {
        super(transport);
    }

    @Override
    public void onEntry() {
        super.onEntry();
        // Start playing now!
    }

    @Override
    public Class<? extends AbstractState> setTransportURI(URI uri, String metaData) {
        // Your choice of action here, and what the next state is going to be!
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState> stop() {
        // Stop playing!
        return RendererMachineStopped.class;
    }

    @Override
    public Class<? extends AbstractState<?>> play(String s) {
        return null;
    }

    @Override
    public Class<? extends AbstractState<?>> pause() {
        return null;
    }

    @Override
    public Class<? extends AbstractState<?>> next() {
        return null;
    }

    @Override
    public Class<? extends AbstractState<?>> previous() {
        return null;
    }

    @Override
    public Class<? extends AbstractState<?>> seek(SeekMode seekMode, String s) {
        return null;
    }
}