package com.carlosmagno.mywallet.data.repositoryImpl

import com.carlosmagno.mywallet.data.local.AccountDao
import com.carlosmagno.mywallet.data.local.AccountEntity
import com.carlosmagno.mywallet.data.local.TransactionDao
import com.carlosmagno.mywallet.data.local.TransactionEntity
import com.carlosmagno.mywallet.domain.repository.AccountRepository

class AccountRepositoryImpl(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : AccountRepository {

    override suspend fun getAccountByUserId(userId: Int): AccountEntity? {
        return accountDao.getAccount(userId)
    }

    override suspend fun updateAccount(account: AccountEntity) {
        accountDao.updateAccount(account)
    }

    override suspend fun createAccount(account: AccountEntity) {
        accountDao.insertAccount(account)
    }

    override suspend fun transfer(fromUserId: Int, toUserId: Int, amount: Double): Boolean {
        if (fromUserId == toUserId) {
            return false
        }

        val fromAccount = accountDao.getAccount(fromUserId)
        val toAccount = accountDao.getAccount(toUserId)

        if (fromAccount == null || toAccount == null || fromAccount.balance < amount) {
            return false
        }

        val updatedFrom = fromAccount.copy(balance = fromAccount.balance - amount)
        val updatedTo = toAccount.copy(balance = toAccount.balance + amount)

        accountDao.updateAccount(updatedFrom)
        accountDao.updateAccount(updatedTo)

        transactionDao.insertTransaction(
            TransactionEntity(
                fromUserId = fromUserId,
                toUserId = toUserId,
                amount = amount,
                timestamp = System.currentTimeMillis()
            )
        )

        return true
    }
}
