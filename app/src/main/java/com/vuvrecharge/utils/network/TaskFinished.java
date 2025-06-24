package com.vuvrecharge.utils.network;

interface TaskFinished<T> {
    void onTaskFinished(T data);
}
