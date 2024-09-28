package com.samtest.demo;

import com.samtest.demo.AbstractTestcontainers;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainersTest extends AbstractTestcontainers {
//    @Container
//    protected static PostgreSQLContainer<?> postgreSQLContainer =
//            new PostgreSQLContainer<>("postgres:latest")
//                    .withDatabaseName("sam-dao-unit-test")
//                    .withUsername("amigoscode")
//                    .withPassword("password");
//
//    @DynamicPropertySource
//    private static void registerDataSourceProperties(DynamicPropertyRegistry registry){
//        registry.add(
//                "spring.datasource.url",
//                postgreSQLContainer::getJdbcUrl
//        );
//        registry.add(
//                "spring.datasource.username",
//                ()-> postgreSQLContainer.getUsername()
//        );
//        registry.add(
//                "spring.datasource.password",
//                ()-> postgreSQLContainer.getPassword()
//        );
//    }
    @Test
    void canStartPostGresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        //assertThat(postgreSQLContainer.isHealthy()).isTrue();
    }
//
//    @Test
//    void canApplyDbMigrationWithFlyway() {
//    Flyway flyway = Flyway.configure().dataSource(
//            postgreSQLContainer.getJdbcUrl(),
//            postgreSQLContainer.getUsername(),
//            postgreSQLContainer.getPassword()
//    ).load();
//    flyway.migrate();
//    System.out.println();
//    }


}
