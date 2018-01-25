package com.ning.util;


import com.ning.functions.Callable;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/1/19
 * Time: 22:20
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public abstract class Try<T> {

    /** Returns `true` if the `Try` is a `Failure`, `false` otherwise.
     */
    public abstract boolean isFailure();

    /** Returns `true` if the `Try` is a `Success`, `false` otherwise.
     */
    public abstract boolean isSuccess();


    /** Returns the value from this `Success` or the given `default` argument if this is a `Failure`.
     *
     * ''Note:'': This will throw an exception if it is not a success and default throws an exception.
     */
    public T  getOrElse(Callable<T> callable){
        if (isSuccess()) {
            return get();
        } else {
            return callable.call();
        }

    }
    public static <T> Try apply(Callable<T> call){
        try{
            return new Success(call.call());
        }catch (Exception ex){
            return new Failure(ex);
        }

    }
    /** Returns the value from this `Success` or throws the exception if this is a `Failure`.
     */
    public abstract T get();

    static class Success<T> extends Try<T>{
        private T value ;
        public Success(T value){
            this.value = value;
        }
        public boolean isFailure() {
            return true;
        }
        public boolean isSuccess(){
            return false;
        }

        @Override
        public T get() {
            return value;
        }
    }
    static class Failure<T> extends Try<T>{
        private Throwable exception ;
        public Failure(Throwable exception){
            this.exception = exception;
        }
        public boolean isFailure() {
            return true;
        }
        public boolean isSuccess(){
            return false;
        }
        public T get() {
            throw new RuntimeException(exception);
        }

    }

}
