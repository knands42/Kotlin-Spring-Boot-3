package internal.database

import internal.entity.Account
import internal.entity.Client
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class AccountDbTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            Config.connect()
            Config.migrate()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            Config.migrateDown()
        }
    }

    @Test
    fun `should save an account`() {
        val clientDb = ClientDb()
        val accountDb = AccountDb(clientDb)
        val client = Client.create(
            name = "John Doe",
            email = "mail@mail.com",
        )
        val account = Account.create(
            balance = 100.0f,
            client = client,
        )
        clientDb.save(client)
        accountDb.save(account)

        val accountResult = accountDb.getById(account.id)
        assert(accountResult.id == account.id)
        assert(accountResult.balance == account.balance)
        assert(accountResult.client.id == account.client.id)
    }
}