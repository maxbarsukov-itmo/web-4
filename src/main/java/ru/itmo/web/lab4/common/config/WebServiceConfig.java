package ru.itmo.web.lab4.common.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
  public static final String NAMESPACE_URI = "http://itmo.ru/web/lab4";

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean(name = "attempts")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema attemptsSchema) {
    var wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("AttemptsPort");
    wsdl11Definition.setLocationUri("/ws");
    wsdl11Definition.setTargetNamespace(NAMESPACE_URI);
    wsdl11Definition.setSchema(attemptsSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema attemptsSchema() {
    return new SimpleXsdSchema(new ClassPathResource("web-services/attempts.xsd"));
  }
}
