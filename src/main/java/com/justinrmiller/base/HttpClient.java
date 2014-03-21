package com.justinrmiller.base;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.http.Http;
import com.twitter.util.FutureEventListener;

public class HttpClient {
    public static void main(String[] args) {
        Service<HttpRequest, HttpResponse> client = ClientBuilder
                .safeBuild(ClientBuilder.get().codec(Http.get())
                        .hosts("localhost:10000").hostConnectionLimit(1));
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET, "/");
        client.apply(request).addEventListener(
                new FutureEventListener<HttpResponse>() {
                    public void onSuccess(HttpResponse response) {
                        System.out.println("received response: " + response);
                        System.out.println(response.getContent().toString(
                                CharsetUtil.UTF_8));
                    }

                    public void onFailure(Throwable cause) {
                        System.out.println("failed with cause: " + cause);
                    }
                });
        client.release();
    }
}
