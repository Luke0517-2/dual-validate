//package cht.bss.morder.dual.validate.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.runner.ApplicationContextRunner;
//
//public class MOrderFacadeTest {
//
//	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();
//
//	@Test
//	public void testFacade() {
//
//		this.contextRunner.withPropertyValues("dual-validate.mode=FILE").run(context -> {
//			String value = context.getEnvironment().getProperty("dual-validate.mode");
//			assertNotNull(value);
//			assertEquals("FILE", value);
//			MOrderFacade targetBean = context.getBean(MOrderFacade.class);
//			assertTrue(targetBean instanceof MOrderFacadeFileImpl);
//		});
//
//	}
//
//}
