package de.info3.wegfinder24.newtwork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class OpenrouteService extends Service {
    public OpenrouteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}