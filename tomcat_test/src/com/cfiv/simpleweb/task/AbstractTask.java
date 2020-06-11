package com.cfiv.simpleweb.task;

public abstract class AbstractTask {
    /**
     * タスクの実行を行う。
     * @throws Exception
     */
    public void execute() throws Exception {
        doTask();
    }

    /**
     * 実際に実行するタスクを記載する。
     * @throws Exception
     */
    protected abstract boolean doTask() throws Exception;
}
