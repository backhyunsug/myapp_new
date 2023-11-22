package com.likelion.myapp.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:/application.properties")
//@PropertySource : application.properties 파일을 읽기 위한 어노테이션
//classpath : /src/main/resoures 
public class WebConfig implements WebMvcConfigurer{
	//변수 => application.properties 에 있는 내가 만든변수를 읽어온다 
	@Value("${fileUploadPath}")
	String  fileUploadPath;  //application.properties 파일에서 ${키값}
	
	@Value("${domain}")
	String  domain;
	
	//파일서버를 만들 수 있다 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		exposeDirectory(registry);
	}
	
	//물리적경로를 도메인으로 접근할수 있도록 만들었음 
	void exposeDirectory(ResourceHandlerRegistry registry)
	{
		Path uploadDir = Paths.get(fileUploadPath);
		//업로드 폴더의 물리적 경로 알아내기 
		String uploadPath = uploadDir.toFile().getAbsolutePath();
		System.out.println("업로드 경로 : " + uploadPath);
		
		if(fileUploadPath.startsWith("../"))
		{
			fileUploadPath = fileUploadPath.replace("../", "");
		}
		
		//http://127.0.0.1:9000/fileUpload/3.gif/ 
		System.out.println("업로드경로 상대적경로 : " + fileUploadPath);
		registry.addResourceHandler("/"+fileUploadPath+"/**")
		        .addResourceLocations("file:/"+uploadPath+ "/");
	}
}





