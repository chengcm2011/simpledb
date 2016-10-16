package db;


public interface TransactionNoRet {

    void apply(DBRunner db) throws Exception;

}
