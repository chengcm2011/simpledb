package db;


public interface Transaction<T> {

    T apply(DBRunner db) throws Exception;

}
