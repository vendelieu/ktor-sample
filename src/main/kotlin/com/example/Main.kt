package com.example

import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

fun main() {
    val appEnv = applicationEngineEnvironment {
        connector {
            port = 8080
        }

        val keyStoreFile = File("build/keystore.jks")
        val keyStore = buildKeyStore {
            certificate("sampleAlias") {
                password = "foobar"
                domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
            }
        }
        keyStore.saveToFile(keyStoreFile, "123456")

        sslConnector(keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "foobar".toCharArray() }
        ) {
            port = 8443
        }

        module(Application::main)
    }

    embeddedServer(Netty, appEnv).start(wait = true)
}
