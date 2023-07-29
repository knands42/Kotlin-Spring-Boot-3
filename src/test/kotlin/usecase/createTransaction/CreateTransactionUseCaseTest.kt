package usecase.createTransaction

import org.example.eda.internal.entity.Account
import org.example.eda.internal.entity.Client
import org.example.eda.usecase.createTransaction.CreateTransactionInputDTO
import org.example.eda.usecase.createTransaction.CreateTransactionUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.example.eda.domain.event.EventDispatcherInterface
import org.example.eda.domain.event.EventInterface
import org.example.eda.internal.repository.AccountRepository
import org.example.eda.internal.repository.TransactionRepository

internal class CreateTransactionUseCaseTest {

    private val transactionRepository = mockk<TransactionRepository>()
    private val accountRepository = mockk<AccountRepository>()
    private val eventDispatcher = mockk<EventDispatcherInterface>()
    private val transactionCreated = mockk<EventInterface>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `create transaction`() = runTest {
        every { transactionRepository.create(any()) } returns Unit
        every { accountRepository.getById(any()) } returns account1 andThen account2
        every { transactionCreated.setPayload(any()) } returns Unit
        every { eventDispatcher.dispatch(any()) } returns Unit

        val useCase = CreateTransactionUseCase.create(transactionRepository, accountRepository, eventDispatcher, transactionCreated)
        val outputDTO = useCase.execute(inputDTO)

        assert(outputDTO.id.isNotBlank())
    }

    companion object {
        val client = Client.create(
            name = "name",
            email = "mail@mail.com"
        )

        val account1 = Account.create(
            client = client,
            balance = 100f
        )

        val account2 = Account.create(
            client = client,
            balance = 100f
        )

        val inputDTO = CreateTransactionInputDTO(
            accountFromId = account1.id,
            accountToId = account2.id,
            amount = 100f
        )
    }
}