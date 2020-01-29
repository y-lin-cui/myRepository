package cn.test.back;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ConsumerFallbackProvider implements ZuulFallbackProvider {

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            
            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application","json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
            
            @Override
            public InputStream getBody() throws IOException {
            	System.out.println("测试");
                // 响应体
                return new ByteArrayInputStream((ConsumerFallbackProvider.this.getRoute()+"微服务不可用，请稍后再试" ).getBytes());
            }
            
            @Override
            public String getStatusText() throws IOException {
                 return HttpStatus.BAD_REQUEST.getReasonPhrase();
            }
            
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.BAD_REQUEST;
            }
            
            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.BAD_REQUEST.value();
            }
            
            @Override
            public void close() {
                // TODO Auto-generated method stub
                
            }
        };
    }

    @Override
    public String getRoute() {
        // TODO Auto-generated method stub
        return "hello-service";
    }

}