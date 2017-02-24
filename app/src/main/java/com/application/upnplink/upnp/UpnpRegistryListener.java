package com.application.upnplink.upnp;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class UpnpRegistryListener extends DefaultRegistryListener {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Device> ITEMS = new ArrayList<Device>();
    public static Device SelectedDevice = null;

    private static void addItem(Device item) {
        ITEMS.add(item);
    }

    public static void clear() {
        ITEMS.clear();
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }


    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
            /*
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(
                            BrowserActivity.this,
                            "Discovery failed of '" + device.getDisplayString() + "': "
                                    + (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });*/
        deviceRemoved(device);
    }
            /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void beforeShutdown(Registry registry) {
        System.out.println(
                "Before shutdown, the registry has devices: "
                        + registry.getDevices().size()
        );
    }

    @Override
    public void afterShutdown() {
        System.out.println("afterShutdown Shutdown of registry complete!");

    }
    public void deviceAdded(final Device device) {
//            runOnUiThread(new Runnable() {
//                public void run() {
//        DeviceDisplay d = new DeviceDisplay(device);
        int position = ITEMS.indexOf(device);
        if (position >= 0) {
            // Device already in the list, re-set new value at same position
            ITEMS.remove(device);
            ITEMS.add(position, device);
        } else {
            ITEMS.add(device);
        }
        //               }
        //           });

    }

    public void deviceRemoved(final Device device) {
            /*
            runOnUiThread(new Runnable() {
                public void run() {
                    listAdapter.remove(new DeviceContent(device));
                }
            });
            */
        ITEMS.remove(device);
    }



}
