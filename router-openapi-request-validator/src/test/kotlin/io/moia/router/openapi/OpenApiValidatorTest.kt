package io.moia.router.openapi

import io.mockk.mockk
import io.moia.router.get
import io.moia.router.Request
import io.moia.router.RequestHandler
import io.moia.router.ResponseEntity
import io.moia.router.Router
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Test

class OpenApiValidatorTest {

    val testHandler = TestRequestHandler()

    val validator = OpenApiValidator("openapi.yml")

    @Test
    fun `should handle and validate request`() {
        val request = get("/tests")
            .withHeaders(mapOf("Accept" to "application/json"))

        val response = testHandler.handleRequest(request, mockk())

        validator.assertValidRequest(request)
        validator.assertValidResponse(request, response)
        validator.assertValid(request, response)
    }

    @Test
    fun `should fail on undocumented request`() {
        val request = get("/tests-not-documented")
            .withHeaders(mapOf("Accept" to "application/json"))

        val response = testHandler.handleRequest(request, mockk())

        thenThrownBy { validator.assertValid(request, response) }.isInstanceOf(OpenApiValidator.ApiInteractionInvalid::class.java)
        thenThrownBy { validator.assertValidRequest(request) }.isInstanceOf(OpenApiValidator.ApiInteractionInvalid::class.java)
    }

    @Test
    fun `should fail on invalid schema`() {
        val request = get("/tests")
            .withHeaders(mapOf("Accept" to "application/json"))

        val response = TestInvalidRequestHandler()
            .handleRequest(request, mockk())

        thenThrownBy { validator.assertValid(request, response) }.isInstanceOf(OpenApiValidator.ApiInteractionInvalid::class.java)
    }

    class TestRequestHandler : RequestHandler() {

        data class TestResponse(val name: String)

        override val router = Router.router {
            get("/tests") { _: Request<Unit> ->
                ResponseEntity.ok(TestResponse("Hello"))
            }
            get("/tests-not-documented") { _: Request<Unit> ->
                ResponseEntity.ok(TestResponse("Hello"))
            }
        }
    }

    class TestInvalidRequestHandler : RequestHandler() {

        data class TestResponseInvalid(val invalid: String)

        override val router = Router.router {
            get("/tests") { _: Request<Unit> ->
                ResponseEntity.ok(TestResponseInvalid("Hello"))
            }
        }
    }
}
