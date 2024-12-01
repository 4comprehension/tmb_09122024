package com.for_comprehension.reactor;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

class UnitTest {

    @Test
    void shouldStartDockerContainer() {
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine")).start();
    }
}
