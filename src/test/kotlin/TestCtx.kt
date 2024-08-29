import com.example.main
import com.example.model.CountersEntity
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

abstract class TestCtx : AnnotationSpec() {
    lateinit var testApp: TestApplication

    @BeforeAll
    fun setup()  {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(CountersEntity)
        }

        testApp = TestApplication {
            application(Application::main)
        }
    }

    @AfterAll
    fun teardown() {
        testApp.stop()
    }
}