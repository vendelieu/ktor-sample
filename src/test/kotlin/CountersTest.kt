import com.example.service.CountersService
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.transaction

class CountersTest : TestCtx() {
    @Test
    suspend fun `GET Read should return 200 and counter value when counter exists`() {
        transaction {
            CountersService.create("test", 1)
        }

        val response = testApp.client.get("/counters/read?counter=test")

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe """{"name":"test","counter":1}"""
    }

    @Test
    suspend fun `GET Read should return 404 when counter does not exist`() {
        val response = testApp.client.get("/counters/read?counter=nonexistent")

        response.status shouldBe HttpStatusCode.NotFound
    }

    @Test
    suspend fun `POST Create should return 201 when counter is created`() {
        val response = testApp.client.post("/counters/create") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"newCounter","counter":10}""")
        }

        response.status shouldBe HttpStatusCode.Created
        response.bodyAsText() shouldBe """{"name":"newCounter","counter":10}"""
    }

    @Test
    suspend fun `POST Create should return 400 when name is empty`() {
        val response = testApp.client.post("/counters/create") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"","counter":10}""")
        }

        response.status shouldBe HttpStatusCode.BadRequest
        response.bodyAsText() shouldBe """["name can't be blank"]"""
    }

    @Test
    suspend fun `POST Create should return 400 when counter is negative`() {
        val response = testApp.client.post("/counters/create") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"validName","counter":-5}""")
        }

        response.status shouldBe HttpStatusCode.BadRequest
        response.bodyAsText() shouldBe """["value can't be negative"]"""
    }

    @Test
    suspend fun `POST Create should return 400 when name is empty and counter is negative`() {
        val response = testApp.client.post("/counters/create") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"","counter":-5}""")
        }

        response.status shouldBe HttpStatusCode.BadRequest
        response.bodyAsText() shouldBe """["name can't be blank","value can't be negative"]"""
    }

    @Test
    suspend fun `PATCH Increment should return 200 and incremented value when counter exists`() {
        transaction {
            CountersService.create("testIncrement", 5)
        }

        val response = testApp.client.patch("/counters/increment?counter=testIncrement")

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe "6"
    }

    @Test
    suspend fun `PATCH Increment should return 204 when counter does not exist`() {
        val response = testApp.client.patch("/counters/increment?counter=nonexistent")

        response.status shouldBe HttpStatusCode.UnprocessableEntity
    }

    @Test
    suspend fun `DELETE Delete should return 204 when counter is deleted`() {
        transaction {
            CountersService.create("testDelete", 1)
        }

        val response = testApp.client.delete("/counters/delete?counter=testDelete")

        response.status shouldBe HttpStatusCode.NoContent

        // Verify that the counter is deleted
        val verifyResponse = testApp.client.get("/counters/read?counter=testDelete")
        verifyResponse.status shouldBe HttpStatusCode.NotFound
    }

    @Test
    suspend fun `GET GetAll should return 200 and list of all counters`() {
        transaction {
            CountersService.create("testGetAll1", 1)
            CountersService.create("testGetAll2", 2)
        }

        val response = testApp.client.get("/counters/all")

        response.status shouldBe HttpStatusCode.OK
        response.bodyAsText() shouldBe """[{"name":"testGetAll1","counter":1},{"name":"testGetAll2","counter":2}]"""
    }
}