package list.domain

import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import list.domain.ObserveCoordinatorTestAccessors.disposable
import list.impl.ListItem
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Test

internal class ObserveCoordinatorTest {
  private val functionalityHolder = mockk<FunctionalityHolder>()
  private val subscribeScheduler = mockk<Scheduler>()
  private val observeScheduler = mockk<Scheduler>()
  private val subject = ObserveCoordinator(
      functionalityHolder, subscribeScheduler, observeScheduler)

  @After
  fun tearDown() {
    confirmVerified(functionalityHolder, subscribeScheduler, observeScheduler)
  }

  @Test
  fun run() {
    val disposable = mockk<Disposable>()
    val onNext = mockk<Consumer<List<ListItem>>>()
    val onError = mockk<Consumer<Throwable>>()
    val observed = mockk<Flowable<List<ListItem>>> {
      every { subscribe(onNext, onError) } returns disposable
    }
    val subscribed = mockk<Flowable<List<ListItem>>> {
      every { observeOn(observeScheduler) } returns observed
    }
    val initial = mockk<Flowable<List<ListItem>>> {
      every { subscribeOn(subscribeScheduler) } returns subscribed
    }
    every { functionalityHolder.observe } returns initial

    subject.run(onNext, onError)

    verify(exactly = 1) {
      observed.subscribe(onNext, onError)
      subscribed.observeOn(observeScheduler)
      initial.subscribeOn(subscribeScheduler)
      functionalityHolder.observe
    }
    confirmVerified(initial, subscribed, observed, onNext, onError, disposable)
    assertSame(disposable, subject.disposable<Disposable>())
  }

  @Test
  fun abortDisposableIsNull() {
    // In practice this assignment is not needed because by default the field is null,
    // but we don't want to have the test rely on the implementation of the constructor,
    // so we do it in case the default value changes
    subject.disposable(null)

    subject.abort()

    // Nothing to check here
  }

  @Test
  fun abortDisposableIsNotNull() {
    val disposable = mockk<Disposable> {
      every { dispose() } just Runs
    }

    subject.disposable(disposable)

    subject.abort()

    verify(exactly = 1) {
      disposable.dispose()
    }
  }
}
