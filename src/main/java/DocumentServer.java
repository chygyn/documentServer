import com.sun.net.httpserver.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;

public class DocumentServer {


    public static void main(String[] args) {

        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8001), 0);
            System.out.println("server started at 8001");
            //get all entries from db
            server.createContext("/getDocument", he -> {

                String aInputFileName = "D://works/testPDF.pdf";
                File file = new File(aInputFileName);
                /*String line;
                String resp = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                while ((line = bufferedReader.readLine()) != null) {
                  //  System.out.println(line);
                    resp += line;
                }
                bufferedReader.close();*/
                ByteArrayOutputStream ous = null;
                InputStream ios = null;
                try {
                    byte[] buffer = new byte[4096];
                    ous = new ByteArrayOutputStream();
                    ios = new FileInputStream(file);
                    int read = 0;
                    while ((read = ios.read(buffer)) != -1) {
                        ous.write(buffer, 0, read);
                    }
                }finally {
                    try {
                        if (ous != null)
                            ous.close();
                    } catch (IOException e) {
                    }

                    try {
                        if (ios != null)
                            ios.close();
                    } catch (IOException e) {
                    }
                }
                byte[] result = ous.toByteArray();
               // byte[] result = new byte[(int)file.length()];
                //byte[] result = new byte[(int)file.length()];
                String encoding = "UTF-8";
                he.getResponseHeaders().set("Accept-Ranges", "bytes");
                he.getResponseHeaders().add("Content-Type", "application/pdf; charset="+encoding);
                OutputStream os = he.getResponseBody();
                he.sendResponseHeaders(200, result.length);
                os.write(result);
                os.close();
            });
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
