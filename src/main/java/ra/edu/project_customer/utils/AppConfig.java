package ra.edu.project_customer.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "ra.edu.project_customer.controller",
        "ra.edu.project_customer.service",
        "ra.edu.project_customer.repository",
})
public class AppConfig {
    private String HOST_NAME = "dqodyytc6";
    private String API_KEY = "579886866183763";
    private String API_SECRET = "UjDW8SYuISy9uFl8cS2L4ztLd0s";
//    @Bean
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver mResolver = new CommonsMultipartResolver();
//        // kích thước tối đa 1 file 10MB
//        mResolver.setMaxUploadSizePerFile(1024 * 1024 * 10);
//        // kích thước tối đa 1 lần upload tất cả file là 30MB
//        mResolver.setMaxUploadSize(1024 * 1024 * 30);
//        return mResolver;
//    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", HOST_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true
        ));
    }
}