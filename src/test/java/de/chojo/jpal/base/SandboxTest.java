package de.chojo.jpal.base;

import de.chojo.jpal.entities.auth.OAuth2Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SandboxTest {

    @Test
    void auth() {
        Sandbox sandbox = new Sandbox(System.getenv("client_id"), System.getenv("client_secret"));
        Optional<OAuth2Response> auth = sandbox.auth().join();
        Assertions.assertTrue(auth.isPresent());
    }
}
