package org.springframework.issues;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bar {
	
	@Bean
	public Foo foo() {
		return new Foo(this);
	}

}
