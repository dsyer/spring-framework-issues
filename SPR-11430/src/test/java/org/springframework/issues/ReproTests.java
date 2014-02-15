package org.springframework.issues;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Unit test that reproduces an issue reported against SPR JIRA. @Test methods within need not pass with the green bar!
 * Rather they should fail in such a way that demonstrates the reported issue.
 */
public class ReproTests {

	@Test
	public void xml() {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:org/springframework/issues/ReproTests-context.xml");
		ctx.refresh();

		Foo foo = ctx.getBean(Foo.class);
		Bar bar = ctx.getBean(Bar.class);

		assertThat(foo.getBar(), sameInstance(bar));
	}

	@Test
	public void annotation() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Bar.class);
		ctx.refresh();

		Foo foo = ctx.getBean(Foo.class);
		Bar bar = ctx.getBean(Bar.class);

		assertThat(foo.getBar(), sameInstance(bar));
	}

	@Test
	public void imported() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Imported.class);
		ctx.refresh();

		Foo foo = ctx.getBean(Foo.class);
		Bar bar = ctx.getBean(Bar.class);

		assertThat(foo.getBar(), sameInstance(bar));
	}

	@Test
	public void importSelected() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ImportSelected.class);
		ctx.refresh();

		Foo foo = ctx.getBean(Foo.class);
		Bar bar = ctx.getBean(Bar.class);

		assertThat(foo.getBar(), sameInstance(bar));
	}

	@Test
	public void importRegistered() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ImportRegistered.class);
		ctx.refresh();

		Foo foo = ctx.getBean(Foo.class);
		Bar bar = ctx.getBean(Bar.class);

		assertThat(foo.getBar(), sameInstance(bar));
	}

	@Test
	public void importRegisterImported() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ImportRegisterImported.class);
		ctx.refresh();

		Foo foo = ctx.getBean(Foo.class);
		Bar bar = ctx.getBean(Bar.class);

		assertThat(foo.getBar(), sameInstance(bar));
	}

	@Configuration
	@Import(Bar.class)
	protected static class Imported {
	}

	@Configuration
	@Import(Selector.class)
	protected static class ImportSelected {
	}

	@Configuration
	@Import(Registrar.class)
	protected static class ImportRegistered {
	}

	@Configuration
	@Import(RegistrarImport.class)
	protected static class ImportRegisterImported {
	}

	protected static class Selector implements ImportSelector {

		@Override
		public String[] selectImports(AnnotationMetadata arg0) {
			return new String[] { Bar.class.getName() };
		}

	}
	
	protected static class Registrar implements ImportBeanDefinitionRegistrar {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			registry.registerBeanDefinition("bar", new RootBeanDefinition(Bar.class));
		}
		
	}

	protected static class RegistrarImport implements ImportBeanDefinitionRegistrar {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			registry.registerBeanDefinition("bar", new RootBeanDefinition(Imported.class));
		}
		
	}

}
