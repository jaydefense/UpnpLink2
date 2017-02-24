package com.application.upnplink.upnp;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;

/**
 * Created by jperraudeau on 17/02/2017.
 */

public class UpnpUtils {
    /**
     * findServiceRenderer -> AVTransport
     * @param device
     * @return
     */
    public static Service findServiceRenderer(Device device) {
        if (device == null) {
            return null;
        }

        Service serviceRenderer;
        UDAServiceType udaServiceTypeMediaRenderer = new UDAServiceType("AVTransport");
        System.out.println("device: " + device.getDisplayString() + " " + device.getIdentity().getUdn());
        if ((serviceRenderer = device.findService(udaServiceTypeMediaRenderer)) != null) {
            System.out.println("Service AVTransport discovered: " );
            return serviceRenderer;
        }
        return null;
    }

    /**
     * Play
     * @param upnpService
     * @param service
     * @param url
     */
    public static void play(final AndroidUpnpService upnpService,  final Service service, String url) {
        if (upnpService == null || service==null || url == null ) {
            return;
        }

        // Executes SetAVTransportURI
        SetActionInvocation setTargetInvocation = new SetActionInvocation(service,"SetAVTransportURI",0,"CurrentURI", url);
        upnpService.getControlPoint().execute(
                new ActionCallback(setTargetInvocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        System.out.println("setTargetInvocation Successfully called action!");

                        // Executes Play
                        SetActionInvocation setPlayInvocation = new SetActionInvocation(service,"Play",0,"Speed", "1");
                        upnpService.getControlPoint().execute(
                                new ActionCallback(setPlayInvocation) {

                                    @Override
                                    public void success(ActionInvocation invocation) {
                                        assert invocation.getOutput().length == 0;
                                        System.out.println("setPlayInvocation Successfully called action!");
                                    }

                                    @Override
                                    public void failure(ActionInvocation invocation,
                                                        UpnpResponse operation,
                                                        String defaultMsg) {
                                        System.err.println(defaultMsg);
                                    }
                                }
                        );
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation,
                                        String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                }
        );
    }
}
