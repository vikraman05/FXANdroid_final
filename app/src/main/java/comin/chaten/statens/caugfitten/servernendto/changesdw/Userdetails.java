package comin.chaten.statens.caugfitten.servernendto.changesdw;

import org.fejoa.library.Remote;


public class Userdetails {

    final public String name;
    final public String server;
    final public Remote remote;
    public boolean firstSync;

    public Userdetails(String name, String server) {
        this.name = name;
        this.server = server;
        this.remote = new Remote(name, server);
    }
}
