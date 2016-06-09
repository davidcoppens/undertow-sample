package nl.concipit;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * Example of using Undertow webserver
 * 
 * @author dcoppens
 *
 */
public class UndertowSample {
    @Parameter(names = { "--start-port", "-s" })
    int startPort;
    @Parameter(names = { "--end-port", "-e" })
    int endPort;

    /**
     * Start Undertow server with listeners for the configured ports
     * 
     * @param args
     *            --port-start: start port --port-end: end port
     */
    public static void main(String[] args) {
        UndertowSample undertowSample = new UndertowSample();
        new JCommander(undertowSample, args);
        undertowSample.startServers();
    }

    /**
     * Start Undertow server with listeners for the configured ports
     */
    public void startServers() {
        Builder builder = Undertow.builder();
        for (int i = startPort; i <= endPort; i++) {
            MyHandler h = new MyHandler(String.valueOf(i));
            builder.addHttpListener(i, "localhost").setHandler(h);
            
        }
        Undertow server = builder.build();
        server.start();

    }

    /**
     * Simple {@link HttpHandler} echoing the specified reply on any request
     */
    class MyHandler implements HttpHandler {
        private String reply;

        /**
         * Constructor
         * 
         * @param reply
         *            Reply to be issued by this handler
         */
        public MyHandler(String reply) {
            this.reply = reply;
        }

        /**
         * Handle request by writing the specified reply
         */
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(reply);
        }
    }

}
