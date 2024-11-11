//TODO Find way to use base
interface BaseDao<T> {
    suspend fun insert(obj: T): Long
    suspend fun insert(vararg obj: T)
    suspend fun insert(list: List<T>)
    suspend fun update(obj: T): Int
    suspend fun upsert(obj: T): Long
    suspend fun delete(obj: T): Int
    suspend fun delete(list: List<T>)
}
