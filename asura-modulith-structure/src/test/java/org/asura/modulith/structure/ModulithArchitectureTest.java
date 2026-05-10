package org.asura.modulith.structure;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * 验证模块架构：无循环依赖、无非法访问、模块隔离合规
 */
class ModulithArchitectureTest {

    private final ApplicationModules modules = ApplicationModules.of(AsuraModulithStructureApplication.class);

    @Test
    void verifyModuleArchitecture() {
        modules.verify();
        modules.forEach(System.out::println);
    }

}
