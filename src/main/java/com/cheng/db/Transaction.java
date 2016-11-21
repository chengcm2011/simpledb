package com.cheng.db;


public interface Transaction<T> {

    T apply(DBRunner db) throws Exception;

}
