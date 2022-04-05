package rpc;

import java.io.IOException;

interface RPCServer {
    public void startService() throws IOException;

    public boolean isRunning();

    public int getPort();

    public void register(Class serviceInterface, Class impl);

    public void stopService();
}
