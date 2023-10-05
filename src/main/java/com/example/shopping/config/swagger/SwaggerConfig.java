package com.example.shopping.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                // ApiSelectorBuilder 생성
                .select()
                // api 문서화를 할 패키지 경로
                .apis(RequestHandlerSelectors.basePackage("com.example.shopping.controller"))
                // 어떤 API URL에 대해서 문서화할지
                // PathSelectors.any() : 모든 API에 대한 URL을 문서화
                // PathSelectors.ant("/member/**") : 특정 URL을 지정해서 문서화
                .paths(PathSelectors.any())
                .build()
                // API 문서에 대한 정보 추가
                .apiInfo(apiInfo())
                // swagger에서 제공하는 기본 응답 코드 설명 제거
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API 문서")
                .description("구현한 사이트의 기능을 보여주는 곳입니다.")
                .version("1.0")
                .build();
    }
}
