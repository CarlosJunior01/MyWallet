package com.carlosmagno.mywallet.utils

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InstantTaskExecutorRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
                    override fun postToMainThread(runnable: Runnable) = runnable.run()
                    override fun isMainThread() = true
                })

                try {
                    base.evaluate()
                } finally {
                    ArchTaskExecutor.getInstance().setDelegate(null)
                }
            }
        }
}
