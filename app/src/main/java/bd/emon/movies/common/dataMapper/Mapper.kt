package bd.emon.movies.common.dataMapper

abstract class Mapper<in E, T> {
    abstract fun mapFrom(from: E): T
}
