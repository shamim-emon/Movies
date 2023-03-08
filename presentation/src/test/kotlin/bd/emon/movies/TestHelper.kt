package bd.emon.movies

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
 * when null is returned.
 */
@Suppress("UNCHECKED_CAST")
fun <T> capture(captor: ArgumentCaptor<T>): T = captor.capture()

fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
