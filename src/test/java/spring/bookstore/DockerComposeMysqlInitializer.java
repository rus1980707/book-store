package spring.bookstore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DockerComposeMysqlInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 5306;
    private static final String DB_NAME = "bookstore_test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0707";
    private static final Duration START_TIMEOUT = Duration.ofSeconds(90);
    private static final Duration RETRY_DELAY = Duration.ofSeconds(2);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (STARTED.compareAndSet(false, true)) {
            Path projectRoot = findProjectRoot();
            startMysql(projectRoot);
            waitForMysql();
        }
    }

    private static Path findProjectRoot() {
        Path current = Path.of("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("docker-compose.yaml"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not locate project root with pom.xml and docker-compose.yaml");
    }

    private static void startMysql(Path projectRoot) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                List.of("docker", "compose", "up", "-d", "mysqldb"));
        processBuilder.directory(projectRoot.toFile());
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("Failed to start MySQL via docker compose. "
                        + "Ensure Docker Desktop is running. Output: " + output);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not run docker compose. Ensure Docker is installed.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while starting MySQL via docker compose", e);
        }
    }

    private static void waitForMysql() {
        Instant deadline = Instant.now().plus(START_TIMEOUT);
        while (Instant.now().isBefore(deadline)) {
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                            + "?createDatabaseIfNotExist=true",
                    DB_USER,
                    DB_PASSWORD);
                 Statement statement = connection.createStatement()) {
                statement.execute("SELECT 1");
                return;
            } catch (SQLException e) {
                try {
                    Thread.sleep(RETRY_DELAY.toMillis());
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while waiting for MySQL to start",
                            interruptedException);
                }
            }
        }
        throw new IllegalStateException("MySQL did not become available on "
                + DB_HOST + ":" + DB_PORT + " within " + START_TIMEOUT.getSeconds() + " seconds");
    }
}
