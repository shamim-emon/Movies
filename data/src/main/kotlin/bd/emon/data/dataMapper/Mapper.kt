package bd.emon.data.dataMapper

abstract class Mapper<in E, T> {
    abstract fun mapFrom(from: E): T
}
