import bd.emon.domain.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler

class TestScheduleProvider(private val scheduler: Scheduler) : SchedulerProvider {

    override fun io() = scheduler

    override fun ui() = scheduler

    override fun computation() = scheduler
}
